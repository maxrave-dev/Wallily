package com.maxrave.wallily.viewModel;

import static com.maxrave.wallily.data.FirebaseRepository.getAccountFromHashMap;
import static com.maxrave.wallily.data.datastore.DataStoreManager.FALSE;

import android.app.Application;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.Toast;

import androidx.datastore.preferences.rxjava3.RxPreferenceDataStoreBuilder;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelKt;
import androidx.paging.Pager;
import androidx.paging.PagingConfig;
import androidx.paging.PagingData;
import androidx.paging.PagingLiveData;
import androidx.paging.rxjava3.PagingRx;

import com.airbnb.lottie.L;
import com.maxrave.wallily.data.FirebaseRepository;
import com.maxrave.wallily.data.MainRepository;
import com.maxrave.wallily.data.datastore.DataStoreManager;
import com.maxrave.wallily.data.model.firebase.Account;
import com.maxrave.wallily.data.model.pixabayResponse.Hit;
import com.maxrave.wallily.pagination.PicturePagingSource;

import java.util.HashMap;
import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import kotlinx.coroutines.CoroutineScope;

@HiltViewModel
public class SharedViewModel extends AndroidViewModel {
    private final Application application;

    private CompositeDisposable disposable;

    private final MainRepository mainRepository;
    private final FirebaseRepository firebaseRepository;

    public String query;

    private LiveData<PagingData<Hit>> dataList;

    public MutableLiveData<Bitmap> bitmapLiveData;

    public String keyword;

    CoroutineScope viewModelScope;
    public MutableLiveData<Hit> picture;
    public Flowable<PagingData<Hit>> picturePagingDataFlowable;

    DataStoreManager dataStoreManager;
    @Inject
    public SharedViewModel(Application application, MainRepository mainRepository) {
        super(application);
        this.application = application;
        this.mainRepository = mainRepository;
        this.firebaseRepository = FirebaseRepository.getInstance();
        picture = new MutableLiveData<>();
        bitmapLiveData = new MutableLiveData<>();
        loggedIn = new MutableLiveData<>();
        account = new MutableLiveData<>();
        keyword = null;
        Log.d("SharedViewModel", "SharedViewModel: Initialized");
        viewModelScope = ViewModelKt.getViewModelScope(this);
        disposable = new CompositeDisposable();
        DataStoreManager.getInstance().setDataStore(new RxPreferenceDataStoreBuilder(application, "wallily").build());
        dataStoreManager = DataStoreManager.getInstance();
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
    private MutableLiveData<String> loggedIn;

    public MutableLiveData<String> getLoggedInLiveData() {
        if (loggedIn == null) {
            loggedIn = new MutableLiveData<String>();
        }
        return loggedIn;
    }

    public void getLoggedIn() {
        disposable.add(dataStoreManager.getLoggedIn().first(FALSE).subscribeOn(Schedulers.io()).subscribe(
                s -> loggedIn.postValue(s),
                throwable -> Log.d("SharedViewModel", "getLoggedIn: " + throwable.getMessage())
        ));
    }

     public void setLoggedIn(Boolean logged) {
        if (logged) loggedIn.postValue(DataStoreManager.TRUE);
        else loggedIn.postValue(DataStoreManager.FALSE);
        disposable.add(
                 dataStoreManager.setLoggedIn(logged).subscribeOn(Schedulers.io()).subscribe(
                         result -> {
                             Log.d("SharedViewModel", "setLoggedIn: " + result);
                         },
                         throwable ->
                                 Log.d("SharedViewModel", "setLoggedIn: " + throwable.getMessage())
                 )
         );
     }

     private MutableLiveData<Account> account;
    public MutableLiveData<Account> getAccountLiveData() {
        if (account == null) {
            account = new MutableLiveData<Account>();
        }
        return account;
    }
     public void getAccount() {
         firebaseRepository.getAccount().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.d("SharedViewModel", "getAccount: " + task.getResult().getDocuments().toString());
                if (task.getResult().getDocuments().size() > 0) {
                    Account acc = getAccountFromHashMap((HashMap) task.getResult().getDocuments().get(0).get("account"));
                    Log.w("SharedViewModel", "getAccount: " + acc.toString());
                    account.postValue(acc);
                }
            }
         });
     }

    public void getData(String keyword, String query) {
        Pager<Integer, Hit> pager = new Pager<>(new PagingConfig(40), () -> new PicturePagingSource(mainRepository, keyword, query));

        dataList = PagingLiveData.cachedIn(PagingLiveData.getLiveData(pager), viewModelScope);

        picturePagingDataFlowable = PagingRx.getFlowable(pager);
        PagingRx.cachedIn(picturePagingDataFlowable, viewModelScope);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
    }

    public void createNewAccount(Account account) {
         firebaseRepository.createNewAccount(account);
         getAccount();
    }

    public void removeAccount() {
        account.postValue(null);
    }
}
