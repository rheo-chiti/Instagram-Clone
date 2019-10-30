package com.example.chat;


public class Photo {
    private String username;
    private String imageURL;
    private long timeStamp, likes;

    public Photo(String username, String imageURL, long timeStamp, long likes) {
        this.username = username;
        this.imageURL = imageURL;
        this.timeStamp = timeStamp;
        this.likes = likes;
    }

    public long getLikes() {
        return likes;
    }

    public void setLikes(long likes) {
        this.likes = likes;
    }

    public Photo() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    @Override
    public String toString() {
        return this.getUsername() + " " + this.getImageURL() + " " + this.getTimeStamp();

    }
}
