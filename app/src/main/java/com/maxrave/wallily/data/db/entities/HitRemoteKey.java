package com.maxrave.wallily.data.db.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class HitRemoteKey {
    @PrimaryKey(autoGenerate = false)
    private float id;
    private int prevKey;
    private int nextKey;

    public HitRemoteKey(float id, int prevKey, int nextKey) {
        this.id = id;
        this.prevKey = prevKey;
        this.nextKey = nextKey;
    }


    public int getPrevKey() {
        return prevKey;
    }

    public void setPrevKey(int prevKey) {
        this.prevKey = prevKey;
    }

    public float getId() {
        return id;
    }

    public void setId(float id) {
        this.id = id;
    }

    public int getNextKey() {
        return nextKey;
    }

    public void setNextKey(int nextKey) {
        this.nextKey = nextKey;
    }
}
