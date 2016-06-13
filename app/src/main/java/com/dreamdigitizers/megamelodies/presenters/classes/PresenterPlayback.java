package com.dreamdigitizers.megamelodies.presenters.classes;

import com.dreamdigitizers.androidbaselibrary.utilities.UtilsDialog;
import com.dreamdigitizers.androiddatafetchingapisclient.core.Api;
import com.dreamdigitizers.androiddatafetchingapisclient.core.IApi;
import com.dreamdigitizers.androiddatafetchingapisclient.models.nct.MusicNct;
import com.dreamdigitizers.androiddatafetchingapisclient.models.zing.MusicZing;
import com.dreamdigitizers.androiddatafetchingapisclient.models.nct.NctSearchResult;
import com.dreamdigitizers.androiddatafetchingapisclient.models.zing.ZingSearchResult;
import com.dreamdigitizers.megamelodies.presenters.interfaces.IPresenterPlayback;
import com.dreamdigitizers.megamelodies.views.interfaces.IViewPlayback;

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
                .subscribe(new Subscriber<MusicNct>() {
                    @Override
                    public void onStart() {
                        PresenterPlayback.this.onStart();
                    }

                    @Override
                    public void onNext(MusicNct pMusicNct) {
                        IViewPlayback view = PresenterPlayback.this.getView();
                        if (view != null) {
                            view.onRxNctFetchNext(pMusicNct);
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
                .subscribe(new Subscriber<MusicZing>() {
                    @Override
                    public void onStart() {
                        PresenterPlayback.this.onStart();
                    }

                    @Override
                    public void onNext(MusicZing pMusicZing) {
                        IViewPlayback view = PresenterPlayback.this.getView();
                        if (view != null) {
                            view.onRxZingFetchNext(pMusicZing);
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

    private void unsubscribe() {
        if(this.mSubscription != null) {
            this.mSubscription.unsubscribe();
            this.mSubscription = null;
        }
    }
}
