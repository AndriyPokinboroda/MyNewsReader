package com.example.apokyn.mynewsreader;

import android.app.IntentService;
import android.content.Intent;

/**
 * Created by apokyn on 25.08.2015.
 */
public class NewsWireService extends IntentService {

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public NewsWireService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }
}
