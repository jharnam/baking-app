package com.example.android.jitsbankingtime.ui.screens;

import android.content.Context;
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
import com.example.android.jitsbankingtime.databinding.FragmentStepsBinding;
import com.example.android.jitsbankingtime.model.Recipe;
import com.example.android.jitsbankingtime.model.Step;
import com.example.android.jitsbankingtime.ui.adapters.StepsListAdapter;

import java.util.Objects;

import timber.log.Timber;

import static com.example.android.jitsbankingtime.utils.ConstantsDefined.EXTRA_RECIPE;

public class StepsListFragment extends Fragment implements StepsListAdapter.StepAdapterOnClickHandler {
    FragmentStepsBinding binding;
    Recipe recipe;
    private StepsListAdapter stepsListAdapter;
    private RecyclerView stepsListRecyclerView;

    //Define a new interface that triggers a callback in the host activity
    //This is for communication with/via the host activity (with other fragments)
    private OnStepClickListener stepClickListenerCallback;


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

        //TODO should we call this again?
        //populateRecipeDetails();
        populateRecipeFromIntent();

        stepsListRecyclerView = binding.recyclerViewSteps;

        // Initialize a IngredientAdapter
        initAdapter();

        //return super.onCreateView(inflater, container, savedInstanceState);
        return binding.getRoot();
    }

    private void initAdapter() {

        // The IngredientsAdapter is responsible for displaying each ingredient in the list
        stepsListAdapter = new StepsListAdapter(this);

        setupUIForRecyclerViewStepsList();

        stepsListRecyclerView.setAdapter(stepsListAdapter);
        // Add a list of ingredients to the IngredientsAdapter
        stepsListAdapter.setStepsList(recipe.getSteps());


    }

    private void setupUIForRecyclerViewStepsList() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext());
        stepsListRecyclerView.setLayoutManager(linearLayoutManager);
        stepsListRecyclerView.setHasFixedSize(true);
    }

    @Override
    public void onStepClick(Step stepClickedOn) {
        //Trigger the callback method
        stepClickListenerCallback.onStepSelected(stepClickedOn);
    }

    //to make sure the host activity implements the callback for the OnStepClickListener
    //interface to aid communication between fragment and host activity
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            //if the host activity has not implemented the callback,
            // an exception is thrown
            stepClickListenerCallback = (OnStepClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnStepClickListener");
        }
    }

    //OnStepClickListener interface, calls a method in the host activity
    public interface OnStepClickListener {
        void onStepSelected(Step stepClickedOn);
    }
}
