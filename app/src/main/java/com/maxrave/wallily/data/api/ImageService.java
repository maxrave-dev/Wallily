package com.maxrave.wallily.data.api;

import com.maxrave.wallily.common.Config;
import com.maxrave.wallily.data.model.pixabayResponse.PixabayResponse;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import retrofit2.Response;

public class ImageService {
    ApiService service;
    @Inject
    public ImageService(ApiService service) {
        this.service = service;
    }

    public Single<PixabayResponse> getImages(String query, String category, String orientation, int page, int per_page) {
        return service.getImages(Config.API_KEY, query, category, orientation, page, per_page);
    }

}
