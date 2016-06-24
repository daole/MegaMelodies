package com.dreamdigitizers.megamelodies.views.classes.fragments.screens;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dreamdigitizers.megamelodies.R;
import com.dreamdigitizers.megamelodies.models.Playlist;
import com.dreamdigitizers.megamelodies.presenters.classes.PresenterFactory;
import com.dreamdigitizers.megamelodies.presenters.interfaces.IPresenterPlaylist;
import com.dreamdigitizers.megamelodies.views.interfaces.IViewPlaylist;

public class ScreenPlaylist extends ScreenTracks<IPresenterPlaylist> implements IViewPlaylist {
    private Playlist mPlaylist;

    @Override
    public String getTitleString() {
        if (this.mPlaylist == null) {
            return null;
        }
        return String.format(this.getString(R.string.title__playlist), this.mPlaylist.getName());
    }

    @Override
    public void showNetworkProgress() {
    }

    @Override
    public void hideNetworkProgress() {
    }

    @Override
    public int getScreenId() {
        return 0;
    }

    @Override
    protected IPresenterPlaylist createPresenter() {
        return (IPresenterPlaylist) PresenterFactory.createPresenter(IPresenterPlaylist.class, this);
    }

    @Override
    protected View loadView(LayoutInflater pInflater, ViewGroup pContainer) {
        View rootView = pInflater.inflate(R.layout.screen__playlist, pContainer, false);
        return rootView;
    }

    @Override
    protected int getTitle() {
        return 0;
    }

    @Override
    public int getPlaylistId() {
        return this.mPlaylist.getId();
    }

    public void setPlaylist(Playlist pPlaylist) {
        this.mPlaylist = pPlaylist;
    }

    public void onShow() {
        this.mFragmentMediaItems.clearMediaItems();
        this.mPresenter.connect();
    }

    public void onHide() {
        if (this.mPresenter != null) {
            this.mPresenter.disconnect();
        }
    }
}
