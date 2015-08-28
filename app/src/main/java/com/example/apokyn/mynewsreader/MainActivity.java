package com.example.apokyn.mynewsreader;

import android.app.FragmentTransaction;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

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

        TextView title = (TextView) findViewById(R.id.toolbar_title);
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "old_english.ttf");

        title.setTypeface(custom_font);
        mDataManager = NewsReaderApplication.getDataManager();
        mNewsWireFragment =  new NewsWireFragment();
        mNewsWireFragment.setSection(NYTimesContract.Section.ALL);
    }

    @Override
    protected void onResume() {
        super.onResume();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.add(R.id.fragment_container, mNewsWireFragment);
        transaction.commit();
    }

    @Override
    public void onPause() {
        super.onPause();

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.remove(mNewsWireFragment);
        transaction.commit();
    }
}
