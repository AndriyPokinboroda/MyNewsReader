package com.example.apokyn.mynewsreader.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.net.URL;

/**
 * Created by apokyn on 25.08.2015.
 */
public class NewsItem implements Parcelable {

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


    protected NewsItem(Parcel in) {
        mUrl = in.readString();
        mTitle = in.readString();
        mByline = in.readString();
        mAbstract = in.readString();
        mImage = (Image) in.readValue(Image.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mUrl);
        dest.writeString(mTitle);
        dest.writeString(mByline);
        dest.writeString(mAbstract);
        dest.writeValue(mImage);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<NewsItem> CREATOR = new Parcelable.Creator<NewsItem>() {
        @Override
        public NewsItem createFromParcel(Parcel in) {
            return new NewsItem(in);
        }

        @Override
        public NewsItem[] newArray(int size) {
            return new NewsItem[size];
        }
    };
}
