package com.example.android.jitsbankingtime.utils;

import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;

import com.example.android.jitsbankingtime.testIdlingResource.SimpleIdlingResource;

public class IdlingResourceUtils {

    //Used only for testing
    @VisibleForTesting
    @Nullable
    private static SimpleIdlingResource sIdlingResource;

    public static SimpleIdlingResource getIdlingResource() {
        if (sIdlingResource == null) {
            sIdlingResource = new SimpleIdlingResource();
        }
        return sIdlingResource;
    }

    //Used only for testing
    @VisibleForTesting
    public static void setIdlingResource(boolean isIdleNow) {
        if (sIdlingResource == null) {
            sIdlingResource = new SimpleIdlingResource();
        }
        sIdlingResource.setIdleState(isIdleNow);
    }
}
