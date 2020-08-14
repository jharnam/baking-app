package com.example.android.jitsbankingtime.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.android.jitsbankingtime.model.Recipe;

import java.util.List;

@Dao
public interface IngredientsDao {
    @Query("SELECT * from ingredients where recipe_id = :id")
    LiveData<List<IngredientEntity>> getAllIngredientsForRecipeId(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertIngredient(IngredientEntity insertEntry);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void bulkInsert(IngredientEntity... ingredients);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateIngredient(IngredientEntity updateEntry);

    @Delete
    void deleteIngredient(IngredientEntity deleteEntry);

    @Query("DELETE from ingredients where recipe_id = :id")
    void deleteAllIngredientsForRecipeId(int id);

}
