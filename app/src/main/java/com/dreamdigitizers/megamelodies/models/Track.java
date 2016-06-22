package com.dreamdigitizers.megamelodies.models;

import com.dreamdigitizers.androidbaselibrary.models.ModelBase;

import java.io.Serializable;

public class Track extends ModelBase {
    private Serializable mOriginalTrack;
    private boolean mIsFavorite;

    public Serializable getOriginalTrack() {
        return this.mOriginalTrack;
    }

    public void setOriginalTrack(Serializable pOriginalTrack) {
        this.mOriginalTrack = pOriginalTrack;
    }

    public boolean isFavorite() {
        return this.mIsFavorite;
    }

    public void setFavorite(boolean pIsFavorite) {
        this.mIsFavorite = pIsFavorite;
    }
}
