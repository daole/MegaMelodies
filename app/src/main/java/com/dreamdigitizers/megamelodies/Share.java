package com.dreamdigitizers.megamelodies;

import android.os.Bundle;

import com.dreamdigitizers.megamelodies.models.Track;

import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.WeakHashMap;

public class Share {
    public static final String SHARE_KEY__CURRENT_TRACK = "current_track";

    private static final Object dummy = new Object();
    private static final Bundle bundle = new Bundle();
    private static final Map<IOnDataChangedListener, Object> listeners = Collections.synchronizedMap(new WeakHashMap<IOnDataChangedListener, Object>());

    public static Track getCurrentTrack() {
        return (Track) Share.bundle.getSerializable(Share.SHARE_KEY__CURRENT_TRACK);
    }

    public static void setCurrentTrack(Track pTrack) {
        if (pTrack != null) {
            Track oldTrack = Share.getCurrentTrack();
            if (pTrack != oldTrack) {
                Share.bundle.putSerializable(Share.SHARE_KEY__CURRENT_TRACK, pTrack);
                for (Iterator<IOnDataChangedListener> iterator = Share.listeners.keySet().iterator(); iterator.hasNext();) {
                    IOnDataChangedListener listener = iterator.next();
                    listener.onCurrentTrackChanged(pTrack, oldTrack);
                }
            }
        }
    }

    public static void registerListener(IOnDataChangedListener pListener) {
        if (pListener != null && !Share.listeners.keySet().contains(pListener)) {
            Share.listeners.put(pListener, dummy);
        }
    }

    public static void unregisterListener(IOnDataChangedListener pListener) {
        if (pListener != null && Share.listeners.keySet().contains(pListener)) {
            Share.listeners.remove(pListener);
        }
    }

    public static void dispose() {
        Share.bundle.clear();
        Share.listeners.clear();
    }

    public interface IOnDataChangedListener {
        void onCurrentTrackChanged(Track pNewTrack, Track pOldTrack);
    }
}
