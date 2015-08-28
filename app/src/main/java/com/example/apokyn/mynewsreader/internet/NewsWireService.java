package com.example.apokyn.mynewsreader.internet;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.example.apokyn.mynewsreader.data.DataManager;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.net.UnknownHostException;


public class NewsWireService extends IntentService {

    private static final String LOG_TAG = NewsWireService.class.getSimpleName();

    /* Intent required extras */
    public static final String KEY_URL = "requestUrl";

    /* Return by broadcast */
    public static final String KEY_RESULT_JSON = "resultJSON";
    public static final String KEY_ERROR_MESSAGE = "errorMessage";

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
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        return START_REDELIVER_INTENT;
    }

    @Override
    protected void onHandleIntent(Intent requestIntent) {
        /* Validate request data */
        String requestUrl;

        if ((requestUrl = requestIntent.getStringExtra(KEY_URL)) == null) {
            throw new IllegalArgumentException("Intent should contains not null url extra");
        }

        /* Perform request */
        Request request = new Request.Builder().url(requestUrl).build();
        Response response = null;
        String errorMessage = null;
        String responseJSON = null;

        try {
            response = mHttpClient.newCall(request).execute();
        } catch (IOException e) {
            Log.d(LOG_TAG, e.getMessage());

            if (e instanceof UnknownHostException) {
                errorMessage = "Can't connect to server";
            } else {
                errorMessage = "Server Error";
            }
            //TODO validate errors
        }

        boolean isSuccessful = (errorMessage == null);

        if (isSuccessful) {
            try {
                responseJSON = response.body().string();
            } catch (IOException e) {
                Log.d(LOG_TAG, e.getMessage());
            }
        }
        /* Build response intent */
        Intent resultIntent = new Intent(requestIntent.getStringExtra(DataManager.KEY_BROADCAST_ACTION));

        resultIntent.putExtra(DataManager.KEY_SECTION, requestIntent.getStringExtra(DataManager.KEY_SECTION));
        resultIntent.putExtra(DataManager.KEY_OVERRIDE, requestIntent.getBooleanExtra(DataManager.KEY_OVERRIDE, false));
        resultIntent.putExtra(KEY_ERROR_MESSAGE, errorMessage);
        resultIntent.putExtra(KEY_RESULT_JSON, responseJSON);

        mBroadcastManager.sendBroadcast(resultIntent);
    }
}
