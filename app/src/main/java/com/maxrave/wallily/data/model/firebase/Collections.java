package com.maxrave.wallily.data.model.firebase;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Collections {
    @SerializedName("id")
    private String id;

    @SerializedName("listHitsId")
    private List<HitId> listHitsId;
    @SerializedName("name")
    private String name;


    public Collections(String id, List<HitId> listHitsId, String name) {
        this.id = id;
        this.listHitsId = listHitsId;
        this.name = name;
    }

    public List<HitId> getListHitsId() {
        return listHitsId;
    }

    public void setListHitsId(List<HitId> listHitsId) {
        this.listHitsId = listHitsId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
