package com.example.android.jitsbankingtime;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.android.jitsbankingtime.databinding.ActivityMainBinding;
import com.example.android.jitsbankingtime.model.Recipe;
import com.example.android.jitsbankingtime.ui.adapters.RecipesListAdapter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

import static com.example.android.jitsbankingtime.utils.ConstantsDefined.EXTRA_RECIPE;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, RecipesListAdapter.RecipeAdapterOnClickHandler
{

    private RecyclerView recipesListRecyclerView;
    private RecipesListAdapter recipesListAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ActivityMainBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Inflate the content view (replacing `setContentView`)
        //setContentView(R.layout.activity_main);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        //recipesListRecyclerView = findViewById(R.id.recycler_view_recipes);
        recipesListRecyclerView = binding.recyclerViewRecipes;
        recipesListAdapter = new RecipesListAdapter(this);
        setupUIForRecyclerViewRecipesList();

        //swipeRefreshLayout = findViewById(R.id.swipe_container_main);
        swipeRefreshLayout = binding.swipeContainerMain;
        //Add the swipe to refresh listener
        swipeRefreshLayout.setOnRefreshListener(this);

        /*
        //retrieve the data to be displayed
         */
        retrieveAndLoadJson();
        recipesListRecyclerView.setAdapter(recipesListAdapter);

    }

    private void setupUIForRecyclerViewRecipesList() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recipesListRecyclerView.setLayoutManager(linearLayoutManager);
        recipesListRecyclerView.setHasFixedSize(true);
    }

    private void retrieveAndLoadJson() {
        //retrieve the data
        JItsBakingTimeApp.sApiClient.getRecipes(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> recipesResponse) {
                int statusCode = recipesResponse.code();
                if (recipesResponse.isSuccessful()) {
                    Timber.d("recipes response successful");
                    //We clear the adapter list first
                    recipesListAdapter.clearList();
                    List<Recipe> recipesList = recipesResponse.body();
                    recipesListAdapter.setRecipesList(recipesList);
                    Timber.d( "Number of Recipes Received: %s", recipesList.size());

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
                Timber.d( "HTTP request failed: %s", t.getMessage());
                //Toast.makeText(application.getApplicationContext(), R.string.connectivity_error_text,
                //       Toast.LENGTH_LONG).show();
                //TODO - implement Alert
                /*
                / if we have a network error, prompt a dialog asking to retry or exit
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage(R.string.main_no_network)
                        .setNegativeButton(R.string.main_no_network_try_again, (dialog, id) -> loadJSON())
                        .setPositiveButton(R.string.main_no_network_close, (dialog, id) -> finish());
                builder.create().show();
                 */

            }
        });
    }

    @Override
    public void onRefresh() {
        Timber.d("in the SwipeRefreshLayout onRefreshListener");

        //When the Json is received, the adapter list is updated
        retrieveAndLoadJson();
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onClick(Recipe currentRecipe) {
        Timber.d("inside the onClick in MainActivity");

        Context context = this;
        Class destinationClass = RecipeDetailActivity.class;
        Intent intentToStartRecipeDetailActivity = new Intent(context, destinationClass);

        //Pass the Data to the Detail activity
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_RECIPE, currentRecipe);

        intentToStartRecipeDetailActivity.putExtra(EXTRA_RECIPE, bundle);
        startActivity(intentToStartRecipeDetailActivity);


    }
}