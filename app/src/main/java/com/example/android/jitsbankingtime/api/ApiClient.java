package com.example.android.jitsbankingtime.api;

import android.util.Log;

import com.example.android.jitsbankingtime.model.Recipe;
import com.example.android.jitsbankingtime.utils.ConstantsDefined;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

public class ApiClient {
    private static ApiClient sApiClient;
    private static RecipeService recipeApiService;

    private ApiClient() {
        setupRetrofit();
    }

    private void setupRetrofit() {
        Timber.d("JKM - setting up retrofit from ApiClient");
        Gson gson = new GsonBuilder().serializeNulls().create();

        //Trying to log the network requests
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();

        //Retrofit instance
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ConstantsDefined.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();

        //Using retrofit we can create our api class
        recipeApiService = retrofit.create(RecipeService.class);
    }

    public static ApiClient getInstance(){
        if(sApiClient == null){
            sApiClient = new ApiClient();
        }
        return sApiClient;
    }

    public void getRecipes(Callback<List<Recipe>> callback) {
        Call<List<Recipe>> recipesCall = recipeApiService.getRecipes();
        recipesCall.enqueue(callback);

    }
}
