package com.maxrave.wallily.data.model.firebase;

import com.google.gson.annotations.SerializedName;

public class HitId {
    @SerializedName("id")
    private String id;

    @SerializedName("thumbnailUrl")
    private String thumbnailUrl;

    public HitId(String id, String thumbnailUrl) {
        this.id = id;
        this.thumbnailUrl = thumbnailUrl;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }
}
