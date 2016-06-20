package com.dreamdigitizers.megamelodies.views.classes.fragments;

import com.dreamdigitizers.megamelodies.views.classes.support.AdapterMediaItem;
import com.dreamdigitizers.megamelodies.views.classes.support.AdapterPlaylist;

public class FragmentPlaylists extends FragmentMediaItems {
    @Override
    protected AdapterMediaItem createAdapter() {
        return new AdapterPlaylist(this.getContext());
    }
}
