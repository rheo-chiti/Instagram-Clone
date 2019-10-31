package com.example.chat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ViewCommentAdapter extends RecyclerView.Adapter<ViewCommentAdapter.ViewHolder> {

    private Context context;
    private List<Comment> commentList;

    public ViewCommentAdapter(Context context, List<Comment> commentList) {
        this.context = context;
        this.commentList = commentList;
    }

    @NonNull
    @Override
    public ViewCommentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewCommentAdapter.ViewHolder holder, int position) {
        Comment comment = commentList.get(position);
        holder.commentText.setText(comment.getCommentText());
        holder.userName.setText(comment.getUserName());

        holder.timeStamp.setText(comment.getTimeStamp());
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView userName, timeStamp, commentText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.commentUsername);
            timeStamp = itemView.findViewById(R.id.commentTime);
            commentText = itemView.findViewById(R.id.commentText);

        }
    }
}
