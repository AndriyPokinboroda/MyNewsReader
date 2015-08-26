package com.example.apokyn.mynewsreader.entity;

/**
 * Created by apokyn on 25.08.2015.
 */
public class Image {

    private String mImage;
    private String mCapture;
    private String mCopyright;

    public Image(String image, String capture, String copyright) {
        mImage = image;
        mCapture = capture;
        mCopyright = copyright;
    }

    public String getImage() {
        return mImage;
    }

    public String getCapture() {
        return mCapture;
    }

    public String getCopyright() {
        return mCopyright;
    }
}
