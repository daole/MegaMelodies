package com.dreamdigitizers.megamelodies.views.classes.fragments.screens;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dreamdigitizers.androidbaselibrary.views.classes.fragments.screens.ScreenBase;
import com.dreamdigitizers.megamelodies.R;
import com.dreamdigitizers.megamelodies.presenters.classes.PresenterFactory;
import com.dreamdigitizers.megamelodies.presenters.interfaces.IPresenterPlaylists;
import com.dreamdigitizers.megamelodies.views.interfaces.IViewPlaylists;

public class ScreenPlaylists extends ScreenBase<IPresenterPlaylists> implements IViewPlaylists {
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
    protected void retrieveScreenItems(View pView) {

    }

    @Override
    protected void mapInformationToScreenItems(View pView) {

    }

    @Override
    protected int getTitle() {
        return R.string.title__playlists;
    }
}
