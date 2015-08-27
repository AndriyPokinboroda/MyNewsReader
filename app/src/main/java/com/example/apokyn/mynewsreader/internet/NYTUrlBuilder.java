package com.example.apokyn.mynewsreader.internet;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by apokyn on 26.08.2015.
 */
public class NYTUrlBuilder {
    public static class NewsWire {

        private List<String> mSections;
        private String mSource;
        private int mTimePeriod;
        private int mLimit;
        private int mOffset;

        public NewsWire(){
            mSections = new ArrayList<>();
            mSource = NYTimesContract.NewsWire.SOURCE_ALL;
            mTimePeriod = NYTimesContract.NewsWire.TIME_PERIOD_DEFAULT;
            mLimit = NYTimesContract.NewsWire.LIMIT_DEFAULT;
            mOffset = NYTimesContract.NewsWire.OFFSET_DEFAULT;
        }

        public NewsWire addSection(@NonNull String section) { //TODO section validation
            mSections.add(section);
            return this;
        }

        public NewsWire setSource(String source) { //TODO source validation
            mSource = source;
            return this;
        }

        public NewsWire setTimePeriod(int hour) {
            if ((hour < NYTimesContract.NewsWire.TIME_PERIOD_MIN)
                    || (hour > NYTimesContract.NewsWire.TIME_PERIOD_MAX)) {
                throw new IllegalArgumentException(
                        "The hour attr should be in range from "
                        + NYTimesContract.NewsWire.TIME_PERIOD_MIN  + " to "
                        + NYTimesContract.NewsWire.TIME_PERIOD_MAX);
            }

            mTimePeriod = hour;
            return this;
        }

        public NewsWire setLimit(int limit) {
            if ((limit < NYTimesContract.NewsWire.LIMIT_MIN)
                    || (limit > NYTimesContract.NewsWire.LIMIT_MAX)) {
                throw new IllegalArgumentException(
                        "The limit attr should be in range from "
                                + NYTimesContract.NewsWire.LIMIT_MIN  + " to "
                                + NYTimesContract.NewsWire.LIMIT_MAX);
            }

            mLimit = limit;
            return this;
        }

        public NewsWire setOffset(int offset) {
            if (offset < NYTimesContract.NewsWire.OFFSET_MIN) {
                throw new IllegalArgumentException(
                        "The offset attr should be grater then"
                        + NYTimesContract.NewsWire.OFFSET_MIN);
            }

            mOffset = offset;
            return this;
        }

        public String build(){

            return new StringBuilder()
                    .append(NYTimesContract.NewsWire.BASE_URL)
                    .append(mSource)
                    .append("/")
                    .append((mSections.size() == 0)
                            ? NYTimesContract.NewsWire.SECTION_ALL
                            : TextUtils.join(",", mSections))
                    .append("/")
                    .append(mTimePeriod)
                    .append(NYTimesContract.RESPONSE_FORMAT_JSON)
                    .append("?")
                    .append(buildParameter(NYTimesContract.NewsWire.PARAMETER_LIMIT,
                            String.valueOf(mLimit)))
                    .append("&")
                    .append(buildParameter(NYTimesContract.NewsWire.PARAMETER_OFFSET,
                            String.valueOf(mOffset)))
                    .append("&")
                    .append(buildParameter(NYTimesContract.NewsWire.PARAMETER_TIME_PERIOD,
                            String.valueOf(mTimePeriod)))
                    .append("&")
                    .append(buildParameter(NYTimesContract.PARAMETER_API_KEY,
                            NYTimesContract.NewsWire.API_KEY))
                    .toString();
        }

        private String buildParameter(String parameter, String value) {
            return parameter + "=" + value;
        }
    }
}
