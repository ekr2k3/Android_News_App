package com.example.myrssreaderapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myrssreaderapp.DataHelper.DatabaseHelper;
import com.example.myrssreaderapp.models.BookmarkItem;
import com.example.myrssreaderapp.ui.ArticleActivity;
import com.example.myrssreaderapp.R;

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

        holder.tvUrl.setText(item.getUrl());

        // Click mở bài báo
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ArticleActivity.class);
            intent.putExtra("url", item.getUrl());
            intent.putExtra("userId", item.getUserId());
            context.startActivity(intent);
        });

        // Nút xóa bookmark
        holder.btnDelete.setOnClickListener(v -> {
            db.deleteBookmark(item.getUserId(), item.getUrl());
            list.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, list.size());
        });
    }

    @Override
    public int getItemCount(){ return list != null ? list.size() : 0; }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvUrl;
        ImageButton btnDelete;

        public ViewHolder(@NonNull View itemView){
            super(itemView);
            tvUrl = itemView.findViewById(R.id.tvUrl);
            btnDelete = itemView.findViewById(R.id.btnDeleteBookmark);
        }
    }
}
