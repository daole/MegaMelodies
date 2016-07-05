package com.dreamdigitizers.megamelodies.views.classes.fragments.screens;

import android.support.v4.media.MediaBrowserCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.dreamdigitizers.androidbaselibrary.views.classes.fragments.screens.ScreenBase;
import com.dreamdigitizers.megamelodies.R;
import com.dreamdigitizers.megamelodies.models.Playlist;
import com.dreamdigitizers.megamelodies.presenters.classes.PresenterFactory;
import com.dreamdigitizers.megamelodies.presenters.interfaces.IPresenterWrapperPlaylists;
import com.dreamdigitizers.megamelodies.views.classes.services.support.MediaMetadataBuilder;
import com.dreamdigitizers.megamelodies.views.interfaces.IViewWrapperPlaylists;

public class ScreenWrapperPlaylists extends ScreenBase<IPresenterWrapperPlaylists> implements IViewWrapperPlaylists, ScreenPlaylists.IOnPlaylistSelected {
    private FrameLayout mPlaceHolderChildScreen;
    private ScreenPlaylists mScreenPlaylists;
    private ScreenPlaylist mScreenPlaylist;
    private ScreenBase mLastShownChildScreen;

    public ScreenWrapperPlaylists() {
        this.mScreenPlaylists = new ScreenPlaylists();
        this.mScreenPlaylists.setOnPlaylistSelectedListener(this);
        this.mScreenPlaylist = new ScreenPlaylist();
        this.mLastShownChildScreen = this.mScreenPlaylists;
    }

    @Override
    public boolean onBackPressed() {
        if (!this.mScreenPlaylists.isHidden()) {
            return this.mScreenPlaylists.onBackPressed();
        } else if (!this.mScreenPlaylist.isHidden()) {
            boolean isHandled = this.mScreenPlaylist.onBackPressed();
            if (isHandled) {
                return true;
            }
            this.showScreenPlaylists();
            return true;
        }
        return false;
    }

    @Override
    public void onShow() {
        this.getChildFragmentManager()
                .beginTransaction()
                .show(this.mLastShownChildScreen)
                .commit();

        this.mLastShownChildScreen.onShow();
    }

    @Override
    public void onHide() {
        this.getChildFragmentManager()
                .beginTransaction()
                .hide(this.mLastShownChildScreen)
                .commit();

        this.mLastShownChildScreen.onHide();
    }

    @Override
    protected boolean shouldSetThisScreenAsCurrentScreen() {
        return false;
    }

    @Override
    public int getScreenId() {
        return 0;
    }

    @Override
    protected IPresenterWrapperPlaylists createPresenter() {
        return (IPresenterWrapperPlaylists) PresenterFactory.createPresenter(IPresenterWrapperPlaylists.class, this);
    }

    @Override
    protected View loadView(LayoutInflater pInflater, ViewGroup pContainer) {
        View rootView = pInflater.inflate(R.layout.screen__wrapper_playlists, pContainer, false);
        return rootView;
    }

    @Override
    protected void retrieveScreenItems(View pView) {
        this.mPlaceHolderChildScreen = (FrameLayout) pView.findViewById(R.id.placeHolderChildScreen);
    }

    @Override
    protected void mapInformationToScreenItems(View pView) {
        this.getChildFragmentManager()
                .beginTransaction()
                .add(R.id.placeHolderChildScreen, this.mScreenPlaylists)
                .add(R.id.placeHolderChildScreen, this.mScreenPlaylist)
                .hide(this.mScreenPlaylists)
                .hide(this.mScreenPlaylist)
                .commit();
    }

    @Override
    protected int getTitle() {
        return 0;
    }

    @Override
    public void onPlaylistSelected(MediaBrowserCompat.MediaItem pMediaItem) {
        Playlist playlist = (Playlist) pMediaItem.getDescription().getExtras().getSerializable(MediaMetadataBuilder.BUNDLE_KEY__PLAYLIST);
        this.mScreenPlaylist.setPlaylist(playlist);
        this.showScreenPlaylist();
    }

    private void showScreenPlaylists() {
        if (!this.mScreenPlaylist.isHidden()) {
            this.mScreenPlaylist.onHide();
            this.mScreenPlaylists.onShow();
            this.getChildFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(
                            com.dreamdigitizers.androidbaselibrary.R.anim.slide_in_from_left,
                            com.dreamdigitizers.androidbaselibrary.R.anim.slide_out_to_right)
                    .hide(this.mScreenPlaylist)
                    .show(this.mScreenPlaylists)
                    .commit();
            this.mLastShownChildScreen = this.mScreenPlaylists;
        }
    }

    private void showScreenPlaylist() {
        if (this.mScreenPlaylist.isHidden()) {
            this.mScreenPlaylists.onHide();
            this.mScreenPlaylist.onShow();
            this.getChildFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(
                            com.dreamdigitizers.androidbaselibrary.R.anim.slide_in_from_right,
                            com.dreamdigitizers.androidbaselibrary.R.anim.slide_out_to_left)
                    .hide(this.mScreenPlaylists)
                    .show(this.mScreenPlaylist)
                    .commit();
            this.mLastShownChildScreen = this.mScreenPlaylist;
        }
    }
}
