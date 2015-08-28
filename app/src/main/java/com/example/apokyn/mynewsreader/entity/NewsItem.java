package com.example.apokyn.mynewsreader.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.net.URL;
import java.util.List;

/**
 * Created by apokyn on 25.08.2015.
 */
public class NewsItem {

    private String mUrl;
    private String mTitle;
    private String mByline;
    private String mAbstract;
    private List<Image> mImage;

    public NewsItem(String title, String url, String byline, String abstractText, List<Image> image) {
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
    public List<Image> getPhotos() {
        return mImage;
    }

}
