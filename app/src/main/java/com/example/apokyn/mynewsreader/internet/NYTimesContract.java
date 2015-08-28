package com.example.apokyn.mynewsreader.internet;

/**
 * Created by apokyn on 25.08.2015.
 */
public class NYTimesContract {

    public static final String BASE_URL = "http://api.nytimes.com/svc/";
    public static final String RESPONSE_FORMAT_JSON = ".json";
    public static final String PARAMETER_API_KEY = "api-key";

    public enum Section {
        ALL("all"),
        ARTS("Arts"),
        BOOKS("Books"),
        BUSINESS("Business"),
        EDUCATION("Education"),
        HEALTH("Health");

        private String mSection;

        Section(String section) {
            mSection = section;
        }

        public static Section getSection(String section) {
            for (Section availableSection : Section.values()) {
                if (availableSection.mSection.equals(section)) {
                    return availableSection;
                }
            }
            return null;
        }
        public String getValue() {
            return mSection;
        }
    }

    public enum Source {
        ALL("all"),
        NEW_YORK_TIMES("nyt"),
        INTERNATIONAL_HERALD_TRIBUNE("iht");

        private String mSources;

        Source(String source) {
            mSources = source;
        }

        public String getValue() {
            return mSources;
        }
    }

    public static class NewsWire {
        //------------------------------------------------------------------------------------------
        // COMMON URL PATTERN
        // Curly braces {} indicate required items.
        // Square brackets [] indicate optional items or placeholders.
        //
        // http://api.nytimes.com/svc/
        // news/{version}/content/{source}/{section}[/time-period][.response-format]
        // ?[parameter1=value1& ... &]api-key={your-API-key}
        //------------------------------------------------------------------------------------------

        /* for building url */
            /* Time period attribute (measure in hour) */
        public static final String PARAMETER_TIME_PERIOD = "time-period";
        public static final int TIME_PERIOD_MIN = 1;
        public static final int TIME_PERIOD_MAX = 720;
        public static final int TIME_PERIOD_DEFAULT = 24;

            /* Count of article limit */
        public static final String PARAMETER_LIMIT = "limit";
        public static final int LIMIT_MIN = 1;
        public static final int LIMIT_MAX = 20;
        public static final int LIMIT_DEFAULT = 20;

            /* Offset - starting point of the result set (The max limit is the count of available records) */
        public static final String PARAMETER_OFFSET = "offset";
        public static final int OFFSET_MIN = 0;
        public static final int OFFSET_DEFAULT = 0;

        public static final String API_KEY = "ce28bf606898b473be1a4cdd17ee165a%3A16%3A72738095";
        public static final String VERSION = "v3";
        public static final String API_NAME = "news";
        public static final String BASE_URL = NYTimesContract.BASE_URL + API_NAME + "/" + VERSION + "/content/";

        /* for parse response */
        public static final String FIELD_TITLE = "title";
        public static final String FIELD_ABSTRACT = "abstract";
        public static final String FIELD_URL = "url";
        public static final String FIELD_BYLINE = "byline";
        public static final String FIELD_RESULTS = "results";
    }

    public static class Multimedia {
        public static final String FIELD_MULTIMEDIA = "multimedia";
        public static final String FIELD_URL = "url";
        public static final String FIELD_CAPTION = "caption";
        public static final String FIELD_COPYRIGHT = "copyright";
    }
}
