package com.example.android.jitsbankingtime;

import android.app.Application;

import com.example.android.jitsbankingtime.api.ApiClient;

import timber.log.Timber;

/**
 * Android Application class. Used for accessing singletons.
 */

public class JItsBakingTimeApp extends Application {


    //TODO 1 - temporary - see if this needs to stay here - after we addd a datarepository
    public static ApiClient sApiClient;

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }

        sApiClient = ApiClient.getInstance();

    }
}
