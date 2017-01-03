package com.namazed.myedit.data;

import android.content.SharedPreferences;

public class PreferenceDataManager {

    public static final String PREF_TEXT_SIZE = "Size";
    public static final String PREF_TEXT_STYLE = "Style";
    public static final String PREF_TEXT_COLOR = "Color";

    private static final String COLOR_RED = "Red color";
    private static final String COLOR_GREEN = "Green color";
    private static final String COLOR_BLUE = "Blue color";


    private final SharedPreferences sharedPreferences;

    public PreferenceDataManager(SharedPreferences preferences) {
        sharedPreferences = preferences;
    }

    public float getTextSize() {
        return Float.
                parseFloat(sharedPreferences.getString(PREF_TEXT_SIZE, "20"));
    }

    public String getTextStyle() {
        return sharedPreferences.getString(PREF_TEXT_STYLE, "");
    }

    public boolean whatColor(String color) {
        switch (color) {
            case COLOR_RED:
               return sharedPreferences.getBoolean(COLOR_RED,false);
            case COLOR_GREEN:
                return sharedPreferences.getBoolean(COLOR_GREEN,false);
            case COLOR_BLUE:
                return sharedPreferences.getBoolean(COLOR_BLUE,false);
            default:
                return false;
        }
    }
}
