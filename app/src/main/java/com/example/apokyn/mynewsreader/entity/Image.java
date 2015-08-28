package com.example.apokyn.mynewsreader.entity;

/**
 * Created by apokyn on 25.08.2015.
 */
public class Image {

    private String mImageUrl;
    private String mCapture;
    private String mCopyright;

    public Image(String imageUrl, String capture, String copyright) {
        mImageUrl = imageUrl;
        mCapture = capture;
        mCopyright = copyright;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public String getCapture() {
        return mCapture;
    }

    public String getCopyright() {
        return mCopyright;
    }
}
