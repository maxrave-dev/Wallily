package com.maxrave.wallily.data;

import com.maxrave.wallily.data.api.ImageService;
import com.maxrave.wallily.data.model.pixabayResponse.PixabayResponse;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.rxjava3.core.Single;

@Singleton
public class MainRepository {
    private static final String TAG = "MainRepository";
    private final ImageService imageService;

    @Inject
    public MainRepository(ImageService imageService) {
        this.imageService = imageService;
    }

    public Single<PixabayResponse> getImageByPage(String query, String orientation, int page, int per_page) {
        return imageService.getImages(query, orientation, page, per_page);
    }
}
