package com.example.myrssreaderapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myrssreaderapp.DataHelper.DatabaseHelper;
import com.example.myrssreaderapp.models.BookmarkItem;
import com.example.myrssreaderapp.ui.ArticleActivity;
import com.example.myrssreaderapp.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class BookmarkAdapter extends RecyclerView.Adapter<BookmarkAdapter.ViewHolder> {

    private Context context;
    private List<BookmarkItem> list;
    private DatabaseHelper db;

    public BookmarkAdapter(List<BookmarkItem> list, Context context, DatabaseHelper db){
        this.list = list;
        this.context = context;
        this.db = db;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View v = LayoutInflater.from(context).inflate(R.layout.item_bookmark, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position){
        BookmarkItem item = list.get(position);

        holder.title.setText(item.getTitle());
        holder.desc.setText(item.getDescription());

        if (item.getImageUrl() != null && !item.getImageUrl().isEmpty()) {
            Picasso.get().load(item.getImageUrl()).into(holder.img);
        }

        // Click mở bài báo
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ArticleActivity.class);
            intent.putExtra("url", item.getUrl());
            intent.putExtra("userId", item.getUserId());
            intent.putExtra("title", item.getTitle());
            intent.putExtra("description", item.getDescription());
            intent.putExtra("imageUrl", item.getImageUrl());
            context.startActivity(intent);
        });

        // Nút xóa bookmark
//        holder.btnDelete.setOnClickListener(v -> {
//            db.deleteBookmark(item.getUserId(), item.getUrl());
//            list.remove(position);
//            notifyItemRemoved(position);
//            notifyItemRangeChanged(position, list.size());
//        });
    }

    @Override
    public int getItemCount(){ return list != null ? list.size() : 0; }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView title, desc;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.bookmark_img);
            title = itemView.findViewById(R.id.bookmark_title);
            desc = itemView.findViewById(R.id.bookmark_desc);
        }
    }
}
