package com.dreamdigitizers.megamelodies.presenters.classes;

import com.dreamdigitizers.megamelodies.Constants;
import com.dreamdigitizers.megamelodies.presenters.interfaces.IPresenterSearch;
import com.dreamdigitizers.megamelodies.views.classes.services.ServicePlayback;
import com.dreamdigitizers.megamelodies.views.interfaces.IViewSearch;

class PresenterSearch extends PresenterTracks<IViewSearch> implements IPresenterSearch {
    public PresenterSearch(IViewSearch pView) {
        super(pView);
    }

    @Override
    protected boolean isLoadDataOnConnection() {
        return false;
    }

    @Override
    protected void load(String pMediaId) {
        this.connect();
        super.load(pMediaId);
    }

    @Override
    protected String getMediaId() {
        return this.buildMediaId();
    }

    @Override
    public void search() {
        IViewSearch view = this.getView();
        if (view != null) {
            this.mOffset = 0;
            view.showNetworkProgress();
            this.load(this.getMediaId());
        }
    }

    private String buildMediaId() {
        String mediaId = null;
        IViewSearch view = this.getView();
        if (view != null) {
            int serverId = view.getServerId();
            String query = view.getQuery();
            mediaId = String.format(ServicePlayback.MEDIA_ID__SEARCH, serverId, query, this.mOffset, Constants.PAGE_SIZE, Constants.ZING_TYPE, Constants.ZING_NUM);
        }
        return mediaId;
    }
}
