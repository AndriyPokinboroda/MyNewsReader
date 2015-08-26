package com.example.apokyn.mynewsreader.data;

import com.example.apokyn.mynewsreader.entity.NewsWireItem;

import java.util.List;

/**
 * Created by apokyn on 26.08.2015.
 */
public interface NewsWireListener {

    void onNewsUpdated(String section, List<NewsWireItem> freshNews);

    void onNewsUpdateFailed(String message);
}
