package com.example.android.jitsbankingtime.ui.widget;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.android.jitsbankingtime.R;
import com.example.android.jitsbankingtime.model.Ingredient;
import com.example.android.jitsbankingtime.utils.SharedPrefUtils;

import java.util.List;

import timber.log.Timber;

import static com.example.android.jitsbankingtime.utils.ConstantsDefined.DEFAULT_STRING;

public class WidgetListService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ListProviderRemoteViewsFactory(this.getApplicationContext());
    }
}

class ListProviderRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private Context context;
    private List<Ingredient> ingredientsList;

    public ListProviderRemoteViewsFactory(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate() {

    }

    //called on Start and when notifyAppWidgetViewDataChanged is called
    //TODO call notifyAppWidgetViewDataChanged to the service whenever we ask the
    // intentservice to update the widget - to notify the listview that the data
    //has changed
    @Override
    public void onDataSetChanged() {
        Timber.d("jkm - onDataSetChanged");
        //Get the updated list of ingredients from the shared preference
        SharedPreferences sharedPref = context.getSharedPreferences
                (context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        String ingredientsListString = sharedPref.getString(
                context.getString(R.string.pref_ingredients_key), DEFAULT_STRING);

        //Convert the string to a list of ingredient objects
        ingredientsList = SharedPrefUtils.toListOfIngredients(ingredientsListString);
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        if (ingredientsList == null) return 0;
        return ingredientsList.size();
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
        Timber.d("jkm: inside getViewAt(), position is: %d", position);
        if (ingredientsList == null || ingredientsList.size() == 0) return null;
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
        return 1; //Treat all items in the ListView the same
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

