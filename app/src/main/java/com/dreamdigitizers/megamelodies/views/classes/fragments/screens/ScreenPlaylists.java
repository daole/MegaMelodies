package com.dreamdigitizers.megamelodies.views.classes.fragments.screens;

import android.support.v4.media.MediaBrowserCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dreamdigitizers.megamelodies.R;
import com.dreamdigitizers.megamelodies.presenters.classes.PresenterFactory;
import com.dreamdigitizers.megamelodies.presenters.interfaces.IPresenterPlaylists;
import com.dreamdigitizers.megamelodies.views.classes.fragments.FragmentMediaItems;
import com.dreamdigitizers.megamelodies.views.classes.fragments.FragmentPlaylists;
import com.dreamdigitizers.megamelodies.views.classes.support.AdapterPlaylist;
import com.dreamdigitizers.megamelodies.views.interfaces.IViewPlaylists;

public class ScreenPlaylists extends ScreenMediaItems<IPresenterPlaylists> implements IViewPlaylists, AdapterPlaylist.IOnItemClickListener {
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
        return R.string.title__playlists;
    }

    @Override
    protected FragmentMediaItems createFragmentMediaItems() {
        return new FragmentPlaylists();
    }

    @Override
    public void removeMediaItem(MediaBrowserCompat.MediaItem pMediaItem) {
        this.mFragmentMediaItems.removeMediaItem(pMediaItem);
    }

    @Override
    public void onItemClicked(MediaBrowserCompat.MediaItem pMediaItem) {

    }

    @Override
    public void onDeleteContextMenuItemClicked(MediaBrowserCompat.MediaItem pMediaItem) {
        this.mPresenter.deletePlaylist(pMediaItem);
    }
}
