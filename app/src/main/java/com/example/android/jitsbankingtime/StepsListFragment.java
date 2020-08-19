package com.example.android.jitsbankingtime;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.android.jitsbankingtime.databinding.FragmentStepsBinding;
import com.example.android.jitsbankingtime.model.Recipe;

import java.util.Objects;

import timber.log.Timber;

import static com.example.android.jitsbankingtime.utils.ConstantsDefined.EXTRA_RECIPE;

public class StepsListFragment extends Fragment {
    FragmentStepsBinding binding;
    Recipe recipe;

    //required empty constructor
    public StepsListFragment() {

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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_steps, container, false);

        if (recipe == null){
            recipe =((RecipeDetailActivity) Objects.requireNonNull(getActivity())).getRecipe();
        }
        //return super.onCreateView(inflater, container, savedInstanceState);
        return binding.getRoot();
    }
}

