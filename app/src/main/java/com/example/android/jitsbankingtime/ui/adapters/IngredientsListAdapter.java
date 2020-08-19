package com.example.android.jitsbankingtime.ui.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.jitsbankingtime.R;
import com.example.android.jitsbankingtime.databinding.IngredientRowItemBinding;
import com.example.android.jitsbankingtime.model.Ingredient;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class IngredientsListAdapter extends RecyclerView.Adapter<IngredientsListAdapter.IngredientsViewHolder> {
    private List<Ingredient> ingredientsList = new ArrayList<>();

    @NonNull
    @Override
    public IngredientsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        IngredientRowItemBinding binding = DataBindingUtil.inflate(layoutInflater,
                R.layout.ingredient_row_item, parent, false);
        return new IngredientsListAdapter.IngredientsViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientsViewHolder holder, int position) {
        Ingredient currentIngredient = ingredientsList.get(position);
        Timber.d("currentIngredientName is: %s", currentIngredient.getName());

        holder.bind(currentIngredient);

    }

    @Override
    public int getItemCount() {
        if (ingredientsList !=null) {
            return ingredientsList.size();
        }
        //Error case
        Timber.e("Ingredient list does not exist, or is empty");
        return 0;
    }

    public void setIngredientsList (List<Ingredient> ingredientsListForAdapter) {
        Timber.d("inside setIngredientsList");
        //TODO - check if the following addAll is better
        //recipesList.addAll(recipesListForAdapter);
        //TODO - test this - update MainActivity to remove the clearList call from there
        //recipesList.clear();
        ingredientsList = ingredientsListForAdapter;
        notifyDataSetChanged();
        Timber.d("we have %d ingredients", ingredientsList.size());
    }

    /* Clean all elements of the recycler */
    public void clearList() {
        Timber.d("clearing the list");
        ingredientsList.clear();
        notifyDataSetChanged();
    }


    public class IngredientsViewHolder extends RecyclerView.ViewHolder{

        // This field is used for data binding
        private IngredientRowItemBinding binding;

        public IngredientsViewHolder(@NonNull IngredientRowItemBinding binding) {

            super(binding.getRoot());
            this.binding = binding;

            //No on click listener here :)

        }

        /* This method takes an Ingredient object and uses that recipe to display
       the appropriate information within a list item.
        */
        private void bind(Ingredient ingredient) {
            if (ingredient == null)
            {
                Timber.e("Ingredient is null :(");
                return;
            }

            binding.textViewQuantity.setText(String.valueOf(ingredient.getQuantity()));
            binding.textViewMeasure.setText(ingredient.getMeasure());
            binding.textViewIngredientName.setText(ingredient.getName());

        }

    }
}
