package com.example.apokyn.mynewsreader;

import android.app.Application;

import com.example.apokyn.mynewsreader.data.DataManager;

/**
 * Created by apokyn on 26.08.2015.
 */
public class NewsReaderApplication extends Application {

    private static DataManager sDataManager;

    @Override
    public void onCreate() {
        super.onCreate();

        sDataManager = new DataManager(this);
    }

    public static DataManager getDataManager(){
        return sDataManager;
    }
}
