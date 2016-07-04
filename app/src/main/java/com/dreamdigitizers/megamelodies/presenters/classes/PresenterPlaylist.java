package com.dreamdigitizers.megamelodies.presenters.classes;

import android.net.Uri;
import android.support.v4.media.MediaBrowserCompat;

import com.dreamdigitizers.megamelodies.Constants;
import com.dreamdigitizers.megamelodies.models.Track;
import com.dreamdigitizers.megamelodies.presenters.interfaces.IPresenterPlaylist;
import com.dreamdigitizers.megamelodies.views.classes.services.ServicePlayback;
import com.dreamdigitizers.megamelodies.views.classes.services.support.MediaMetadataBuilder;
import com.dreamdigitizers.megamelodies.views.interfaces.IViewPlaylist;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

class PresenterPlaylist extends PresenterTracks<IViewPlaylist> implements IPresenterPlaylist {
    public PresenterPlaylist(IViewPlaylist pView) {
        super(pView);
    }

    @Override
    protected void handleAddToPlaylistEvent(Uri pUri) {
        String id = pUri.getQueryParameter("playlistId");
        HashMap transactions = this.mTransactionActions.get(ServicePlayback.CUSTOM_ACTION__ADD_TO_PLAYLIST);
        MediaItemPlaylistPair mediaItemPlaylistPair = (MediaItemPlaylistPair) transactions.get(id);
        Track track = (Track) mediaItemPlaylistPair.mMediaItem.getDescription().getExtras().getSerializable(MediaMetadataBuilder.BUNDLE_KEY__TRACK);
        mediaItemPlaylistPair.mPlaylist.getTracks().add(track);
        transactions.remove(id);
        IViewPlaylist view = this.getView();
        if (view != null) {
            if (Integer.parseInt(id) == view.getPlaylistId()) {
                List<MediaBrowserCompat.MediaItem> mediaItems = new ArrayList<>();
                mediaItems.add(mediaItemPlaylistPair.mMediaItem);
                view.addMediaItems(mediaItems, false);
            }
            view.hideNetworkProgress();
            view.onAddToRemoveFromPlaylistResult();
        }
    }

    @Override
    protected void handleRemoveFromPlaylistEvent(Uri pUri) {
        String id = pUri.getQueryParameter("playlistId");
        HashMap transactions = this.mTransactionActions.get(ServicePlayback.CUSTOM_ACTION__REMOVE_FROM_PLAYLIST);
        MediaItemPlaylistPair mediaItemPlaylistPair = (MediaItemPlaylistPair) transactions.get(id);
        Track track = (Track) mediaItemPlaylistPair.mMediaItem.getDescription().getExtras().getSerializable(MediaMetadataBuilder.BUNDLE_KEY__TRACK);
        mediaItemPlaylistPair.mPlaylist.getTracks().remove(track);
        transactions.remove(id);
        IViewPlaylist view = this.getView();
        if (view != null) {
            if (Integer.parseInt(id) == view.getPlaylistId()) {
                view.removeMediaItem(mediaItemPlaylistPair.mMediaItem);
            }
            view.hideNetworkProgress();
            view.onAddToRemoveFromPlaylistResult();
        }
    }

    @Override
    protected String getMediaId() {
        return this.buildMediaId();
    }

    private String buildMediaId() {
        String mediaId = null;
        IViewPlaylist view = this.getView();
        if (view != null) {
            int playlistId = view.getPlaylistId();
            mediaId = String.format(ServicePlayback.MEDIA_ID__PLAYLIST, playlistId, this.mOffset, Constants.PAGE_SIZE);
        }
        return mediaId;
    }
}
