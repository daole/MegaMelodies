package com.dreamdigitizers.megamelodies.presenters.classes;

import com.dreamdigitizers.androidbaselibrary.presenters.classes.PresenterBase;
import com.dreamdigitizers.megamelodies.presenters.interfaces.IPresenterMain;
import com.dreamdigitizers.megamelodies.views.interfaces.IViewMain;

class PresenterMain extends PresenterBase<IViewMain> implements IPresenterMain {
    public PresenterMain(IViewMain pView) {
        super(pView);
    }
}
