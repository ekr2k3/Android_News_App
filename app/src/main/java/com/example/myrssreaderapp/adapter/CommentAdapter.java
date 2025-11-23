package com.example.myrssreaderapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.myrssreaderapp.R;
import com.example.myrssreaderapp.models.Comment;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    private List<Comment> commentList;

    public CommentAdapter(List<Comment> list) {
        this.commentList = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_comment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Comment c = commentList.get(position);
        holder.txtEmail.setText(c.getEmail());
        holder.txtContent.setText(c.getContent());
    }

    @Override
    public int getItemCount() {
        return commentList != null ? commentList.size() : 0;
    }

    public void setComments(List<Comment> list) {
        this.commentList = list;
        notifyDataSetChanged();
    }

    public void addComment(Comment c) {
        if (commentList != null) {
            commentList.add(0, c); // thêm lên đầu nếu muốn mới nhất trước
            notifyItemInserted(0);
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtEmail;
        TextView txtContent;

        ViewHolder(View itemView) {
            super(itemView);
            txtEmail = itemView.findViewById(R.id.txtCommentEmail);
            txtContent = itemView.findViewById(R.id.txtCommentContent);
        }
    }
}
