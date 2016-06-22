package com.dreamdigitizers.megamelodies.presenters.classes;

import android.content.Context;

import com.dreamdigitizers.androidbaselibrary.utilities.UtilsDialog;
import com.dreamdigitizers.androiddatafetchingapisclient.core.Api;
import com.dreamdigitizers.androiddatafetchingapisclient.core.IApi;
import com.dreamdigitizers.androiddatafetchingapisclient.models.nct.NctMusic;
import com.dreamdigitizers.androiddatafetchingapisclient.models.nct.NctSearchResult;
import com.dreamdigitizers.androiddatafetchingapisclient.models.nct.NctSong;
import com.dreamdigitizers.androiddatafetchingapisclient.models.zing.ZingMusic;
import com.dreamdigitizers.androiddatafetchingapisclient.models.zing.ZingSearchResult;
import com.dreamdigitizers.androiddatafetchingapisclient.models.zing.ZingSong;
import com.dreamdigitizers.megamelodies.models.Playlist;
import com.dreamdigitizers.megamelodies.models.Track;
import com.dreamdigitizers.megamelodies.models.local.sqlite.helpers.HelperNctSong;
import com.dreamdigitizers.megamelodies.models.local.sqlite.helpers.HelperPlaylist;
import com.dreamdigitizers.megamelodies.models.local.sqlite.helpers.HelperPlaylistSong;
import com.dreamdigitizers.megamelodies.models.local.sqlite.helpers.HelperZingSong;
import com.dreamdigitizers.megamelodies.presenters.interfaces.IPresenterPlayback;
import com.dreamdigitizers.megamelodies.views.interfaces.IViewPlayback;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

class PresenterPlayback extends PresenterRx<IViewPlayback> implements IPresenterPlayback {
    private Subscription mSubscription;

    public PresenterPlayback(IViewPlayback pView) {
        super(pView);
    }

    @Override
    public void dispose() {
        super.dispose();
        this.unsubscribe();
    }

    @Override
    public void nctSearch(final UtilsDialog.IRetryAction pRetryAction, String pQ) {
        this.unsubscribe();
        final IApi api = Api.getInstance();
        this.mSubscription = api.nctSearch(pQ)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<NctSearchResult>() {
                    @Override
                    public void onStart() {
                        PresenterPlayback.this.onStart();
                    }

                    @Override
                    public void onNext(NctSearchResult pNctSearchResult) {
                        IViewPlayback view = PresenterPlayback.this.getView();
                        if (view != null) {
                            view.onRxNctSearchNext(pNctSearchResult);
                        }
                    }

                    @Override
                    public void onCompleted() {
                        PresenterPlayback.this.onCompleted();
                    }

                    @Override
                    public void onError(Throwable pError) {
                        PresenterPlayback.this.onError(pError, pRetryAction);
                    }
                });
    }

    @Override
    public void zingSearch(final UtilsDialog.IRetryAction pRetryAction, String pType, int pNum, String pQuery) {
        this.unsubscribe();
        final IApi api = Api.getInstance();
        this.mSubscription = api.zingSearch(pType, pNum, pQuery)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ZingSearchResult>() {
                    @Override
                    public void onStart() {
                        PresenterPlayback.this.onStart();
                    }

                    @Override
                    public void onNext(ZingSearchResult pZingSearchResult) {
                        IViewPlayback view = PresenterPlayback.this.getView();
                        if (view != null) {
                            view.onRxZingSearchNext(pZingSearchResult);
                        }
                    }

                    @Override
                    public void onCompleted() {
                        PresenterPlayback.this.onCompleted();
                    }

                    @Override
                    public void onError(Throwable pError) {
                        PresenterPlayback.this.onError(pError, pRetryAction);
                    }
                });
    }

    @Override
    public void nctFetch(final UtilsDialog.IRetryAction pRetryAction, String pUrl, String pKeyword) {
        this.unsubscribe();
        final IApi api = Api.getInstance();
        this.mSubscription = api.nctFetch(pUrl, pKeyword)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<NctMusic>() {
                    @Override
                    public void onStart() {
                        PresenterPlayback.this.onStart();
                    }

                    @Override
                    public void onNext(NctMusic pNctMusic) {
                        IViewPlayback view = PresenterPlayback.this.getView();
                        if (view != null) {
                            view.onRxNctFetchNext(pNctMusic);
                        }
                    }

                    @Override
                    public void onCompleted() {
                        PresenterPlayback.this.onCompleted();
                    }

                    @Override
                    public void onError(Throwable pError) {
                        PresenterPlayback.this.onError(pError, pRetryAction);
                    }
                });
    }

    @Override
    public void zingFetch(final UtilsDialog.IRetryAction pRetryAction, String pName, String pArtist, String pId) {
        this.unsubscribe();
        final IApi api = Api.getInstance();
        this.mSubscription = api.zingFetch(pName, pArtist, pId)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ZingMusic>() {
                    @Override
                    public void onStart() {
                        PresenterPlayback.this.onStart();
                    }

                    @Override
                    public void onNext(ZingMusic pZingMusic) {
                        IViewPlayback view = PresenterPlayback.this.getView();
                        if (view != null) {
                            view.onRxZingFetchNext(pZingMusic);
                        }
                    }

                    @Override
                    public void onCompleted() {
                        PresenterPlayback.this.onCompleted();
                    }

                    @Override
                    public void onError(Throwable pError) {
                        PresenterPlayback.this.onError(pError, pRetryAction);
                    }
                });
    }

    @Override
    public List<Playlist> retrieveAllPlaylists() {
        IViewPlayback view = this.getView();
        if (view != null) {
            Context context = view.getViewContext();
            List<Playlist> playlists = HelperPlaylist.retrieveAll(context);
            return playlists;
        }
        return new ArrayList<>();
    }

    @Override
    public void checkFavoriteTrack(Track pTrack) {
        IViewPlayback view = this.getView();
        if (view != null) {
            Track track = null;
            Context context = view.getViewContext();
            Serializable originalTrack = pTrack.getOriginalTrack();
            if (originalTrack instanceof NctSong) {
                NctSong nctSong = (NctSong) originalTrack;
                track = HelperNctSong.retrieveById(context, nctSong.getId());
            } else if (originalTrack instanceof ZingSong) {
                ZingSong zingSong = (ZingSong) originalTrack;
                track = HelperZingSong.retrieveById(context, zingSong.getId());
            }
            if (track != null) {
                pTrack.setFavorite(track.isFavorite());
            }
        }
    }

    @Override
    public void favorite(Track pTrack) {
        IViewPlayback view = this.getView();
        if (view != null) {
            Context context = view.getViewContext();
            Serializable originalTrack = pTrack.getOriginalTrack();
            if (originalTrack instanceof NctSong) {
                NctSong nctSong = (NctSong) originalTrack;
                HelperNctSong.favorite(context, nctSong);
            } else if (originalTrack instanceof ZingSong) {
                ZingSong zingSong = (ZingSong) originalTrack;
                HelperZingSong.favorite(context, zingSong);
            }
        }
    }

    @Override
    public void unfavorite(Track pTrack) {
        IViewPlayback view = this.getView();
        if (view != null) {
            Context context = view.getViewContext();
            Serializable originalTrack = pTrack.getOriginalTrack();
            if (originalTrack instanceof NctSong) {
                NctSong nctSong = (NctSong) originalTrack;
                HelperNctSong.unfavorite(context, nctSong);
            } else if (originalTrack instanceof ZingSong) {
                ZingSong zingSong = (ZingSong) originalTrack;
                HelperZingSong.unfavorite(context, zingSong);
            }
        }
    }

    @Override
    public void createPlaylist(Track pTrack, String pPlaylistName) {
        IViewPlayback view = this.getView();
        if (view != null) {
            Context context = view.getViewContext();
            HelperPlaylist.insert(context, pTrack, pPlaylistName);
        }
    }

    @Override
    public void deletePlaylist(Playlist pPlaylist) {
        IViewPlayback view = this.getView();
        if (view != null) {
            Context context = view.getViewContext();
            HelperPlaylist.delete(context, pPlaylist);
        }
    }

    @Override
    public void addToPlaylist(Track pTrack, Playlist pPlaylist) {
        IViewPlayback view = this.getView();
        if (view != null) {
            Context context = view.getViewContext();
            int playlistId = pPlaylist.getId();
            Serializable originalTrack = pTrack.getOriginalTrack();
            if (originalTrack instanceof NctSong) {
                NctSong nctSong = (NctSong) originalTrack;
                HelperPlaylistSong.insert(context, playlistId, nctSong);
            } else if (originalTrack instanceof ZingSong) {
                ZingSong zingSong = (ZingSong) originalTrack;
                HelperPlaylistSong.insert(context, playlistId, zingSong);
            }
        }
    }

    @Override
    public void removeFromPlaylist(Track pTrack, Playlist pPlaylist) {
        IViewPlayback view = this.getView();
        if (view != null) {
            Context context = view.getViewContext();
            int playlistId = pPlaylist.getId();
            Serializable originalTrack = pTrack.getOriginalTrack();
            if (originalTrack instanceof NctSong) {
                NctSong nctSong = (NctSong) originalTrack;
                HelperPlaylistSong.delete(context, playlistId, nctSong);
            } else if (originalTrack instanceof ZingSong) {
                ZingSong zingSong = (ZingSong) originalTrack;
                HelperPlaylistSong.delete(context, playlistId, zingSong);
            }
        }
    }

    private void unsubscribe() {
        if(this.mSubscription != null) {
            this.mSubscription.unsubscribe();
            this.mSubscription = null;
        }
    }
}
