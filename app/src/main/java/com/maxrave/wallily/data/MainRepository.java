package com.maxrave.wallily.data;

import androidx.paging.PagingSource;

import com.maxrave.wallily.data.api.ImageService;
import com.maxrave.wallily.data.db.LocalDataSource;
import com.maxrave.wallily.data.db.entities.HitEntity;
import com.maxrave.wallily.data.db.entities.HitRemoteKey;
import com.maxrave.wallily.data.db.entities.SearchHistory;
import com.maxrave.wallily.data.model.pixabayResponse.PixabayResponse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.functions.Function;

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

    public Single<PixabayResponse> getImageByPage(String query, String category, String orientation, int page, int per_page) {
        return imageService.getImages(query, category, orientation, page, per_page);
    }

    public Single<List<PixabayResponse>> getImageById(ArrayList<String> ids) {
        List<Single<PixabayResponse>> result = new ArrayList<>();
        ids.forEach(id -> {
            result.add(imageService.getImageById(id));
        });
        return Single.zip(result, new Function<Object[], List<PixabayResponse>>() {
            @Override
            public List<PixabayResponse> apply(Object[] objects) throws Throwable {
                return Stream.of(objects).map(o -> (PixabayResponse) o).collect(Collectors.toList());
            }
        });
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
