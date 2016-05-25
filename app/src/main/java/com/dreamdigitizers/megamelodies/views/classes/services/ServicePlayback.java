package com.dreamdigitizers.megamelodies.views.classes.services;

import android.os.Bundle;
import android.support.v4.media.MediaBrowserCompat;

import com.dreamdigitizers.androidbaselibrary.views.classes.services.ServiceMediaBrowser;
import com.dreamdigitizers.androidbaselibrary.views.classes.services.support.MediaPlayerNotificationReceiver;
import com.dreamdigitizers.megamelodies.views.classes.services.support.PlaybackNotificationReceiver;

import java.util.List;

public class ServicePlayback extends ServiceMediaBrowser {
    public static final String ERROR_CODE__MEDIA_NETWORK = "1";

    public static final String CUSTOM_ACTION__FAVORITE = "com.dreamdigitizers.megamelodies.views.classes.services.ServicePlayback.FAVORITE";
    public static final String CUSTOM_ACTION__DELETE_PLAYLIST = "com.dreamdigitizers.megamelodies.views.classes.services.ServicePlayback.DELETE_PLAYLIST";
    public static final String CUSTOM_ACTION__ADD_TO_PLAYLIST = "com.dreamdigitizers.megamelodies.views.classes.services.ServicePlayback.ADD_TO_PLAYLIST";
    public static final String CUSTOM_ACTION__REMOVE_FROM_PLAYLIST = "com.dreamdigitizers.megamelodies.views.classes.services.ServicePlayback.REMOVE_FROM_PLAYLIST";
    public static final String CUSTOM_ACTION__CREATE_PLAYLIST = "com.dreamdigitizers.megamelodies.views.classes.services.ServicePlayback.CREATE_PLAYLIST";

    private static final String EVENT_URI__ROOT = "media://serviceplayback?action=%s";
    private static final String EVENT_URI__FAVORITE = ServicePlayback.EVENT_URI__ROOT + "&trackId=%s&userFavorite=%s";
    private static final String EVENT_URI__CREATE_PLAYLIST = ServicePlayback.EVENT_URI__ROOT + "&trackId=%s&playlistId=%s";
    private static final String EVENT_URI__DELETE_PLAYLIST = ServicePlayback.EVENT_URI__ROOT + "&playlistId=%s";
    private static final String EVENT_URI__ADD_TO_PLAYLIST = ServicePlayback.EVENT_URI__ROOT + "&trackId=%s&playlistId=%s";
    private static final String EVENT_URI__REMOVE_FROM_PLAYLIST = ServicePlayback.EVENT_URI__ROOT + "&trackId=%s&playlistId=%s";

    public static final String MEDIA_ID__ROOT = "__ROOT__";
    public static final String MEDIA_ID__SEARCH = "__SEARCH__";
    public static final String MEDIA_ID__SEARCH_MORE = "__SEARCH_MORE__";
    public static final String MEDIA_ID__FAVORITES = "__FAVORITES__";
    public static final String MEDIA_ID__FAVORITES_MORE = "__FAVORITES_MORE__";
    public static final String MEDIA_ID__PLAYLISTS_ALL = "__PLAYLISTS_ALL__";
    public static final String MEDIA_ID__PLAYLISTS = "__PLAYLISTS__";
    public static final String MEDIA_ID__PLAYLISTS_MORE = "__PLAYLISTS_MORE__";
    public static final String MEDIA_ID__PLAYLIST = "__PLAYLIST__";
    public static final String MEDIA_ID__PLAYLIST_MORE = "__PLAYLIST_MORE__";

    @Override
    protected boolean isOnlineStreaming() {
        return true;
    }

    @Override
    protected MediaPlayerNotificationReceiver createMediaPlayerNotificationReceiver() {
        return new PlaybackNotificationReceiver(this);
    }

    @Override
    public BrowserRoot onGetRoot(String pClientPackageName, int pClientUid, Bundle pRootHints) {
        return new BrowserRoot(ServicePlayback.MEDIA_ID__ROOT, null);
    }

    @Override
    public void onLoadChildren(String pParentId, Result<List<MediaBrowserCompat.MediaItem>> pResult) {
        if (pParentId.startsWith(ServicePlayback.MEDIA_ID__SEARCH)) {
            String query = pParentId.substring(ServicePlayback.MEDIA_ID__SEARCH.length());
            this.loadChildrenSearch(query, pResult);
            return;
        }

        if (pParentId.startsWith(ServicePlayback.MEDIA_ID__PLAYLIST)) {
            String playlistId = pParentId.substring(ServicePlayback.MEDIA_ID__PLAYLIST.length());
            this.loadChildrenPlaylist(playlistId, pResult);
            return;
        }

        switch (pParentId) {
            case ServicePlayback.MEDIA_ID__ROOT:
                this.loadChildrenRoot(pResult);
                break;
            case ServicePlayback.MEDIA_ID__SEARCH_MORE:
                this.loadChildrenSearchMore(pResult);
                break;
            case ServicePlayback.MEDIA_ID__FAVORITES:
                this.loadChildrenFavorites(pResult);
                break;
            case ServicePlayback.MEDIA_ID__FAVORITES_MORE:
                this.loadChildrenFavoritesMore(pResult);
                break;
            case ServicePlayback.MEDIA_ID__PLAYLISTS_ALL:
                this.loadChildrenPlaylistsAll(pResult);
                break;
            case ServicePlayback.MEDIA_ID__PLAYLISTS:
                this.loadChildrenPlaylists(pResult);
                break;
            case ServicePlayback.MEDIA_ID__PLAYLISTS_MORE:
                this.loadChildrenPlaylistsMore(pResult);
                break;
            case ServicePlayback.MEDIA_ID__PLAYLIST_MORE:
                this.loadChildrenPlaylistMore(pResult);
                break;
            default:
                break;
        }
    }

    private void loadChildrenRoot(Result<List<MediaBrowserCompat.MediaItem>> pResult) {

    }

    private void loadChildrenSearch(String pQuery, Result<List<MediaBrowserCompat.MediaItem>> pResult) {

    }

    private void loadChildrenSearchMore(Result<List<MediaBrowserCompat.MediaItem>> pResult) {

    }

    private void loadChildrenFavorites(Result<List<MediaBrowserCompat.MediaItem>> pResult) {

    }

    private void loadChildrenFavoritesMore(Result<List<MediaBrowserCompat.MediaItem>> pResult) {

    }

    private void loadChildrenPlaylistsAll(Result<List<MediaBrowserCompat.MediaItem>> pResult) {

    }

    private void loadChildrenPlaylists(Result<List<MediaBrowserCompat.MediaItem>> pResult) {

    }

    private void loadChildrenPlaylistsMore(Result<List<MediaBrowserCompat.MediaItem>> pResult) {

    }

    private void loadChildrenPlaylist(String pPlaylistId, Result<List<MediaBrowserCompat.MediaItem>> pResult) {

    }

    private void loadChildrenPlaylistMore(Result<List<MediaBrowserCompat.MediaItem>> pResult) {

    }
}
