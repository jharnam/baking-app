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
public interface StepsDao {

    @Query("SELECT * from steps where recipe_id = :id")
    LiveData<List<StepEntity>> getAllStepsForRecipeId(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertStep(StepEntity insertEntry);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void bulkInsert(StepEntity... steps);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateStep(StepEntity updateEntry);

    @Delete
    void deleteStep(StepEntity deleteEntry);

    @Query("DELETE from steps where recipe_id = :id")
    void deleteAllStepsForRecipeId(int id);


}
