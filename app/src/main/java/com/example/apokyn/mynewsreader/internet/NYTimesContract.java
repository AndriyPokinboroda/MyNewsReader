package com.example.apokyn.mynewsreader.internet;

/**
 * Created by apokyn on 25.08.2015.
 */
public class NYTimesContract {

    public static final String BASE_URL = "http://api.nytimes.com/svc/";
    public static final String RESPONCE_FORMAT = ".json";

    public static final String FIELD_API_KEY = "api-key";

    public static class NewsWire {

        public static final String NEWS_WIRE_API_KEY = "ce28bf606898b473be1a4cdd17ee165a%3A16%3A72738095";
        public static final String VERSION = "v3";
        public static final String API_NAME = "news";

        public static final String BASE_URL = NYTimesContract.BASE_URL + API_NAME + "/" + VERSION + "/";

        public static final String FIELD_SECTION = "section";
        public static final String FIELD_TITLE = "title";
        public static final String FIELD_ABSTRACT = "abstract";
        public static final String FIELD_URL = "url";
        public static final String FIELD_BYLINE = "byline";
        public static final String FIELD_RESULTS = "results";

    }

    public static class Multimedia {
        public static final String FIELD_MULTIMEDIA = "multimedia";

    }
}
