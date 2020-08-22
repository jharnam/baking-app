package com.example.android.jitsbankingtime;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.android.jitsbankingtime.databinding.ActivityStepDetailBinding;
import com.example.android.jitsbankingtime.model.Step;

public class StepDetailActivity extends AppCompatActivity {
    ActivityStepDetailBinding binding;
    Step step;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_step_detail);

        // Get the recipe, step data that was sent from the launching activity/fragment
        populateStepDetailsFromIntent();

        //Set the title for the selected recipe

        //Show the back arrow button in the actionbar

        //Create the StepDetailFragment
    }

    private void populateStepDetailsFromIntent() {
        //TODO
    }
}
