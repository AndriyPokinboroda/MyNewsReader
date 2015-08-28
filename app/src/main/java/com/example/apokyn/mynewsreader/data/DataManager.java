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

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DataManager {

    private static final String LOG_TAG = DataManager.class.getSimpleName();

    public static final String KEY_BROADCAST_ACTION = "broadcastAction";
    public static final String KEY_SECTION = "section";
    public static final String KEY_OVERRIDE = "override";

    public static final String BROADCAST_ACTION_NEWS_WIRE_UPDATE = "newsWireUpdate";

    private Context mContext;
    private List<NewsWireListener> mNewsWireListeners;
    private Map<NYTimesContract.Section, List<NewsItem>> mNews;

    public DataManager(Context context) {
        mContext = context;
        mNewsWireListeners = new ArrayList<>();
        mNews = new HashMap<>();

        LocalBroadcastManager
                .getInstance(mContext)
                .registerReceiver(
                        new NewsWireResponseHandler(),
                        new IntentFilter(BROADCAST_ACTION_NEWS_WIRE_UPDATE));
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

    private void notifyNewsWireUpdated(NYTimesContract.Section section) {
        for (NewsWireListener listener : mNewsWireListeners) {
            listener.onNewWireUpdated(section);
        }
    }

    private void notifyNewsWireUpdateFailed(NYTimesContract.Section section, String message) {
        for (NewsWireListener listener : mNewsWireListeners) {
            listener.onNewsUpdateFailed(section, message);
        }
    }
    //----------------------------------------------------------------------------------------------
    // News Wire Updating
    //----------------------------------------------------------------------------------------------
    public void forceNewsUpdate(NYTimesContract.Section section) {
        List<NewsItem> sectionNews = mNews.get(section);

        if (sectionNews != null && sectionNews.size() != 0) {
            notifyNewsWireUpdated(section);
        } else {
            refreshNews(section);
        }
    }

    public void refreshNews(NYTimesContract.Section section) {
       performNewsWireRequest(
               new NYTUrlBuilder.NewsWire()
                       .addSection(section)
                       .build(),
               section.getValue(),
               true);
    }

    public void appendNews(NYTimesContract.Section section) {
        performNewsWireRequest(
                new NYTUrlBuilder.NewsWire()
                        .addSection(section)
                        .setOffset(mNews.get(section).size())
                        .build(),
                section.getValue(),
                false);
    }

    public List<NewsItem> getNews(NYTimesContract.Section section) {
        return mNews.get(section);
    }

    private void performNewsWireRequest(String url, String section, boolean override) {
        Intent requestIntent = new Intent(mContext, NewsWireService.class);

        requestIntent.putExtra(KEY_BROADCAST_ACTION, BROADCAST_ACTION_NEWS_WIRE_UPDATE);
        requestIntent.putExtra(NewsWireService.KEY_URL, url);
        requestIntent.putExtra(KEY_SECTION, section);
        requestIntent.putExtra(KEY_OVERRIDE, override);

        mContext.startService(requestIntent);
    }

    private class NewsWireResponseHandler extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String errorMessage;
            NYTimesContract.Section section =
                    NYTimesContract.Section.getSection(
                            intent.getStringExtra(KEY_SECTION));

            if (section != null) {
                errorMessage = intent.getStringExtra(NewsWireService.KEY_ERROR_MESSAGE);
                boolean isSuccessful = (errorMessage == null);

                if (isSuccessful) {
                    JSONObject newsJSONObj = null;

                    try {
                        newsJSONObj = new JSONObject(
                                intent.getStringExtra(NewsWireService.KEY_RESULT_JSON));
                    } catch (JSONException e) {
                        Log.d(LOG_TAG, e.getMessage());
                    }

                    if (newsJSONObj != null) {
                        List<NewsItem> freshNews = Parser.parseNewsWireItems(newsJSONObj);
                        boolean override = intent.getBooleanExtra(KEY_OVERRIDE, false);

                        if (override) {
                            mNews.put(section, freshNews);
                        } else {
                            if (mNews.get(section) == null) {
                                mNews.put(section, new ArrayList<NewsItem>());
                            }

                            mNews.get(section).addAll(freshNews);
                        }

                        notifyNewsWireUpdated(section);
                        return;
                    } else {
                        errorMessage = "Internal error";
                    }
                }
            } else {
                errorMessage = "Internal error";
            }

            notifyNewsWireUpdateFailed(section, errorMessage);
        }
    }
    //----------------------------------------------------------------------------------------------
}
