package com.dreamdigitizers.megamelodies.presenters.classes;

import com.dreamdigitizers.androidbaselibrary.presenters.classes.PresenterBase;
import com.dreamdigitizers.megamelodies.presenters.interfaces.IPresenterSearch;
import com.dreamdigitizers.megamelodies.views.interfaces.IViewSearch;

class PresenterSearch extends PresenterBase<IViewSearch> implements IPresenterSearch {
    public PresenterSearch(IViewSearch pView) {
        super(pView);
    }
}
