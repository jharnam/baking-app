package com.example.android.jitsbankingtime.api;

import androidx.annotation.Nullable;

import java.io.IOException;

import retrofit2.Response;
import timber.log.Timber;

/**
 * Common class used by API responses.
 * @param <T>
 */
/*
public class ApiResponse<T> {
    public final int code;
    @Nullable
    public final T body;
    @Nullable
    public final Throwable error;

    public ApiResponse(@Nullable Throwable error) {
        this.error = error;
        code = 500;
        body = null;
    }

    public ApiResponse(Response<T> response) {
        code = response.code();
        if (response.isSuccessful()) {
            body = response.body();
            error = null;
        } else {
            String message = null;
            if (response.errorBody() != null) {
                try{
                    message = response.errorBody().string();
                } catch (IOException exception) {
                    Timber.e(exception, "Error when passing API response");
                }
                if (message == null || message.trim().length() == 0) {
                    message = response.message();
                }
                error = new IOException(message);
                body = null;
            }
        }
    }

    public boolean isSuccessful() {
        return (code >= 200 && code < 300);
    }

    public int getCode() {
        return code;
    }

    @Nullable
    public T getBody() {
        return body;
    }

    @Nullable
    public Throwable getError() {
        return error;
    }
}

 */
