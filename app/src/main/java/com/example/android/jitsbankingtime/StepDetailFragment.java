package com.example.android.jitsbankingtime;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.android.jitsbankingtime.databinding.FragmentStepDetailBinding;
import com.example.android.jitsbankingtime.model.Recipe;
import com.example.android.jitsbankingtime.model.Step;

import java.util.Objects;

public class StepDetailFragment extends Fragment {
    FragmentStepDetailBinding binding;
    Step step;
    Recipe recipe;

    //required empty constructor
    public StepDetailFragment() {

    }

    /* 	OnCreateView() is where a fragment inflates its UI, hooks up any data sources it needs
    and returns the created view to the host activity.
     */

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        //inflate the fragment layout
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_step_detail, container, false);

        //TODO should we call this again?
        populateRecipeAndStepDetails();


        //get a reference to the textview in the fragment layout
        TextView stepDescriptionTextView = binding.textViewStepDescription;
        stepDescriptionTextView.setText(step.getDescription());

        //handler for previous button click
        onPreviousButtonClick();

        return binding.getRoot();
    }

    private void onPreviousButtonClick() {
        //binding.buttonPrevious.setOnClickListener();
    }

    private void populateRecipeAndStepDetails() {
        if (recipe == null){
            recipe =((StepDetailActivity) Objects.requireNonNull(getActivity())).getRecipe();
        }
        if (step == null) {
            step =((StepDetailActivity) Objects.requireNonNull(getActivity())).getStep();
        }

    }


}
