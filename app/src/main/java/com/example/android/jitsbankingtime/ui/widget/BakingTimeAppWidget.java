package com.example.android.jitsbankingtime.ui.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.RemoteViews;

import com.example.android.jitsbankingtime.R;

import static com.example.android.jitsbankingtime.utils.ConstantsDefined.DEFAULT_STRING;

/**
 * Implementation of App Widget functionality.
 */
public class BakingTimeAppWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        //TODO Cleanup CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_baking_time);

        //get the data from the shared preferences
        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        String recipeName = sharedPref.getString(
                context.getString(R.string.pref_recipe_name_key), DEFAULT_STRING);
        String ingredientsListString = sharedPref.getString(
                context.getString(R.string.pref_ingredients_key), DEFAULT_STRING);

        views.setTextViewText(R.id.recipe_name_widget_tv, recipeName);
        //views.setTextViewText(R.id.appwidget_text, widgetText);

        // we want to display the ingredients list
        //RemoteViews remoteViews = getIngredientListRemoteViewsObj(context, appWidgetId)
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    /*
    private static RemoteViews getIngredientListRemoteViewsObj(Context context, int appWidgetId) {
        //Create the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.new_app_widget)
    }

     */

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
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
}

