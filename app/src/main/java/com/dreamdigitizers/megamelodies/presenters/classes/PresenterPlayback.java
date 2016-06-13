package com.dreamdigitizers.megamelodies.presenters.classes;

import com.dreamdigitizers.megamelodies.presenters.interfaces.IPresenterPlayback;
import com.dreamdigitizers.megamelodies.views.interfaces.IViewPlayback;

class PresenterPlayback extends PresenterRx<IViewPlayback> implements IPresenterPlayback {
    public PresenterPlayback(IViewPlayback pView) {
        super(pView);
    }

    @Override
    public void dispose() {
        super.dispose();
        //this.unsubscribe();
    }
}
