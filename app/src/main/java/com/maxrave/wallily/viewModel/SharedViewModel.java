package com.maxrave.wallily.viewModel;

import static com.maxrave.wallily.data.FirebaseRepository.getAccountFromHashMap;
import static com.maxrave.wallily.data.FirebaseRepository.getCollectionsListFromHashMap;
import static com.maxrave.wallily.data.datastore.DataStoreManager.FALSE;

import android.app.Application;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.CountDownTimer;
import android.util.Log;
import android.util.Pair;
import android.widget.Toast;

import androidx.arch.core.internal.SafeIterableMap;
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
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.maxrave.wallily.data.FirebaseRepository;
import com.maxrave.wallily.data.MainRepository;
import com.maxrave.wallily.data.datastore.DataStoreManager;
import com.maxrave.wallily.data.model.firebase.Account;
import com.maxrave.wallily.data.model.firebase.Collections;
import com.maxrave.wallily.data.model.firebase.HitId;
import com.maxrave.wallily.data.model.pixabayResponse.Hit;
import com.maxrave.wallily.data.model.pixabayResponse.PixabayResponse;
import com.maxrave.wallily.pagination.PicturePagingSource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
        listCollections = new MutableLiveData<>();
        collectionId = new MutableLiveData<>();
        hitsFromCollection = new MutableLiveData<>();
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

    public MutableLiveData<String> collectionId;

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

    public void logOut() {
        account.postValue(null);
    }
    public void removeAccount() {
        firebaseRepository.removeAccount();
        account.postValue(null);
    }

    public void uploadImage(Uri uri) {
        Pair<UploadTask, StorageReference> pair = firebaseRepository.uploadImage(uri);
        if (pair != null) {
            UploadTask uploadTask = pair.first;
            StorageReference ref = pair.second;
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@androidx.annotation.NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful() && task.getException() != null) {
                                Log.e("SharedViewModel", "then: " + task.getException().getMessage());
                            }

                            // Continue with the task to get the download URL
                            return ref.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@androidx.annotation.NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                Uri downloadUri = task.getResult();
                                updateAccount(new Account(account.getValue().getEmail(), account.getValue().getUid(), account.getValue().getDisplay_name(), downloadUri.toString()));
                            } else {
                                if (task.getException() != null) {
                                    Log.e("SharedViewModel", "onComplete: " + task.getException().getMessage());
                                }
                            }
                        }
                    });
                }
            });
        }
    }

    public void updateAccount(Account account) {
        firebaseRepository.findDocument().addOnCompleteListener(task -> {
           if (task.isSuccessful()) {
               firebaseRepository.updateAccount(account, task.getResult().getDocuments().get(0).getId()).addOnCompleteListener(taskUpdate -> {
                  if (taskUpdate.isSuccessful()) {
                      Log.d("SharedViewModel", "updateAccount: " + task.getResult());
                      getAccount();
                  }
                  else {
                      if (taskUpdate.getException() != null) {
                          Log.d("SharedViewModel", "updateAccount: " + taskUpdate.getException().getMessage());
                      }
                  }
               });
           }
           else {
               if (task.getException() != null) {
                   Log.d("SharedViewModel", "updateAccount: " + task.getException().getMessage());
               }
           }
        });
    }
    private MutableLiveData<ArrayList<Collections>> listCollections;
    public MutableLiveData<ArrayList<Collections>> getListCollectionsLiveData() {
        return listCollections;
    }

    public void getListCollections() {
        firebaseRepository.findDocument().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                String id = task.getResult().getDocuments().get(0).getId();
                firebaseRepository.getDataOfDocument(id).addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful()) {
                        if (task1.getResult().get("collections") != null) {
                            Log.w("SharedViewModel", "getListCollections: " + task1.getResult().get("collections").toString());
                            listCollections.postValue(getCollectionsListFromHashMap((ArrayList<Object>) task1.getResult().get("collections")));
                        }
                    }
                    else {
                        if (task1.getException() != null) {
                            Log.d("SharedViewModel", "getListCollections: " + task1.getException().getMessage());
                        }
                    }
                });
            }
            else  {
                if (task.getException() != null) {
                    Log.d("SharedViewModel", "getListCollections: " + task.getException().getMessage());
                }
            }
        });
    }

    public void createCollection(String name) {
        int count;
        if (listCollections.getValue() != null) {
            count = listCollections.getValue().size();
        } else {
            count = 0;
        }
        firebaseRepository.findDocument().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                String id = task.getResult().getDocuments().get(0).getId();
                firebaseRepository.createCollection(id, name, id + count).addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful()) {
                        getListCollections();
                    }
                    else {
                        if (task1.getException() != null) {
                            Log.d("SharedViewModel", "createCollection: " + task1.getException().getMessage());
                        }
                    }
                });
            }
            else {
                if (task.getException() != null) {
                    Log.d("SharedViewModel", "createCollection: " + task.getException().getMessage());
                }
            }
        });
    }

    public Collections getCollectionData(String collectionId) {
        int index = -1; 
        ArrayList<Collections> temp = getListCollectionsLiveData().getValue();
        if (temp != null) {
            for (int i = 0; i < temp.size(); i++) {
                if (temp.get(i).getId().equals(collectionId)) {
                    index = i;
                    return temp.get(i);
                }
            }
        }
        return null;
    }
    public MutableLiveData<ArrayList<Hit>> hitsFromCollection;

    public void getHitsFromCollection(List<HitId> listHitsId) {
         ArrayList<String> ids = new ArrayList<>();
         listHitsId.forEach(hitId -> {
             ids.add(hitId.getId());
         });
         disposable.add(
            mainRepository.getImageById(ids).subscribeOn(Schedulers.io()).subscribe(data -> {
                ArrayList<Hit> hitList = new ArrayList<>();
                data.forEach(response -> {
                    hitList.addAll(response.getHits());
                });
                hitsFromCollection.postValue(hitList);
            })
         );
    }

    public void addPictureToCollection(Hit value, String id) {
        firebaseRepository.findDocument().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                String document = task.getResult().getDocuments().get(0).getId();
                ArrayList<Collections> collections = getListCollectionsLiveData().getValue();
                Collections collection = getCollectionData(id);
                assert collections != null;
                collections.remove(collection);
                List<HitId> temp;
                if (collection.getListHitsId() == null) {
                    temp = new ArrayList<>();
                }
                else {
                    temp = collection.getListHitsId();
                }
                temp.add(new HitId(String.valueOf(value.getId()), value.getLargeImageURL()));
                collection.setListHitsId(temp);
                collections.add(collection);

                firebaseRepository.updateCollection(collections, document).addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful()) {
                        getListCollections();
                        Toast.makeText(application, "Added to collection", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        if (task1.getException() != null) {
                            Log.d("SharedViewModel", "addPictureToCollection: " + task1.getException().getMessage());
                        }
                    }
                });
            }
            else {
                if (task.getException() != null) {
                    Log.d("SharedViewModel", "createCollection: " + task.getException().getMessage());
                }
            }
        });
    }

    public void removeHitFromCollection(String collectionId, Hit item) {
        firebaseRepository.findDocument().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                String document = task.getResult().getDocuments().get(0).getId();
                ArrayList<Collections> collections = getListCollectionsLiveData().getValue();
                Collections collection = getCollectionData(collectionId);
                assert collections != null;
                collections.remove(collection);
                List<HitId> temp;
                if (collection.getListHitsId() == null) {
                    temp = new ArrayList<>();
                }
                else {
                    temp = collection.getListHitsId();
                }
                temp.remove(collection.getListHitsId().stream().filter(hitId -> hitId.getId().equals(String.valueOf(item.getId()))).findFirst().get());
                collection.setListHitsId(temp);
                collections.add(collection);

                firebaseRepository.updateCollection(collections, document).addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful()) {
                        getListCollections();
                        Toast.makeText(application, "Removed to collection", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        if (task1.getException() != null) {
                            Log.d("SharedViewModel", "addPictureToCollection: " + task1.getException().getMessage());
                        }
                    }
                });
            }
            else {
                if (task.getException() != null) {
                    Log.d("SharedViewModel", "createCollection: " + task.getException().getMessage());
                }
            }
        });
    }
}
