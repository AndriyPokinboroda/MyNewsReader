package com.example.apokyn.mynewsreader;

import android.util.Log;

import com.example.apokyn.mynewsreader.entity.Image;
import com.example.apokyn.mynewsreader.entity.NewsWireItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by apokyn on 25.08.2015.
 */
public class Parser {

    private static final String LOG_TAG = "Parser";

    private Parser() { }

    public static List<NewsWireItem> parseNewsWireItems(JSONObject jsonObject) {
        JSONArray newsWireItems = null;
        List<NewsWireItem> resultItems = new ArrayList<>();
        try {
            newsWireItems = jsonObject.getJSONArray(NYTimesContract.NewsWire.FIELD_RESULTS);

            if (newsWireItems != null) {
                for (int i = 0; i < newsWireItems.length(); i++) {
                    resultItems.add(parseNewsWireItem(newsWireItems.getJSONObject(i)));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return resultItems;
    }

    public static NewsWireItem parseNewsWireItem(JSONObject jsonObject) {
        List<Image> images = null;
        try {
            images = parseImages(jsonObject.getJSONArray(NYTimesContract.Multimedia.FIELD_MULTIMEDIA));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new NewsWireItem(
                jsonObject.optString(NYTimesContract.NewsWire.FIELD_TITLE),
                jsonObject.optString(NYTimesContract.NewsWire.FIELD_URL),
                jsonObject.optString(NYTimesContract.NewsWire.FIELD_BYLINE),
                jsonObject.optString(NYTimesContract.NewsWire.FIELD_ABSTRACT),
                (images != null && images.size() > 0) ? images.get(0) : null);
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
                jsonObject.optString("url"),
                jsonObject.optString("caption"),
                jsonObject.optString("copyright"));

    }
}
