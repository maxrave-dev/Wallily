package com.maxrave.wallily.data.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.maxrave.wallily.data.db.entities.HitEntity;
import com.maxrave.wallily.data.db.entities.HitRemoteKey;
import com.maxrave.wallily.data.db.entities.SearchHistory;

@Database(entities = {HitEntity.class, HitRemoteKey.class, SearchHistory.class}, version = 1, exportSchema = true)
public abstract class PictureDatabase extends RoomDatabase {
    abstract public PictureDao pictureDao();
}
