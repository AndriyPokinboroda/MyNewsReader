package com.example.apokyn.mynewsreader.internet;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.net.UnknownHostException;


public class DownloadService extends IntentService {
    /* Intent required extras */
    public static final String KEY_BROADCAST_ACTION = "broadcastAction";
    public static final String KEY_URL = "requestUrl";
    /* Broadcast */
    public static final String KEY_RESULT_JSON = "resultJSON";
    public static final String KEY_IS_RESULT_OK = "isResultOk";
    public static final String KEY_ERROR_MESSAGE = "errorMessage";

    private String mLogTag = getClass().getSimpleName();
    private OkHttpClient mHttpClient;
    private LocalBroadcastManager mBroadcastManager;

    public DownloadService() {
        this("Worker thread");
    }

    public DownloadService(String name) {
        super(name);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mHttpClient = new OkHttpClient();
        mBroadcastManager = LocalBroadcastManager.getInstance(this);
    }

    @Override
    protected void onHandleIntent(Intent requestIntent) {
        /* Validate request data */
        Bundle requestExtras = requestIntent.getExtras();
        String requestUrl;
        String broadCastAction;

        if ((requestUrl = requestExtras.getString(KEY_URL)) == null
                ||(broadCastAction = requestExtras.getString(KEY_BROADCAST_ACTION)) == null) {
            throw new IllegalArgumentException("Intent should contains not null broadcast action and url extras");
        }
        /* Perform request */
        Request request = new Request.Builder().url(requestUrl).build();
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
            //TODO validate error
        }

        /* Build response intent */
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

        resultIntent.setAction(broadCastAction);
        resultIntent.putExtra(KEY_IS_RESULT_OK, isSuccessful);
        resultIntent.putExtra(KEY_ERROR_MESSAGE, !isSuccessful ? errorMessage : null);
        resultIntent.putExtra(KEY_RESULT_JSON, (responseJSON != null) ? responseJSON : null);

        mBroadcastManager.sendBroadcast(resultIntent);
    }

}