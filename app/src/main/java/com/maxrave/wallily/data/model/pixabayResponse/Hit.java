package com.maxrave.wallily.data.model.pixabayResponse;

import com.google.gson.annotations.SerializedName;

public class Hit {
    @SerializedName("id")
    private float id;
    @SerializedName("pageURL")
    private String pageURL;
    @SerializedName("type")
    private String type;
    @SerializedName("tags")
    private String tags;
    @SerializedName("previewURL")
    private String previewURL;
    @SerializedName("previewWidth")
    private float previewWidth;
    @SerializedName("previewHeight")
    private float previewHeight;
    @SerializedName("webformatURL")
    private String webformatURL;
    @SerializedName("webformatWidth")
    private float webformatWidth;
    @SerializedName("webformatHeight")
    private float webformatHeight;
    @SerializedName("largeImageURL")
    private String largeImageURL;
    @SerializedName("imageWidth")
    private float imageWidth;
    @SerializedName("imageHeight")
    private float imageHeight;
    @SerializedName("imageSize")
    private float imageSize;
    @SerializedName("views")
    private float views;
    @SerializedName("downloads")
    private float downloads;
    @SerializedName("collections")
    private float collections;
    @SerializedName("likes")
    private float likes;
    @SerializedName("comments")
    private float comments;
    @SerializedName("user_id")
    private float user_id;
    @SerializedName("user")
    private String user;
    @SerializedName("userImageURL")
    private String userImageURL;

    public Hit(float id, String pageURL, String type, String tags, String previewURL, float previewWidth, float previewHeight, String webformatURL, float webformatWidth, float webformatHeight, String largeImageURL, float imageWidth, float imageHeight, float imageSize, float views, float downloads, float collections, float likes, float comments, float user_id, String user, String userImageURL) {
        this.id = id;
        this.pageURL = pageURL;
        this.type = type;
        this.tags = tags;
        this.previewURL = previewURL;
        this.previewWidth = previewWidth;
        this.previewHeight = previewHeight;
        this.webformatURL = webformatURL;
        this.webformatWidth = webformatWidth;
        this.webformatHeight = webformatHeight;
        this.largeImageURL = largeImageURL;
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
        this.imageSize = imageSize;
        this.views = views;
        this.downloads = downloads;
        this.collections = collections;
        this.likes = likes;
        this.comments = comments;
        this.user_id = user_id;
        this.user = user;
        this.userImageURL = userImageURL;
    }
    // Getter Methods

    public float getId() {
        return id;
    }

    public String getPageURL() {
        return pageURL;
    }

    public String getType() {
        return type;
    }

    public String getTags() {
        return tags;
    }

    public String getPreviewURL() {
        return previewURL;
    }

    public float getPreviewWidth() {
        return previewWidth;
    }

    public float getPreviewHeight() {
        return previewHeight;
    }

    public String getWebformatURL() {
        return webformatURL;
    }

    public float getWebformatWidth() {
        return webformatWidth;
    }

    public float getWebformatHeight() {
        return webformatHeight;
    }

    public String getLargeImageURL() {
        return largeImageURL;
    }

    public float getImageWidth() {
        return imageWidth;
    }

    public float getImageHeight() {
        return imageHeight;
    }

    public float getImageSize() {
        return imageSize;
    }

    public float getViews() {
        return views;
    }

    public float getDownloads() {
        return downloads;
    }

    public float getCollections() {
        return collections;
    }

    public float getLikes() {
        return likes;
    }

    public float getComments() {
        return comments;
    }

    public float getUser_id() {
        return user_id;
    }

    public String getUser() {
        return user;
    }

    public String getUserImageURL() {
        return userImageURL;
    }

    // Setter Methods

    public void setId(float id) {
        this.id = id;
    }

    public void setPageURL(String pageURL) {
        this.pageURL = pageURL;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public void setPreviewURL(String previewURL) {
        this.previewURL = previewURL;
    }

    public void setPreviewWidth(float previewWidth) {
        this.previewWidth = previewWidth;
    }

    public void setPreviewHeight(float previewHeight) {
        this.previewHeight = previewHeight;
    }

    public void setWebformatURL(String webformatURL) {
        this.webformatURL = webformatURL;
    }

    public void setWebformatWidth(float webformatWidth) {
        this.webformatWidth = webformatWidth;
    }

    public void setWebformatHeight(float webformatHeight) {
        this.webformatHeight = webformatHeight;
    }

    public void setLargeImageURL(String largeImageURL) {
        this.largeImageURL = largeImageURL;
    }

    public void setImageWidth(float imageWidth) {
        this.imageWidth = imageWidth;
    }

    public void setImageHeight(float imageHeight) {
        this.imageHeight = imageHeight;
    }

    public void setImageSize(float imageSize) {
        this.imageSize = imageSize;
    }

    public void setViews(float views) {
        this.views = views;
    }

    public void setDownloads(float downloads) {
        this.downloads = downloads;
    }

    public void setCollections(float collections) {
        this.collections = collections;
    }

    public void setLikes(float likes) {
        this.likes = likes;
    }

    public void setComments(float comments) {
        this.comments = comments;
    }

    public void setUser_id(float user_id) {
        this.user_id = user_id;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setUserImageURL(String userImageURL) {
        this.userImageURL = userImageURL;
    }
}
