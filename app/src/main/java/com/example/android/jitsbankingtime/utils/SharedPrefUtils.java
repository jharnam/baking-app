package com.example.android.jitsbankingtime.utils;

import com.example.android.jitsbankingtime.model.Ingredient;
import com.example.android.jitsbankingtime.model.Step;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

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
}
