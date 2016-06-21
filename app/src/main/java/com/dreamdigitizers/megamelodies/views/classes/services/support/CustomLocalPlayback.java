package com.dreamdigitizers.megamelodies.views.classes.services.support;

import com.dreamdigitizers.androidbaselibrary.utilities.UtilsMediaPlayer;
import com.dreamdigitizers.androidbaselibrary.views.classes.services.ServiceMediaBrowser;
import com.dreamdigitizers.androidbaselibrary.views.classes.services.support.LocalPlayback;

public class CustomLocalPlayback extends LocalPlayback {
    private IOnMediaPlayerPreparedListener mListener;

    public CustomLocalPlayback(ServiceMediaBrowser pService, boolean pIsOnlineStreaming) {
        super(pService, pIsOnlineStreaming);
    }

    @Override
    public void onPrepared(UtilsMediaPlayer.CustomMediaPlayer pMediaPlayer) {
        if (this.mListener != null) {
            this.mListener.onPrepared();
        }
        super.onPrepared(pMediaPlayer);
    }

    public void setOnMediaPlayerPreparedListener(IOnMediaPlayerPreparedListener pListener) {
        this.mListener = pListener;
    }

    public interface IOnMediaPlayerPreparedListener {
        void onPrepared();
    }
}
