package com.example.android.jitsbankingtime.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "recipes")
public class RecipeEntity {

    @PrimaryKey()
    @ColumnInfo(name = "recipe_id")
    private long recipeId;

    @ColumnInfo(name = "recipe_name")
    private String name;

    @ColumnInfo(name = "is_favorite")
    private boolean isFavorite = false;

    @ColumnInfo(name = "recipe_servings")
    private int servings;

    @ColumnInfo(name = "recipe_image")
    private String image;

    public long getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(long recipeId) {
        this.recipeId = recipeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
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

    public RecipeEntity(long recipeId, String name, boolean isFavorite, int servings, String image) {
        this.recipeId = recipeId;
        this.name = name;
        this.isFavorite = isFavorite;
        this.servings = servings;
        this.image = image;
    }
}
