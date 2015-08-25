package com.example.apokyn.mynewsreader.entity;

import android.graphics.Bitmap;

/**
 * Created by apokyn on 25.08.2015.
 */
public class Image {

    private Bitmap mImage;
    private String mCapture;
    private String mCopyright;

    public Image(Bitmap image, String capture, String copyright) {
        mImage = image;
        mCapture = capture;
        mCopyright = copyright;
    }

    public Bitmap getImage() {
        return mImage;
    }

    public String getCapture() {
        return mCapture;
    }

    public String getCopyright() {
        return mCopyright;
    }
}
