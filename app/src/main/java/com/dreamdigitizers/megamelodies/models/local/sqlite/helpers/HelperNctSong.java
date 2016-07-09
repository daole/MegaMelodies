package com.dreamdigitizers.megamelodies.models.local.sqlite.helpers;

import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.os.RemoteException;

import com.dreamdigitizers.androiddatafetchingapisclient.models.nct.NctSinger;
import com.dreamdigitizers.androiddatafetchingapisclient.models.nct.NctSong;
import com.dreamdigitizers.megamelodies.models.Track;
import com.dreamdigitizers.megamelodies.models.local.sqlite.ContentProviderMegaMelodies;
import com.dreamdigitizers.megamelodies.models.local.sqlite.tables.TableNctSinger;
import com.dreamdigitizers.megamelodies.models.local.sqlite.tables.TableNctSong;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HelperNctSong {
    public static Track retrieveById(Context pContext, String pId) {
        String[] projection = new String[0];
        projection = TableNctSong.getColumnsForJoin().toArray(projection);
        String selection = TableNctSong.TABLE_NAME + "." + TableNctSong.COLUMN_NAME___ID + " = ?";
        String[] selectionArgs = new String[] { pId };
        Cursor cursor = pContext.getContentResolver().query(ContentProviderMegaMelodies.CONTENT_URI__NCT_SONG, projection, selection, selectionArgs, null);
        List<Track> tracks = HelperNctSong.fetchData(cursor);
        cursor.close();
        if (tracks.isEmpty()) {
            return null;
        } else {
            return tracks.get(0);
        }
    }

    public static List<Track> retrieveFavoriteTracks(Context pContext) {
        String[] projection = new String[0];
        projection = TableNctSong.getColumnsForJoin().toArray(projection);
        String selection = TableNctSong.TABLE_NAME + "." + TableNctSong.COLUMN_NAME__IS_FAVORITE + " > 0";
        Cursor cursor = pContext.getContentResolver().query(ContentProviderMegaMelodies.CONTENT_URI__NCT_SONG, projection, selection, null, null);
        List<Track> tracks = HelperNctSong.fetchData(cursor);
        cursor.close();
        return tracks;
    }

    public static ContentProviderResult[] insert(Context pContext, NctSong pNctSong, boolean pIsFavorite) {
        try {
            ArrayList<ContentProviderOperation> contentProviderOperations = new ArrayList<>();

            ContentValues contentValues = HelperNctSong.buildContentValues(pNctSong, pIsFavorite);
            contentProviderOperations.add(ContentProviderOperation.newInsert(ContentProviderMegaMelodies.CONTENT_URI__NCT_SONG)
                    .withValues(contentValues)
                    .build());

            for (NctSinger nctSinger : pNctSong.getSingers()) {
                contentValues = HelperNctSinger.buildContentValues(pNctSong.getId(), nctSinger);
                contentProviderOperations.add(ContentProviderOperation.newInsert(ContentProviderMegaMelodies.CONTENT_URI__NCT_SINGER)
                        .withValues(contentValues)
                        .build());
            }

            ContentProviderResult[] contentProviderResults;
            contentProviderResults = pContext.getContentResolver().applyBatch(ContentProviderMegaMelodies.AUTHORITY, contentProviderOperations);
            return contentProviderResults;
        } catch (RemoteException e) {
            throw new RuntimeException();
        } catch (OperationApplicationException e) {
            throw new RuntimeException();
        }
    }

    public static void favorite(Context pContext, NctSong pNctSong) {
        String id = pNctSong.getId();
        Track track = HelperNctSong.retrieveById(pContext, id);
        if (track == null) {
            HelperNctSong.insert(pContext, pNctSong, true);
        } else {
            ContentValues contentValues = HelperNctSong.buildContentValues(pNctSong, true);
            String selection = TableNctSong.COLUMN_NAME___ID + " = ?";
            String[] selectionArgs = new String[] { id };
            pContext.getContentResolver().update(ContentProviderMegaMelodies.CONTENT_URI__NCT_SONG, contentValues, selection, selectionArgs);
        }
    }

    public static void unfavorite(Context pContext, NctSong pNctSong) {
        ContentValues contentValues = HelperNctSong.buildContentValues(pNctSong, false);
        String selection = TableNctSong.COLUMN_NAME___ID + " = ?";
        String[] selectionArgs = new String[] { pNctSong.getId() };
        pContext.getContentResolver().update(ContentProviderMegaMelodies.CONTENT_URI__NCT_SONG, contentValues, selection, selectionArgs);

    }

    public static ContentValues buildContentValues(NctSong pNctSong, boolean pIsFavorite) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TableNctSong.COLUMN_NAME___ID, pNctSong.getId());
        contentValues.put(TableNctSong.COLUMN_NAME__NAME, pNctSong.getName());
        contentValues.put(TableNctSong.COLUMN_NAME__URL, pNctSong.getUrl());
        contentValues.put(TableNctSong.COLUMN_NAME__IS_FAVORITE, pIsFavorite);
        return contentValues;
    }

    public static List<Track> fetchData(Cursor pCursor) {
        HashMap<String, Track> hashMap = new HashMap<>();

        if (pCursor.moveToFirst()) {
            do {
                Track track;
                String id = pCursor.getString(pCursor.getColumnIndex(TableNctSong.COLUMN_NAME_ALIAS___ID));
                if (hashMap.containsKey(id)) {
                    track = hashMap.get(id);
                } else {
                    String name = pCursor.getString(pCursor.getColumnIndex(TableNctSong.COLUMN_NAME_ALIAS__NAME));
                    String url = pCursor.getString(pCursor.getColumnIndex(TableNctSong.COLUMN_NAME_ALIAS__URL));
                    int isFavorite = pCursor.getInt(pCursor.getColumnIndex(TableNctSong.COLUMN_NAME_ALIAS__IS_FAVORITE));

                    NctSong nctSong = new NctSong();
                    nctSong.setId(id);
                    nctSong.setName(name);
                    nctSong.setUrl(url);
                    nctSong.setSingers(new ArrayList<NctSinger>());

                    track = new Track();
                    track.setOriginalTrack(nctSong);
                    track.setFavorite(isFavorite > 0);

                    hashMap.put(id, track);
                }

                String name = pCursor.getString(pCursor.getColumnIndex(TableNctSinger.COLUMN_NAME_ALIAS__NAME));
                String url = pCursor.getString(pCursor.getColumnIndex(TableNctSinger.COLUMN_NAME_ALIAS__URL));

                NctSinger nctSinger = new NctSinger();
                nctSinger.setName(name);
                nctSinger.setUrl(url);

                ((NctSong) track.getOriginalTrack()).getSingers().add(nctSinger);
            } while (pCursor.moveToNext());
        }

        List<Track> tracks = new ArrayList<>(hashMap.values());
        for (Track track : tracks) {
            track.buildData();
        }
        return tracks;
    }
}
