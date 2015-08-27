package com.example.apokyn.mynewsreader.data;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.example.apokyn.mynewsreader.internet.NYTUrlBuilder;
import com.example.apokyn.mynewsreader.internet.NYTimesContract;
import com.example.apokyn.mynewsreader.utils.Parser;
import com.example.apokyn.mynewsreader.entity.NewsItem;
import com.example.apokyn.mynewsreader.internet.NewsWireService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DataManager {

    private static final String BROADCAST_ACTION_NEWS_WIRE_UPDATE = "newsWireUpdate";

    private final String mLogTag = getClass().getSimpleName();

    private Context mContext;
    private BroadcastReceiver mNewsWireReceiver;
    private LocalBroadcastManager mBroadcastManager;
    private List<NewsWireListener> mNewsWireListeners;

    private Map<String, List<NewsItem>> mNews;

    public DataManager(Context context) {
        mContext = context;
        mNewsWireReceiver = new NewsWireResponseHandler();
        mBroadcastManager = LocalBroadcastManager.getInstance(mContext);
        mNewsWireListeners = new ArrayList<>();
        mNews = new HashMap<>();

        mBroadcastManager.registerReceiver(
                mNewsWireReceiver,
                new IntentFilter(BROADCAST_ACTION_NEWS_WIRE_UPDATE));
    }

    //----------------------------------------------------------------------------------------------
    // Observer
    //----------------------------------------------------------------------------------------------
    public void registerNewsWireListener(NewsWireListener listener, boolean forceUpdate) { //TODO force update
        mNewsWireListeners.add(listener);
    }

    public void unregisterNewsWireListener(NewsWireListener listener) {
        mNewsWireListeners.remove(listener);
    }

    private void notifyNewsWireUpdated(String section) {
        for (NewsWireListener listener : mNewsWireListeners) {
            listener.onNewWireUpdated(section);
        }
    }

    private void notifyNewsWireUpdateFailed(String section, String message) {
        for (NewsWireListener listener : mNewsWireListeners) {
            listener.onNewsUpdateFailed(section, message);
        }
    }
    //----------------------------------------------------------------------------------------------
    // News Wire Updating
    //----------------------------------------------------------------------------------------------
    public void refreshNews(String section) {
       performNewsWireRequest(
               new NYTUrlBuilder.NewsWire().addSection(section).build(),
               section,
               true);

    }

    public void appendNews(String section) {
        performNewsWireRequest(
                new NYTUrlBuilder.NewsWire().addSection(section).setOffset(mNews.get(section).size()).build(),
                section,
                false);
    }

    public List<NewsItem> getNews(String section) {
        return mNews.get(section);
    }

    private void performNewsWireRequest(String url, String section, boolean override) {
        Intent requestIntent = new Intent(mContext, NewsWireService.class);

        requestIntent.putExtra(NewsWireService.KEY_BROADCAST_ACTION, BROADCAST_ACTION_NEWS_WIRE_UPDATE);
        requestIntent.putExtra(NewsWireService.KEY_URL, url);
        requestIntent.putExtra(NewsWireService.KEY_SECTION, section);
        requestIntent.putExtra(NewsWireService.KEY_OVERRIDE, override);

        mContext.startService(requestIntent);
    }

    private class NewsWireResponseHandler extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String section = intent.getStringExtra(NewsWireService.KEY_SECTION);

            if (intent.getBooleanExtra(NewsWireService.KEY_IS_RESULT_OK, false)) {
                JSONObject newsJSONObj = null;

                try {
                    newsJSONObj = new JSONObject(intent.getStringExtra(NewsWireService.KEY_RESULT_JSON));
                } catch (JSONException e) {
                    Log.d(mLogTag, e.getMessage());
                }

                if (newsJSONObj != null) {
                    List<NewsItem> freshNews = Parser.parseNewsWireItems(newsJSONObj);

                    boolean override;
                    if (override  = intent.getBooleanExtra(NewsWireService.KEY_OVERRIDE, false)) {
                        mNews.put(section, freshNews);
                    } else {
                        if (mNews.get(section) == null) {
                            mNews.put(section, new ArrayList<NewsItem>());
                        }
                        mNews.get(section).addAll(freshNews);
                    }
                    for (NewsItem item : freshNews) {
                        Log.d(mLogTag, item.getTitle());
                    }
                    notifyNewsWireUpdated(section);
                    return;
                }
            }

            notifyNewsWireUpdateFailed(section, intent.getStringExtra(NewsWireService.KEY_ERROR_MESSAGE));
        }
    }
    //----------------------------------------------------------------------------------------------
}
