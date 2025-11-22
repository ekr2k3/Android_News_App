package com.example.myrssreaderapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myrssreaderapp.ui.ArticleActivity;
import com.example.myrssreaderapp.R;
import com.example.myrssreaderapp.models.Item;
import com.squareup.picasso.Picasso;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    private Context context;
    private List<Item> list;

    public NewsAdapter(Context context, List<Item> list){
        this.context=context;
        this.list=list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View v = LayoutInflater.from(context).inflate(R.layout.item_news,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position){
        Item it = list.get(position);
        holder.tvTitle.setText(it.getTitle()!=null ? it.getTitle():"");
        holder.tvDesc.setText(it.getDescription()!=null ? android.text.Html.fromHtml(it.getDescription()).toString():"");

        String imageUrl = extractImageFromDescription(it.getDescription());
        if(imageUrl!=null){
            Picasso.get().load(imageUrl).fit().centerCrop().into(holder.imgThumb);
        }else{
            holder.imgThumb.setImageResource(R.drawable.ic_placeholder);
        }

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ArticleActivity.class);
            intent.putExtra("url", it.getLink());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount(){ return list!=null ? list.size() : 0; }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imgThumb;
        TextView tvTitle,tvDesc;
        public ViewHolder(@NonNull View itemView){
            super(itemView);
            imgThumb = itemView.findViewById(R.id.imgThumb);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDesc = itemView.findViewById(R.id.tvDesc);
        }
    }

    private String extractImageFromDescription(String desc){
        if(desc==null) return null;
        int idx = desc.indexOf("<img");
        if(idx==-1) return null;
        int src = desc.indexOf("src=",idx);
        if(src==-1) return null;
        int start = desc.indexOf('"',src);
        if(start==-1) return null;
        int end = desc.indexOf('"',start+1);
        if(end==-1) return null;
        return desc.substring(start+1,end);
    }
}