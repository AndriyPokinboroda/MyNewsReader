package com.example.apokyn.mynewsreader;

import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.apokyn.mynewsreader.data.DataManager;
import com.example.apokyn.mynewsreader.data.NewsWireListener;
import com.example.apokyn.mynewsreader.entity.NewsItem;
import com.example.apokyn.mynewsreader.internet.NYTimesContract;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final String mLogTag = getClass().getSimpleName();
    private DataManager mDataManager;
    private NewsWireFragment mNewsWireFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDataManager = NewsReaderApplication.getDataManager();
        mNewsWireFragment =  new NewsWireFragment();
        mNewsWireFragment.setSection("all");

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.add(R.id.fragment_container, mNewsWireFragment);
        transaction.commit();
    }
}
