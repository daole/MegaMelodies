package com.dreamdigitizers.megamelodies.views.classes.services;

import android.os.Bundle;
import android.support.v4.media.MediaBrowserCompat;

import com.dreamdigitizers.androidbaselibrary.views.classes.services.ServiceMediaBrowser;
import com.dreamdigitizers.androidbaselibrary.views.classes.services.support.MediaPlayerNotificationReceiver;

import java.util.List;

public class ServicePlayback extends ServiceMediaBrowser {
    @Override
    protected boolean isOnlineStreaming() {
        return true;
    }

    @Override
    protected MediaPlayerNotificationReceiver createMediaPlayerNotificationReceiver() {
        return null;
    }

    @Override
    public BrowserRoot onGetRoot(String clientPackageName, int clientUid, Bundle rootHints) {
        return null;
    }

    @Override
    public void onLoadChildren(String parentId, Result<List<MediaBrowserCompat.MediaItem>> result) {

    }
}
