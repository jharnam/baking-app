package com.example.android.jitsbankingtime;

import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.IdlingResource;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.example.android.jitsbankingtime.ui.screens.MainActivity;
import com.example.android.jitsbankingtime.utils.IdlingResourceUtils;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class MainActivityBasicTestSunnyDay {
    /**
     * Constants used for MainActivityBasicTest
     */
    public static final String RECIPE_NAME_AT_ZERO = "Nutella Pie";
    public static final String RECIPE_NAME_AT_ONE = "Brownies";
    /**
     * Constants for position number
     */
    public static final int POSITION_ZERO = 0;
    public static final int POSITION_ONE = 1;
    public static final int POSITION_TWO = 2;
    static final int POSITION_THREE = 3;
    @Rule
    public ActivityTestRule<MainActivity> mainActivityActivityTestRule
            = new ActivityTestRule<>(MainActivity.class);
    private IdlingResource mIdlingResource;

    @Before
    public void registerIdlingResource() {
        //downloading json from the cloud can cause delay - hence the idling resource
        mIdlingResource = IdlingResourceUtils.getIdlingResource();
        IdlingRegistry.getInstance().register(mIdlingResource);

    }

    /**
     * RecyclerView comes into view
     */
    @Test
    public void test_isRecipeListVisible_OnAppLaunch() {
        //1. Find the view
        //2. Perform action on the view
        //3. Check if the view does what you expect
        onView(withId(R.id.recycler_view_recipes))
                .check(matches(isDisplayed()));
    }

    /**
     * RecyclerView data matches what is expected
     */
    @Test
    public void test_scrollToPosition_CheckRecipeName() {
        //1. Find the view
        onView(withId(R.id.recycler_view_recipes))
                //2. Perform action on the view
                .perform(RecyclerViewActions.scrollToPosition(POSITION_ONE));
        //3. Check if the view does what you expect
        onView(withText(RECIPE_NAME_AT_ONE))
                .check(matches(isDisplayed()));
    }

    /**
     * click recipe, opens DetailActivity
     */
    @Test
    public void test_clickRecipe_OpenRecipeDetailActivity_CheckRecipeMediaDisplayed() {
        //1. Find the view
        onView(withId(R.id.recycler_view_recipes))
                //2. Perform action on the view
                .perform(RecyclerViewActions.actionOnItemAtPosition(POSITION_ZERO, click()));
        //3. Check if the view does what you expect
        /*
        onView(withText(RECIPE_NAME_AT_ZERO))
                .check(matches(isDisplayed()));

         */

        onView(withText("STEPS")).perform(click());
        //Now that we can click on the step ids - regardless of phone/tablet
        onView(withId(R.id.recycler_view_steps))
                .perform(RecyclerViewActions.actionOnItemAtPosition(POSITION_ZERO, click()));
        onView(withId(R.id.frame_layout_step_media)).check(matches(isDisplayed()));

    }

    // Unregister resources when not needed to avoid malfunction
    @After
    public void unregisterIdlingResource() {
        if (mIdlingResource != null) {
            IdlingRegistry.getInstance().unregister(mIdlingResource);
        }
    }
}
