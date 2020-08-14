package com.example.android.jitsbankingtime.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface RecipesDao {
    @Query("SELECT * from recipes")
    LiveData<List<RecipeEntity>> getAllRecipes();

    @Query("SELECT * from recipes where recipe_id = :id")
    LiveData<RecipeEntity> getRecipeDetailsForId(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertRecipe(RecipeEntity insertEntry);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void bulkInsert(RecipeEntity... recipe);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateRecipe(RecipeEntity updateEntry);

    @Query("UPDATE recipes SET is_favorite = 1 WHERE recipe_id = :id:")
    int markFavorite(long id);

    @Query("UPDATE recipes SET is_favorite = 0 WHERE recipe_id = :id:")
    int markNotFavorite(long id);

    @Delete
    void deleteRecipe(RecipeEntity deleteEntry);

    @Query("DELETE from recipes")
    void deleteAllRecipes();


}
