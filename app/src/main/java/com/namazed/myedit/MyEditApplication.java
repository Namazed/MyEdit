package com.namazed.myedit;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.namazed.myedit.data.PreferenceDataManager;


public class MyEditApplication extends Application {

    private SharedPreferences preferences;
    private PreferenceDataManager preferenceDataManager;

    @Override
    public void onCreate() {
        super.onCreate();

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        preferenceDataManager = new PreferenceDataManager(preferences);
    }


    public PreferenceDataManager getPreferenceDataManager() {
        return preferenceDataManager;
    }
}
