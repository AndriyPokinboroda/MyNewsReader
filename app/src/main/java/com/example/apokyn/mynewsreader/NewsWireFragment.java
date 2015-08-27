package com.example.apokyn.mynewsreader;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apokyn.mynewsreader.data.DataManager;
import com.example.apokyn.mynewsreader.data.NewsWireListener;
import com.example.apokyn.mynewsreader.entity.NewsItem;
import com.squareup.picasso.Picasso;

import java.util.List;


public class NewsWireFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, NewsWireListener {

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private NewsWireAdapter mAdapter;
    private String mSection = "all";
    private DataManager mDataManager;
    private List<NewsItem> mNews;
    private boolean isFirstTimeOnStart = true;
    private boolean loading = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDataManager = NewsReaderApplication.getDataManager();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRecyclerView = new RecyclerView(getActivity());
        mAdapter = new NewsWireAdapter();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mSwipeRefreshLayout = new SwipeRefreshLayout(getActivity());
        mSwipeRefreshLayout.setOnRefreshListener(this);

        mSwipeRefreshLayout.addView(mRecyclerView);

        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int visibleItemCount = mRecyclerView.getLayoutManager().getChildCount();
                int totalItemCount = mRecyclerView.getLayoutManager().getItemCount();
                int pastVisiblesItems = ((LinearLayoutManager) mRecyclerView.getLayoutManager()).findFirstVisibleItemPosition();

                if (loading) {
                    if ( (visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                        loading = false;
                        Toast.makeText(getActivity(), "More", Toast.LENGTH_SHORT).show();
                        mDataManager.appendNews(mSection);
                    }
                }
            }
        });

        return mSwipeRefreshLayout;
    }

    @Override
    public void onStart() {
        super.onStart();

        mDataManager.registerNewsWireListener(this);

        if (isFirstTimeOnStart) {
            isFirstTimeOnStart = false;
            mDataManager.refreshNews(mSection);
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        mDataManager.unregisterNewsWireListener(this);
    }

    public void setSection(String section) {
        mSection = section;
    }

    //----------------------------------------------------------------------------------------------
    // SwipeRefreshLayout.OnRefreshListener
    //----------------------------------------------------------------------------------------------
    @Override
    public void onRefresh() {
        mSwipeRefreshLayout.setRefreshing(true);
        mSwipeRefreshLayout.setEnabled(false);
        mDataManager.refreshNews(mSection);
    }
    //----------------------------------------------------------------------------------------------
    // NewsWireListener
    //----------------------------------------------------------------------------------------------
    @Override
    public void onNewWireUpdated(String section) {
        mNews = mDataManager.getNews(mSection);
        mAdapter.notifyDataSetChanged();
        mSwipeRefreshLayout.setRefreshing(false);
        mSwipeRefreshLayout.setEnabled(true);
        loading = true;
    }

    @Override
    public void onNewsUpdateFailed(String section, String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        mSwipeRefreshLayout.setRefreshing(false);
        mSwipeRefreshLayout.setEnabled(true);
        loading = true;
    }
    //----------------------------------------------------------------------------------------------
    private class NewsWireAdapter extends RecyclerView.Adapter<NewsWireAdapter.ViewHolder> {

        private LayoutInflater mInflater;

        public NewsWireAdapter() {
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

            if (mNews.get(index).getPhoto() != null) {
                Picasso.with(getActivity()).load(mNews.get(index).getPhoto().getImage()).into(viewHolder.thumbnailView);
            }
        }

        @Override
        public int getItemCount() {
            return (mNews == null) ? 0 : mNews.size();
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
            }
        }
    }
}
