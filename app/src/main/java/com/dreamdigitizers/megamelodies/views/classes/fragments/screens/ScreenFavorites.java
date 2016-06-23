package com.dreamdigitizers.megamelodies.views.classes.fragments.screens;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dreamdigitizers.megamelodies.R;
import com.dreamdigitizers.megamelodies.presenters.classes.PresenterFactory;
import com.dreamdigitizers.megamelodies.presenters.interfaces.IPresenterFavorites;
import com.dreamdigitizers.megamelodies.views.interfaces.IViewFavorites;

public class ScreenFavorites extends ScreenTracks<IPresenterFavorites> implements IViewFavorites {
    @Override
    public int getScreenId() {
        return 0;
    }

    @Override
    protected IPresenterFavorites createPresenter() {
        return (IPresenterFavorites) PresenterFactory.createPresenter(IPresenterFavorites.class, this);
    }

    @Override
    protected View loadView(LayoutInflater pInflater, ViewGroup pContainer) {
        View rootView = pInflater.inflate(R.layout.screen__favorites, pContainer, false);
        return rootView;
    }

    @Override
    protected int getTitle() {
        return R.string.title__favorites;
    }
}
