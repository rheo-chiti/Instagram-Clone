package com.example.chat;

public class Comment {
    private String timeStamp, userName, commentText;

    public Comment(String timeStamp, String userName, String commentText) {
        this.timeStamp = timeStamp;
        this.userName = userName;
        this.commentText = commentText;
    }

    public Comment() {
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }
}

