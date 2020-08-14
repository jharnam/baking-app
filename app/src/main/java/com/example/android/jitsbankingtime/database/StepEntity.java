package com.example.android.jitsbankingtime.database;

import androidx.annotation.NonNull;
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

    public long getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(long recipeId) {
        this.recipeId = recipeId;
    }

    public int getStepId() {
        return stepId;
    }

    public void setStepId(int stepId) {
        this.stepId = stepId;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVideoURL() {
        return videoURL;
    }

    public void setVideoURL(String videoURL) {
        this.videoURL = videoURL;
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }

    public void setThumbnailURL(String thumbnailURL) {
        this.thumbnailURL = thumbnailURL;
    }

    public StepEntity(long recipeId, int stepId, String shortDescription, String description, String videoURL, String thumbnailURL) {
        this.recipeId = recipeId;
        this.stepId = stepId;
        this.shortDescription = shortDescription;
        this.description = description;
        this.videoURL = videoURL;
        this.thumbnailURL = thumbnailURL;
    }
}

