package com.dreamdigitizers.megamelodies.views.classes.services;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;

import com.dreamdigitizers.androidbaselibrary.utilities.UtilsDialog;
import com.dreamdigitizers.androidbaselibrary.utilities.UtilsString;
import com.dreamdigitizers.androidbaselibrary.views.classes.services.ServiceMediaBrowser;
import com.dreamdigitizers.androidbaselibrary.views.classes.services.support.CustomQueueItem;
import com.dreamdigitizers.androidbaselibrary.views.classes.services.support.IPlayback;
import com.dreamdigitizers.androidbaselibrary.views.classes.services.support.MediaPlayerNotificationReceiver;
import com.dreamdigitizers.androiddatafetchingapisclient.core.Api;
import com.dreamdigitizers.androiddatafetchingapisclient.models.nct.NctData;
import com.dreamdigitizers.androiddatafetchingapisclient.models.nct.NctMusic;
import com.dreamdigitizers.androiddatafetchingapisclient.models.nct.NctSearchResult;
import com.dreamdigitizers.androiddatafetchingapisclient.models.nct.NctSong;
import com.dreamdigitizers.androiddatafetchingapisclient.models.zing.ZingData;
import com.dreamdigitizers.androiddatafetchingapisclient.models.zing.ZingMusic;
import com.dreamdigitizers.androiddatafetchingapisclient.models.zing.ZingSearchResult;
import com.dreamdigitizers.androiddatafetchingapisclient.models.zing.ZingSong;
import com.dreamdigitizers.megamelodies.R;
import com.dreamdigitizers.megamelodies.Share;
import com.dreamdigitizers.megamelodies.models.Track;
import com.dreamdigitizers.megamelodies.presenters.classes.PresenterFactory;
import com.dreamdigitizers.megamelodies.presenters.interfaces.IPresenterPlayback;
import com.dreamdigitizers.megamelodies.views.classes.services.support.CustomLocalPlayback;
import com.dreamdigitizers.megamelodies.views.classes.services.support.MetadataBuilder;
import com.dreamdigitizers.megamelodies.views.classes.services.support.PlaybackNotificationReceiver;
import com.dreamdigitizers.megamelodies.views.interfaces.IViewPlayback;
import com.dreamdigitizers.megamelodies.views.interfaces.IViewRx;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ServicePlayback extends ServiceMediaBrowser implements CustomLocalPlayback.IOnMediaPlayerPreparedListener {
    public static final String ERROR_CODE__MEDIA_NETWORK = "1";

    public static final String MEDIA_ID__ROOT = "mediaId://servicePlayback/";
    private static final String MEDIA_ID__SEARCH_ROOT = ServicePlayback.MEDIA_ID__ROOT + "search";
    public static final String MEDIA_ID__SEARCH = ServicePlayback.MEDIA_ID__SEARCH_ROOT + "?serverId=%d&query=%s&offset=%d&pageSize=%d&type=%s&num=%d";
    public static final String MEDIA_ID__FAVORITES = ServicePlayback.MEDIA_ID__ROOT + "favorites";
    public static final String MEDIA_ID__PLAYLISTS = ServicePlayback.MEDIA_ID__ROOT + "playlists";
    public static final String MEDIA_ID__PLAYLIST = ServicePlayback.MEDIA_ID__ROOT + "playlist";

    public static final String CUSTOM_ACTION__FAVORITE = "com.dreamdigitizers.megamelodies.views.classes.services.ServicePlayback.FAVORITE";
    public static final String CUSTOM_ACTION__CREATE_PLAYLIST = "com.dreamdigitizers.megamelodies.views.classes.services.ServicePlayback.CREATE_PLAYLIST";
    public static final String CUSTOM_ACTION__DELETE_PLAYLIST = "com.dreamdigitizers.megamelodies.views.classes.services.ServicePlayback.DELETE_PLAYLIST";
    public static final String CUSTOM_ACTION__ADD_TO_PLAYLIST = "com.dreamdigitizers.megamelodies.views.classes.services.ServicePlayback.ADD_TO_PLAYLIST";
    public static final String CUSTOM_ACTION__REMOVE_FROM_PLAYLIST = "com.dreamdigitizers.megamelodies.views.classes.services.ServicePlayback.REMOVE_FROM_PLAYLIST";

    private static final String EVENT_URI__ROOT = "mediaEvent://servicePlayback?action=%s";
    private static final String EVENT_URI__FAVORITE = ServicePlayback.EVENT_URI__ROOT + "&trackId=%s&userFavorite=%s";
    private static final String EVENT_URI__CREATE_PLAYLIST = ServicePlayback.EVENT_URI__ROOT + "&trackId=%s&playlistId=%s";
    private static final String EVENT_URI__DELETE_PLAYLIST = ServicePlayback.EVENT_URI__ROOT + "&playlistId=%s";
    private static final String EVENT_URI__ADD_TO_PLAYLIST = ServicePlayback.EVENT_URI__ROOT + "&trackId=%s&playlistId=%s";
    private static final String EVENT_URI__REMOVE_FROM_PLAYLIST = ServicePlayback.EVENT_URI__ROOT + "&trackId=%s&playlistId=%s";

    private static final String URI__DRAWABLE = "android.resource://com.dreamdigitizers.megamelodies/drawable/";
    private static final String URI__DRAWABLE_ICON_SEARCH = ServicePlayback.URI__DRAWABLE + "ic__search_white";
    private static final String URI__DRAWABLE_ICON_FAVORITE =  ServicePlayback.URI__DRAWABLE + "ic__favorite";
    private static final String URI__DRAWABLE_ICON_PLAYLIST =  ServicePlayback.URI__DRAWABLE + "ic__playlist_white";

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

    private boolean mIsSearchMore;
    private boolean mIsFavoritesMore;
    private boolean mIsPlaylistsMore;
    private boolean mIsPlaylistMore;

    private ViewPlayback mView;
    private IPresenterPlayback mPresenter;

    private int mActiveMode;

    private int mLastServerId;
    private String mLastQuery;
    private String mLastType;
    private int mLastNum;
    private int mLastStartIndex;
    private int mLastEndIndex;

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

        Api.initialize(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mPresenter.dispose();
    }

    @Override
    protected IPlayback createPlayback() {
        CustomLocalPlayback customLocalPlayback = new CustomLocalPlayback(this, this.isOnlineStreaming());
        customLocalPlayback.setOnMediaPlayerPreparedListener(this);
        return customLocalPlayback;
    }

    @Override
    protected void processPlayFromMediaIdRequest(String pMediaId, Bundle pExtras) {
        this.setPlayingQueue(this.mActiveQueue);
        super.processPlayFromMediaIdRequest(pMediaId, pExtras);
    }

    @Override
    protected void processPlayRequest() {
        if (!this.isStarted()) {
            this.setStarted(true);
        }

        MediaSessionCompat mediaSession = this.getMediaSession();
        if (!mediaSession.isActive()) {
            mediaSession.setActive(true);
        }

        int currentIndexOnQueue = this.getCurrentIndexOnQueue();
        List<CustomQueueItem> playingQueue = this.getPlayingQueue();
        IPlayback playback = this.getPlayback();
        if (this.isIndexPlayable(currentIndexOnQueue, playingQueue)) {
            CustomQueueItem customQueueItem = playingQueue.get(currentIndexOnQueue);
            if (UtilsString.isEmpty(customQueueItem.getStreamUrl())) {
                MediaMetadataCompat mediaMetadata = customQueueItem.getMediaMetadata();
                Track track = (Track) mediaMetadata.getBundle().getSerializable(MetadataBuilder.BUNDLE_KEY__TRACK);
                Serializable originalTrack = track.getOriginalTrack();
                if (originalTrack instanceof NctSong) {
                    NctSong nctSong = (NctSong) originalTrack;
                    this.mPresenter.nctFetch(null, nctSong.getUrl(), nctSong.getName());
                } else if (originalTrack instanceof ZingSong) {
                    ZingSong zingSong = (ZingSong) originalTrack;
                    this.mPresenter.zingFetch(null, zingSong.getName(), zingSong.getArtist(), zingSong.getId());
                }

                playback.setState(PlaybackStateCompat.STATE_BUFFERING);
                this.updateMetadata();
            } else {
                this.updateMetadata();
                playback.play(customQueueItem);
            }
        }
    }

    @Override
    protected void processCustomActionRequest(String pAction, Bundle pExtras) {
        switch (pAction) {
            case ServicePlayback.CUSTOM_ACTION__FAVORITE:
                this.processFavoriteRequest(pExtras);
                break;
            case ServicePlayback.CUSTOM_ACTION__CREATE_PLAYLIST:
                this.processCreatePlaylistRequest(pExtras);
                break;
            case ServicePlayback.CUSTOM_ACTION__DELETE_PLAYLIST:
                this.processDeletePlaylistRequest(pExtras);
                break;
            case ServicePlayback.CUSTOM_ACTION__ADD_TO_PLAYLIST:
                this.processAddToPlaylistRequest(pExtras);
                break;
            case ServicePlayback.CUSTOM_ACTION__REMOVE_FROM_PLAYLIST:
                this.processRemoveFromPlaylistRequest(pExtras);
                break;
            default:
                break;
        }
    }

    @Override
    protected void updateMetadata() {
        if (!this.isIndexPlayable(this.getCurrentIndexOnQueue(), this.getPlayingQueue())) {
            this.updatePlaybackState(ServiceMediaBrowser.ERROR_CODE__MEDIA_INVALID_INDEX);
            return;
        }

        CustomQueueItem customQueueItem = this.getPlayingQueue().get(this.getCurrentIndexOnQueue());
        MediaMetadataCompat mediaMetadata = customQueueItem.getMediaMetadata();
        if (Build.VERSION.SDK_INT >= 21) {
            Track track = (Track) mediaMetadata.getBundle().getSerializable(MetadataBuilder.BUNDLE_KEY__TRACK);
            Share.setCurrentTrack(track);
        }
        this.getMediaSession().setMetadata(mediaMetadata);

        MediaDescriptionCompat mediaDescription = mediaMetadata.getDescription();
        if (mediaDescription.getIconBitmap() == null && mediaDescription.getIconUri() != null) {
            this.fetchArt(customQueueItem);
        }
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

        if (pParentId.startsWith(ServicePlayback.MEDIA_ID__SEARCH_ROOT)) {
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

    @Override
    public void onPrepared() {
        CustomQueueItem customQueueItem = this.getPlayingQueue().get(this.getCurrentIndexOnQueue());
        MediaMetadataCompat oldMediaMetadata = customQueueItem.getMediaMetadata();
        MediaMetadataCompat newMediaMetadata = new MediaMetadataCompat.Builder(oldMediaMetadata)
                .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, this.getPlayback().getDuration())
                .build();
        customQueueItem.setMediaMetadata(newMediaMetadata);
        this.updateMetadata();
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

        int serverId = Integer.parseInt(pUri.getQueryParameter("serverId"));
        String query = pUri.getQueryParameter("query");
        int offset = Integer.parseInt(pUri.getQueryParameter("offset"));
        int pageSize = Integer.parseInt(pUri.getQueryParameter("pageSize"));
        String type = pUri.getQueryParameter("type");
        int num = Integer.parseInt(pUri.getQueryParameter("num"));

        if (offset < 0) {
            offset = 0;
        }

        int startIndex = offset * pageSize;
        int endIndex = (offset + 1) * pageSize;

        if (serverId == this.mLastServerId
                && UtilsString.equals(query, this.mLastQuery)
                && UtilsString.equals(type, this.mLastType)
                && this.mLastNum == num
                && !this.mSearchMediaItems.isEmpty()) {
            int size = this.mSearchMediaItems.size();
            if (startIndex >= size) {
                this.mIsSearchMore = false;
                pResult.sendResult(new ArrayList<MediaBrowserCompat.MediaItem>());
                return;
            }
            if (endIndex > size) {
                if (!this.mIsSearchMore) {
                    pResult.sendResult(new ArrayList<MediaBrowserCompat.MediaItem>());
                    return;
                }
                endIndex = size;
                this.mIsSearchMore = false;
            }

            List<MediaBrowserCompat.MediaItem> mediaItems = this.mSearchMediaItems.subList(startIndex, endIndex);
            pResult.sendResult(mediaItems);
        } else {
            this.mIsSearchMore = true;

            this.mLastServerId = serverId;
            this.mLastQuery = query;
            this.mLastType = type;
            this.mLastNum = num;
            this.mLastStartIndex = startIndex;
            this.mLastEndIndex = endIndex;

            this.mSearchQueue = new ArrayList<>();
            this.mSearchMediaItems.clear();

            this.mSearchResult = pResult;
            this.mSearchResult.detach();

            switch (this.mLastServerId) {
                case ServicePlayback.SERVER_ID__ZING:
                    this.mPresenter.zingSearch(null, this.mLastType, this.mLastNum, this.mLastQuery);
                    break;
                case ServicePlayback.SERVER_ID__NCT:
                    this.mPresenter.nctSearch(null, this.mLastQuery);
                    break;
                default:
                    break;
            }
        }
    }

    private void loadChildrenFavorites(Result<List<MediaBrowserCompat.MediaItem>> pResult) {

    }

    private void loadChildrenPlaylists(Result<List<MediaBrowserCompat.MediaItem>> pResult) {

    }

    private void loadChildrenPlaylist(String pPlaylistId, Result<List<MediaBrowserCompat.MediaItem>> pResult) {

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
        NctData nctData = pNctSearchResult.getData();
        if (nctData != null) {
            long i = 0;
            List<NctSong> nctSongs = nctData.getSongs();
            for (NctSong nctSong : nctSongs) {
                MediaMetadataCompat mediaMetadata = MetadataBuilder.build(nctSong);
                MediaDescriptionCompat mediaDescription = MetadataBuilder.build(mediaMetadata);

                MediaBrowserCompat.MediaItem mediaItem = new MediaBrowserCompat.MediaItem(mediaDescription, MediaBrowserCompat.MediaItem.FLAG_PLAYABLE);
                mediaItems.add(mediaItem);

                MediaSessionCompat.QueueItem queueItem = new MediaSessionCompat.QueueItem(mediaDescription, customQueueItemsSize + i);
                CustomQueueItem customQueueItem = new CustomQueueItem(queueItem, mediaMetadata, null);
                customQueueItems.add(customQueueItem);

                i++;
            }
        }

        customQueueItemsSize = customQueueItems.size();
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
        List<ZingData> zingDataList = pZingSearchResult.getDataList();
        ZingData zingData = zingDataList.isEmpty() ? null : zingDataList.get(0);
        if (zingData != null) {
            long i = 0;
            List<ZingSong> zingSongs = zingData.getSongs();
            for (ZingSong zingSong : zingSongs) {
                MediaMetadataCompat mediaMetadata = MetadataBuilder.build(zingSong);
                MediaDescriptionCompat mediaDescription = MetadataBuilder.build(mediaMetadata);

                MediaBrowserCompat.MediaItem mediaItem = new MediaBrowserCompat.MediaItem(mediaDescription, MediaBrowserCompat.MediaItem.FLAG_PLAYABLE);
                mediaItems.add(mediaItem);

                MediaSessionCompat.QueueItem queueItem = new MediaSessionCompat.QueueItem(mediaDescription, customQueueItemsSize + i);
                CustomQueueItem customQueueItem = new CustomQueueItem(queueItem, mediaMetadata, null);
                customQueueItems.add(customQueueItem);

                i++;
            }
        }

        customQueueItemsSize = customQueueItems.size();
        if (customQueueItemsSize > 0) {
            if (pIsAddToTop) {
                pPlayingQueue.addAll(0, customQueueItems);
            } else {
                pPlayingQueue.addAll(customQueueItems);
            }
        }

        return mediaItems;
    }

    private void processFavoriteRequest(Bundle pExtras) {
        Track track = (Track) pExtras.getSerializable(MetadataBuilder.BUNDLE_KEY__TRACK);
        if (track.isFavorite()) {
            this.mPresenter.unfavorite(track);
        } else {
            this.mPresenter.favorite(track);
        }
    }

    private void processCreatePlaylistRequest(Bundle pExtras) {

    }

    private void processDeletePlaylistRequest(Bundle pExtras) {

    }

    private void processAddToPlaylistRequest(Bundle pExtras) {

    }

    private void processRemoveFromPlaylistRequest(Bundle pExtras) {

    }

    private void onRxSearchNext(List<MediaBrowserCompat.MediaItem> pMediaItems) {
        this.mSearchMediaItems.addAll(pMediaItems);

        List<MediaBrowserCompat.MediaItem> mediaItems;
        int size = this.mSearchMediaItems.size();
        if (this.mLastStartIndex >= size) {
            this.mIsSearchMore = false;
            mediaItems = new ArrayList<>();
        } else {
            if (this.mLastEndIndex > size) {
                this.mLastEndIndex = size;
                this.mIsSearchMore = false;
            }
            mediaItems = this.mSearchMediaItems.subList(this.mLastStartIndex, this.mLastEndIndex);
        }
        this.mSearchResult.sendResult(mediaItems);
        this.mSearchResult = null;
        if (this.mActiveMode == ServicePlayback.ACTIVE_MODE__SEARCH) {
            this.mActiveQueue = this.mSearchQueue;
        }
    }

    private void onRxNctSearchNext(NctSearchResult pNctSearchResult) {
        if (this.mSearchResult != null) {
            List<MediaBrowserCompat.MediaItem> mediaItems = this.buildMediaItemList(pNctSearchResult, this.mSearchQueue, false);
            this.onRxSearchNext(mediaItems);
        }
    }

    private void onRxZingSearchNext(ZingSearchResult pZingSearchResult) {
        if (this.mSearchResult != null) {
            List<MediaBrowserCompat.MediaItem> mediaItems = this.buildMediaItemList(pZingSearchResult, this.mSearchQueue, false);
            this.onRxSearchNext(mediaItems);
        }
    }

    private void onRxFetchNext(String pStreamUrl) {
        CustomQueueItem customQueueItem = this.getPlayingQueue().get(this.getCurrentIndexOnQueue());
        customQueueItem.setStreamUrl(pStreamUrl);
        this.updateMetadata();
        this.getPlayback().play(customQueueItem);
    }

    private void onRxNctFetchNext(NctMusic pNctMusic) {
        this.onRxFetchNext(pNctMusic.getLocation());
    }

    private void onRxZingFetchNext(ZingMusic pZingMusic) {
        this.onRxFetchNext(pZingMusic.getSource());
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
        public void onRxNctFetchNext(NctMusic pNctMusic) {
            ServicePlayback.this.onRxNctFetchNext(pNctMusic);
        }

        @Override
        public void onRxZingFetchNext(ZingMusic pZingMusic) {
            ServicePlayback.this.onRxZingFetchNext(pZingMusic);
        }
    }
}
