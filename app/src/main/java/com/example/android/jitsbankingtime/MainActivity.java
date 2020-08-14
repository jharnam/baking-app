package com.example.android.jitsbankingtime;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.android.jitsbankingtime.api.ApiClient;
import com.example.android.jitsbankingtime.model.Recipe;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recipesListRecyclerView;
    private RecipesListAdapter recipesListAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recipesListRecyclerView = findViewById(R.id.recycler_view_recipes);
        recipesListAdapter = new RecipesListAdapter();
        setupUIForRecyclerViewRecipesList();

        //retrieve the data
        JItsBakingTimeApp.sApiClient.getRecipes(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> recipesResponse) {
                int statusCode = recipesResponse.code();
                if (recipesResponse.isSuccessful()) {
                    Timber.d("recipes response successful");
                    List<Recipe> recipesList = recipesResponse.body();
                    recipesListAdapter.setRecipesList(recipesList);
                    Timber.d( "Number of Recipes Received: " + recipesList.size());

                    //enableMoviePostersRecyclerView();
                    //moviePosterRecyclerView.setAdapter(movieOuterAdapter);
                    //if (mMovieAdapter == null) {
                        //mMovieAdapter = new MovieAdapter(movieList, MainActivity.this, context,false);

                        //mRecyclerView.setAdapter(mMovieAdapter);
                        //mRecyclerView.setHasFixedSize(true);

                    //} else {
                    //    mMovieAdapter.updateRecyclerData(movieList,false);
                    //    mMovieAdapter.notifyDataSetChanged();
                    //}
                } else {

                    Timber.e("Failed to load list of recipes");
                    //showErrorMessage("Failed to Load list of recipes, Status Code=" + statusCode);
                }

            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                Timber.d( "HTTP request failed: " + t.getMessage());
                //Toast.makeText(application.getApplicationContext(), R.string.connectivity_error_text,
                 //       Toast.LENGTH_LONG).show();

            }
        });
        recipesListRecyclerView.setAdapter(recipesListAdapter);

    }

    private void setupUIForRecyclerViewRecipesList() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recipesListRecyclerView.setLayoutManager(linearLayoutManager);
        recipesListRecyclerView.setHasFixedSize(true);
    }
}