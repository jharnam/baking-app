package com.example.android.jitsbankingtime.database;

import androidx.room.Entity;
import androidx.room.ForeignKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "ingredients", primaryKeys = {"recipe_id", "ingredient_name"},
        foreignKeys = @ForeignKey(entity = RecipeEntity.class,
        parentColumns = "recipe_id",
        childColumns = "recipe_id",
        onDelete = CASCADE))
public class IngredientEntity {
    private long recipe_id;

    private String ingredient_name;

    private float quantity;

    private String measure;

    public IngredientEntity(long recipe_id, String ingredient_name, float quantity, String measure) {
        this.recipe_id = recipe_id;
        this.ingredient_name = ingredient_name;
        this.quantity = quantity;
        this.measure = measure;
    }

    public long getRecipe_id() {
        return recipe_id;
    }

    public void setRecipe_id(long recipe_id) {
        this.recipe_id = recipe_id;
    }

    public String getIngredient_name() {
        return ingredient_name;
    }

    public void setIngredient_name(String ingredient_name) {
        this.ingredient_name = ingredient_name;
    }

    public float getQuantity() {
        return quantity;
    }

    public void setQuantity(float quantity) {
        this.quantity = quantity;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }
}
