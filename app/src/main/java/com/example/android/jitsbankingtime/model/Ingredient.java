package com.example.android.jitsbankingtime.model;

import com.google.gson.annotations.SerializedName;

/* POJO */
public class Ingredient {
    private float quantity;
    private String measure;

    @SerializedName("ingredient")
    private String name;
}
