package com.dreamdigitizers.megamelodies.presenters.classes;

import com.dreamdigitizers.megamelodies.Constants;
import com.dreamdigitizers.megamelodies.presenters.interfaces.IPresenterPlaylist;
import com.dreamdigitizers.megamelodies.views.classes.services.ServicePlayback;
import com.dreamdigitizers.megamelodies.views.interfaces.IViewPlaylist;

class PresenterPlaylist extends PresenterTracks<IViewPlaylist> implements IPresenterPlaylist {
    public PresenterPlaylist(IViewPlaylist pView) {
        super(pView);
    }

    @Override
    protected String getMediaId() {
        return this.buildMediaId();
    }

    private String buildMediaId() {
        String mediaId = null;
        IViewPlaylist view = this.getView();
        if (view != null) {
            int playlistId = view.getPlaylistId();
            mediaId = String.format(ServicePlayback.MEDIA_ID__PLAYLIST, playlistId, this.mOffset, Constants.PAGE_SIZE);
        }
        return mediaId;
    }
}
