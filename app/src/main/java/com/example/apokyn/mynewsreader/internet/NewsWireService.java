package com.example.apokyn.mynewsreader.internet;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.apokyn.mynewsreader.Parser;
import com.example.apokyn.mynewsreader.entity.NewsWireItem;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

/**
 * Created by apokyn on 25.08.2015.
 */
public class NewsWireService extends IntentService {

    private static final String NEWS_WIRE_API_KEY = "ce28bf606898b473be1a4cdd17ee165a%3A16%3A72738095";
    private String url = "http://api.nytimes.com/svc/news/v3/content/all/all/.json?api-key=" + NEWS_WIRE_API_KEY;
    private NewsWireListener mNewsListeners;
    private OkHttpClient mHttpClient;

    private String mTag = getClass().getSimpleName();

    public interface NewsWireListener {
        void onNewsWireUpdated(String section, List<NewsWireItem> news);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mHttpClient = new OkHttpClient();
    }

    public NewsWireService() {
        this("Worker thread");
    }

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public NewsWireService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        JSONObject jsonObject = run(url);

        if (jsonObject != null) {
            List<NewsWireItem> newsWireItems = Parser.parseNewsWireItems(jsonObject);

            for (NewsWireItem item : newsWireItems) {
                Log.d(mTag, "Title : " + item.getTitle() + " Byline : " + item.getByline() + " Abstract : " + item.getAbstract());
            }
        } else {
            Toast.makeText(getApplicationContext(), "Some error", Toast.LENGTH_SHORT).show();
            Log.d(mTag, "Some error");
        }
    }

    private JSONObject run(String url) {
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = null;
        try {
            response = mHttpClient.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (response != null) {
            try {
                return new JSONObject(response.body().string());
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }
}
