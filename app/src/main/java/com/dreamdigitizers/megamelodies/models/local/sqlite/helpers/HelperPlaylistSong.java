package com.dreamdigitizers.megamelodies.models.local.sqlite.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;

import com.dreamdigitizers.androiddatafetchingapisclient.models.nct.NctSong;
import com.dreamdigitizers.androiddatafetchingapisclient.models.zing.ZingSong;
import com.dreamdigitizers.megamelodies.models.Track;
import com.dreamdigitizers.megamelodies.models.local.sqlite.ContentProviderMegaMelodies;
import com.dreamdigitizers.megamelodies.models.local.sqlite.tables.TablePlaylistSong;

public class HelperPlaylistSong {
    public static Uri insert(Context pContext, int pPlaylistId, NctSong pNctSong) {
        String id = pNctSong.getId();
        Track track = HelperNctSong.retrieveById(pContext, id);
        if (track == null) {
            HelperNctSong.insert(pContext, pNctSong, false);
        }

        ContentValues contentValues = HelperPlaylistSong.buildContentValues(pPlaylistId, pNctSong);
        Uri uri = pContext.getContentResolver().insert(ContentProviderMegaMelodies.CONTENT_URI__PLAYLIST_SONG, contentValues);
        return uri;
    }

    public static Uri insert(Context pContext, int pPlaylistId, ZingSong pZingSong) {
        String id = pZingSong.getId();
        Track track = HelperZingSong.retrieveById(pContext, id);
        if (track == null) {
            HelperZingSong.insert(pContext, pZingSong, false);
        }

        ContentValues contentValues = HelperPlaylistSong.buildContentValues(pPlaylistId, pZingSong);
        Uri uri = pContext.getContentResolver().insert(ContentProviderMegaMelodies.CONTENT_URI__PLAYLIST_SONG, contentValues);
        return uri;
    }

    public static int delete(Context pContext, int pPlaylistId, NctSong pNctSong) {
        String selection = TablePlaylistSong.COLUMN_NAME__PLAYLIST_ID + " = ? and " + TablePlaylistSong.COLUMN_NAME__NCT_SONG_ID + " = ?";
        String[] selectionArgs = new String[] { Integer.toString(pPlaylistId), pNctSong.getId() };
        int affectedRows = pContext.getContentResolver().delete(ContentProviderMegaMelodies.CONTENT_URI__PLAYLIST_SONG, selection, selectionArgs);
        return affectedRows;
    }

    public static int delete(Context pContext, int pPlaylistId, ZingSong pZingSong) {
        String selection = TablePlaylistSong.COLUMN_NAME__PLAYLIST_ID + " = ? and " + TablePlaylistSong.COLUMN_NAME__ZING_SONG_ID + " = ?";
        String[] selectionArgs = new String[] { Integer.toString(pPlaylistId), pZingSong.getId() };
        int affectedRows = pContext.getContentResolver().delete(ContentProviderMegaMelodies.CONTENT_URI__PLAYLIST_SONG, selection, selectionArgs);
        return affectedRows;
    }

    public static ContentValues buildContentValues(int pPlaylistId, NctSong pNctSong) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TablePlaylistSong.COLUMN_NAME__PLAYLIST_ID, pPlaylistId);
        contentValues.put(TablePlaylistSong.COLUMN_NAME__NCT_SONG_ID, pNctSong.getId());
        return contentValues;
    }

    public static ContentValues buildContentValues(int pPlaylistId, ZingSong pZingSong) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TablePlaylistSong.COLUMN_NAME__PLAYLIST_ID, pPlaylistId);
        contentValues.put(TablePlaylistSong.COLUMN_NAME__ZING_SONG_ID, pZingSong.getId());
        return  contentValues;
    }
}
