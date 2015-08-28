package com.example.apokyn.mynewsreader;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.apokyn.mynewsreader.data.DataManager;
import com.example.apokyn.mynewsreader.data.NewsWireListener;
import com.example.apokyn.mynewsreader.entity.NewsItem;
import com.example.apokyn.mynewsreader.internet.NYTimesContract;
import com.squareup.picasso.Picasso;

import java.util.List;


public class NewsWireFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, NewsWireListener {

    private static final String KEY_SAVED_OFFSET = "listOffset";

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private NewsWireAdapter mAdapter;

    private NYTimesContract.Section mSection = null;
    private DataManager mDataManager;
    private List<NewsItem> mNews;

    private boolean mIsFirstTimeOnStart = true;
    private boolean mIsAppending = false;
    private int mProgressViewIndex = -1;
    private int mThumbnailSize;

    private Typeface mTitleFont;
    private Typeface mContentFont;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (mSection == null) {
            throw new IllegalStateException("Should set section before display fragment");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mThumbnailSize = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.thumbnail_news_size),
                getResources().getDisplayMetrics());

        mDataManager = NewsReaderApplication.getDataManager();

        mRecyclerView = new RecyclerView(getActivity());
        mAdapter = new NewsWireAdapter();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mSwipeRefreshLayout = new SwipeRefreshLayout(getActivity());
        mSwipeRefreshLayout.setOnRefreshListener(this);

        mSwipeRefreshLayout.addView(mRecyclerView);
        mSwipeRefreshLayout.setColorSchemeColors(R.color.blue_gray_400);

        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int visibleItemCount = mRecyclerView.getLayoutManager().getChildCount();
                int totalItemCount = mRecyclerView.getLayoutManager().getItemCount();
                int pastVisibleItems = ((LinearLayoutManager) mRecyclerView.getLayoutManager()).findFirstVisibleItemPosition();

                if (!mIsAppending) {
                    if ((visibleItemCount + pastVisibleItems) >= totalItemCount && dy > 0) {
                        showAppendProgressBar(true);
                        mDataManager.appendNews(mSection);
                    }
                }
            }
        });

        if (savedInstanceState != null && savedInstanceState.containsKey(KEY_SAVED_OFFSET)) {
            mRecyclerView.scrollToPosition(savedInstanceState.getInt(KEY_SAVED_OFFSET));
        }

        return mSwipeRefreshLayout;
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Log.d("Tag", ((LinearLayoutManager) mRecyclerView.getLayoutManager()).findFirstVisibleItemPosition() + "");
        outState.putInt(KEY_SAVED_OFFSET, ((LinearLayoutManager) mRecyclerView.getLayoutManager()).findFirstVisibleItemPosition());
    }

    @Override
    public void onStart() {
        super.onStart();

        mDataManager.registerNewsWireListener(this);

        if (mIsFirstTimeOnStart) {
            mIsFirstTimeOnStart = false;
            mDataManager.forceNewsUpdate(mSection);
        }

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        int offset = preferences.getInt(KEY_SAVED_OFFSET, 0);

        mRecyclerView.scrollToPosition(offset);
    }

    @Override
    public void onStop() {
        super.onStop();

        mDataManager.unregisterNewsWireListener(this);
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getActivity()).edit();
        editor.putInt(KEY_SAVED_OFFSET, ((LinearLayoutManager) mRecyclerView.getLayoutManager()).findFirstVisibleItemPosition());
        editor.apply();

        Log.d("Tag", "" + ((LinearLayoutManager) mRecyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition());
    }

    public void setSection(NYTimesContract.Section section) {
        mSection = section;
    }

    //----------------------------------------------------------------------------------------------
    // SwipeRefreshLayout.OnRefreshListener
    //----------------------------------------------------------------------------------------------
    @Override
    public void onRefresh() {
        showRefreshProgressBar(true);
        mDataManager.refreshNews(mSection);
    }
    //----------------------------------------------------------------------------------------------
    // NewsWireListener
    //----------------------------------------------------------------------------------------------
    @Override
    public void onNewWireUpdated(NYTimesContract.Section section) {
        if (section == mSection) {
            if (mSwipeRefreshLayout.isRefreshing()) {
                showRefreshProgressBar(false);
            } else if (mIsAppending) {
                showAppendProgressBar(false);
            }

            mNews = mDataManager.getNews(mSection);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onNewsUpdateFailed(NYTimesContract.Section section, String message) {
        if (section == mSection) {
            Snackbar.make(getView(), message, Snackbar.LENGTH_SHORT).show();

            if (mSwipeRefreshLayout.isRefreshing()) {
                showRefreshProgressBar(false);
            } else if (mIsAppending) {
                showAppendProgressBar(false);
            }

            mAdapter.notifyDataSetChanged();
        }
    }
    //----------------------------------------------------------------------------------------------

    private void showRefreshProgressBar(boolean isRefreshing) {
        mSwipeRefreshLayout.setRefreshing(isRefreshing);
        mSwipeRefreshLayout.setEnabled(!isRefreshing);
    }

    private void showAppendProgressBar(boolean appending) {
        mIsAppending = appending;

        if (mIsAppending) {
            mNews.add(null);
            mProgressViewIndex = mNews.size() - 1;
            mAdapter.notifyItemChanged(mProgressViewIndex);
        } else {
            mNews.remove(mProgressViewIndex);
        }

    }
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
                ProgressViewHolder progressViewHolder = (ProgressViewHolder) viewHolder;
                progressViewHolder.progressBar.getIndeterminateDrawable()
                        .setColorFilter(0xF000000, android.graphics.PorterDuff.Mode.MULTIPLY);
                progressViewHolder.progressBar.setIndeterminate(true);
            } else {
                NewsItemViewHolder newsItemViewHolder = (NewsItemViewHolder) viewHolder;
                newsItemViewHolder.titleView.setText(mNews.get(index).getTitle());
                newsItemViewHolder.bylineView.setText(mNews.get(index).getByline());
                newsItemViewHolder.abstractView.setText(mNews.get(index).getAbstract());

                if (mNews.get(index).getPhotos() != null && mNews.get(index).getPhotos().size() != 0) {
                    Picasso.with(getActivity())
                            .load(mNews.get(index).getPhotos().get(0).getImageUrl())
                            .resize(mThumbnailSize, mThumbnailSize)
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

                titleView.setTypeface(mTitleFont);
                bylineView.setTypeface(mContentFont);
                abstractView.setTypeface(mContentFont);
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
