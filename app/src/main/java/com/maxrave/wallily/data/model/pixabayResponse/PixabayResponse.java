package com.maxrave.wallily.data.model.pixabayResponse;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class PixabayResponse {
    @SerializedName("total")
    private float total;

    @SerializedName("totalHits")
    private float totalHits;
    @SerializedName("hits")
    ArrayList<Hit> hits;

    public PixabayResponse(float total, float totalHits, ArrayList<Hit> hits){
        this.total = total;
        this.totalHits = totalHits;
        this.hits = hits;
    }

    // Getter Methods

    public float getTotal() {
        return total;
    }

    public float getTotalHits() {
        return totalHits;
    }

    public ArrayList<Hit> getHits() {
        return hits;
    }

    // Setter Methods

    public void setTotal(float total) {
        this.total = total;
    }

    public void setTotalHits(float totalHits) {
        this.totalHits = totalHits;
    }
    public void setHits(ArrayList<Hit> hits) {
        this.hits = hits;
    }
}
