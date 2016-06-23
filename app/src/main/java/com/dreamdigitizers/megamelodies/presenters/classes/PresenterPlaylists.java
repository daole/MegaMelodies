package com.dreamdigitizers.megamelodies.presenters.classes;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.media.MediaBrowserCompat;

import com.dreamdigitizers.androidbaselibrary.utilities.UtilsDialog;
import com.dreamdigitizers.megamelodies.Constants;
import com.dreamdigitizers.megamelodies.R;
import com.dreamdigitizers.megamelodies.models.Playlist;
import com.dreamdigitizers.megamelodies.presenters.interfaces.IPresenterPlaylists;
import com.dreamdigitizers.megamelodies.views.classes.services.ServicePlayback;
import com.dreamdigitizers.megamelodies.views.classes.services.support.MediaMetadataBuilder;
import com.dreamdigitizers.megamelodies.views.interfaces.IViewPlaylists;

import java.util.HashMap;

class PresenterPlaylists extends PresenterMediaItems<IViewPlaylists> implements IPresenterPlaylists {
    public PresenterPlaylists(IViewPlaylists pView) {
        super(pView);
        this.mTransactionActions.put(ServicePlayback.CUSTOM_ACTION__DELETE_PLAYLIST, new HashMap<String, Object>());
    }

    @Override
    protected void onSessionEvent(String pEvent, Bundle pExtras) {
        Uri uri = Uri.parse(pEvent);
        String action = uri.getQueryParameter("action");
        switch (action) {
            case ServicePlayback.CUSTOM_ACTION__DELETE_PLAYLIST:
                this.handleDeletePlaylistEvent(uri);
                break;
            default:
                break;
        }
    }

    @Override
    protected String getMediaId() {
        return this.buildMediaId();
    }

    @Override
    public void deletePlaylist(final MediaBrowserCompat.MediaItem pMediaItem) {
        if (this.mTransportControls != null) {
            IViewPlaylists view = this.getView();
            if (view != null) {
                view.showConfirmation(R.string.confirmation__delete_playlist, new UtilsDialog.IOnDialogButtonClickListener() {
                    @Override
                    public void onPositiveButtonClick(Activity pActivity, String pTitle, String pMessage, boolean pIsTwoButtons, String pPositiveButtonText, String pNegativeButtonText) {
                        Bundle bundle =  pMediaItem.getDescription().getExtras();
                        Playlist playlist = (Playlist) bundle.getSerializable(MediaMetadataBuilder.BUNDLE_KEY__PLAYLIST);
                        PresenterPlaylists.this.mTransactionActions.get(ServicePlayback.CUSTOM_ACTION__DELETE_PLAYLIST).put(Integer.toString(playlist.getId()), pMediaItem);
                        PresenterPlaylists.this.mTransportControls.sendCustomAction(ServicePlayback.CUSTOM_ACTION__DELETE_PLAYLIST, bundle);
                    }

                    @Override
                    public void onNegativeButtonClick(Activity pActivity, String pTitle, String pMessage, boolean pIsTwoButtons, String pPositiveButtonText, String pNegativeButtonText) {
                    }
                });
            }
        }
    }

    private void handleDeletePlaylistEvent(Uri pUri) {
        IViewPlaylists view = this.getView();
        if (view != null) {
            String id = pUri.getQueryParameter("playlistId");
            HashMap transactions = this.mTransactionActions.get(ServicePlayback.CUSTOM_ACTION__DELETE_PLAYLIST);
            MediaBrowserCompat.MediaItem mediaItem = (MediaBrowserCompat.MediaItem) transactions.get(id);
            transactions.remove(id);
            view.removeMediaItem(mediaItem);
        }
    }

    private String buildMediaId() {
        return String.format(ServicePlayback.MEDIA_ID__PLAYLISTS, this.mOffset, Constants.PAGE_SIZE);
    }
}
