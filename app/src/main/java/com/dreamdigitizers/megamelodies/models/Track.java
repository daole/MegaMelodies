package com.dreamdigitizers.megamelodies.models;

import com.dreamdigitizers.androidbaselibrary.models.ModelBase;
import com.dreamdigitizers.androiddatafetchingapisclient.models.nct.NctSinger;
import com.dreamdigitizers.androiddatafetchingapisclient.models.nct.NctSong;
import com.dreamdigitizers.androiddatafetchingapisclient.models.zing.ZingSong;

import java.io.Serializable;

public class Track extends ModelBase {
    private Serializable mOriginalTrack;
    private boolean mIsFavorite;
    private String mId;
    private String mName;
    private String mArtist;

    public Serializable getOriginalTrack() {
        return this.mOriginalTrack;
    }

    public void setOriginalTrack(Serializable pOriginalTrack) {
        this.mOriginalTrack = pOriginalTrack;
        this.buildData();
    }

    public boolean isFavorite() {
        return this.mIsFavorite;
    }

    public void setFavorite(boolean pIsFavorite) {
        this.mIsFavorite = pIsFavorite;
    }

    public String getId() {
        return this.mId;
    }

    public String getName() {
        return this.mName;
    }

    public String getArtist() {
        return this.mArtist;
    }

    public void buildData() {
        if (this.mOriginalTrack instanceof NctSong) {
            NctSong nctSong = (NctSong) this.mOriginalTrack;
            this.mId = nctSong.getId();
            this.mName = nctSong.getName();
            StringBuilder stringBuilder = new StringBuilder();
            int i = 0;
            for (NctSinger nctSinger : nctSong.getSingers()) {
                if (i > 0) {
                    stringBuilder.append(", ");
                }
                stringBuilder.append(nctSinger.getName());
                i++;
            }
            this.mArtist = stringBuilder.toString();
        } else if (this.mOriginalTrack instanceof ZingSong) {
            ZingSong zingSong = (ZingSong) this.mOriginalTrack;
            this.mId = zingSong.getId();
            this.mName = zingSong.getName();
            this.mArtist = zingSong.getArtist();
        }
    }
}
