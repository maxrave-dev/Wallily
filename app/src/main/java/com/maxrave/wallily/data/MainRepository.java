package com.maxrave.wallily.data;

import androidx.paging.PagingSource;

import com.maxrave.wallily.data.api.ImageService;
import com.maxrave.wallily.data.db.LocalDataSource;
import com.maxrave.wallily.data.db.entities.HitEntity;
import com.maxrave.wallily.data.db.entities.HitRemoteKey;
import com.maxrave.wallily.data.model.pixabayResponse.PixabayResponse;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.rxjava3.core.Single;

@Singleton
public class MainRepository {
    private static final String TAG = "MainRepository";
    private final ImageService imageService;

    private final LocalDataSource localDataSource;

    @Inject
    public MainRepository(ImageService imageService, LocalDataSource localDataSource) {
        this.imageService = imageService;
        this.localDataSource = localDataSource;
    }

    public Single<PixabayResponse> getImageByPage(String query, String orientation, int page, int per_page) {
        return imageService.getImages(query, orientation, page, per_page);
    }

    //Database
    public void clearAll() {
        localDataSource.clearAll();
    }
//    public Single<List<Float>> upsertAll(List<HitEntity> hits) {
//        return localDataSource.upsertAll(hits);
//    }
//    public PagingSource<Integer, HitEntity> pagingSource() {
//        return localDataSource.pagingSource();
//    }

    public void clearAllRemoteKeys() {
        localDataSource.clearAllRemoteKeys();
    }

//    public Single<List<Float>> upsertAllRemoteKeys(List<HitRemoteKey> hits) {
//        return localDataSource.upsertAllRemoteKeys(hits);
//    }

    public Single<HitRemoteKey> getRemoteKey(Float id) {
        return localDataSource.getRemoteKey(id);
    }

}
