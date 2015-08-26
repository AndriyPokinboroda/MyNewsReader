package com.example.apokyn.mynewsreader;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.example.apokyn.mynewsreader.internet.NewsWireService;


public class DataManager {

    private final String mLogTag = getClass().getSimpleName();

    private Context mContext;
    private BroadcastReceiver mNewsWireReceiver;
    private LocalBroadcastManager mBroadcastManager;

    public DataManager(Context context) {
        mContext = context;
        mNewsWireReceiver = new NewsWireReceiver();
        mBroadcastManager = LocalBroadcastManager.getInstance(mContext);

        mBroadcastManager.registerReceiver(
                mNewsWireReceiver,
                new IntentFilter(NewsWireService.ACTION_NEWS_WIRE_UPDATE));
        /* TEMPORARY*/
        mContext.startService(new Intent(mContext, NewsWireService.class));

        Log.d(mLogTag, mLogTag);
    }


    private class NewsWireReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getBooleanExtra(NewsWireService.KEY_IS_NEWS_LOADED, false)) {
                Log.d(mLogTag, intent.getStringExtra(NewsWireService.KEY_NEWS_WIRE_RESULT_JSON));
            } else {
                Log.d(mLogTag, "Don't work!");
            }
        }
    }
}
