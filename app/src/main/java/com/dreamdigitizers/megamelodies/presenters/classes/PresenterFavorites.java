package com.dreamdigitizers.megamelodies.presenters.classes;

import com.dreamdigitizers.androidbaselibrary.presenters.classes.PresenterBase;
import com.dreamdigitizers.megamelodies.presenters.interfaces.IPresenterFavorites;
import com.dreamdigitizers.megamelodies.views.interfaces.IViewFavorites;

class PresenterFavorites extends PresenterBase<IViewFavorites> implements IPresenterFavorites {
    public PresenterFavorites(IViewFavorites pView) {
        super(pView);
    }
}
