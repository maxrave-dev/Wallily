package com.maxrave.wallily.data.db;

import androidx.paging.PagingSource;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Upsert;

import com.maxrave.wallily.data.db.entities.HitEntity;
import com.maxrave.wallily.data.db.entities.HitRemoteKey;
import com.maxrave.wallily.data.db.entities.SearchHistory;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface PictureDao {
    @Upsert
    void upsertAll(List<HitEntity> hits);

//    @Query("SELECT * FROM hitentity")
//    PagingSource<Integer, HitEntity> pagingSource();

    @Query("DELETE FROM hitentity")
    void clearAll();

    @Upsert
    void upsertAllRemoteKeys(List<HitRemoteKey> hits);

    @Query("SELECT * FROM hitremotekey WHERE id = :id")
    Single<HitRemoteKey> getRemoteKey(Float id);

    @Query("DELETE FROM hitremotekey")
    void clearAllRemoteKeys();
}
