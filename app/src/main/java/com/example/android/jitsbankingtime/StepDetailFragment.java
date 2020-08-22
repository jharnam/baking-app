package com.example.android.jitsbankingtime;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.android.jitsbankingtime.databinding.FragmentStepDetailBinding;
import com.example.android.jitsbankingtime.model.Recipe;
import com.example.android.jitsbankingtime.model.Step;

import java.util.Objects;

public class StepDetailFragment extends Fragment {
    FragmentStepDetailBinding binding;
    Step step;
    int currentStepId;
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

        //handler for next button click
        onNextButtonClick();

        return binding.getRoot();
    }

    private void onNextButtonClick() {

        binding.buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StepDetailFragment stepDetailFragment = new StepDetailFragment();
                if (currentStepId < recipe.getSteps().size() - 1) {
                    currentStepId++;
                    stepDetailFragment.currentStepId = currentStepId;
                    stepDetailFragment.step = recipe.getSteps().get(currentStepId);
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.step_container, stepDetailFragment)
                            .commit();
                } else {
                    Toast.makeText(getContext(), "Can't go further. Nothing after this step!",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void onPreviousButtonClick() {
        binding.buttonPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StepDetailFragment stepDetailFragment = new StepDetailFragment();
                if (currentStepId > 0) {
                    currentStepId--;
                    stepDetailFragment.currentStepId = currentStepId;
                    stepDetailFragment.step = recipe.getSteps().get(currentStepId);
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.step_container, stepDetailFragment)
                            .commit();
                } else {
                    Toast.makeText(getContext(), "Can't go further. Nothing before this step!",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void populateRecipeAndStepDetails() {
        if (recipe == null){
            recipe =((StepDetailActivity) Objects.requireNonNull(getActivity())).getRecipe();
        }
        if (step == null) {
            step = ((StepDetailActivity) Objects.requireNonNull(getActivity())).getStep();
            currentStepId = step.getId();
        }

    }


}
