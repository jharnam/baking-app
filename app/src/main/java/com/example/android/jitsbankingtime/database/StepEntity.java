package com.example.android.jitsbankingtime.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity (tableName = "steps", primaryKeys = {"recipe_id", "step_id"},
        foreignKeys = @ForeignKey(entity = RecipeEntity.class,
        parentColumns = "recipe_id",
        childColumns = "recipe_id",
        onDelete = CASCADE))
public class StepEntity {
    @ColumnInfo(name ="recipe_id")
    private long recipeId;

    @ColumnInfo(name = "step_id")
    private int stepId;

    @ColumnInfo(name = "short_description")
    private String shortDescription;

    private String description;

    @ColumnInfo(name = "video_url")
    private String videoURL;

    @ColumnInfo(name = "thumbnail_url")
    private String thumbnailURL;

}

