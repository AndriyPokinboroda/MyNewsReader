package com.example.apokyn.mynewsreader;

import android.app.Fragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apokyn.mynewsreader.data.DataManager;
import com.example.apokyn.mynewsreader.data.NewsWireListener;
import com.example.apokyn.mynewsreader.entity.NewsItem;
import com.example.apokyn.mynewsreader.internet.NYTimesContract;
import com.squareup.picasso.Picasso;

import java.util.List;


public class NewsWireFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, NewsWireListener {

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private NewsWireAdapter mAdapter;
    private NYTimesContract.Section mSection = NYTimesContract.Section.ALL;
    private DataManager mDataManager;
    private List<NewsItem> mNews;
    private boolean isFirstTimeOnStart = true;
    private boolean loading = true;
    private int progresViewIndex = -1;

    Typeface title;
    Typeface content;

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
                    if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                        loading = false;
                        mDataManager.appendNews(mSection);
                        mNews.add(null);
                        progresViewIndex = mNews.size() - 1;
                        mAdapter.notifyItemChanged(progresViewIndex);
                    }
                }
            }
        });

        return mSwipeRefreshLayout;
    }

    @Override
    public void onStart() {
        super.onStart();

        title = Typeface.createFromAsset(getActivity().getAssets(), "cheltenham-bold.ttf");
        content = Typeface.createFromAsset(getActivity().getAssets(), "cheltenham.ttf");
        mDataManager.registerNewsWireListener(this);

        if (isFirstTimeOnStart) {
            isFirstTimeOnStart = false;
            mDataManager.forceNewsUpdate(mSection);
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        mDataManager.unregisterNewsWireListener(this);
    }

    public void setSection(NYTimesContract.Section section) {
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
    public void onNewWireUpdated(NYTimesContract.Section section) {
        if (mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
            mSwipeRefreshLayout.setEnabled(true);
        } else {
            if (progresViewIndex != -1) {
                mNews.remove(progresViewIndex);
            }
            loading = true;
        }
        mNews = mDataManager.getNews(mSection);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onNewsUpdateFailed(NYTimesContract.Section section, String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        mSwipeRefreshLayout.setRefreshing(false);
        mSwipeRefreshLayout.setEnabled(true);
        loading = true;
    }
    //----------------------------------------------------------------------------------------------
    private class NewsWireAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private static final int VIEW_PROG = -1;
        private LayoutInflater mInflater;

        public NewsWireAdapter() {
            mInflater = LayoutInflater.from(NewsWireFragment.this.getActivity());
        }


        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int index) {
            View contentView;
            if (index == VIEW_PROG) {
                contentView = mInflater.inflate(R.layout.item_progress, parent, false);
                return new ProgressViewHolder(contentView);

            } else {
                contentView = mInflater.inflate(R.layout.item_new, parent, false);
                return new NewsItemViewHolder(contentView);
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int index) {
            if (viewHolder instanceof ProgressViewHolder) {
                ((ProgressViewHolder) viewHolder).progressBar.setIndeterminate(true);
            } else {
                NewsItemViewHolder newsItemViewHolder = (NewsItemViewHolder) viewHolder;
                newsItemViewHolder.titleView.setText(mNews.get(index).getTitle());
                newsItemViewHolder.bylineView.setText(mNews.get(index).getByline());
                newsItemViewHolder.abstractView.setText(mNews.get(index).getAbstract());

                if (mNews.get(index).getPhotos() != null && mNews.get(index).getPhotos().size() == 0) {
                    Picasso.with(getActivity())
                            .load(mNews.get(index).getPhotos().get(0).getImageUrl())
                            .into(newsItemViewHolder.thumbnailView);
                }
            }
        }

        @Override
        public int getItemCount() {
            return (mNews == null) ? 0 : mNews.size();
        }

        @Override
        public int getItemViewType(int position) {
            return mNews.get(position)!=null? position: VIEW_PROG;
        }

        public class NewsItemViewHolder extends RecyclerView.ViewHolder {

            public TextView titleView;
            public TextView bylineView;
            public TextView abstractView;
            public ImageView thumbnailView;

            public NewsItemViewHolder(View contentView) {
                super(contentView);

                titleView = (TextView) contentView.findViewById(R.id.item_title);
                bylineView = (TextView) contentView.findViewById(R.id.item_byline);
                abstractView = (TextView) contentView.findViewById(R.id.item_abstract);
                thumbnailView = (ImageView) contentView.findViewById(R.id.item_thumbnail);

                titleView.setTypeface(title);
                bylineView.setTypeface(content);
                abstractView.setTypeface(content);
            }
        }

        public class ProgressViewHolder extends RecyclerView.ViewHolder {

            public ProgressBar progressBar;

            public ProgressViewHolder(View contentView) {
                super(contentView);

                progressBar = (ProgressBar) contentView.findViewById(R.id.progress_bar);
            }
        }
    }
}
