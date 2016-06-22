package com.dreamdigitizers.megamelodies.models.local.sqlite.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.dreamdigitizers.androiddatafetchingapisclient.models.zing.ZingSong;
import com.dreamdigitizers.megamelodies.models.Track;
import com.dreamdigitizers.megamelodies.models.local.sqlite.ContentProviderMegaMelodies;
import com.dreamdigitizers.megamelodies.models.local.sqlite.tables.TableZingSong;

import java.util.ArrayList;
import java.util.List;

public class HelperZingSong {
    public static Track retrieveById(Context pContext, String pId) {
        String selection = TableZingSong.COLUMN_NAME___ID + " = ?";
        String[] selectionArgs = new String[] { pId };
        Cursor cursor = pContext.getContentResolver().query(ContentProviderMegaMelodies.CONTENT_URI__ZING_SONG, null, selection, selectionArgs, null);
        List<Track> tracks = HelperZingSong.fetchData(cursor);
        if (tracks.isEmpty()) {
            return null;
        } else {
            return tracks.get(0);
        }
    }

    public static Uri insert(Context pContext, ZingSong pZingSong, boolean pIsFavorite) {
        ContentValues contentValues = HelperZingSong.buildContentValues(pZingSong, pIsFavorite);
        Uri uri = pContext.getContentResolver().insert(ContentProviderMegaMelodies.CONTENT_URI__ZING_SONG, contentValues);
        return uri;
    }

    public static void favorite(Context pContext, ZingSong pZingSong) {
        String id = pZingSong.getId();
        Track track = HelperZingSong.retrieveById(pContext, id);
        if (track == null) {
            HelperZingSong.insert(pContext, pZingSong, true);
        } else {
            ContentValues contentValues = HelperZingSong.buildContentValues(pZingSong, true);
            String selection = TableZingSong.COLUMN_NAME___ID + " = ?";
            String[] selectionArgs = new String[] { id };
            pContext.getContentResolver().update(ContentProviderMegaMelodies.CONTENT_URI__NCT_SONG, contentValues, selection, selectionArgs);
        }
    }

    public static void unfavorite(Context pContext, ZingSong pZingSong) {
        ContentValues contentValues = HelperZingSong.buildContentValues(pZingSong, false);
        String selection = TableZingSong.COLUMN_NAME___ID + " = ?";
        String[] selectionArgs = new String[] { pZingSong.getId() };
        pContext.getContentResolver().update(ContentProviderMegaMelodies.CONTENT_URI__NCT_SONG, contentValues, selection, selectionArgs);
    }

    public static ContentValues buildContentValues(ZingSong pZingSong, boolean pIsFavorite) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TableZingSong.COLUMN_NAME___ID, pZingSong.getId());
        contentValues.put(TableZingSong.COLUMN_NAME__NAME, pZingSong.getName());
        contentValues.put(TableZingSong.COLUMN_NAME__ARTIST, pZingSong.getArtist());
        contentValues.put(TableZingSong.COLUMN_NAME__IS_FAVORITE, pIsFavorite);
        return contentValues;
    }

    public static List<Track> fetchData(Cursor pCursor) {
        List<Track> list = new ArrayList<>();

        if (pCursor.moveToFirst()) {
            do {
                String id = pCursor.getString(pCursor.getColumnIndex(TableZingSong.COLUMN_NAME___ID));
                String name = pCursor.getString(pCursor.getColumnIndex(TableZingSong.COLUMN_NAME__NAME));
                String artist = pCursor.getString(pCursor.getColumnIndex(TableZingSong.COLUMN_NAME__ARTIST));
                int isFavorite = pCursor.getInt(pCursor.getColumnIndex(TableZingSong.COLUMN_NAME__IS_FAVORITE));

                ZingSong zingSong = new ZingSong();
                zingSong.setId(id);
                zingSong.setName(name);
                zingSong.setArtist(artist);

                Track track = new Track();
                track.setOriginalTrack(zingSong);
                track.setFavorite(isFavorite > 0);

                list.add(track);
            } while (pCursor.moveToNext());
        }

        return list;
    }
}
