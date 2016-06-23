package com.dreamdigitizers.megamelodies.presenters.classes;

import com.dreamdigitizers.megamelodies.Constants;
import com.dreamdigitizers.megamelodies.presenters.interfaces.IPresenterFavorites;
import com.dreamdigitizers.megamelodies.views.classes.services.ServicePlayback;
import com.dreamdigitizers.megamelodies.views.interfaces.IViewFavorites;

class PresenterFavorites extends PresenterTracks<IViewFavorites> implements IPresenterFavorites {
    public PresenterFavorites(IViewFavorites pView) {
        super(pView);
    }

    @Override
    protected String getMediaId() {
        return this.buildMediaId();
    }

    private String buildMediaId() {
        return String.format(ServicePlayback.MEDIA_ID__FAVORITES, this.mOffset, Constants.PAGE_SIZE);
    }
}
