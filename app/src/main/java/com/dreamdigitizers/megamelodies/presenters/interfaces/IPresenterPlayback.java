package com.dreamdigitizers.megamelodies.presenters.interfaces;

import com.dreamdigitizers.androidbaselibrary.utilities.UtilsDialog;
import com.dreamdigitizers.megamelodies.models.Track;

public interface IPresenterPlayback extends IPresenterRx {
    void nctSearch(final UtilsDialog.IRetryAction pRetryAction, String pQ);
    void zingSearch(final UtilsDialog.IRetryAction pRetryAction, String pType, int pNum, String pQuery);
    void nctFetch(final UtilsDialog.IRetryAction pRetryAction, String pUrl, String pKeyword);
    void zingFetch(final UtilsDialog.IRetryAction pRetryAction, String pName, String pArtist, String pId);
    void checkFavoriteTrack(Track pTrack);
    void favorite(Track pTrack);
    void unfavorite(Track pTrack);
}
