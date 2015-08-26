package com.example.apokyn.mynewsreader;

import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.apokyn.mynewsreader.data.DataManager;
import com.example.apokyn.mynewsreader.data.NewsWireListener;
import com.example.apokyn.mynewsreader.entity.NewsItem;

import java.util.List;

public class MainActivity extends AppCompatActivity implements NewsWireListener {

    private final String mLogTag = getClass().getSimpleName();
    private DataManager mDataManager;
    private NewsWireFragment mNewsWireFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDataManager = NewsReaderApplication.getDataManager();
        mNewsWireFragment =  new NewsWireFragment();

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.add(R.id.fragment_container, mNewsWireFragment);
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
    public void onNewsUpdated(String section, List<NewsItem> freshNews) {
        Toast.makeText(this, "All is OK", Toast.LENGTH_SHORT).show();
        mNewsWireFragment.appendNews(freshNews);
    }

    @Override
    public void onNewsUpdateFailed(String message) {
        Toast.makeText(this, "Some error occurred : " + message, Toast.LENGTH_SHORT).show();
    }
    //----------------------------------------------------------------------------------------------
}
