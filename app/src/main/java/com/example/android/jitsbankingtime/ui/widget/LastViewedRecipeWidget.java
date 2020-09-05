package com.example.android.jitsbankingtime.ui.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;

import com.example.android.jitsbankingtime.R;
import com.example.android.jitsbankingtime.model.Recipe;
import com.example.android.jitsbankingtime.ui.screens.MainActivity;
import com.example.android.jitsbankingtime.ui.screens.RecipeDetailActivity;
import com.example.android.jitsbankingtime.utils.SharedPrefUtils;

import timber.log.Timber;

import static android.view.View.GONE;
import static com.example.android.jitsbankingtime.utils.ConstantsDefined.EXTRA_RECIPE;
import static com.example.android.jitsbankingtime.utils.ConstantsDefined.WIDGET_PENDING_INTENT_ID;

/**
 * Implementation of App Widget functionality.
 */
public class LastViewedRecipeWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        Recipe storedRecipe = SharedPrefUtils.retrieveRecipe(context);
        if (storedRecipe != null) {

            Class destinationClass = RecipeDetailActivity.class;
            if (storedRecipe.getName() == null || storedRecipe.getName() == "") {
                destinationClass = MainActivity.class;
                Intent intentToStartActivity = new Intent(context, destinationClass);
                PendingIntent pendingIntent = PendingIntent.getActivity(context, WIDGET_PENDING_INTENT_ID,
                        intentToStartActivity, PendingIntent.FLAG_UPDATE_CURRENT);
                // Construct the RemoteViews object
                RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.last_viewed_recipe_widget);
                views.setTextViewText(R.id.recipe_name_widget_tv, context.getString(R.string.app_name));
                // Widgets allow click handlers to only launch pending intents
                views.setOnClickPendingIntent(R.id.recipe_name_widget_tv, pendingIntent);
                //Set up the intent that starts the WidgetService, which will provide the views for
                //this ListView collection
                Intent intent = new Intent(context, WidgetService.class);
                //Add the widgetId to the intent extras
                intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);


                // Set up the RemoteViews object to use a RemoteViews adapter.
                // This adapter connects
                // to a RemoteViewsService  through the specified intent.
                // This is how you populate the data.
                views.setRemoteAdapter(R.id.widget_ingredients_list_view, intent);

                //Handle empty view
                // The empty view is displayed when the collection has no items.
                // It should be in the same layout used to instantiate the RemoteViews
                // object above.
                views.setEmptyView(R.id.widget_ingredients_list_view, R.id.widget_empty_view);

                // Instruct the widget manager to update the widget
                appWidgetManager.updateAppWidget(appWidgetId, views);
                appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.widget_ingredients_list_view);
            } else {
                Intent intentToStartActivity = new Intent(context, destinationClass);

                //Add the recipe as an extra to the bundle
                Bundle b = new Bundle();
                b.putParcelable(EXTRA_RECIPE, storedRecipe);
                intentToStartActivity.putExtra(EXTRA_RECIPE, b);

                PendingIntent pendingIntent = PendingIntent.getActivity(context, WIDGET_PENDING_INTENT_ID,
                        intentToStartActivity, PendingIntent.FLAG_UPDATE_CURRENT);

                // Construct the RemoteViews object
                RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.last_viewed_recipe_widget);


                views.setTextViewText(R.id.recipe_name_widget_tv, storedRecipe.getName());


                // Widgets allow click handlers to only launch pending intents
                views.setOnClickPendingIntent(R.id.recipe_name_widget_tv, pendingIntent);
                //Set up the intent that starts the WidgetService, which will provide the views for
                //this ListView collection
                Intent intent = new Intent(context, WidgetService.class);
                //Add the widgetId to the intent extras
                intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);


                // Set up the RemoteViews object to use a RemoteViews adapter.
                // This adapter connects
                // to a RemoteViewsService  through the specified intent.
                // This is how you populate the data.
                views.setRemoteAdapter(R.id.widget_ingredients_list_view, intent);

                //Handle empty view
                // The empty view is displayed when the collection has no items.
                // It should be in the same layout used to instantiate the RemoteViews
                // object above.
                views.setEmptyView(R.id.widget_ingredients_list_view, R.id.widget_empty_view);

                if (storedRecipe.getIngredients().size() > 0) {
                    views.setViewVisibility(R.id.widget_empty_view, GONE);
                }

                // Instruct the widget manager to update the widget
                appWidgetManager.updateAppWidget(appWidgetId, views);
                appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.widget_ingredients_list_view);
            }
        } else {
            Timber.e("no recipe in the shared preferences file");
            //TODO - can start MainActivity
        }

    }


    public static void updateAppWidgets(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

