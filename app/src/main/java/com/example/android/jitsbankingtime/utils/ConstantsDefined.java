package com.example.android.jitsbankingtime.utils;

public final class ConstantsDefined {

        public static final String APP_NAME = "Baking Time";

        public static final String BASE_URL = "https://d17h27t6h515a5.cloudfront.net/";
        public static final String URL_PARAMS = "topher/2017/May/59121517_baking/baking.json";

        /**
         * Extra-Tags for the recipe, step to be sent/received in the intent
         */
        public static final String EXTRA_RECIPE = "recipe";
        public static final String EXTRA_STEP = "step";
        public static final String EXTRA_STEP_ID = "stepId";

        /**
         * Tags for the saving instance state in StepDetailFragment
         */
        public static final String SAVE_RECIPE = "save_recipe";
        public static final String SAVE_STEP = "save_step";
        public static final String SAVE_CURRENT_STEP_ID = "save_current_step_id";
        public static final String SAVE_PLAYBACK_POSITION = "save_playback_position";
        public static final String SAVE_CURRENT_WINDOW = "save_current_window";
        public static final String SAVE_PLAY_WHEN_READY = "save_play_when_ready";


        /**
         * EXOPLAYER CONSTANTS
         */
        public static final int REWIND_FAST_FORWARD_INCREMENT = 3000;


        /**
         * String array used to display the tab names
         */
        public static final String[] TAB_NAMES = new String[]{"Ingredients", "Steps"};
        /**
         * The number of pages
         */
        public static final int PAGE_COUNT = TAB_NAMES.length;
        /**
         * Constants for the fragments/tabs in a recipe
         **/
        public static final int INGREDIENTS = 0;
        public static final int STEPS = 1;

        /**
         * Default values for SharedPreferences
         */
        public static final String DEFAULT_STRING = "";

}

