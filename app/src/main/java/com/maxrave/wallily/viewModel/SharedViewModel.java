package com.maxrave.wallily.viewModel;

import android.app.Application;
import android.graphics.Bitmap;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelKt;
import androidx.paging.Pager;
import androidx.paging.PagingConfig;
import androidx.paging.PagingData;
import androidx.paging.PagingLiveData;
import androidx.paging.rxjava3.PagingRx;

import com.maxrave.wallily.data.MainRepository;
import com.maxrave.wallily.data.model.pixabayResponse.Hit;
import com.maxrave.wallily.pagination.PicturePagingSource;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import kotlinx.coroutines.CoroutineScope;

@HiltViewModel
public class SharedViewModel extends AndroidViewModel {
    private final Application application;

    private final MainRepository mainRepository;

    private LiveData<PagingData<Hit>> dataList;

    public MutableLiveData<Bitmap> bitmapLiveData;


    CoroutineScope viewModelScope;
    public MutableLiveData<Hit> picture;
    public Flowable<PagingData<Hit>> picturePagingDataFlowable;
    @Inject
    public SharedViewModel(Application application, MainRepository mainRepository) {
        super(application);
        this.application = application;
        this.mainRepository = mainRepository;
        picture = new MutableLiveData<>();
        bitmapLiveData = new MutableLiveData<>();
        Log.d("SharedViewModel", "SharedViewModel: Initialized");
        viewModelScope = ViewModelKt.getViewModelScope(this);
        Pager<Integer, Hit> pager = new Pager<>(new PagingConfig(40), () -> new PicturePagingSource(mainRepository));

        dataList = PagingLiveData.cachedIn(PagingLiveData.getLiveData(pager), viewModelScope);

        picturePagingDataFlowable = PagingRx.getFlowable(pager);
        PagingRx.cachedIn(picturePagingDataFlowable, viewModelScope);
    }

    public LiveData<PagingData<Hit>> getDataList() {
        return dataList;
    }

    public void setPicture(Hit hit) {
        picture.postValue(hit);
    }

    public void setBitmap(Bitmap bitmap) {
        bitmapLiveData.postValue(bitmap);
    }
}
