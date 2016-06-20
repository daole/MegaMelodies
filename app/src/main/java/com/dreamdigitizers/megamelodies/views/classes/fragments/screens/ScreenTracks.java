package com.dreamdigitizers.megamelodies.views.classes.fragments.screens;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.media.MediaBrowserCompat;

import com.dreamdigitizers.megamelodies.Constants;
import com.dreamdigitizers.megamelodies.R;
import com.dreamdigitizers.megamelodies.presenters.interfaces.IPresenterTracks;
import com.dreamdigitizers.megamelodies.views.classes.dialogs.DialogPlaylist;
import com.dreamdigitizers.megamelodies.views.classes.fragments.FragmentCreateNewPlaylist;
import com.dreamdigitizers.megamelodies.views.classes.fragments.FragmentMediaItems;
import com.dreamdigitizers.megamelodies.views.classes.fragments.FragmentTracks;
import com.dreamdigitizers.megamelodies.views.classes.support.AdapterPlaylistDialog;
import com.dreamdigitizers.megamelodies.views.classes.support.AdapterTrack;
import com.dreamdigitizers.megamelodies.views.interfaces.IViewTracks;

import java.util.ArrayList;
import java.util.List;

public abstract class ScreenTracks<P extends IPresenterTracks> extends ScreenMediaItems<P>
        implements IViewTracks,
        AdapterTrack.IOnItemClickListener,
        AdapterPlaylistDialog.IOnAddRemoveButtonClickListener,
        FragmentCreateNewPlaylist.IOnOkButtonClickListener {
    private DialogPlaylist mDialogPlaylist;
    private MediaBrowserCompat.MediaItem mMediaItem;

    @Override
    protected FragmentMediaItems createFragmentMediaItems() {
        return new FragmentTracks();
    }

    @Override
    public void onAllPlaylistsLoaded(List<MediaBrowserCompat.MediaItem> pMediaItems) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        Fragment previousInstance = this.getChildFragmentManager().findFragmentByTag(DialogPlaylist.class.getName());
        if (previousInstance != null) {
            fragmentTransaction.remove(previousInstance);
        }
        fragmentTransaction.addToBackStack(null);
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.BUNDLE_KEY__TRACK, this.mMediaItem);
        bundle.putParcelableArrayList(Constants.BUNDLE_KEY__PLAYLISTS, (ArrayList) pMediaItems);
        this.mDialogPlaylist = new DialogPlaylist();
        this.mDialogPlaylist.setArguments(bundle);
        this.mDialogPlaylist.setOnAddRemoveButtonClickListener(this);
        this.mDialogPlaylist.setOnOkButtonClickListener(this);
        this.mDialogPlaylist.show(fragmentTransaction, DialogPlaylist.class.getName());
    }

    @Override
    public void onAddToRemoveFromPlaylistResult() {
        this.mDialogPlaylist.onAddToRemoveFromPlaylistResult();
    }

    @Override
    public void onPlaylistCreated() {
        this.showMessage(R.string.message__playlist_created);
    }

    @Override
    public void onItemClicked(MediaBrowserCompat.MediaItem pMediaItem) {
        this.mPresenter.playFromMediaId(pMediaItem);
    }

    @Override
    public void onFavoriteContextMenuItemClicked(MediaBrowserCompat.MediaItem pMediaItem) {
        this.mPresenter.favorite(pMediaItem);
    }

    @Override
    public void onPlaylistContextMenuItemClicked(MediaBrowserCompat.MediaItem pMediaItem) {
        this.mMediaItem = pMediaItem;
        this.mPresenter.loadAllPlaylists();
    }

    @Override
    public void onAddRemoveButtonClicked(MediaBrowserCompat.MediaItem pTrack, MediaBrowserCompat.MediaItem pPlaylist, boolean pIsAdd) {
        this.mPresenter.addToRemoveFromPlaylist(pTrack, pPlaylist, pIsAdd);
    }

    @Override
    public void onOkButtonClicked(MediaBrowserCompat.MediaItem pTrack, String pPlaylistTitle) {
        this.mPresenter.createPlaylist(pTrack, pPlaylistTitle);
    }
}
