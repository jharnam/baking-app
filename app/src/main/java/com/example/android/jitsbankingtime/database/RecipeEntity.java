package com.example.android.jitsbankingtime.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "recipes")
public class RecipeEntity {

    @PrimaryKey(autoGenerate = false)
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

}
