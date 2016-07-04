package com.dreamdigitizers.megamelodies.presenters.classes;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.media.MediaBrowserCompat;

import com.dreamdigitizers.androidbaselibrary.utilities.UtilsString;
import com.dreamdigitizers.megamelodies.Constants;
import com.dreamdigitizers.megamelodies.R;
import com.dreamdigitizers.megamelodies.models.Playlist;
import com.dreamdigitizers.megamelodies.models.Track;
import com.dreamdigitizers.megamelodies.presenters.interfaces.IPresenterTracks;
import com.dreamdigitizers.megamelodies.views.classes.services.ServicePlayback;
import com.dreamdigitizers.megamelodies.views.classes.services.support.MediaMetadataBuilder;
import com.dreamdigitizers.megamelodies.views.interfaces.IViewTracks;

import java.util.HashMap;
import java.util.List;

abstract class PresenterTracks<V extends IViewTracks> extends PresenterMediaItems<V> implements IPresenterTracks {
    public PresenterTracks(V pView) {
        super(pView);
        this.mTransactionActions.put(ServicePlayback.CUSTOM_ACTION__FAVORITE, new HashMap<String, Object>());
        this.mTransactionActions.put(ServicePlayback.CUSTOM_ACTION__CREATE_PLAYLIST, new HashMap<String, Object>());
        this.mTransactionActions.put(ServicePlayback.CUSTOM_ACTION__ADD_TO_PLAYLIST, new HashMap<String, Object>());
        this.mTransactionActions.put(ServicePlayback.CUSTOM_ACTION__REMOVE_FROM_PLAYLIST, new HashMap<String, Object>());
    }

    @Override
    protected void onSessionEvent(String pEvent, Bundle pExtras) {
        Uri uri = Uri.parse(pEvent);
        String action = uri.getQueryParameter("action");
        switch (action) {
            case ServicePlayback.CUSTOM_ACTION__FAVORITE:
                this.handleFavoriteEvent(uri);
                break;
            case ServicePlayback.CUSTOM_ACTION__CREATE_PLAYLIST:
                this.handleCreatePlaylistEvent(uri);
                break;
            case ServicePlayback.CUSTOM_ACTION__ADD_TO_PLAYLIST:
                this.handleAddToPlaylistEvent(uri);
                break;
            case ServicePlayback.CUSTOM_ACTION__REMOVE_FROM_PLAYLIST:
                this.handleRemoveFromPlaylistEvent(uri);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onChildrenLoaded(String pParentId, List<MediaBrowserCompat.MediaItem> pChildren) {
        if (UtilsString.equals(pParentId, ServicePlayback.MEDIA_ID__PLAYLISTS_ALL)) {
            V view = this.getView();
            if (view != null) {
                view.hideNetworkProgress();
                view.onAllPlaylistsLoaded(pChildren);
            }
        } else {
            super.onChildrenLoaded(pParentId, pChildren);
        }
    }

    @Override
    public void loadAllPlaylists() {
        V view = this.getView();
        if (view != null) {
            view.showNetworkProgress();
            this.load(ServicePlayback.MEDIA_ID__PLAYLISTS_ALL);
        }
    }

    @Override
    public void playFromMediaId(MediaBrowserCompat.MediaItem pMediaItem) {
        if (this.mTransportControls != null) {
            this.mTransportControls.playFromMediaId(pMediaItem.getMediaId(), null);
        }
    }

    @Override
    public void favorite(MediaBrowserCompat.MediaItem pMediaItem) {
        if (this.mTransportControls != null) {
            Bundle bundle =  pMediaItem.getDescription().getExtras();
            this.mTransactionActions.get(ServicePlayback.CUSTOM_ACTION__FAVORITE).put(pMediaItem.getMediaId(), pMediaItem);
            this.mTransportControls.sendCustomAction(ServicePlayback.CUSTOM_ACTION__FAVORITE, bundle);
        }
    }

    @Override
    public void createPlaylist(MediaBrowserCompat.MediaItem pTrack, String pPlaylistName) {
        int errorResourceId = this.checkPlaylistInputData(pPlaylistName);
        if (errorResourceId > 0) {
            V view = this.getView();
            if (view != null) {
                view.showError(errorResourceId);
            }
        } else {
            if (this.mTransportControls != null) {
                V view = this.getView();
                if (view != null) {
                    view.showNetworkProgress();
                }
                Track track = (Track) pTrack.getDescription().getExtras().getSerializable(MediaMetadataBuilder.BUNDLE_KEY__TRACK);
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constants.BUNDLE_KEY__TRACK, track);
                bundle.putString(Constants.BUNDLE_KEY__PLAYLIST_NAME, pPlaylistName);
                this.mTransactionActions.get(ServicePlayback.CUSTOM_ACTION__CREATE_PLAYLIST).put(track.getId(), track);
                this.mTransportControls.sendCustomAction(ServicePlayback.CUSTOM_ACTION__CREATE_PLAYLIST, bundle);
            }
        }
    }

    @Override
    public void addToRemoveFromPlaylist(MediaBrowserCompat.MediaItem pTrack, MediaBrowserCompat.MediaItem pPlaylist, boolean pIsAdd) {
        if (this.mTransportControls != null) {
            V view = this.getView();
            if (view != null) {
                view.showNetworkProgress();
            }

            Track track = (Track) pTrack.getDescription().getExtras().getSerializable(MediaMetadataBuilder.BUNDLE_KEY__TRACK);
            Playlist playlist = (Playlist) pPlaylist.getDescription().getExtras().getSerializable(MediaMetadataBuilder.BUNDLE_KEY__PLAYLIST);
            Bundle bundle = new Bundle();
            bundle.putSerializable(Constants.BUNDLE_KEY__TRACK, track);
            bundle.putSerializable(Constants.BUNDLE_KEY__PLAYLIST, playlist);
            MediaItemPlaylistPair mediaItemPlaylistPair = new MediaItemPlaylistPair(pTrack, playlist);
            String customAction;
            if (pIsAdd) {
                customAction = ServicePlayback.CUSTOM_ACTION__ADD_TO_PLAYLIST;
            } else {
                customAction = ServicePlayback.CUSTOM_ACTION__REMOVE_FROM_PLAYLIST;
            }
            this.mTransactionActions.get(customAction).put(Integer.toString(playlist.getId()), mediaItemPlaylistPair);
            this.mTransportControls.sendCustomAction(customAction, bundle);
        }
    }

    protected int checkPlaylistInputData(String pPlaylistName) {
        if (UtilsString.isEmpty(pPlaylistName)) {
            return R.string.error__blank_playlist_title;
        }
        return 0;
    }

    protected void handleFavoriteEvent(Uri pUri) {
        String id = pUri.getQueryParameter("trackId");
        boolean userFavorite = Boolean.parseBoolean(pUri.getQueryParameter("userFavorite"));
        HashMap transactions = this.mTransactionActions.get(ServicePlayback.CUSTOM_ACTION__FAVORITE);
        MediaBrowserCompat.MediaItem mediaItem = (MediaBrowserCompat.MediaItem) transactions.get(id);
        Track track = (Track) mediaItem.getDescription().getExtras().getSerializable(MediaMetadataBuilder.BUNDLE_KEY__TRACK);
        track.setFavorite(userFavorite);
        transactions.remove(id);
        V view = this.getView();
        if (view != null) {
            view.updateState();
        }
    }

    protected void handleCreatePlaylistEvent(Uri pUri) {
        String id = pUri.getQueryParameter("trackId");
        HashMap transactions = this.mTransactionActions.get(ServicePlayback.CUSTOM_ACTION__CREATE_PLAYLIST);
        transactions.remove(id);
        V view = this.getView();
        if (view != null) {
            view.hideNetworkProgress();
            view.onPlaylistCreated();
        }
    }

    protected void handleAddToPlaylistEvent(Uri pUri) {
        String id = pUri.getQueryParameter("playlistId");
        HashMap transactions = this.mTransactionActions.get(ServicePlayback.CUSTOM_ACTION__ADD_TO_PLAYLIST);
        MediaItemPlaylistPair mediaItemPlaylistPair = (MediaItemPlaylistPair) transactions.get(id);
        Track track = (Track) mediaItemPlaylistPair.mMediaItem.getDescription().getExtras().getSerializable(MediaMetadataBuilder.BUNDLE_KEY__TRACK);
        mediaItemPlaylistPair.mPlaylist.getTracks().add(track);
        transactions.remove(id);
        V view = this.getView();
        if (view != null) {
            view.hideNetworkProgress();
            view.onAddToRemoveFromPlaylistResult();
        }
    }

    protected void handleRemoveFromPlaylistEvent(Uri pUri) {
        String id = pUri.getQueryParameter("playlistId");
        HashMap transactions = this.mTransactionActions.get(ServicePlayback.CUSTOM_ACTION__REMOVE_FROM_PLAYLIST);
        MediaItemPlaylistPair mediaItemPlaylistPair = (MediaItemPlaylistPair) transactions.get(id);
        Track track = (Track) mediaItemPlaylistPair.mMediaItem.getDescription().getExtras().getSerializable(MediaMetadataBuilder.BUNDLE_KEY__TRACK);
        mediaItemPlaylistPair.mPlaylist.getTracks().remove(track);
        transactions.remove(id);
        V view = this.getView();
        if (view != null) {
            view.hideNetworkProgress();
            view.onAddToRemoveFromPlaylistResult();
        }
    }

    protected class MediaItemPlaylistPair {
        protected MediaBrowserCompat.MediaItem mMediaItem;
        protected Playlist mPlaylist;

        private MediaItemPlaylistPair(MediaBrowserCompat.MediaItem pMediaItem, Playlist pPlaylist) {
            this.mMediaItem = pMediaItem;
            this.mPlaylist = pPlaylist;
        }
    }
}
