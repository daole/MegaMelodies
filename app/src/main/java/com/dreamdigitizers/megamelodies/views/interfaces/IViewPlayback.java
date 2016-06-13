package com.dreamdigitizers.megamelodies.views.interfaces;

import com.dreamdigitizers.androiddatafetchingapisclient.models.nct.MusicNct;
import com.dreamdigitizers.androiddatafetchingapisclient.models.zing.MusicZing;
import com.dreamdigitizers.androiddatafetchingapisclient.models.nct.NctSearchResult;
import com.dreamdigitizers.androiddatafetchingapisclient.models.zing.ZingSearchResult;

public interface IViewPlayback extends IViewRx {
    void onRxNctSearchNext(NctSearchResult pNctSearchResult);
    void onRxZingSearchNext(ZingSearchResult pZingSearchResult);
    void onRxNctFetchNext(MusicNct pMusicNct);
    void onRxZingFetchNext(MusicZing pMusicZing);
}
