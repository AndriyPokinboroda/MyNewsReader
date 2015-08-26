package com.example.apokyn.mynewsreader.internet;

/**
 * Created by apokyn on 25.08.2015.
 */
public class NYTimesContract {

    public static final String BASE_URL = "http://api.nytimes.com/svc/";
    public static final String RESPONCE_FORMAT = ".json";

    public static final String FIELD_API_KEY = "api-key";

    public static class NewsWire {
        //------------------------------------------------------------------------------------------
        // COMMON URL PATTERN
        // Curly braces {} indicate required items.
        // Square brackets [] indicate optional items or placeholders.
        //
        // http://api.nytimes.com/svc/news/
        // {version}/content/{source}/{section}[/time-period][.response-format]?
        // [parameter1=value1& ... &]api-key={your-API-key}
        //------------------------------------------------------------------------------------------
        /* for building url */
        public static final String API_KEY = "ce28bf606898b473be1a4cdd17ee165a%3A16%3A72738095";
        public static final String VERSION = "v3";
        public static final String API_NAME = "news";
            /* Some available values for section  */
        public static final String SECTION_ALL = "all";
            /* Available values for source */
        public static final String SOURCE_ALL = "all";
        public static final String SOURCE_NEW_YORK_TIMES_= "nyt";
        public static final String SOURCE_INTERATIONAL_HERALD_TRIBUNE = "iht";

        public static final String BASE_URL = NYTimesContract.BASE_URL + API_NAME + "/" + VERSION + "/content/";

        /* for parse response */
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
