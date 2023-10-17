package com.maxrave.wallily.di;

import android.content.Context;

import androidx.room.Room;

import com.maxrave.wallily.data.datastore.DataStoreManager;
import com.maxrave.wallily.data.db.PictureDao;
import com.maxrave.wallily.data.db.PictureDatabase;

import javax.inject.Singleton;

import coil.ImageLoader;
import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class LocalModule {
    @Provides
    @Singleton
    public static PictureDatabase provideMusicDatabase(@ApplicationContext Context context) {
        return Room.databaseBuilder(context, PictureDatabase.class, "Wallily DB")
                .build();
    }

    @Provides
    @Singleton
    public static PictureDao provideDatabaseDao(PictureDatabase pictureDatabase) {
        return pictureDatabase.pictureDao();
    }
}
