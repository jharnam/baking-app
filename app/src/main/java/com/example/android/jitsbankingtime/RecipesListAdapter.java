package com.example.android.jitsbankingtime;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.jitsbankingtime.model.Recipe;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class RecipesListAdapter extends RecyclerView.Adapter<RecipesListAdapter.RecipeViewHolder> {
    List<Recipe> recipesList = new ArrayList<>();

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View recipesView = layoutInflater.inflate(R.layout.item_recipe, parent, false);

        return new RecipeViewHolder(recipesView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        Recipe currentRecipe = recipesList.get(position);
        Timber.d("currentRecipeName is: " + currentRecipe.getName());
        holder.recipeName.setText(currentRecipe.getName());
        holder.numberOfServings.setText(String.valueOf(currentRecipe.getServings()) + " servings");


    }

    @Override
    public int getItemCount() {
        if (recipesList !=null) {
            return recipesList.size();
        }
        return 0;
    }

    public void setRecipesList(List<Recipe> recipesListForAdapter) {
        Timber.d("inside setRecipesList");
        //TODO - check if the following addAll is better
        //recipesList.addAll(recipesListForAdapter);
        recipesList = recipesListForAdapter;
        notifyDataSetChanged();
    }

    /* Clean all elements of the recycler */
    public void clearList() {
        Timber.d("clearing the list");
        recipesList.clear();
        notifyDataSetChanged();
    }

    public class RecipeViewHolder extends RecyclerView.ViewHolder {
        final TextView recipeName;
        final TextView numberOfServings;


        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            recipeName = itemView.findViewById(R.id.recipe_title);
            numberOfServings = itemView.findViewById(R.id.recipe_servings);
            //itemView.setOnClickListener(this);
        }
    }
}
