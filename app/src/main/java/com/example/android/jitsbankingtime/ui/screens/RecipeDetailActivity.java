package com.example.android.jitsbankingtime.ui.screens;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.android.jitsbankingtime.R;
import com.example.android.jitsbankingtime.databinding.ActivityRecipeDetailBinding;
import com.example.android.jitsbankingtime.model.Recipe;
import com.example.android.jitsbankingtime.model.Step;
import com.example.android.jitsbankingtime.ui.adapters.DetailPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.squareup.picasso.Picasso;

import timber.log.Timber;

import static com.example.android.jitsbankingtime.utils.ConstantsDefined.EXTRA_RECIPE;
import static com.example.android.jitsbankingtime.utils.ConstantsDefined.EXTRA_STEP;

public class RecipeDetailActivity extends AppCompatActivity implements StepsListFragment.OnStepClickListener {

    private ActivityRecipeDetailBinding binding;
    private Recipe recipe;

    //A single-pane display refers to phone screens, and two-pane to larger tablet screens
    private boolean mTwoPane;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_recipe_detail);


        //TODO initOnClickListenersForTextViews();

        // Get the recipe data that was sent from the MainActivity
        populateRecipeFromIntent();

        //Handle the two pane case
        if (binding.stepDetailContainer != null) {
            // The detail container view will be present only in the
            // large-screen layouts (>600dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            Timber.d("this is the 2 pane mode");
            mTwoPane = true;
            if (savedInstanceState == null) {
                Timber.d("we dont have saved instance state");

                //Get the 0th Step
                Step step = recipe.getSteps().get(0);

                //Create the StepDetailFragment
                StepDetailFragment stepDetailFragment = new StepDetailFragment();
                //initialize details of the 0th step in the fragment
                stepDetailFragment.setRecipe(recipe);
                stepDetailFragment.setStep(step);
                stepDetailFragment.setCurrentStepId(0);

                //Add the fragment to its container using a FragmentManager and a Transaction
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .add(R.id.step_detail_container, stepDetailFragment)
                        .commit();
            }
        } else {
            mTwoPane = false;

            binding.tabLayout.addTab(binding.tabLayout.newTab());
            binding.tabLayout.addTab(binding.tabLayout.newTab());

            // Set gravity for the TabLayout
            binding.tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

            // Find the view pager that will allow the user to swipe between fragments
            //ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
            binding.tabLayout.setupWithViewPager(binding.viewPager);


            // Create an adapter that knows which fragment should be shown on each page
            DetailPagerAdapter adapter = new DetailPagerAdapter(getSupportFragmentManager(),
                    FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, binding.tabLayout.getTabCount());

            // Set the adapter onto the view pager
            binding.viewPager.setAdapter(adapter);

            // Display the number of servings
            binding.textViewServings.setText(String.valueOf(recipe.getServings()));

            // Setup TabLayout with ViewPager
            setupUI();

            setCollapsingToolbarTextColor();

        }
        // Get the recipe data that was sent from the MainActivity
        //TODO why is this called a second time populateRecipeFromIntent();

        // Set the title for a selected recipe
        setTitle(recipe.getName());

        // Show the up button in the actionbar
        showUpButton();
    }

    private void showUpButton() {
        if (mTwoPane == false) {
            setSupportActionBar(binding.toolbar);
        }
        //getSupportActionBar().setDisplayShowTitleEnabled(true);
        //getSupportActionBar().setTitle("now this");
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(true);
            ;
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }

    }

    //Use different inputs from the current recipe object to display the recipe details to the user
    private void setupUI() {
        //starting with the image
        displayRecipeImage();

        // lots more to do TODO


    }

    private void displayRecipeImage() {
        String url = recipe.getImage();
        if (url.isEmpty() || url.trim().equals("")) {
            //use a default image
            binding.recipeImageView.setImageResource(R.drawable.recipe_placeholder_icon);
        } else {
            Picasso.get()
                    .load(url)
                    .error(R.drawable.recipe_placeholder_icon)
                    .placeholder(R.drawable.recipe_placeholder_icon)
                    .into(binding.recipeImageView);
        }
    }

    private void populateRecipeFromIntent() {
        Intent intentThatStartedThisActivity = getIntent();
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

    public Recipe getRecipe() {
        return recipe;
    }

    /**
     * Sets the text color to white for the expanded title and sets the text color to primary dark
     * for the collapsed title.
     *
     * Reference: @see "https://stackoverflow.com/questions/43874025/toolbar-title-text-color"
     */
    private void setCollapsingToolbarTextColor() {
        binding.collapsingToolbarLayout.setTitleEnabled(true);
        //binding.collapsingToolbarLayout.setTitle("Jharna1");
        binding.collapsingToolbarLayout.setExpandedTitleColor(
                getResources().getColor(R.color.white));
        binding.collapsingToolbarLayout.setCollapsedTitleTextColor(
                getResources().getColor(R.color.colorPrimaryDark));
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

    @Override
    public boolean onSupportNavigateUp() {
        return super.onSupportNavigateUp();
    }

    @Override
    public void onStepSelected(Step stepClickedOn) {
        if (stepClickedOn != null) {
            if (mTwoPane) {
                // this is for tablets
                StepDetailFragment stepDetailFragment = new StepDetailFragment();
                stepDetailFragment.setCurrentStepId(stepClickedOn.getId());
                stepDetailFragment.setStep(stepClickedOn);
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.step_detail_container, stepDetailFragment)
                        .commit();

            } else {
                //this is for phones
                //start the StepDetailActivity
                Context context = this;
                Class destinationClass = StepDetailActivity.class;
                Intent intentToStartDetailActivity = new Intent(context, destinationClass);

                // Pass the Data to the DetailActivity
                Bundle bundle = new Bundle();
                bundle.putParcelable(EXTRA_STEP, stepClickedOn);
                bundle.putParcelable(EXTRA_RECIPE, recipe);

                intentToStartDetailActivity.putExtra(EXTRA_RECIPE, bundle);
                intentToStartDetailActivity.putExtra(EXTRA_STEP, bundle);
                startActivity(intentToStartDetailActivity);
            }
        } else {
            Timber.e("Step was clicked, but it is null, doing nothing..");
        }

    }
}
