package com.dreamdigitizers.megamelodies.models.local.sqlite.helpers;

import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.net.Uri;
import android.os.RemoteException;

import com.dreamdigitizers.androiddatafetchingapisclient.models.nct.NctSinger;
import com.dreamdigitizers.androiddatafetchingapisclient.models.nct.NctSong;
import com.dreamdigitizers.androiddatafetchingapisclient.models.zing.ZingSong;
import com.dreamdigitizers.megamelodies.models.Playlist;
import com.dreamdigitizers.megamelodies.models.Track;
import com.dreamdigitizers.megamelodies.models.local.sqlite.ContentProviderMegaMelodies;
import com.dreamdigitizers.megamelodies.models.local.sqlite.tables.TableNctSinger;
import com.dreamdigitizers.megamelodies.models.local.sqlite.tables.TableNctSong;
import com.dreamdigitizers.megamelodies.models.local.sqlite.tables.TablePlaylist;
import com.dreamdigitizers.megamelodies.models.local.sqlite.tables.TablePlaylistSong;
import com.dreamdigitizers.megamelodies.models.local.sqlite.tables.TableZingSong;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HelperPlaylist {
    public static List<Playlist> retrieveAll(Context pContext) {
        String[] projection = new String[0];
        projection = TablePlaylist.getColumnsForJoin().toArray(projection);
        Cursor cursor = pContext.getContentResolver().query(ContentProviderMegaMelodies.CONTENT_URI__PLAYLIST, projection, null, null, null);
        List<Playlist> playlists = HelperPlaylist.fetchData(cursor);
        cursor.close();
        return playlists;
    }

    public static Uri insert(Context pContext, String pPlaylistName) {
        ContentValues contentValues = HelperPlaylist.buildContentValues(pPlaylistName);
        Uri uri = pContext.getContentResolver().insert(ContentProviderMegaMelodies.CONTENT_URI__PLAYLIST, contentValues);
        return uri;
    }

    public static Uri insert(Context pContext, Track pTrack, String pPlaylistName) {
        Uri uri = HelperPlaylist.insert(pContext, pPlaylistName);
        int id = Integer.parseInt(uri.getLastPathSegment());

        Serializable originalTrack = pTrack.getOriginalTrack();
        if (originalTrack instanceof NctSong) {
            NctSong nctSong = (NctSong) originalTrack;
            HelperPlaylistSong.insert(pContext, id, nctSong);
        } else if (originalTrack instanceof ZingSong) {
            ZingSong zingSong = (ZingSong) originalTrack;
            HelperPlaylistSong.insert(pContext, id, zingSong);
        }

        return uri;
    }

    public static ContentProviderResult[] delete(Context pContext, Playlist pPlaylist) {
        try {
            ArrayList<ContentProviderOperation> contentProviderOperations = new ArrayList<>();

            String selection = TablePlaylistSong.COLUMN_NAME__PLAYLIST_ID + " = ?";
            String[] selectionArgs = new String[]{Integer.toString(pPlaylist.getId())};
            contentProviderOperations.add(ContentProviderOperation.newDelete(ContentProviderMegaMelodies.CONTENT_URI__PLAYLIST_SONG)
                    .withSelection(selection, selectionArgs)
                    .build());

            selection = TablePlaylist.COLUMN_NAME___ID + " = ?";
            contentProviderOperations.add(ContentProviderOperation.newDelete(ContentProviderMegaMelodies.CONTENT_URI__PLAYLIST)
                    .withSelection(selection, selectionArgs)
                    .build());

            ContentProviderResult[] contentProviderResults;
            contentProviderResults = pContext.getContentResolver().applyBatch(ContentProviderMegaMelodies.AUTHORITY, contentProviderOperations);
            return contentProviderResults;
        } catch (RemoteException e) {
            throw new RuntimeException();
        } catch (OperationApplicationException e) {
            throw new RuntimeException();
        }
    }

    public static ContentValues buildContentValues(String pPlaylistName) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TablePlaylist.COLUMN_NAME__NAME, pPlaylistName);
        return contentValues;
    }

    public static List<Playlist> fetchData(Cursor pCursor) {
        HashMap<Integer, Playlist> playlistHashMap = new HashMap<>();
        HashMap<String, Track> trackHashMap = new HashMap<>();

        if (pCursor.moveToFirst()) {
            do {
                Playlist playlist;
                int id = pCursor.getInt(pCursor.getColumnIndex(TablePlaylist.COLUMN_NAME_ALIAS___ID));
                if (playlistHashMap.containsKey(id)) {
                    playlist = playlistHashMap.get(id);
                } else {
                    String name = pCursor.getString(pCursor.getColumnIndex(TablePlaylist.COLUMN_NAME_ALIAS__NAME));

                    playlist = new Playlist();
                    playlist.setId(id);
                    playlist.setName(name);
                    playlist.setTracks(new ArrayList<Track>());

                    playlistHashMap.put(id, playlist);
                }

                int nctSongIdColumnIndex = pCursor.getColumnIndex(TableNctSong.COLUMN_NAME_ALIAS___ID);
                if (!pCursor.isNull(nctSongIdColumnIndex)) {
                    Track track;
                    String nctSongId = pCursor.getString(nctSongIdColumnIndex);
                    if (trackHashMap.containsKey(nctSongId)) {
                        track = trackHashMap.get(nctSongId);
                    } else {
                        String name = pCursor.getString(pCursor.getColumnIndex(TableNctSong.COLUMN_NAME_ALIAS__NAME));
                        String url = pCursor.getString(pCursor.getColumnIndex(TableNctSong.COLUMN_NAME_ALIAS__URL));
                        int isFavorite = pCursor.getInt(pCursor.getColumnIndex(TableNctSong.COLUMN_NAME_ALIAS__IS_FAVORITE));

                        NctSong nctSong = new NctSong();
                        nctSong.setId(nctSongId);
                        nctSong.setName(name);
                        nctSong.setUrl(url);
                        nctSong.setSingers(new ArrayList<NctSinger>());

                        track = new Track();
                        track.setOriginalTrack(nctSong);
                        track.setFavorite(isFavorite > 0);

                        trackHashMap.put(nctSongId, track);

                        playlist.getTracks().add(track);
                    }

                    String name = pCursor.getString(pCursor.getColumnIndex(TableNctSinger.COLUMN_NAME_ALIAS__NAME));
                    String url = pCursor.getString(pCursor.getColumnIndex(TableNctSinger.COLUMN_NAME_ALIAS__URL));

                    NctSinger nctSinger = new NctSinger();
                    nctSinger.setName(name);
                    nctSinger.setUrl(url);

                    ((NctSong) track.getOriginalTrack()).getSingers().add(nctSinger);
                }

                int zingSongIdColumnIndex = pCursor.getColumnIndex(TableZingSong.COLUMN_NAME_ALIAS___ID);
                if (!pCursor.isNull(zingSongIdColumnIndex)) {
                    String zingSongId = pCursor.getString(zingSongIdColumnIndex);
                    String name = pCursor.getString(pCursor.getColumnIndex(TableZingSong.COLUMN_NAME_ALIAS__NAME));
                    String artist = pCursor.getString(pCursor.getColumnIndex(TableZingSong.COLUMN_NAME_ALIAS__ARTIST));
                    int isFavorite = pCursor.getInt(pCursor.getColumnIndex(TableZingSong.COLUMN_NAME_ALIAS__IS_FAVORITE));

                    ZingSong zingSong = new ZingSong();
                    zingSong.setId(zingSongId);
                    zingSong.setName(name);
                    zingSong.setArtist(artist);

                    Track track = new Track();
                    track.setOriginalTrack(zingSong);
                    track.setFavorite(isFavorite > 0);

                    playlist.getTracks().add(track);
                }
            } while (pCursor.moveToNext());
        }

        List<Playlist> playlists = new ArrayList<>(playlistHashMap.values());
        return playlists;
    }
}
