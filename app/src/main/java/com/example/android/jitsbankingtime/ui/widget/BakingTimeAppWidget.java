package com.example.android.jitsbankingtime.ui.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.RemoteViews;

import com.example.android.jitsbankingtime.R;
import com.example.android.jitsbankingtime.model.Ingredient;
import com.example.android.jitsbankingtime.model.Recipe;
import com.example.android.jitsbankingtime.model.Step;
import com.example.android.jitsbankingtime.ui.screens.MainActivity;
import com.example.android.jitsbankingtime.ui.screens.RecipeDetailActivity;
import com.example.android.jitsbankingtime.utils.SharedPrefUtils;

import java.util.List;

import timber.log.Timber;

import static android.appwidget.AppWidgetManager.EXTRA_APPWIDGET_IDS;
import static com.example.android.jitsbankingtime.utils.ConstantsDefined.DEFAULT_INT;
import static com.example.android.jitsbankingtime.utils.ConstantsDefined.DEFAULT_LONG;
import static com.example.android.jitsbankingtime.utils.ConstantsDefined.DEFAULT_STRING;
import static com.example.android.jitsbankingtime.utils.ConstantsDefined.EXTRA_RECIPE;
import static com.example.android.jitsbankingtime.utils.ConstantsDefined.WIDGET_PENDING_INTENT_ID;

/**
 * Implementation of App Widget functionality.
 */
public class BakingTimeAppWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        Timber.d("jkm: updateAppWidget");

        // Construct the RemoteViews object
        RemoteViews views = getIngredientListRemoteViewsObj(context, appWidgetId);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);

        //updateAppWidgetTitle(context, appWidgetManager, appWidgetId);

    }


    private static RemoteViews getIngredientListRemoteViewsObj(Context context, int appWidgetId) {
        //Create the RemoteViews object
        Timber.d("jkm: getIngredientListRemoteViewsObj");

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_baking_time);
        //Set up the intent that starts the WidgetListService, which will provide the views for
        //this ListView collection
        Intent intent = new Intent(context, WidgetListService.class);

        //Add the widgetId to the intent extras
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        views.setRemoteAdapter(R.id.widget_ingredients_list_view, intent);


        //Handle empty view
        views.setEmptyView(R.id.widget_ingredients_list_view, R.id.widget_empty_view);

        return views;

    }

    static void updateAppWidgetTitle(Context context, AppWidgetManager appWidgetManager,
                                     int appWidgetId) {
        Timber.d("jkm: updateAppWidgetTitle");

        RemoteViews views = getRecipeTitleRemoteViewsObj(context);
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);


    }

    private static RemoteViews getRecipeTitleRemoteViewsObj(Context context) {
        Timber.d("jkm: getRecipeTitleRemoteViewsObj");


        //get the data from the shared preferences
        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        String recipeName = sharedPref.getString(
                context.getString(R.string.pref_recipe_name_key), DEFAULT_STRING);
        String ingredientsListString = sharedPref.getString(
                context.getString(R.string.pref_ingredients_key), DEFAULT_STRING);
        String stepsListString = sharedPref.getString(
                context.getString(R.string.pref_steps_key), DEFAULT_STRING);
        long recipeId = sharedPref.getLong(
                context.getString(R.string.pref_recipe_id_key), DEFAULT_LONG);
        String image = sharedPref.getString(
                context.getString(R.string.pref_image_key), DEFAULT_STRING);
        int numServings = sharedPref.getInt(
                context.getString(R.string.pref_num_servings_key), DEFAULT_INT);

        //Create the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_baking_time);

        //If we do not have the information needed to launch the RecipeDetailActivity
        //..then we could atleast launch the MainActivity
        Intent intentToStartActivity;
        if (ingredientsListString.isEmpty() || stepsListString.isEmpty()) {
            Timber.d("jkm: something is empty");
            Class destinationClass = MainActivity.class;
            intentToStartActivity = new Intent(context, destinationClass);
            views.setTextViewText(R.id.recipe_name_widget_tv, context.getString(R.string.app_name));

        } else {
            Timber.d("jkm: all data is available");

            List<Ingredient> ingredientsList = SharedPrefUtils.toListOfIngredients(ingredientsListString);

            List<Step> stepsList = SharedPrefUtils.toListOfSteps(stepsListString);

            //Create the Recipe object
            Recipe recipe = new Recipe(recipeId, recipeName, false, ingredientsList, stepsList, numServings, image);

            Class destinationClass = RecipeDetailActivity.class;
            intentToStartActivity = new Intent(context, destinationClass);

            //Add the recipe as an extra to the bundle
            Bundle b = new Bundle();
            b.putParcelable(EXTRA_RECIPE, recipe);
            intentToStartActivity.putExtra(EXTRA_RECIPE, b);

            views.setTextViewText(R.id.recipe_name_widget_tv, recipeName);

        }

        PendingIntent pendingIntent = PendingIntent.getActivity(context, WIDGET_PENDING_INTENT_ID,
                intentToStartActivity, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.widget_ingredients_list_view, pendingIntent);

        return views;

    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        Timber.d("jkm: onUpdate");

        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
            //Also update the recipe name in the widget
            updateAppWidgetTitle(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    /**
     * @param context
     * @param intent  https://stackoverflow.com/questions/10663800/sending-an-update-broadcast-to-an-app-widget
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        Timber.d("jkm: onReceive here");

        String action = intent.getAction();
        if (AppWidgetManager.ACTION_APPWIDGET_UPDATE.equals(action)) {
            //Do update
            int[] appWidgetIds = intent.getIntArrayExtra(EXTRA_APPWIDGET_IDS);
            if (appWidgetIds != null) {
                Timber.d("jkm - appWidgetIds exist");
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                for (int appWidgetId : appWidgetIds) {
                    updateAppWidget(context, appWidgetManager, appWidgetId);
                    //TODO - check
                    updateAppWidgetTitle(context, appWidgetManager, appWidgetId);
                }
                //Trigger data update to handle the ListView widgets and force a data refresh
                appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_ingredients_list_view);
            }


        }
    }
}

