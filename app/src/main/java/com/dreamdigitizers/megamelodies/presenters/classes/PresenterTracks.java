package com.dreamdigitizers.megamelodies.presenters.classes;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.media.MediaBrowserCompat;

import com.dreamdigitizers.androidbaselibrary.utilities.UtilsString;
import com.dreamdigitizers.androiddatafetchingapisclient.models.nct.NctSong;
import com.dreamdigitizers.androiddatafetchingapisclient.models.zing.ZingSong;
import com.dreamdigitizers.megamelodies.Constants;
import com.dreamdigitizers.megamelodies.R;
import com.dreamdigitizers.megamelodies.models.Playlist;
import com.dreamdigitizers.megamelodies.models.Track;
import com.dreamdigitizers.megamelodies.presenters.interfaces.IPresenterTracks;
import com.dreamdigitizers.megamelodies.views.classes.services.ServicePlayback;
import com.dreamdigitizers.megamelodies.views.classes.services.support.MediaMetadataBuilder;
import com.dreamdigitizers.megamelodies.views.interfaces.IViewTracks;

import java.io.Serializable;
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

            String id = null;
            Track track = (Track) bundle.getSerializable(MediaMetadataBuilder.BUNDLE_KEY__TRACK);
            Serializable originalTrack = track.getOriginalTrack();
            if (originalTrack instanceof NctSong) {
                NctSong nctSong = (NctSong) originalTrack;
                id = nctSong.getId();
            } else if (originalTrack instanceof ZingSong) {
                ZingSong zingSong = (ZingSong) originalTrack;
                id = zingSong.getId();
            }

            if (!UtilsString.isEmpty(id)) {
                this.mTransactionActions.get(ServicePlayback.CUSTOM_ACTION__FAVORITE).put(id, track);
                this.mTransportControls.sendCustomAction(ServicePlayback.CUSTOM_ACTION__FAVORITE, bundle);
            }
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
                Serializable originalTrack = track.getOriginalTrack();
                Bundle bundle = new Bundle();

                String id = null;
                if (originalTrack instanceof NctSong) {
                    NctSong nctSong = (NctSong) originalTrack;
                    id = nctSong.getId();
                } else if (originalTrack instanceof ZingSong) {
                    ZingSong zingSong = (ZingSong) originalTrack;
                    zingSong.getId();
                }

                bundle.putSerializable(Constants.BUNDLE_KEY__TRACK, track);
                bundle.putString(Constants.BUNDLE_KEY__PLAYLIST_NAME, pPlaylistName);
                this.mTransactionActions.get(ServicePlayback.CUSTOM_ACTION__CREATE_PLAYLIST).put(id, track);
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
            TrackPlaylistPair trackPlaylistPair = new TrackPlaylistPair(track, playlist);
            String customAction;
            if (pIsAdd) {
                customAction = ServicePlayback.CUSTOM_ACTION__ADD_TO_PLAYLIST;
            } else {
                customAction = ServicePlayback.CUSTOM_ACTION__REMOVE_FROM_PLAYLIST;
            }
            this.mTransactionActions.get(customAction).put(Integer.toString(playlist.getId()), trackPlaylistPair);
            this.mTransportControls.sendCustomAction(customAction, bundle);
        }
    }

    private int checkPlaylistInputData(String pPlaylistName) {
        if (UtilsString.isEmpty(pPlaylistName)) {
            return R.string.error__blank_playlist_title;
        }
        return 0;
    }

    private void handleFavoriteEvent(Uri pUri) {
        String id = pUri.getQueryParameter("trackId");
        boolean userFavorite = Boolean.parseBoolean(pUri.getQueryParameter("userFavorite"));
        HashMap transactions = this.mTransactionActions.get(ServicePlayback.CUSTOM_ACTION__FAVORITE);
        Track track = (Track) transactions.get(id);
        track.setFavorite(userFavorite);
        transactions.remove(id);
        V view = this.getView();
        if (view != null) {
            view.updateState();
        }
    }

    private void handleCreatePlaylistEvent(Uri pUri) {
        String id = pUri.getQueryParameter("trackId");
        HashMap transactions = this.mTransactionActions.get(ServicePlayback.CUSTOM_ACTION__CREATE_PLAYLIST);
        transactions.remove(id);
        V view = this.getView();
        if (view != null) {
            view.hideNetworkProgress();
            view.onPlaylistCreated();
        }
    }

    private void handleAddToPlaylistEvent(Uri pUri) {
        String id = pUri.getQueryParameter("playlistId");
        HashMap transactions = this.mTransactionActions.get(ServicePlayback.CUSTOM_ACTION__ADD_TO_PLAYLIST);
        TrackPlaylistPair trackPlaylistPair = (TrackPlaylistPair) transactions.get(id);
        trackPlaylistPair.mPlaylist.getTracks().add(trackPlaylistPair.mTrack);
        transactions.remove(id);
        V view = this.getView();
        if (view != null) {
            view.hideNetworkProgress();
            view.onAddToRemoveFromPlaylistResult();
        }
    }

    private void handleRemoveFromPlaylistEvent(Uri pUri) {
        String id = pUri.getQueryParameter("playlistId");
        HashMap transactions = this.mTransactionActions.get(ServicePlayback.CUSTOM_ACTION__REMOVE_FROM_PLAYLIST);
        TrackPlaylistPair trackPlaylistPair = (TrackPlaylistPair) transactions.get(id);
        trackPlaylistPair.mPlaylist.getTracks().remove(trackPlaylistPair.mTrack);
        transactions.remove(id);
        V view = this.getView();
        if (view != null) {
            view.hideNetworkProgress();
            view.onAddToRemoveFromPlaylistResult();
        }
    }

    private class TrackPlaylistPair {
        private Track mTrack;
        private Playlist mPlaylist;

        private TrackPlaylistPair(Track pTrack, Playlist pPlaylist) {
            this.mTrack = pTrack;
            this.mPlaylist = pPlaylist;
        }
    }
}
