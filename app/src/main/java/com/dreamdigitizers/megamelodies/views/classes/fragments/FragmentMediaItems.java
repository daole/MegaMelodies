package com.dreamdigitizers.megamelodies.views.classes.fragments;

import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.dreamdigitizers.androidbaselibrary.views.classes.fragments.FragmentBase;
import com.dreamdigitizers.megamelodies.R;
import com.dreamdigitizers.megamelodies.views.classes.support.AdapterMediaItem;

import java.util.List;

public abstract class FragmentMediaItems extends FragmentBase {
    protected static final int VISIBLE_THRESHOLD = 0;

    protected RecyclerView mLstMediaItems;
    protected ProgressBar mPgbLoading;
    protected FrameLayout mPlaceHolderPlaybackControls;
    protected FragmentPlaybackControls mFragmentPlaybackControls;

    protected LinearLayoutManager mLinearLayoutManager;
    protected AdapterMediaItem mMediaItemAdapter;

    protected IOnScrollEndListener mListener;

    protected boolean mIsLoading;
    protected int mPreviousTotalItemCount;

    public FragmentMediaItems() {
        this.mIsLoading = true;
    }

    @Override
    protected View loadView(LayoutInflater pInflater, ViewGroup pContainer) {
        View rootView = pInflater.inflate(R.layout.fragment__media_items, pContainer, false);
        return rootView;
    }

    @Override
    protected void retrieveScreenItems(View pView) {
        this.mLstMediaItems = (RecyclerView) pView.findViewById(R.id.lstMediaItems);
        this.mPgbLoading = (ProgressBar) pView.findViewById(R.id.pgbLoading);
        this.mPlaceHolderPlaybackControls = (FrameLayout) pView.findViewById(R.id.placeHolderPlaybackControls);
    }

    @Override
    protected void mapInformationToScreenItems(View pView) {
        this.mLinearLayoutManager = new LinearLayoutManager(this.getContext());
        this.mMediaItemAdapter = this.createAdapter();
        this.mLstMediaItems.setLayoutManager(this.mLinearLayoutManager);
        this.mLstMediaItems.setAdapter(this.mMediaItemAdapter);
        this.mLstMediaItems.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView pRecyclerView, int pDx, int pDy) {
                FragmentMediaItems.this.onTrackListScrolled();
            }
        });

        this.mFragmentPlaybackControls = new FragmentPlaybackControls();
        this.getChildFragmentManager()
                .beginTransaction()
                .add(R.id.placeHolderPlaybackControls, this.mFragmentPlaybackControls)
                .commit();
        this.hidePlaybackControls();
    }

    @Override
    protected int getTitle() {
        return 0;
    }

    public void setOnScrollEndListener(IOnScrollEndListener pListener) {
        this.mListener = pListener;
    }

    public void setPlaybackControlListener(FragmentPlaybackControls.IPlaybackControlListener pListener) {
        this.mFragmentPlaybackControls.setPlaybackControlListener(pListener);
    }

    public void setOnItemClickListener(AdapterMediaItem.IOnItemClickListener pListener) {
        this.mMediaItemAdapter.setOnItemClickListener(pListener);
    }

    public void clearMediaItems() {
        this.mPreviousTotalItemCount = 0;
        this.mMediaItemAdapter.clearMediaItems();
    }

    public void addMediaItem(MediaBrowserCompat.MediaItem pMediaItem, boolean pIsAddToTop) {
        this.mMediaItemAdapter.addMediaItem(pMediaItem, pIsAddToTop);
    }

    public void addMediaItems(List<MediaBrowserCompat.MediaItem> pMediaItems, boolean pIsAddToTop) {
        this.mMediaItemAdapter.addMediaItems(pMediaItems, pIsAddToTop);
    }

    public void showLoadMoreProgress() {
        this.mPgbLoading.setVisibility(View.VISIBLE);
    }

    public void hideLoadMoreProgress() {
        this.mPgbLoading.setVisibility(View.GONE);
    }

    public void onPlaybackStateChanged(PlaybackStateCompat pPlaybackState) {
        this.mFragmentPlaybackControls.onPlaybackStateChanged(pPlaybackState);
        if (isShowPlaybackControls(pPlaybackState)) {
            this.showPlaybackControls();
        } else {
            this.hidePlaybackControls();
        }
    }

    public void onMetadataChanged(MediaMetadataCompat pMediaMetadata) {
        this.mFragmentPlaybackControls.onMetadataChanged(pMediaMetadata);
    }

    public void updateState() {
        this.mMediaItemAdapter.notifyDataSetChanged();
    }

    public void removeMediaItem(MediaBrowserCompat.MediaItem pMediaItem) {
        this.mMediaItemAdapter.removeMediaItem(pMediaItem);
    }

    protected boolean isShowPlaybackControls(PlaybackStateCompat pPlaybackState) {
        switch (pPlaybackState.getState()) {
            case PlaybackStateCompat.STATE_NONE:
            case PlaybackStateCompat.STATE_STOPPED:
            case PlaybackStateCompat.STATE_ERROR:
                return false;
            default:
                return true;
        }
    }

    protected void showPlaybackControls() {
        if (this.mFragmentPlaybackControls.isHidden()) {
            this.getChildFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(com.dreamdigitizers.androidbaselibrary.R.anim.slide_in_from_bottom, 0)
                    .show(this.mFragmentPlaybackControls)
                    .commit();
        }
    }

    protected void hidePlaybackControls() {
        if (!this.mFragmentPlaybackControls.isHidden()) {
            this.getChildFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(0, com.dreamdigitizers.androidbaselibrary.R.anim.slide_out_to_bottom)
                    .hide(this.mFragmentPlaybackControls)
                    .commit();
        }
    }

    protected void onTrackListScrolled() {
        int visibleItemCount = this.mLinearLayoutManager.getChildCount();
        int firstVisibleItemPosition = this.mLinearLayoutManager.findFirstVisibleItemPosition();
        int totalItemCount = this.mLinearLayoutManager.getItemCount();

        if (this.mIsLoading) {
            if (totalItemCount > this.mPreviousTotalItemCount) {
                this.mIsLoading = false;
                this.mPreviousTotalItemCount = totalItemCount;
            }
        }

        if (!this.mIsLoading && totalItemCount - visibleItemCount <= firstVisibleItemPosition + FragmentMediaItems.VISIBLE_THRESHOLD) {
            this.mIsLoading = true;
            if (this.mListener != null) {
                this.mListener.onScrollEnd();
            }
        }
    }

    protected abstract AdapterMediaItem createAdapter();

    public interface IOnScrollEndListener {
        void onScrollEnd();
    }
}
