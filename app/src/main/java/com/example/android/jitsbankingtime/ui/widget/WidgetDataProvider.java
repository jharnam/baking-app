package com.example.android.jitsbankingtime.ui.widget;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.android.jitsbankingtime.R;
import com.example.android.jitsbankingtime.model.Ingredient;
import com.example.android.jitsbankingtime.model.Recipe;
import com.example.android.jitsbankingtime.utils.SharedPrefUtils;

import java.util.List;

import timber.log.Timber;

//Acts as the Adapter for our listview in the widget
public class WidgetDataProvider implements RemoteViewsService.RemoteViewsFactory {

    //TODO List<String> ingredientsList = new ArrayList<> ();
    Context context;
    Intent intent;
    Recipe recipe; //this is our collection

    public WidgetDataProvider(Context context, Intent intent) {
        this.context = context;
        this.intent = intent;
    }

    @Override
    public void onCreate() {
        //initialize the data
        //done in onDataSetChanged()
    }

    //called on start and when notifyAppWidgetViewDataChanged is called
    @Override
    public void onDataSetChanged() {
        //initialize the data
        //data is in shared preferences, get it
        recipe = SharedPrefUtils.retrieveRecipe(context);


    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        if (recipe != null && recipe.getIngredients() != null)
            return recipe.getIngredients().size();
        Timber.d("getCount is 0");
        if (recipe == null) Timber.e("Recipe is null");
        return 0;
    }

    /**
     * This method acts like the onBindViewHolder method in an Adapter.
     * We need to describe what each item in the ListView will look like
     *
     * @param position The current position of the item in the GridView to be displayed
     * @return The RemoteViews object to display for the provided position
     */
    @Override
    public RemoteViews getViewAt(int position) {
        Timber.d("inside getViewAt(), position is: %d", position);
        if (recipe == null || recipe.getIngredients() == null
                || recipe.getIngredients().size() == 0) return null;
        List<Ingredient> ingredientsList = recipe.getIngredients();
        Ingredient ingredient = ingredientsList.get(position);

        float quantity = ingredient.getQuantity();
        String measure = ingredient.getMeasure();
        String name = ingredient.getName();

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_list_item);

        views.setTextViewText(R.id.quantity_widget_tv, String.valueOf(quantity));
        views.setTextViewText(R.id.measure_widget_tv, measure);
        views.setTextViewText(R.id.name_widget_tv, name);

        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1; // Treat all items in the ListView the same
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
