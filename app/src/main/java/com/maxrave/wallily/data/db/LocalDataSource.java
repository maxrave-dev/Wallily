package com.maxrave.wallily.data.db;

import androidx.paging.PagingSource;

import com.maxrave.wallily.data.db.entities.HitEntity;
import com.maxrave.wallily.data.db.entities.HitRemoteKey;
import com.maxrave.wallily.data.db.entities.SearchHistory;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

public class LocalDataSource {
    private final PictureDao pictureDao;

    @Inject
    public LocalDataSource(PictureDao pictureDao) {
        super();
        this.pictureDao = pictureDao;
    }

//    public Single<List<Float>> upsertAll(List<HitEntity> hits) {
//        return pictureDao.upsertAll(hits);
//    }

    public void clearAll() {
        pictureDao.clearAll();
    }

//    public PagingSource<Integer, HitEntity> pagingSource() {
//        return pictureDao.pagingSource();
//    }

//    public Single<List<Float>> upsertAllRemoteKeys(List<HitRemoteKey> hits) {
//        return pictureDao.upsertAllRemoteKeys(hits);
//    }

    public Single<HitRemoteKey> getRemoteKey(Float id) {
        return pictureDao.getRemoteKey(id);
    }

    public void clearAllRemoteKeys() {
        pictureDao.clearAllRemoteKeys();
    }
}
