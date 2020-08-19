package com.example.android.jitsbankingtime;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.jitsbankingtime.databinding.ItemMainRecipeBinding;
import com.example.android.jitsbankingtime.model.Recipe;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class RecipesListAdapter extends RecyclerView.Adapter<RecipesListAdapter.RecipeViewHolder> {

    /**
     * The interface that receives onClick messages.
     */
    public interface RecipeAdapterOnClickHandler {
        void onClick (Recipe recipe);
    }

    private List<Recipe> recipesList = new ArrayList<>();
    private final RecipeAdapterOnClickHandler clickHandler;

    public RecipesListAdapter(RecipeAdapterOnClickHandler clickHandler) {
        this.clickHandler = clickHandler;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemMainRecipeBinding binding = DataBindingUtil.inflate(layoutInflater,
                R.layout.item_main_recipe, parent, false);
        return new RecipeViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        Recipe currentRecipe = recipesList.get(position);
        Timber.d("currentRecipeName is: %s", currentRecipe.getName());

        holder.bind(currentRecipe);

    }

    @Override
    public int getItemCount() {
        if (recipesList !=null) {
            return recipesList.size();
        }
        //Error case
        Timber.e("Recipe list does not exist, or is empty");
        return 0;
    }

    public void setRecipesList(List<Recipe> recipesListForAdapter) {
        Timber.d("inside setRecipesList");
        //TODO - check if the following addAll is better
        //recipesList.addAll(recipesListForAdapter);
        //TODO - test this - update MainActivity to remove the clearList call from there
        //recipesList.clear();
        recipesList = recipesListForAdapter;
        notifyDataSetChanged();
    }

    /* Clean all elements of the recycler */
    public void clearList() {
        Timber.d("clearing the list");
        recipesList.clear();
        notifyDataSetChanged();
    }

    public class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        /** This field is used for data binding */
        private ItemMainRecipeBinding binding;


        public RecipeViewHolder(ItemMainRecipeBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            itemView.setOnClickListener(this);
        }


        /* This method takes a Recipe object and uses that recipe to display
        the appropriate information within a list item.
         */
        private void bind(Recipe recipe) {
            if (recipe == null)
            {
                Timber.e("Recipe is null :(");
                return;
            }

            binding.recipeTitle.setText(recipe.getName());

            String imageUrl = recipe.getImage();
            setImage(imageUrl);

            binding.recipeServings.setText(String.valueOf(recipe.getServings()) + " servings");

        }

        private void setImage(String url) {
            if (url.isEmpty() || url.trim().equals("")) {
                //use a default image
                binding.recipeThumbnail.setImageResource(R.drawable.recipe_placeholder_icon);
            } else {
                Picasso.get()
                        .load(url)
                        .into(binding.recipeThumbnail);
            }
        }

        /**
         * This gets called by the child views during a click.
         *
         * @param v The View that was clicked
         */
        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Recipe currentRecipe = recipesList.get(adapterPosition);
            clickHandler.onClick(currentRecipe);

        }
    }
}
