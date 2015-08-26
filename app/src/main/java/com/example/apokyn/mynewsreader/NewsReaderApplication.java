package com.example.apokyn.mynewsreader;

import android.app.Application;
import android.util.Log;

/**
 * Created by apokyn on 26.08.2015.
 */
public class NewsReaderApplication extends Application {

    private static DataManager sDataManager;


    @Override
    public void onCreate() {
        super.onCreate();

        Log.d("APP", "APP");
        sDataManager = new DataManager(this);
    }

    public static DataManager getDataManager(){
        return sDataManager;
    }
}
