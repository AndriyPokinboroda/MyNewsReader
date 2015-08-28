package com.example.apokyn.mynewsreader.utils;

import android.util.Log;

import com.example.apokyn.mynewsreader.internet.NYTimesContract;
import com.example.apokyn.mynewsreader.entity.Image;
import com.example.apokyn.mynewsreader.entity.NewsItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by apokyn on 25.08.2015.
 */
public class Parser {

    private static final String LOG_TAG = Parser.class.getSimpleName();

    private Parser() { }

    public static List<NewsItem> parseNewsWireItems(JSONObject jsonObject) {
        List<NewsItem> resultItems = new ArrayList<>();

        try {
            JSONArray newsWireItems = jsonObject.getJSONArray(NYTimesContract.NewsWire.FIELD_RESULTS);

            if (newsWireItems != null) {
                for (int i = 0; i < newsWireItems.length(); i++) {
                    resultItems.add(parseNewsWireItem(newsWireItems.getJSONObject(i)));
                }
            }
        } catch (JSONException e) {
            Log.d(LOG_TAG, e.getMessage());
        }

        return resultItems;
    }

    public static NewsItem parseNewsWireItem(JSONObject jsonObject) {
        List<Image> images = null;

        try {
            images = parseImages(jsonObject.getJSONArray(NYTimesContract.Multimedia.FIELD_MULTIMEDIA));
        } catch (JSONException e) {
            Log.d(LOG_TAG, e.getMessage());
        }

        return new NewsItem(
                jsonObject.optString(NYTimesContract.NewsWire.FIELD_TITLE),
                jsonObject.optString(NYTimesContract.NewsWire.FIELD_URL),
                jsonObject.optString(NYTimesContract.NewsWire.FIELD_BYLINE),
                jsonObject.optString(NYTimesContract.NewsWire.FIELD_ABSTRACT),
                images);
    }

    public static List<Image> parseImages(JSONArray jsonArray){
        List<Image> images = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                images.add(parseImage(jsonArray.getJSONObject(i)));
            } catch (JSONException e) {
                Log.d(LOG_TAG, e.getMessage());
            }
        }

        return images;
    }

    public static Image parseImage(JSONObject jsonObject) {
        return new Image(
                jsonObject.optString(NYTimesContract.Multimedia.FIELD_URL),
                jsonObject.optString(NYTimesContract.Multimedia.FIELD_CAPTION),
                jsonObject.optString(NYTimesContract.Multimedia.FIELD_COPYRIGHT));
    }
}
