package com.dreamdigitizers.megamelodies.views.classes.services;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat;

import com.dreamdigitizers.androidbaselibrary.utilities.UtilsDialog;
import com.dreamdigitizers.androidbaselibrary.views.classes.services.ServiceMediaBrowser;
import com.dreamdigitizers.androidbaselibrary.views.classes.services.support.CustomQueueItem;
import com.dreamdigitizers.androidbaselibrary.views.classes.services.support.MediaPlayerNotificationReceiver;
import com.dreamdigitizers.androiddatafetchingapisclient.models.nct.MusicNct;
import com.dreamdigitizers.androiddatafetchingapisclient.models.zing.MusicZing;
import com.dreamdigitizers.androiddatafetchingapisclient.models.nct.NctSearchResult;
import com.dreamdigitizers.androiddatafetchingapisclient.models.zing.ZingSearchResult;
import com.dreamdigitizers.megamelodies.R;
import com.dreamdigitizers.megamelodies.presenters.classes.PresenterFactory;
import com.dreamdigitizers.megamelodies.presenters.interfaces.IPresenterPlayback;
import com.dreamdigitizers.megamelodies.views.classes.services.support.MetadataBuilder;
import com.dreamdigitizers.megamelodies.views.classes.services.support.PlaybackNotificationReceiver;
import com.dreamdigitizers.megamelodies.views.interfaces.IViewPlayback;
import com.dreamdigitizers.megamelodies.views.interfaces.IViewRx;

import java.util.ArrayList;
import java.util.List;

public class ServicePlayback extends ServiceMediaBrowser {
    public static final String ERROR_CODE__MEDIA_NETWORK = "1";

    public static final String MEDIA_ID__ROOT = "mediaId://servicePlayback/";
    public static final String MEDIA_ID__SEARCH = ServicePlayback.MEDIA_ID__ROOT + "search";
    public static final String MEDIA_ID__FAVORITES = ServicePlayback.MEDIA_ID__ROOT + "favorites";
    public static final String MEDIA_ID__PLAYLISTS = ServicePlayback.MEDIA_ID__ROOT + "playlists";
    public static final String MEDIA_ID__PLAYLIST = ServicePlayback.MEDIA_ID__ROOT + "playlist";

    public static final String CUSTOM_ACTION__FAVORITE = "com.dreamdigitizers.megamelodies.views.classes.services.ServicePlayback.FAVORITE";
    public static final String CUSTOM_ACTION__DELETE_PLAYLIST = "com.dreamdigitizers.megamelodies.views.classes.services.ServicePlayback.DELETE_PLAYLIST";
    public static final String CUSTOM_ACTION__ADD_TO_PLAYLIST = "com.dreamdigitizers.megamelodies.views.classes.services.ServicePlayback.ADD_TO_PLAYLIST";
    public static final String CUSTOM_ACTION__REMOVE_FROM_PLAYLIST = "com.dreamdigitizers.megamelodies.views.classes.services.ServicePlayback.REMOVE_FROM_PLAYLIST";
    public static final String CUSTOM_ACTION__CREATE_PLAYLIST = "com.dreamdigitizers.megamelodies.views.classes.services.ServicePlayback.CREATE_PLAYLIST";

    private static final String EVENT_URI__ROOT = "mediaEvent://servicePlayback?action=%s";
    private static final String EVENT_URI__FAVORITE = ServicePlayback.EVENT_URI__ROOT + "&trackId=%s&userFavorite=%s";
    private static final String EVENT_URI__CREATE_PLAYLIST = ServicePlayback.EVENT_URI__ROOT + "&trackId=%s&playlistId=%s";
    private static final String EVENT_URI__DELETE_PLAYLIST = ServicePlayback.EVENT_URI__ROOT + "&playlistId=%s";
    private static final String EVENT_URI__ADD_TO_PLAYLIST = ServicePlayback.EVENT_URI__ROOT + "&trackId=%s&playlistId=%s";
    private static final String EVENT_URI__REMOVE_FROM_PLAYLIST = ServicePlayback.EVENT_URI__ROOT + "&trackId=%s&playlistId=%s";

    private static final String URI__DRAWABLE = "android.resource://com.dreamdigitizers.megamelodies/drawable/";
    private static final String URI__DRAWABLE_ICON_SEARCH = ServicePlayback.URI__DRAWABLE + "ic__search";
    private static final String URI__DRAWABLE_ICON_FAVORITE =  ServicePlayback.URI__DRAWABLE + "ic__favorite";
    private static final String URI__DRAWABLE_ICON_PLAYLIST =  ServicePlayback.URI__DRAWABLE + "ic__playlist";

    public static final int SERVER_ID__ZING = 0;
    public static final int SERVER_ID__NCT = 1;

    private static final int ACTIVE_MODE__SEARCH = 0;
    private static final int ACTIVE_MODE__FAVORITES = 1;
    private static final int ACTIVE_MODE__PLAYLISTS = 2;
    private static final int ACTIVE_MODE__PLAYLIST = 3;

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

    private int mCurrentServerId;
    private String mCurrentQuery;
    private String mCurrentType;
    private int mCurrentNum;

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
        Uri uri = Uri.parse(pParentId);

        if (pParentId.startsWith(ServicePlayback.MEDIA_ID__SEARCH)) {
            this.loadChildrenSearch(pResult, uri);
        } else if (pParentId.startsWith(ServicePlayback.MEDIA_ID__FAVORITES)) {
            this.loadChildrenFavorites(pResult);
        } else if (pParentId.startsWith(ServicePlayback.MEDIA_ID__PLAYLISTS)) {

        } else if (pParentId.startsWith(ServicePlayback.MEDIA_ID__PLAYLIST)) {
            this.loadChildrenPlaylists(pResult);
        } else if (pParentId.startsWith(ServicePlayback.MEDIA_ID__ROOT)) {
            this.loadChildrenRoot(pResult);
        }

        if (pParentId.startsWith(ServicePlayback.MEDIA_ID__PLAYLIST)) {
            String playlistId = pParentId.substring(ServicePlayback.MEDIA_ID__PLAYLIST.length());
            this.loadChildrenPlaylist(playlistId, pResult);
            return;
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

    private void loadChildrenSearch(Result<List<MediaBrowserCompat.MediaItem>> pResult, Uri pUri) {
        this.mActiveMode = ServicePlayback.ACTIVE_MODE__SEARCH;
        this.mSearchOffset = 0;
        this.mIsSearchMore = true;
        this.mSearchQueue = new ArrayList<>();
        this.mSearchMediaItems.clear();

        this.mSearchResult = pResult;
        this.mSearchResult.detach();
        this.mCurrentServerId = Integer.parseInt(pUri.getQueryParameter("serverId"));
        this.mCurrentQuery = pUri.getQueryParameter("query");
        this.mCurrentType = null;
        this.mCurrentNum = 0;

        switch (this.mCurrentServerId) {
            case ServicePlayback.SERVER_ID__ZING:
                this.mCurrentType = pUri.getQueryParameter("type");
                this.mCurrentNum = Integer.parseInt(pUri.getQueryParameter("num"));
                this.mPresenter.zingSearch(null, this.mCurrentType, this.mCurrentNum, this.mCurrentQuery);
                break;
            case ServicePlayback.SERVER_ID__NCT:
                this.mPresenter.nctSearch(null, this.mCurrentQuery);
                break;
            default:
                break;
        }
    }

    private void loadChildrenSearchMore(Result<List<MediaBrowserCompat.MediaItem>> pResult) {
    }

    private void loadChildrenFavorites(Result<List<MediaBrowserCompat.MediaItem>> pResult) {

    }

    private void loadChildrenFavoritesMore(Result<List<MediaBrowserCompat.MediaItem>> pResult) {

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

    private List<MediaBrowserCompat.MediaItem> buildMediaItemList(NctSearchResult pNctSearchResult, List<CustomQueueItem> pPlayingQueue, boolean pIsAddToTop) {
        List<MediaBrowserCompat.MediaItem> mediaItems = new ArrayList<>();
        List<CustomQueueItem> customQueueItems = new ArrayList<>();

        long customQueueItemsSize = customQueueItems.size();
        com.dreamdigitizers.androiddatafetchingapisclient.models.nct.Data data = pNctSearchResult.getData();
        if (data != null) {
            long i = 0;
            List<com.dreamdigitizers.androiddatafetchingapisclient.models.nct.Song> songs = data.getSongs();
            for (com.dreamdigitizers.androiddatafetchingapisclient.models.nct.Song song : songs) {
                MediaMetadataCompat mediaMetadata = MetadataBuilder.build(song);
                MediaDescriptionCompat mediaDescription = MetadataBuilder.build(mediaMetadata);

                MediaBrowserCompat.MediaItem mediaItem = new MediaBrowserCompat.MediaItem(mediaDescription, MediaBrowserCompat.MediaItem.FLAG_PLAYABLE);
                mediaItems.add(mediaItem);

                MediaSessionCompat.QueueItem queueItem = new MediaSessionCompat.QueueItem(mediaDescription, customQueueItemsSize + i);
                CustomQueueItem customQueueItem = new CustomQueueItem(queueItem, mediaMetadata, null);
                customQueueItems.add(customQueueItem);

                i++;
            }
        }

        if (customQueueItemsSize > 0) {
            if (pIsAddToTop) {
                pPlayingQueue.addAll(0, customQueueItems);
            } else {
                pPlayingQueue.addAll(customQueueItems);
            }
        }

        return mediaItems;
    }

    private List<MediaBrowserCompat.MediaItem> buildMediaItemList(ZingSearchResult pZingSearchResult, List<CustomQueueItem> pPlayingQueue, boolean pIsAddToTop) {
        List<MediaBrowserCompat.MediaItem> mediaItems = new ArrayList<>();
        List<CustomQueueItem> customQueueItems = new ArrayList<>();

        long customQueueItemsSize = customQueueItems.size();
        List<com.dreamdigitizers.androiddatafetchingapisclient.models.zing.Data> dataList = pZingSearchResult.getDataList();
        com.dreamdigitizers.androiddatafetchingapisclient.models.zing.Data data = dataList.isEmpty() ? null : dataList.get(0);
        if (data != null) {
            long i = 0;
            List<com.dreamdigitizers.androiddatafetchingapisclient.models.zing.Song> songs = data.getSongs();
            for (com.dreamdigitizers.androiddatafetchingapisclient.models.zing.Song song : songs) {
                MediaMetadataCompat mediaMetadata = MetadataBuilder.build(song);
                MediaDescriptionCompat mediaDescription = MetadataBuilder.build(mediaMetadata);

                MediaBrowserCompat.MediaItem mediaItem = new MediaBrowserCompat.MediaItem(mediaDescription, MediaBrowserCompat.MediaItem.FLAG_PLAYABLE);
                mediaItems.add(mediaItem);

                MediaSessionCompat.QueueItem queueItem = new MediaSessionCompat.QueueItem(mediaDescription, customQueueItemsSize + i);
                CustomQueueItem customQueueItem = new CustomQueueItem(queueItem, mediaMetadata, null);
                customQueueItems.add(customQueueItem);

                i++;
            }
        }

        if (customQueueItemsSize > 0) {
            if (pIsAddToTop) {
                pPlayingQueue.addAll(0, customQueueItems);
            } else {
                pPlayingQueue.addAll(customQueueItems);
            }
        }

        return mediaItems;
    }

    private void onRxSearchNext(List<MediaBrowserCompat.MediaItem> pMediaItems) {
        this.mSearchMediaItems.addAll(0, pMediaItems);
        this.mSearchResult.sendResult(pMediaItems);
        this.mSearchResult = null;
        if (this.mActiveMode == ServicePlayback.ACTIVE_MODE__SEARCH) {
            this.mActiveQueue = this.mSearchQueue;
        }
    }

    private void onRxNctSearchNext(NctSearchResult pNctSearchResult) {
        if (this.mSearchResult != null) {
            List<MediaBrowserCompat.MediaItem> mediaItems = this.buildMediaItemList(pNctSearchResult, this.mSearchQueue, true);
            this.onRxSearchNext(mediaItems);
        }
    }

    private void onRxZingSearchNext(ZingSearchResult pZingSearchResult) {
        if (this.mSearchResult != null) {
            List<MediaBrowserCompat.MediaItem> mediaItems = this.buildMediaItemList(pZingSearchResult, this.mSearchQueue, true);
            this.onRxSearchNext(mediaItems);
        }
    }

    private void onRxNctFetchNext(MusicNct pMusicNct) {

    }

    private void onRxZingFetchNext(MusicZing pMusicZing) {

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

        @Override
        public void onRxNctSearchNext(NctSearchResult pNctSearchResult) {
            ServicePlayback.this.onRxNctSearchNext(pNctSearchResult);
        }

        @Override
        public void onRxZingSearchNext(ZingSearchResult pZingSearchResult) {
            ServicePlayback.this.onRxZingSearchNext(pZingSearchResult);
        }

        @Override
        public void onRxNctFetchNext(MusicNct pMusicNct) {
            ServicePlayback.this.onRxNctFetchNext(pMusicNct);
        }

        @Override
        public void onRxZingFetchNext(MusicZing pMusicZing) {
            ServicePlayback.this.onRxZingFetchNext(pMusicZing);
        }
    }
}
