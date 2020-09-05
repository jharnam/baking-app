package com.example.android.jitsbankingtime.ui.widget;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViewsService;

import com.example.android.jitsbankingtime.model.Recipe;
import com.example.android.jitsbankingtime.utils.SharedPrefUtils;

public class WidgetService extends RemoteViewsService {
    public static void updateWidget(Context context, Recipe recipe) {
        SharedPrefUtils.saveRecipe(context, recipe);

        ComponentName provider = new ComponentName(context, LastViewedRecipeWidget.class);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int[] ids = appWidgetManager.getAppWidgetIds(provider);

        LastViewedRecipeWidget.updateAppWidgets(context, appWidgetManager, ids);
    }

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        //return remote view factory here
        return new WidgetDataProvider(this, intent);
    }
}
