package com.maxrave.wallily.data.datastore;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.datastore.preferences.core.MutablePreferences;
import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.preferences.core.PreferencesKeys;
import androidx.datastore.preferences.rxjava3.RxPreferenceDataStoreBuilder;
import androidx.datastore.rxjava3.RxDataStore;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.hilt.android.qualifiers.ApplicationContext;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

public class DataStoreManager {

    public static final String TRUE = "TRUE";
    public static final String FALSE = "FALSE";

    RxDataStore<Preferences> dataStore;
    private static final DataStoreManager ourInstance = new DataStoreManager();
    public static DataStoreManager getInstance() {
        return ourInstance;
    }
    private DataStoreManager() { }
    public void setDataStore(RxDataStore<Preferences> datastore) {
        this.dataStore = datastore;
    }
    public RxDataStore<Preferences> getDataStore() {
        return dataStore;
    }

    private Preferences.Key<String> LOGGED_IN = PreferencesKeys.stringKey("logged_in");

    public Flowable<String> getLoggedIn() {
        if (dataStore == null) {
            return Flowable.just(FALSE);
        }
        else {
            return dataStore.data().map(prefs -> prefs.get(LOGGED_IN));
        }
    }

    public Single<Preferences> setLoggedIn(Boolean logged) {
        return dataStore.updateDataAsync(prefsIn -> {
            MutablePreferences mutablePreferences = prefsIn.toMutablePreferences();
            String currentInt = prefsIn.get(LOGGED_IN);
            mutablePreferences.set(LOGGED_IN, currentInt != null ? (logged ? TRUE : FALSE) : FALSE);
            return Single.just(mutablePreferences);
        });
    }
// The update is completed once updateResult is completed.
}
