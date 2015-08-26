package com.example.apokyn.mynewsreader;

import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.apokyn.mynewsreader.data.DataManager;
import com.example.apokyn.mynewsreader.data.NewsWireListener;
import com.example.apokyn.mynewsreader.entity.NewsWireItem;
import com.example.apokyn.mynewsreader.internet.NewsWireService;

import java.util.List;

public class MainActivity extends AppCompatActivity implements NewsWireListener {

    private final String mLogTeg = getClass().getSimpleName();
    private DataManager mDataManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDataManager = NewsReaderApplication.getDataManager();

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.add(R.id.fragment_container, new NewsWireFragment());
        transaction.commit();
    }

    @Override
    protected void onStart() {
        super.onStart();

        mDataManager.registerNewsWireListener(this);
        mDataManager.updateNewsWire(null);
    }

    @Override
    protected void onStop() {
        super.onStop();

        mDataManager.unregisterNewsWireListener(this);
    }

    //----------------------------------------------------------------------------------------------
    // NewsWireListener
    //----------------------------------------------------------------------------------------------
    @Override
    public void onNewsUpdated(String section, List<NewsWireItem> freshNews) {
        Toast.makeText(this, "All is OK", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onNewsUpdateFailed(String message) {
        Toast.makeText(this, "Some error occurred : " + message, Toast.LENGTH_SHORT).show();
    }
    //----------------------------------------------------------------------------------------------
}
