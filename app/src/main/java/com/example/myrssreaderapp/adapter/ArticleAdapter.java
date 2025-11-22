package com.example.myrssreaderapp.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myrssreaderapp.models.ArticleItem;
import com.example.myrssreaderapp.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ArticleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<ArticleItem> list;

    public ArticleAdapter(Context context, List<ArticleItem> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position).getType();
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {
        LayoutInflater inflater = LayoutInflater.from(context);

        if (viewType == ArticleItem.TYPE_IMAGE) {
            View v = inflater.inflate(R.layout.item_article_image, parent, false);
            return new ImageHolder(v);
        } else {
            View v = inflater.inflate(R.layout.item_article_text, parent, false);
            return new TextHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(
            @NonNull RecyclerView.ViewHolder holder,
            int position
    ) {
        ArticleItem item = list.get(position);

        if (holder instanceof TextHolder) {
            ((TextHolder) holder).tvText.setText(item.getContent());
        } else {
            Log.d("ARTICLE_IMAGE", "URL = " + item.getContent());
            Picasso.get()
                    .load(item.getContent())
                    .fit()
                    .centerCrop()
                    .into(((ImageHolder) holder).img);
        }
    }

    static class TextHolder extends RecyclerView.ViewHolder {
        TextView tvText;

        TextHolder(View v) {
            super(v);
            tvText = v.findViewById(R.id.tvText);
        }
    }

    static class ImageHolder extends RecyclerView.ViewHolder {
        ImageView img;

        ImageHolder(View v) {
            super(v);
            img = v.findViewById(R.id.img);
        }
    }
}