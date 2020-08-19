package com.example.android.jitsbankingtime;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.android.jitsbankingtime.databinding.ActivityRecipeDetailBinding;
import com.example.android.jitsbankingtime.model.Recipe;
import com.google.android.material.tabs.TabLayout;
import com.squareup.picasso.Picasso;

import timber.log.Timber;

import static androidx.fragment.app.FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT;
import static com.example.android.jitsbankingtime.utils.ConstantsDefined.EXTRA_RECIPE;

public class RecipeDetailActivity extends AppCompatActivity {

    private ActivityRecipeDetailBinding binding;
    private Recipe recipe;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_recipe_detail);

        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(R.string.tab_label1));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(R.string.tab_label2));

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
        //TODO initOnClickListenersForTextViews();

        // Get the recipe data that was sent from the MainActivity
        populateRecipeFromIntent();

        // Set the title for a selected recipe
        //TODO setTitle(recipe.getName());
        setTitle("Jharna");

        // Display the number of servings
        binding.textViewServings.setText(String.valueOf(recipe.getServings()));

        // Setup TabLayout with ViewPager
        setupUI();

        setCollapsingToolbarTextColor();

        // Show the up button in the actionbar
        showUpButton();
    }

    private void showUpButton() {
        setSupportActionBar(binding.toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
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
        binding.collapsingToolbarLayout.setExpandedTitleColor(
                //TODO
                getResources().getColor(R.color.color_tab_text));
        binding.collapsingToolbarLayout.setCollapsedTitleTextColor(
                getResources().getColor(R.color.colorPrimaryDark));
    }

}
