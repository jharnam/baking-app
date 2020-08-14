package com.example.android.jitsbankingtime.model;

import java.util.List;

/* POJO */
public class Recipe {

    private long id;
    private String name;
    private boolean isFavorite = false;
    private List<Ingredient> ingredients = null;
    private List<RecipeStep> steps = null;
    private int servings;
    private String image;

    public Recipe(long id, String name, boolean isFavorite, List<Ingredient> ingredients, List<RecipeStep> steps, int servings, String image) {
        this.id = id;
        this.name = name;
        this.isFavorite = isFavorite;
        this.ingredients = ingredients;
        this.steps = steps;
        this.servings = servings;
        this.image = image;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public List<RecipeStep> getSteps() {
        return steps;
    }

    public void setSteps(List<RecipeStep> steps) {
        this.steps = steps;
    }

    public int getServings() {
        return servings;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }
}
