package com.dreamdigitizers.megamelodies.presenters.classes;

import com.dreamdigitizers.androidbaselibrary.presenters.classes.PresenterBase;
import com.dreamdigitizers.megamelodies.presenters.interfaces.IPresenterPlaylists;
import com.dreamdigitizers.megamelodies.views.interfaces.IViewPlaylists;

class PresenterPlaylists extends PresenterBase<IViewPlaylists> implements IPresenterPlaylists {
    public PresenterPlaylists(IViewPlaylists pView) {
        super(pView);
    }
}
