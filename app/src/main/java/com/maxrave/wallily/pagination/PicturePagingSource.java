package com.maxrave.wallily.pagination;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.paging.PagingSource;
import androidx.paging.PagingState;
import androidx.paging.rxjava3.RxPagingSource;

import com.maxrave.wallily.data.MainRepository;
import com.maxrave.wallily.data.api.ImageService;
import com.maxrave.wallily.data.model.pixabayResponse.Hit;
import com.maxrave.wallily.data.model.pixabayResponse.PixabayResponse;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class PicturePagingSource extends RxPagingSource<Integer, Hit> {
    MainRepository mainRepository;
    String keyword;

    String searchQuery;
    public PicturePagingSource(MainRepository mainRepository, String keyword, String searchQuery) {
        this.mainRepository = mainRepository;
        this.keyword = keyword;
        this.searchQuery = searchQuery;
    }

    @Nullable
    @Override
    public Integer getRefreshKey(@NonNull PagingState<Integer, Hit> pagingState) {
        return null;
    }

    @NonNull
    @Override
    public Single<LoadResult<Integer, Hit>> loadSingle(@NonNull LoadParams<Integer> loadParams) {
        try {
            // If page number is already there then init page variable with it otherwise we are loading fist page
            int page = loadParams.getKey() != null ? loadParams.getKey() : 1;
            // Send request to server with page number
            return mainRepository.getImageByPage(searchQuery, keyword, "vertical", page, 20)
                    // Subscribe the result
                    .subscribeOn(Schedulers.io())
                    // Map result top List of movies
                    .map(PixabayResponse::getHits)
                    // Map result to LoadResult Object
                    .map(hits -> toLoadResult(hits, page))
                    // when error is there return error
                    .onErrorReturn(LoadResult.Error::new);
        } catch (Exception e) {
            // Request ran into error return error
            return Single.just(new LoadResult.Error<>(e));
        }
    }
    private LoadResult<Integer, Hit> toLoadResult(List<Hit> data, int page) {
        return new LoadResult.Page<>(data, page == 1 ? null : page - 1, page + 1);
    }
}
