package com.dreamdigitizers.megamelodies.presenters.classes;

import com.dreamdigitizers.androidbaselibrary.presenters.interfaces.IPresenterBase;
import com.dreamdigitizers.androidbaselibrary.views.interfaces.IViewBase;
import com.dreamdigitizers.megamelodies.presenters.interfaces.IPresenterFavorites;
import com.dreamdigitizers.megamelodies.presenters.interfaces.IPresenterMain;
import com.dreamdigitizers.megamelodies.presenters.interfaces.IPresenterPlayback;
import com.dreamdigitizers.megamelodies.presenters.interfaces.IPresenterPlaylist;
import com.dreamdigitizers.megamelodies.presenters.interfaces.IPresenterPlaylists;
import com.dreamdigitizers.megamelodies.presenters.interfaces.IPresenterSearch;
import com.dreamdigitizers.megamelodies.presenters.interfaces.IPresenterWrapperPlaylists;
import com.dreamdigitizers.megamelodies.views.interfaces.IViewFavorites;
import com.dreamdigitizers.megamelodies.views.interfaces.IViewMain;
import com.dreamdigitizers.megamelodies.views.interfaces.IViewPlayback;
import com.dreamdigitizers.megamelodies.views.interfaces.IViewPlaylist;
import com.dreamdigitizers.megamelodies.views.interfaces.IViewPlaylists;
import com.dreamdigitizers.megamelodies.views.interfaces.IViewSearch;
import com.dreamdigitizers.megamelodies.views.interfaces.IViewWrapperPlaylists;

public class PresenterFactory {
    public static IPresenterBase createPresenter(Class pPresenterClass, IViewBase pView) {
        if (pPresenterClass.isAssignableFrom(IPresenterPlayback.class)) {
            return new PresenterPlayback((IViewPlayback) pView);
        }

        if (pPresenterClass.isAssignableFrom(IPresenterMain.class)) {
            return new PresenterMain((IViewMain) pView);
        }

        if (pPresenterClass.isAssignableFrom(IPresenterSearch.class)) {
            return new PresenterSearch((IViewSearch) pView);
        }

        if (pPresenterClass.isAssignableFrom(IPresenterFavorites.class)) {
            return new PresenterFavorites((IViewFavorites) pView);
        }

        if (pPresenterClass.isAssignableFrom(IPresenterWrapperPlaylists.class)) {
            return new PresenterWrapperPlaylists((IViewWrapperPlaylists) pView);
        }

        if (pPresenterClass.isAssignableFrom(IPresenterPlaylists.class)) {
            return new PresenterPlaylists((IViewPlaylists) pView);
        }

        if (pPresenterClass.isAssignableFrom(IPresenterPlaylist.class)) {
            return new PresenterPlaylist((IViewPlaylist) pView);
        }

        throw new RuntimeException("There is no such Presenter class.");
    }
}
