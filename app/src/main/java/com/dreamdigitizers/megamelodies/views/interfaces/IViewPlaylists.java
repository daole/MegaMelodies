package com.dreamdigitizers.megamelodies.views.interfaces;

import android.support.v4.media.MediaBrowserCompat;

public interface IViewPlaylists extends IViewMediaItems {
    void removeMediaItem(MediaBrowserCompat.MediaItem pMediaItem);
}
