package com.maxrave.wallily.data.api;

import com.maxrave.wallily.data.model.pixabayResponse.PixabayResponse;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    @GET("/api/")
    Single<PixabayResponse> getImages(@Query("key") String key, @Query("q") String query, @Query("category") String category, @Query("orientation") String orientation, @Query("page") int page, @Query("per_page") int per_page);

    @GET("/api/")
    Single<PixabayResponse> getImageById(@Query("key") String apiKey, @Query("id") String id);
}
