package com.example.apokyn.mynewsreader.data;

import com.example.apokyn.mynewsreader.entity.NewsItem;

import java.util.List;

/**
 * Created by apokyn on 26.08.2015.
 */
public interface NewsWireListener {

    void onNewWireUpdated(String section);

    void onNewsUpdateFailed(String section, String message);
}
