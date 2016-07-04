package com.dreamdigitizers.megamelodies.views.classes.fragments.screens;

import android.support.v4.media.MediaBrowserCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.dreamdigitizers.androidbaselibrary.views.classes.activities.ActivityBase;
import com.dreamdigitizers.megamelodies.R;
import com.dreamdigitizers.megamelodies.models.Playlist;
import com.dreamdigitizers.megamelodies.presenters.classes.PresenterFactory;
import com.dreamdigitizers.megamelodies.presenters.interfaces.IPresenterPlaylists;
import com.dreamdigitizers.megamelodies.views.classes.fragments.FragmentMediaItems;
import com.dreamdigitizers.megamelodies.views.classes.fragments.FragmentPlaylists;
import com.dreamdigitizers.megamelodies.views.classes.services.support.MediaMetadataBuilder;
import com.dreamdigitizers.megamelodies.views.classes.support.AdapterPlaylist;
import com.dreamdigitizers.megamelodies.views.interfaces.IViewPlaylists;

public class ScreenPlaylists extends ScreenMediaItems<IPresenterPlaylists> implements IViewPlaylists, AdapterPlaylist.IOnItemClickListener {
    private FrameLayout mPlaceHolderPlaylistScreen;
    private ScreenPlaylist mScreenPlaylist;

    @Override
    public boolean onBackPressed() {
        if (!this.mScreenPlaylist.isHidden()) {
            this.hideScreenPlaylist();
            return true;
        }
        return false;
    }

    @Override
    protected void retrieveScreenItems(View pView) {
        super.retrieveScreenItems(pView);
        this.mPlaceHolderPlaylistScreen = (FrameLayout) pView.findViewById(R.id.placeHolderPlaylistScreen);
    }

    @Override
    protected void mapInformationToScreenItems(View pView) {
        super.mapInformationToScreenItems(pView);
        this.mScreenPlaylist = new ScreenPlaylist();
        this.getChildFragmentManager()
                .beginTransaction()
                .add(R.id.placeHolderPlaylistScreen, this.mScreenPlaylist)
                .commit();
        this.hideScreenPlaylist();
    }

    @Override
    public int getScreenId() {
        return 0;
    }

    @Override
    protected IPresenterPlaylists createPresenter() {
        return (IPresenterPlaylists) PresenterFactory.createPresenter(IPresenterPlaylists.class, this);
    }

    @Override
    protected View loadView(LayoutInflater pInflater, ViewGroup pContainer) {
        View rootView = pInflater.inflate(R.layout.screen__playlists, pContainer, false);
        return rootView;
    }

    @Override
    protected int getTitle() {
        if (this.mScreenPlaylist.isHidden()) {
            return R.string.title__playlists;
        } else {
            return 0;
        }
    }

    @Override
    protected FragmentMediaItems createFragmentMediaItems() {
        return new FragmentPlaylists();
    }

    @Override
    public void onItemClicked(MediaBrowserCompat.MediaItem pMediaItem) {
        Playlist playlist = (Playlist) pMediaItem.getDescription().getExtras().getSerializable(MediaMetadataBuilder.BUNDLE_KEY__PLAYLIST);
        this.mScreenPlaylist.setPlaylist(playlist);
        this.showScreenPlaylist();
        ((ActivityBase) this.getActivity()).getSupportActionBar().setTitle(this.mScreenPlaylist.getTitleString());
    }

    @Override
    public void onDeleteContextMenuItemClicked(MediaBrowserCompat.MediaItem pMediaItem) {
        this.mPresenter.deletePlaylist(pMediaItem);
    }

    private void showScreenPlaylist() {
        if (this.mScreenPlaylist.isHidden()) {
            this.getChildFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(com.dreamdigitizers.androidbaselibrary.R.anim.slide_in_from_bottom, 0)
                    .show(this.mScreenPlaylist)
                    .commit();
            this.mScreenPlaylist.onShow();
        }
    }

    private void hideScreenPlaylist() {
        if (!this.mScreenPlaylist.isHidden()) {
            this.getChildFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(0, com.dreamdigitizers.androidbaselibrary.R.anim.slide_out_to_bottom)
                    .hide(this.mScreenPlaylist)
                    .commit();
            this.mScreenPlaylist.onHide();
        }
    }
}
