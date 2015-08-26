package com.example.apokyn.mynewsreader.entity;

import java.net.URL;

/**
 * Created by apokyn on 25.08.2015.
 */
public class NewsItem {

    private String mUrl;
    private String mTitle;
    private String mByline;
    private String mAbstract;
    private Image mImage;

    public NewsItem(String title, String url, String byline, String abstractText, Image image) {
        mUrl = url;
        mTitle = title;
        mByline = byline;
        mAbstract = abstractText;
        mImage = image;
    }


    public String getTitle() {
        return mTitle;
    }

    public String getUrl() {
        return mUrl;
    }

    public String getByline() {
        return mByline;
    }

    public String getAbstract() {
        return mAbstract;
    }
    public Image getPhoto() {
        return mImage;
    }
}
