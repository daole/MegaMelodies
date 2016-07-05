package com.dreamdigitizers.megamelodies.presenters.classes;

import com.dreamdigitizers.androidbaselibrary.presenters.classes.PresenterBase;
import com.dreamdigitizers.megamelodies.presenters.interfaces.IPresenterWrapperPlaylists;
import com.dreamdigitizers.megamelodies.views.interfaces.IViewWrapperPlaylists;

class PresenterWrapperPlaylists extends PresenterBase<IViewWrapperPlaylists> implements IPresenterWrapperPlaylists {
    public PresenterWrapperPlaylists(IViewWrapperPlaylists pView) {
        super(pView);
    }
}
