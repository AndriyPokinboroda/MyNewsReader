package com.example.apokyn.mynewsreader.entity;

import android.graphics.Bitmap;

import java.net.URL;

/**
 * Created by apokyn on 25.08.2015.
 */
public class Image {

    private URL mImage;
    private String mCapture;
    private String mCopyright;

    public Image(URL image, String capture, String copyright) {
        mImage = image;
        mCapture = capture;
        mCopyright = copyright;
    }

    public URL getImage() {
        return mImage;
    }

    public String getCapture() {
        return mCapture;
    }

    public String getCopyright() {
        return mCopyright;
    }
}
