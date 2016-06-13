package com.dreamdigitizers.megamelodies.views.classes.services;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaDescriptionCompat;

import com.dreamdigitizers.androidbaselibrary.utilities.UtilsDialog;
import com.dreamdigitizers.androidbaselibrary.views.classes.services.ServiceMediaBrowser;
import com.dreamdigitizers.androidbaselibrary.views.classes.services.support.CustomQueueItem;
import com.dreamdigitizers.androidbaselibrary.views.classes.services.support.MediaPlayerNotificationReceiver;
import com.dreamdigitizers.megamelodies.R;
import com.dreamdigitizers.megamelodies.presenters.classes.PresenterFactory;
import com.dreamdigitizers.megamelodies.presenters.interfaces.IPresenterPlayback;
import com.dreamdigitizers.megamelodies.views.classes.services.support.PlaybackNotificationReceiver;
import com.dreamdigitizers.megamelodies.views.interfaces.IViewPlayback;
import com.dreamdigitizers.megamelodies.views.interfaces.IViewRx;

import java.util.ArrayList;
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

    private static final int ACTIVE_MODE__SEARCH = 1;
    private static final int ACTIVE_MODE__FAVORITES = 2;
    private static final int ACTIVE_MODE__PLAYLISTS = 3;
    private static final int ACTIVE_MODE__PLAYLIST = 4;

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

    private static final String URI__DRAWABLE = "android.resource://com.dreamdigitizers.megamelodies/drawable/";
    private static final String URI__DRAWABLE_ICON_SEARCH = ServicePlayback.URI__DRAWABLE + "ic__search";
    private static final String URI__DRAWABLE_ICON_FAVORITE =  ServicePlayback.URI__DRAWABLE + "ic__favorite";
    private static final String URI__DRAWABLE_ICON_PLAYLIST =  ServicePlayback.URI__DRAWABLE + "ic__playlist";

    private Result mSearchResult;
    private Result mFavoritesResult;
    private Result mPlaylistsResult;
    private Result mPlaylistResult;

    private List<CustomQueueItem> mActiveQueue;
    private List<CustomQueueItem> mSearchQueue;
    private List<CustomQueueItem> mFavoritesQueue;

    private List<MediaBrowserCompat.MediaItem> mSearchMediaItems;
    private List<MediaBrowserCompat.MediaItem> mFavoritesMediaItems;
    private List<MediaBrowserCompat.MediaItem> mPlaylistsMediaItems;

    private int mSearchOffset;
    private int mFavoritesOffset;
    private int mPlaylistsOffset;
    private int mPlaylistOffset;

    private boolean mIsSearchMore;
    private boolean mIsFavoritesMore;
    private boolean mIsPlaylistsMore;
    private boolean mIsPlaylistMore;

    private ViewPlayback mView;
    private IPresenterPlayback mPresenter;

    private int mActiveMode;
    private String mQuery;

    @Override
    public void onCreate() {
        super.onCreate();

        this.mSearchQueue = new ArrayList<>();
        this.mFavoritesQueue = new ArrayList<>();

        this.mSearchMediaItems = new ArrayList<>();
        this.mFavoritesMediaItems = new ArrayList<>();
        this.mPlaylistsMediaItems = new ArrayList<>();

        this.mIsSearchMore = true;
        this.mIsFavoritesMore = true;
        this.mIsPlaylistsMore = true;
        this.mIsPlaylistMore = true;

        this.mView = new ViewPlayback();
        this.mPresenter = (IPresenterPlayback) PresenterFactory.createPresenter(IPresenterPlayback.class, this.mView);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mPresenter.dispose();
    }

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
        List<MediaBrowserCompat.MediaItem> mediaItems = new ArrayList<>();
        mediaItems.add(this.buildChildrenRootMediaItem(
                ServicePlayback.MEDIA_ID__SEARCH,
                R.string.media_description_title__search,
                ServicePlayback.URI__DRAWABLE_ICON_SEARCH,
                R.string.media_description_subtitle__search));

        mediaItems.add(this.buildChildrenRootMediaItem(
                ServicePlayback.MEDIA_ID__FAVORITES,
                R.string.media_description_title__favorite,
                ServicePlayback.URI__DRAWABLE_ICON_FAVORITE,
                R.string.media_description_subtitle__favorite));

        mediaItems.add(this.buildChildrenRootMediaItem(
                ServicePlayback.MEDIA_ID__PLAYLIST,
                R.string.media_description_title__playlist,
                ServicePlayback.URI__DRAWABLE_ICON_PLAYLIST,
                R.string.media_description_subtitle__playlist));

        pResult.sendResult(mediaItems);
    }

    private void loadChildrenSearch(String pQuery, Result<List<MediaBrowserCompat.MediaItem>> pResult) {
        this.mQuery = pQuery;
        this.mActiveMode = ServicePlayback.ACTIVE_MODE__SEARCH;
        this.mSearchOffset = 0;
        this.mIsSearchMore = true;
        this.mSearchQueue = new ArrayList<>();
        this.mSearchMediaItems.clear();
        this.loadChildrenSearchMore(pQuery, pResult);
    }

    private void loadChildrenSearchMore(Result<List<MediaBrowserCompat.MediaItem>> pResult) {
        this.loadChildrenSearchMore(this.mQuery, pResult);
    }

    private void loadChildrenSearchMore(String pQuery, Result<List<MediaBrowserCompat.MediaItem>> pResult) {
        this.mSearchResult = pResult;
        this.mSearchResult.detach();
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

    private MediaBrowserCompat.MediaItem buildChildrenRootMediaItem(String pMediaId, int pTitleStringId, String pUri, int pSubtitleStringId) {
        return new MediaBrowserCompat.MediaItem(new MediaDescriptionCompat.Builder()
                .setMediaId(pMediaId)
                .setTitle(this.getString(pTitleStringId))
                .setIconUri(Uri.parse(pUri))
                .setSubtitle(this.getString(pSubtitleStringId))
                .build(), MediaBrowserCompat.MediaItem.FLAG_BROWSABLE);
    }

    private void onRxError(Throwable pError, UtilsDialog.IRetryAction pRetryAction) {
        this.mSearchResult = null;
        this.mFavoritesResult = null;
        this.mPlaylistsResult = null;
        this.mPlaylistResult = null;
        this.updatePlaybackState(ServicePlayback.ERROR_CODE__MEDIA_NETWORK);
        pError.printStackTrace();
    }

    private class ViewPlayback extends IViewRx.ViewRx implements IViewPlayback {
        @Override
        public Context getViewContext() {
            return ServicePlayback.this;
        }

        @Override
        public void onRxError(Throwable pError, UtilsDialog.IRetryAction pRetryAction) {
            ServicePlayback.this.onRxError(pError, pRetryAction);
        }
    }
}
