package com.example.apokyn.mynewsreader;

import com.example.apokyn.mynewsreader.entity.NewsWireItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by apokyn on 25.08.2015.
 */
public class Parser {

    private Parser() { }

    public static List<NewsWireItem> parseNewsWireItems(JSONObject jsonObject) {
        JSONArray newsWireItems = null;
        List<NewsWireItem> resultItems = new ArrayList<>();
        try {
            newsWireItems = jsonObject.getJSONArray(NYTimesContract.NewsWire.FIELD_RESAULTS);

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
        return new NewsWireItem(
                jsonObject.optString(NYTimesContract.NewsWire.FIELD_TITLE, "title"),
                null,
                jsonObject.optString(NYTimesContract.NewsWire.FIELD_BYLINE, "byline"),
                jsonObject.optString(NYTimesContract.NewsWire.FIELD_ABSTRACT, "abstract"),
                null);
    }
}
