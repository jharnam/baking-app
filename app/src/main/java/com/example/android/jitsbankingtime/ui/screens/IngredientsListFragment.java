package com.example.android.jitsbankingtime.ui.screens;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.jitsbankingtime.R;
import com.example.android.jitsbankingtime.databinding.FragmentIngredientsBinding;
import com.example.android.jitsbankingtime.model.Ingredient;
import com.example.android.jitsbankingtime.model.Recipe;
import com.example.android.jitsbankingtime.ui.adapters.IngredientsListAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import timber.log.Timber;

import static com.example.android.jitsbankingtime.utils.ConstantsDefined.EXTRA_RECIPE;

public class IngredientsListFragment extends Fragment {
    private FragmentIngredientsBinding binding;
    private Recipe recipe;
    private IngredientsListAdapter ingredientsListAdapter;
    private RecyclerView ingredientsListRecyclerView;

    public IngredientsListFragment() {
        //Required empty constructor
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //populateRecipeFromIntent();
        populateRecipeDetails();


    }

    private void populateRecipeDetails() {
        if (recipe == null){
            recipe =((RecipeDetailActivity) Objects.requireNonNull(getActivity())).getRecipe();
        }
    }

    private void populateRecipeFromIntent() {
        Intent intentThatStartedThisActivity = getActivity().getIntent();
        Bundle extras;
        if (intentThatStartedThisActivity != null) {
            if (intentThatStartedThisActivity.hasExtra(EXTRA_RECIPE)) {
                extras = intentThatStartedThisActivity.getBundleExtra(EXTRA_RECIPE);
                if (extras != null) {
                    /*Also put populate the current recipe object*/
                    recipe = (Recipe) extras.getParcelable(EXTRA_RECIPE);
                    Timber.d("This recipe: " + recipe.getName());
                    return;
                }
            }
        }
        //Error case
        Timber.e("Intent did not have valid recipe data");

    }

    /* 	OnCreateView() is where a fragment inflates its UI, hooks up any data sources it needs
    and returns the created view to the host activity.
     */

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        //inflate the UI
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_ingredients, container, false);

        //TODO should we call this again?
        populateRecipeDetails();

        ingredientsListRecyclerView = binding.recyclerViewIngredients;

        // Initialize a IngredientAdapter
        initAdapter();
        
       //return super.onCreateView(inflater, container, savedInstanceState);
        return binding.getRoot();
    }

    private void initAdapter() {
        // Create a new list of the ingredients
        List<Ingredient> ingredients = new ArrayList<>();

        // The IngredientsAdapter is responsible for displaying each ingredient in the list
        ingredientsListAdapter = new IngredientsListAdapter();

        setupUIForRecyclerViewIngredientsList();

        ingredientsListRecyclerView.setAdapter(ingredientsListAdapter);
        // Add a list of ingredients to the IngredientsAdapter
        ingredientsListAdapter.setIngredientsList(recipe.getIngredients());

        /*
        // A LinearLayoutManager is responsible for measuring and positioning item views within a
        // RecyclerView into a linear list.
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        // Set the layout manager to the RecyclerView
        mIngredientBinding.rvIngredients.setLayoutManager(layoutManager);
        // Use this setting to improve performance if you know that changes in content do not
        // change the child layout size in the RecyclerView
        mIngredientBinding.rvIngredients.setHasFixedSize(true);

         */

    }

    private void setupUIForRecyclerViewIngredientsList() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext());
        ingredientsListRecyclerView.setLayoutManager(linearLayoutManager);
        ingredientsListRecyclerView.setHasFixedSize(true);
    }
}
