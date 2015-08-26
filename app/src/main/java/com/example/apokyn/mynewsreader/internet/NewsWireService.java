package com.example.apokyn.mynewsreader.internet;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.net.UnknownHostException;


public class NewsWireService extends IntentService {
    /* Broadcast */
    public static final String ACTION_NEWS_WIRE_UPDATE = "actionNewsWireUpdate";
    public static final String KEY_NEWS_WIRE_RESULT_JSON = "news";
    public static final String KEY_IS_NEWS_LOADED = "isNewsWireUpdated";
    public static final String KEY_ERROR_MESSAGE = "errorMessage";

    private static final String NEWS_WIRE_API_KEY = "ce28bf606898b473be1a4cdd17ee165a%3A16%3A72738095";
    private String url = "http://api.nytimes.com/svc/news/v3/content/all/all/.json?api-key=" + NEWS_WIRE_API_KEY;

    private String mLogTag = getClass().getSimpleName();
    private OkHttpClient mHttpClient;
    private LocalBroadcastManager mBroadcastManager;

    public NewsWireService() {
        this("Worker thread");
    }

    public NewsWireService(String name) {
        super(name);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mHttpClient = new OkHttpClient();
        mBroadcastManager = LocalBroadcastManager.getInstance(this);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        /* Perform request */
        Request request = new Request.Builder().url(url).build();
        Response response = null;
        String errorMessage = null;

        try {
            response = mHttpClient.newCall(request).execute();
        } catch (IOException e) {
            Log.d(mLogTag, e.getMessage());

            if (e instanceof UnknownHostException) {
                errorMessage = "Can't connect to server";
            } else {
                errorMessage = "Another exception";
            }
        }

        /* Build intent */
        Intent resultIntent = new Intent();
        boolean isSuccessful = (errorMessage == null);
        String responseJSON = null;

        if (isSuccessful) {
            try {
                responseJSON = response.body().string();
            } catch (IOException e) {
                Log.d(mLogTag, e.getMessage());
            }
        }

        resultIntent.setAction(ACTION_NEWS_WIRE_UPDATE);
        resultIntent.putExtra(KEY_IS_NEWS_LOADED, isSuccessful);
        resultIntent.putExtra(KEY_ERROR_MESSAGE, !isSuccessful ? errorMessage : null);
        resultIntent.putExtra(KEY_NEWS_WIRE_RESULT_JSON, (responseJSON != null) ? responseJSON : null);

        mBroadcastManager.sendBroadcast(resultIntent);
    }

}
