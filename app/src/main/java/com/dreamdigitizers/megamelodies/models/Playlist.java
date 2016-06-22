package com.dreamdigitizers.megamelodies.models;

import com.dreamdigitizers.androidbaselibrary.models.ModelBase;

import java.util.List;

public class Playlist extends ModelBase {
    private int mId;
    private String mName;
    private List<Track> mTracks;

    public int getId() {
        return this.mId;
    }

    public void setId(int pId) {
        this.mId = pId;
    }

    public String getName() {
        return this.mName;
    }

    public void setName(String pName) {
        this.mName = pName;
    }

    public List<Track> getTracks() {
        return this.mTracks;
    }

    public void setTracks(List<Track> pTracks) {
        this.mTracks = pTracks;
    }
}
