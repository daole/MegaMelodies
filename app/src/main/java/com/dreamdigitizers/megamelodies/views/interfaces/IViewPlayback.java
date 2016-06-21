package com.dreamdigitizers.megamelodies.views.interfaces;

import com.dreamdigitizers.androiddatafetchingapisclient.models.nct.NctMusic;
import com.dreamdigitizers.androiddatafetchingapisclient.models.zing.ZingMusic;
import com.dreamdigitizers.androiddatafetchingapisclient.models.nct.NctSearchResult;
import com.dreamdigitizers.androiddatafetchingapisclient.models.zing.ZingSearchResult;

public interface IViewPlayback extends IViewRx {
    void onRxNctSearchNext(NctSearchResult pNctSearchResult);
    void onRxZingSearchNext(ZingSearchResult pZingSearchResult);
    void onRxNctFetchNext(NctMusic pNctMusic);
    void onRxZingFetchNext(ZingMusic pZingMusic);
}
