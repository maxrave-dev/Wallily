package com.maxrave.wallily.di;

import android.content.Context;

import androidx.room.Room;

import com.maxrave.wallily.data.db.PictureDao;
import com.maxrave.wallily.data.db.PictureDatabase;

import javax.inject.Singleton;

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
    PictureDatabase provideMusicDatabase(@ApplicationContext Context context) {
        return Room.databaseBuilder(context, PictureDatabase.class, "Wallily DB")
                .build();
    }

    @Provides
    @Singleton
    PictureDao provideDatabaseDao(PictureDatabase pictureDatabase) {
        return pictureDatabase.pictureDao();
    }

}
