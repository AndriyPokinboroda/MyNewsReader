package com.example.apokyn.mynewsreader;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.apokyn.mynewsreader.entity.Image;
import com.example.apokyn.mynewsreader.entity.NewsWireItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by apokyn on 25.08.2015.
 */
public class NewsWireFragment extends Fragment {

    private RecyclerView mRecyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRecyclerView = new RecyclerView(getActivity());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        List<NewsWireItem> news = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            news.add(new NewsWireItem(
                    "Very loooo ooooooooooooo oooooooooooo oooooooong Title",
                    null,
                    "BYLINE",
                    "Some abstract text",
                    new Image(
                            null,
                            null,
                            null)));
        }
        mRecyclerView.setAdapter(new CustomAdapter(news));


        return mRecyclerView;
    }

    private class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

        private LayoutInflater mInflater;
        private List<NewsWireItem> mNews;

        public CustomAdapter(List<NewsWireItem> news) {
            mNews = news;

            mInflater = LayoutInflater.from(NewsWireFragment.this.getActivity());
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int index) {
            View contentView = mInflater.inflate(R.layout.news_wire_item, parent, false);

            return new ViewHolder(contentView);
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int index) {
            viewHolder.titleView.setText(mNews.get(index).getTitle());
            viewHolder.bylineView.setText(mNews.get(index).getByline());
            viewHolder.abstractView.setText(mNews.get(index).getAbstract());
        }

        @Override
        public int getItemCount() {
            return mNews.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            public TextView titleView;
            public TextView bylineView;
            public TextView abstractView;

            public ViewHolder(View contentView) {
                super(contentView);

                titleView = (TextView) contentView.findViewById(R.id.item_title);
                bylineView = (TextView) contentView.findViewById(R.id.item_byline);
                abstractView = (TextView) contentView.findViewById(R.id.item_abstract);
            }
        }
    }
}
