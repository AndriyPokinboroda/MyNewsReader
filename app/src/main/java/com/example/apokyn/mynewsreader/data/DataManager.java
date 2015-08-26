package com.example.apokyn.mynewsreader.data;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.example.apokyn.mynewsreader.utils.Parser;
import com.example.apokyn.mynewsreader.entity.NewsWireItem;
import com.example.apokyn.mynewsreader.internet.NewsWireService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class DataManager {

    private static final String BROADCAST_ACTION_NEWS_UPDATE = "newsUpdate";

    private final String mLogTag = getClass().getSimpleName();

    private Context mContext;
    private BroadcastReceiver mNewsWireReceiver;
    private LocalBroadcastManager mBroadcastManager;

    private List<NewsWireListener> mNewsWireListeners;

    public DataManager(Context context) {
        mContext = context;
        mNewsWireReceiver = new NewsWireReceiver();
        mBroadcastManager = LocalBroadcastManager.getInstance(mContext);
        mNewsWireListeners = new ArrayList<>();

        mBroadcastManager.registerReceiver(
                mNewsWireReceiver,
                new IntentFilter(BROADCAST_ACTION_NEWS_UPDATE));
    }

    //----------------------------------------------------------------------------------------------
    // Observer
    //----------------------------------------------------------------------------------------------
    public void registerNewsWireListener(NewsWireListener listener) {
        mNewsWireListeners.add(listener);
    }

    public void unregisterNewsWireListener(NewsWireListener listener) {
        mNewsWireListeners.remove(listener);
    }

    private void notifyNewsWireUpdate(String section, List<NewsWireItem> freshNews) {
        for (NewsWireListener listener : mNewsWireListeners) {
            listener.onNewsUpdated(section, freshNews);
        }
    }

    private void notifyNewsWireUpdateFailed(String message) {
        for (NewsWireListener listener : mNewsWireListeners) {
            listener.onNewsUpdateFailed(message);
        }
    }
    //----------------------------------------------------------------------------------------------
    // News Wire Updating
    //----------------------------------------------------------------------------------------------
    public void updateNewsWire(String section) {
        Intent requestIntent = new Intent(mContext, NewsWireService.class);
        requestIntent.putExtra(
                NewsWireService.KEY_URL,
                "http://api.nytimes.com/svc/news/v3/content/all/all/.json?api-key=ce28bf606898b473be1a4cdd17ee165a%3A16%3A72738095");
        requestIntent.putExtra(NewsWireService.KEY_BROADCAST_ACTION, BROADCAST_ACTION_NEWS_UPDATE);

        mContext.startService(requestIntent);
    }

    private class NewsWireReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getBooleanExtra(NewsWireService.KEY_IS_RESULT_OK, false)) {
                JSONObject newsJSONObj = null;

                try {
                    newsJSONObj = new JSONObject(
                            intent.getStringExtra(NewsWireService.KEY_RESULT_JSON));
                } catch (JSONException e) {
                    Log.d(mLogTag, e.getMessage());
                }

                if (newsJSONObj != null) {
                    List<NewsWireItem> freshNews = Parser.parseNewsWireItems(newsJSONObj);

                    notifyNewsWireUpdate(null, freshNews);
                    return;
                }
            }

            notifyNewsWireUpdateFailed(intent.getStringExtra(NewsWireService.KEY_ERROR_MESSAGE));
        }
    }
    //----------------------------------------------------------------------------------------------
}
