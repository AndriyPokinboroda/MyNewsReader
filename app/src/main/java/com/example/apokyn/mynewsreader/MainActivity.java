package com.example.apokyn.mynewsreader;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;

import com.example.apokyn.mynewsreader.internet.NewsWireService;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.add(R.id.fragment_container,  new NewsWireFragment());
        transaction.commit();
    }

    @Override
    protected void onStart() {
        super.onStart();

        startService(new Intent(this, NewsWireService.class));
    }
}
