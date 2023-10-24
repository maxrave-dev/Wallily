package com.maxrave.wallily;

import android.app.Application;

import androidx.appcompat.app.AppCompatDelegate;

import com.maxrave.wallily.ui.MainActivity;

import cat.ereza.customactivityoncrash.config.CaocConfig;
import dagger.hilt.android.HiltAndroidApp;

@HiltAndroidApp
public class WallilyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        CaocConfig.Builder.create()
                .backgroundMode(CaocConfig.BACKGROUND_MODE_SILENT) //default: CaocConfig.BACKGROUND_MODE_SHOW_CUSTOM
                .enabled(true) //default: true
                .showErrorDetails(true) //default: true
                .showRestartButton(true) //default: true
                .errorDrawable(R.drawable.ic_launcher_background)
                .logErrorOnRestart(false) //default: true
                .trackActivities(true) //default: false
                .minTimeBetweenCrashesMs(2000) //default: 3000 //default: bug image
                .restartActivity(MainActivity.class)
                .apply();
    }
}
