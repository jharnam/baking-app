package com.example.android.jitsbankingtime.ui.screens;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;

import com.example.android.jitsbankingtime.R;
import com.example.android.jitsbankingtime.databinding.ActivityStepDetailBinding;
import com.example.android.jitsbankingtime.model.Recipe;
import com.example.android.jitsbankingtime.model.Step;

import timber.log.Timber;

import static com.example.android.jitsbankingtime.utils.ConstantsDefined.EXTRA_RECIPE;
import static com.example.android.jitsbankingtime.utils.ConstantsDefined.EXTRA_STEP;

public class StepDetailActivity extends AppCompatActivity {
    ActivityStepDetailBinding binding;
    Step step;
    Recipe recipe;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_step_detail);

        // Get the recipe, step data that was sent from the launching activity/fragment
        populateStepDetailsFromIntent();

        //Set the title for the selected recipe
        setTitle(recipe.getName());

        //Show the back arrow button in the actionbar
        showUpButton();

        //Create the StepDetailFragment
        StepDetailFragment stepDetailFragment = new StepDetailFragment();
        //Add the fragment to its container using a FragmentManager and a Transaction
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.step_container, stepDetailFragment)
                .commit();

    }

    private void populateStepDetailsFromIntent() {

        Intent intentThatStartedThisActivity = getIntent();
        Bundle extras;
        if (intentThatStartedThisActivity != null) {
            if (intentThatStartedThisActivity.hasExtra(EXTRA_RECIPE)) {
                extras = intentThatStartedThisActivity.getBundleExtra(EXTRA_RECIPE);
                if (extras != null) {
                    /*Also put populate the current recipe object*/
                    recipe = (Recipe) extras.getParcelable(EXTRA_RECIPE);
                    Timber.d("This recipe: " + recipe.getName());

                    //Get current step details
                    if (intentThatStartedThisActivity.hasExtra(EXTRA_STEP)) {
                        extras = intentThatStartedThisActivity.getBundleExtra(EXTRA_STEP);
                        if (extras != null) {
                            /*Also put populate the current recipe object*/
                            step = (Step) extras.getParcelable(EXTRA_STEP);
                            Timber.d("This step: " + step.getShortDescription());
                            return;
                        }
                    }
                    //Has the recipe details, but no step details
                    //Error case
                    Timber.e("Intent did not have valid step data");
                    return;
                }
            }

        }
        //Error case
        Timber.e("Intent did not have valid recipe data");
    }

    private void showUpButton() {
        ActionBar actionBar = getSupportActionBar();
        //getSupportActionBar().setDisplayShowTitleEnabled(true);
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }

    }

    public Recipe getRecipe() {
        return recipe;
    }

    public Step getStep() {
        return step;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.home) {
            onBackPressed();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}
