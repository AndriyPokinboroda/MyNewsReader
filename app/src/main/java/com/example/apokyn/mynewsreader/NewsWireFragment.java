package com.example.apokyn.mynewsreader;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.apokyn.mynewsreader.entity.NewsItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class NewsWireFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private CustomAdapter mAdapter;

//    Typeface mCheltenhamBold;
//    Typeface mCheltenham;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRecyclerView = new RecyclerView(getActivity());
        mAdapter = new CustomAdapter(null);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

//        mCheltenhamBold = Typeface.createFromAsset(getActivity().getAssets(), "font/cheltenham-bold.ttf");
//        mCheltenham = Typeface.createFromAsset(getActivity().getAssets(), "font/cheltenham.ttf");
        return mRecyclerView;
    }

    public void appendNews(List<NewsItem> news) {
        mAdapter.addNews(news);
    }

    private class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

        private LayoutInflater mInflater;
        private List<NewsItem> mNews;

        public CustomAdapter(List<NewsItem> news) {
            if (news != null) {
                mNews = news;
            } else {
                mNews = new ArrayList<>();
            }

            mInflater = LayoutInflater.from(NewsWireFragment.this.getActivity());
        }

        public void addNews(List<NewsItem> news) {
            if (!mNews.containsAll(news)) {
                mNews.addAll(news);
                notifyDataSetChanged();
            }
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



            if (mNews.get(index).getPhoto() != null) {
                Picasso.with(getActivity()).load(mNews.get(index).getPhoto().getImage()).into(viewHolder.thumbnailView);
            }
        }

        @Override
        public int getItemCount() {
            return mNews.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            public TextView titleView;
            public TextView bylineView;
            public TextView abstractView;
            public ImageView thumbnailView;

            public ViewHolder(View contentView) {
                super(contentView);

                titleView = (TextView) contentView.findViewById(R.id.item_title);
                bylineView = (TextView) contentView.findViewById(R.id.item_byline);
                abstractView = (TextView) contentView.findViewById(R.id.item_abstract);
                thumbnailView = (ImageView) contentView.findViewById(R.id.item_thumbnail);

//                titleView.setTypeface(mCheltenhamBold);
//                bylineView.setTypeface(mCheltenham);
//                abstractView.setTypeface(mCheltenham);
            }
        }
    }
}
