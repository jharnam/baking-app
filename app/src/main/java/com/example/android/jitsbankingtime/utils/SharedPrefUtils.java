package com.example.android.jitsbankingtime.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.android.jitsbankingtime.R;
import com.example.android.jitsbankingtime.model.Ingredient;
import com.example.android.jitsbankingtime.model.Recipe;
import com.example.android.jitsbankingtime.model.Step;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

import timber.log.Timber;

import static com.example.android.jitsbankingtime.utils.ConstantsDefined.DEFAULT_INT;
import static com.example.android.jitsbankingtime.utils.ConstantsDefined.DEFAULT_LONG;
import static com.example.android.jitsbankingtime.utils.ConstantsDefined.DEFAULT_STRING;

public class SharedPrefUtils {

    //Convert List<Ingredient> to String
    public static String toIngredientsString(List<Ingredient> ingredientsList) {
        if (ingredientsList == null) {
            return null;
        }
        Gson gson = new Gson();
        Type listType = new TypeToken<List<Ingredient>>() {
        }.getType();
        return gson.toJson(ingredientsList, listType);
    }

    //Convert String to List<Ingredient>
    public static List<Ingredient> toListOfIngredients(String ingredientsString) {
        if (ingredientsString == null) {
            return Collections.emptyList();
        }
        Gson gson = new Gson();
        Type listType = new TypeToken<List<Ingredient>>() {
        }.getType();
        return gson.fromJson(ingredientsString, listType);
    }

    //Convert List<Step> to String
    public static String toStepsString(List<Step> stepsList) {
        if (stepsList == null) {
            return null;
        }
        Gson gson = new Gson();
        Type listType = new TypeToken<List<Step>>() {
        }.getType();
        return gson.toJson(stepsList, listType);
    }

    //Convert String to List<Step>
    public static List<Step> toListOfSteps(String stepsString) {
        if (stepsString == null) {
            return Collections.emptyList();
        }
        Gson gson = new Gson();
        Type listType = new TypeToken<List<Step>>() {
        }.getType();
        return gson.fromJson(stepsString, listType);
    }

    //update shared preferences to the current recipes - for widget
    public static void saveRecipe(Context context, Recipe saveThisRecipe) {
        Timber.d("saveRecipe: %s", saveThisRecipe.toString());
        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(context.getString(R.string.pref_recipe_name_key), saveThisRecipe.getName());

        String ingredientsString = SharedPrefUtils.toIngredientsString(saveThisRecipe.getIngredients());
        editor.putString(context.getString(R.string.pref_ingredients_key), ingredientsString);

        String stepsString = SharedPrefUtils.toStepsString(saveThisRecipe.getSteps());
        editor.putString(context.getString(R.string.pref_steps_key), stepsString);

        editor.putLong(context.getString(R.string.pref_recipe_id_key), saveThisRecipe.getId());
        editor.putString(context.getString(R.string.pref_image_key), saveThisRecipe.getImage());
        editor.putInt(context.getString(R.string.pref_num_servings_key), saveThisRecipe.getServings());

        //TODO
        editor.apply();

    }

    //return the recipe from what is store in shared preferences
    public static Recipe retrieveRecipe(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        String recipeName = sharedPref.getString(
                context.getString(R.string.pref_recipe_name_key), DEFAULT_STRING);
        Timber.d("5 retrieveRecipe got recipe name = %s", recipeName);
        String ingredientsListString = sharedPref.getString(
                context.getString(R.string.pref_ingredients_key), DEFAULT_STRING);
        String stepsListString = sharedPref.getString(
                context.getString(R.string.pref_steps_key), DEFAULT_STRING);
        long recipeId = sharedPref.getLong(
                context.getString(R.string.pref_recipe_id_key), DEFAULT_LONG);
        String image = sharedPref.getString(
                context.getString(R.string.pref_image_key), DEFAULT_STRING);
        int numServings = sharedPref.getInt(
                context.getString(R.string.pref_num_servings_key), DEFAULT_INT);
        List<Ingredient> ingredientsList = toListOfIngredients(ingredientsListString);
        List<Step> stepsList = toListOfSteps(stepsListString);

        if (recipeName != null || recipeName != "") {
            Recipe storedRecipe = new Recipe(recipeId, recipeName, false,
                    ingredientsList, stepsList, numServings, image);

            if (storedRecipe != null) {
                Timber.d("retrieveRecipe : got name : %s", storedRecipe.getName());
                if (storedRecipe.getIngredients() != null && storedRecipe.getIngredients().size() > 0) {
                    Timber.d("we have %d number of ingredients ",
                            storedRecipe.getIngredients().size());
                }
            }
            return storedRecipe;
        }
        return null;
    }

}
