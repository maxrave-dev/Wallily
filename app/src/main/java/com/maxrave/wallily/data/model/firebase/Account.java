package com.maxrave.wallily.data.model.firebase;

import com.google.gson.annotations.SerializedName;

public class Account {
    @SerializedName("email")
    private String email;

    @SerializedName("uid")
    private String uid;

    @SerializedName("display_name")
    private String display_name;

    @SerializedName("avatar_url")
    private String avatar_url;

    public Account(String email, String uid, String displayName, String avatarUrl) {
        this.email = email;
        this.uid = uid;
        display_name = displayName;
        avatar_url = avatarUrl;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getDisplay_name() {
        return display_name;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }

    public String getAvatar_url() {
        return avatar_url;
    }

    public void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }
}
