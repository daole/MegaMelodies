package com.dreamdigitizers.megamelodies.views.classes.fragments.screens;

import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.View;
import android.widget.FrameLayout;

import com.dreamdigitizers.androidbaselibrary.views.classes.fragments.screens.ScreenBase;
import com.dreamdigitizers.megamelodies.R;
import com.dreamdigitizers.megamelodies.presenters.interfaces.IPresenterMediaItems;
import com.dreamdigitizers.megamelodies.views.classes.fragments.FragmentMediaItems;
import com.dreamdigitizers.megamelodies.views.classes.fragments.FragmentPlaybackControls;
import com.dreamdigitizers.megamelodies.views.classes.support.AdapterMediaItem;
import com.dreamdigitizers.megamelodies.views.interfaces.IViewMediaItems;

import java.util.List;

public abstract class ScreenMediaItems<P extends IPresenterMediaItems> extends ScreenBase<P>
        implements IViewMediaItems,
        FragmentMediaItems.IOnScrollEndListener,
        AdapterMediaItem.IOnItemClickListener,
        FragmentPlaybackControls.IPlaybackControlListener {
    private static final String ERROR_MESSAGE__MISSING_MEDIA_ITEMS_PLACE_HOLDER = "Missing FrameLayout with id \"placeHolderTracks\" in layout.";

    protected FrameLayout mPlaceHolderTracks;
    protected FragmentMediaItems mFragmentMediaItems;

    @Override
    public void onStart() {
        super.onStart();
        this.mFragmentMediaItems.setOnItemClickListener(this);
        this.mFragmentMediaItems.setPlaybackControlListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mFragmentMediaItems.setOnItemClickListener(null);
        this.mFragmentMediaItems.setPlaybackControlListener(null);
    }

    @Override
    protected boolean shouldSetThisScreenAsCurrentScreen() {
        return false;
    }

    @Override
    protected void retrieveScreenItems(View pView) {
        this.mPlaceHolderTracks = (FrameLayout) pView.findViewById(R.id.placeHolderTracks);
        if (this.mPlaceHolderTracks == null) {
            throw new NullPointerException(ScreenMediaItems.ERROR_MESSAGE__MISSING_MEDIA_ITEMS_PLACE_HOLDER);
        }
    }

    @Override
    protected void mapInformationToScreenItems(View pView) {
        this.mFragmentMediaItems = this.createFragmentMediaItems();
        this.mFragmentMediaItems.setOnScrollEndListener(this);
        this.getChildFragmentManager()
                .beginTransaction()
                .add(R.id.placeHolderTracks, this.mFragmentMediaItems)
                .commit();
    }

    @Override
    public void showLoadMoreProgress() {
        this.mFragmentMediaItems.showLoadMoreProgress();
    }

    @Override
    public void hideLoadMoreProgress() {
        this.mFragmentMediaItems.hideLoadMoreProgress();
    }

    @Override
    public void onPlaybackStateChanged(PlaybackStateCompat pPlaybackState) {
        this.mFragmentMediaItems.onPlaybackStateChanged(pPlaybackState);
    }

    @Override
    public void onMetadataChanged(MediaMetadataCompat pMediaMetadata) {
        this.mFragmentMediaItems.onMetadataChanged(pMediaMetadata);
    }

    @Override
    public void updateState() {
        this.mFragmentMediaItems.updateState();
    }

    @Override
    public void addMediaItems(List<MediaBrowserCompat.MediaItem> pMediaItems, boolean pIsAddToTop) {
        this.mFragmentMediaItems.addMediaItems(pMediaItems, pIsAddToTop);
    }

    @Override
    public void removeMediaItem(MediaBrowserCompat.MediaItem pMediaItem) {
        this.mFragmentMediaItems.removeMediaItem(pMediaItem);
    }

    @Override
    public void onScrollEnd() {
        this.mPresenter.loadMore();
    }

    @Override
    public void skipToPrevious() {
        this.mPresenter.skipToPrevious();
    }

    @Override
    public void play() {
        this.mPresenter.play();
    }

    @Override
    public void pause() {
        this.mPresenter.pause();
    }

    @Override
    public void skipToNext() {
        this.mPresenter.skipToNext();
    }

    @Override
    public void seekTo(int pPosition) {
        this.mPresenter.seekTo(pPosition);
    }

    public void onShow() {
        if (this.mFragmentMediaItems != null) {
            this.mFragmentMediaItems.clearMediaItems();
        }
        if (this.mPresenter != null) {
            this.mPresenter.connect();
        }
    }

    public void onHide() {
        if (this.mPresenter != null) {
            this.mPresenter.disconnect();
        }
    }

    protected abstract FragmentMediaItems createFragmentMediaItems();
}
