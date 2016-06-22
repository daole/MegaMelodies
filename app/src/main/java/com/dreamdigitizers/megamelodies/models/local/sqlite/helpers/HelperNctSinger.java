package com.dreamdigitizers.megamelodies.models.local.sqlite.helpers;

import android.content.ContentValues;

import com.dreamdigitizers.androiddatafetchingapisclient.models.nct.NctSinger;
import com.dreamdigitizers.megamelodies.models.local.sqlite.tables.TableNctSinger;

public class HelperNctSinger {
    public static ContentValues buildContentValues(String pNctSongId, NctSinger pNctSinger) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TableNctSinger.COLUMN_NAME__NCT_SONG_ID, pNctSongId);
        contentValues.put(TableNctSinger.COLUMN_NAME__NAME, pNctSinger.getName());
        contentValues.put(TableNctSinger.COLUMN_NAME__IMG, pNctSinger.getImg());
        contentValues.put(TableNctSinger.COLUMN_NAME__URL, pNctSinger.getUrl());
        return  contentValues;
    }
}
