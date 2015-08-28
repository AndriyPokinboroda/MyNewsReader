package com.example.apokyn.mynewsreader.data;

import com.example.apokyn.mynewsreader.entity.NewsItem;
import com.example.apokyn.mynewsreader.internet.NYTimesContract;

import java.util.List;

/**
 * Created by apokyn on 26.08.2015.
 */
public interface NewsWireListener {

    void onNewWireUpdated(NYTimesContract.Section section);

    void onNewsUpdateFailed(NYTimesContract.Section section, String message);
}
