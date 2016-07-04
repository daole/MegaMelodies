package com.dreamdigitizers.megamelodies.presenters.classes;

import android.net.Uri;
import android.support.v4.media.MediaBrowserCompat;

import com.dreamdigitizers.megamelodies.Constants;
import com.dreamdigitizers.megamelodies.models.Track;
import com.dreamdigitizers.megamelodies.presenters.interfaces.IPresenterFavorites;
import com.dreamdigitizers.megamelodies.views.classes.services.ServicePlayback;
import com.dreamdigitizers.megamelodies.views.classes.services.support.MediaMetadataBuilder;
import com.dreamdigitizers.megamelodies.views.interfaces.IViewFavorites;

import java.util.HashMap;

class PresenterFavorites extends PresenterTracks<IViewFavorites> implements IPresenterFavorites {
    public PresenterFavorites(IViewFavorites pView) {
        super(pView);
    }

    @Override
    protected void handleFavoriteEvent(Uri pUri) {
        String id = pUri.getQueryParameter("trackId");
        boolean userFavorite = Boolean.parseBoolean(pUri.getQueryParameter("userFavorite"));
        HashMap transactions = this.mTransactionActions.get(ServicePlayback.CUSTOM_ACTION__FAVORITE);
        MediaBrowserCompat.MediaItem mediaItem = (MediaBrowserCompat.MediaItem) transactions.get(id);
        Track track = (Track) mediaItem.getDescription().getExtras().getSerializable(MediaMetadataBuilder.BUNDLE_KEY__TRACK);
        track.setFavorite(userFavorite);
        transactions.remove(id);
        IViewFavorites view = this.getView();
        if (view != null) {
            if (userFavorite) {
                // Should not happen
                view.updateState();
            } else {
                view.removeMediaItem(mediaItem);
            }
        }
    }

    @Override
    protected String getMediaId() {
        return this.buildMediaId();
    }

    private String buildMediaId() {
        return String.format(ServicePlayback.MEDIA_ID__FAVORITES, this.mOffset, Constants.PAGE_SIZE);
    }
}
