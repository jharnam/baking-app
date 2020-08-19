package com.example.android.jitsbankingtime.utils;

public final class ConstantsDefined {

        public static final String BASE_URL = "https://d17h27t6h515a5.cloudfront.net/";
        public static final String URL_PARAMS = "topher/2017/May/59121517_baking/baking.json";

        /** Extra-Tag for the recipe to be sent/received in the intent */
        public static final String EXTRA_RECIPE = "recipe";

        /** String array used to display the tab names */
        public static final String[] TAB_NAMES = new String[] {"Ingredients", "Steps"};
        /** The number of pages */
        public static final int PAGE_COUNT = TAB_NAMES.length;
        /** Constants for the fragments/tabs in a recipe **/
        public static final int INGREDIENTS = 0;
        public static final int STEPS = 1;
}

