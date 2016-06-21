package com.dreamdigitizers.megamelodies.models.local.sqlite;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import com.dreamdigitizers.androidbaselibrary.models.local.sqlite.ContentProviderBase;
import com.dreamdigitizers.androidbaselibrary.models.local.sqlite.dal.DalBase;
import com.dreamdigitizers.androidbaselibrary.models.local.sqlite.tables.TableBase;
import com.dreamdigitizers.androidbaselibrary.utilities.UtilsString;
import com.dreamdigitizers.megamelodies.models.local.sqlite.dal.DalFavorite;
import com.dreamdigitizers.megamelodies.models.local.sqlite.dal.DalNctSinger;
import com.dreamdigitizers.megamelodies.models.local.sqlite.dal.DalNctSong;
import com.dreamdigitizers.megamelodies.models.local.sqlite.dal.DalPlaylist;
import com.dreamdigitizers.megamelodies.models.local.sqlite.dal.DalPlaylistSong;
import com.dreamdigitizers.megamelodies.models.local.sqlite.dal.DalZingSong;
import com.dreamdigitizers.megamelodies.models.local.sqlite.helpers.HelperSQLiteOpen;
import com.dreamdigitizers.megamelodies.models.local.sqlite.tables.TableFavorite;
import com.dreamdigitizers.megamelodies.models.local.sqlite.tables.TableNctSinger;
import com.dreamdigitizers.megamelodies.models.local.sqlite.tables.TableNctSong;
import com.dreamdigitizers.megamelodies.models.local.sqlite.tables.TablePlaylist;
import com.dreamdigitizers.megamelodies.models.local.sqlite.tables.TablePlaylistSong;
import com.dreamdigitizers.megamelodies.models.local.sqlite.tables.TableZingSong;

public class ContentProviderMegaMelodies extends ContentProviderBase {
    private static final String ERROR_MESSAGE__UNKNOWN_URI = "Unknown Uri: %s";

    private static final String DATABASE__NAME = "mega_melodies.db";
    private static final int DATABASE__VERSION = 1;

    private static final int FAVORITES = 0;
    private static final int FAVORITE = 1;
    private static final int NCT_SINGERS = 10;
    private static final int NCT_SINGER = 11;
    private static final int NCT_SONGS = 20;
    private static final int NCT_SONG = 21;
    private static final int PLAYLISTS = 30;
    private static final int PLAYLIST = 31;
    private static final int PLAYLIST_SONGS = 40;
    private static final int PLAYLIST_SONG = 41;
    private static final int ZING_SONGS = 50;
    private static final int ZING_SONG = 51;

    private static final String SCHEME = "content://";

    public static final String AUTHORITY = "com.dreamdigitizers.megamelodies.ContentProviderMegaMelodies";

    public static final Uri CONTENT_URI__FAVORITE = Uri.parse(ContentProviderMegaMelodies.SCHEME + ContentProviderMegaMelodies.AUTHORITY + "/" + TableFavorite.TABLE_NAME);
    public static final Uri CONTENT_URI__NCT_SINGER = Uri.parse(ContentProviderMegaMelodies.SCHEME + ContentProviderMegaMelodies.AUTHORITY + "/" + TableNctSinger.TABLE_NAME);
    public static final Uri CONTENT_URI__NCT_SONG = Uri.parse(ContentProviderMegaMelodies.SCHEME + ContentProviderMegaMelodies.AUTHORITY + "/" + TableNctSong.TABLE_NAME);
    public static final Uri CONTENT_URI__PLAYLIST = Uri.parse(ContentProviderMegaMelodies.SCHEME + ContentProviderMegaMelodies.AUTHORITY + "/" + TablePlaylist.TABLE_NAME);
    public static final Uri CONTENT_URI__PLAYLIST_SONG = Uri.parse(ContentProviderMegaMelodies.SCHEME + ContentProviderMegaMelodies.AUTHORITY + "/" + TablePlaylistSong.TABLE_NAME);
    public static final Uri CONTENT_URI__ZING_SONG = Uri.parse(ContentProviderMegaMelodies.SCHEME + ContentProviderMegaMelodies.AUTHORITY + "/" + TableZingSong.TABLE_NAME);

    public static final String CONTENT_TYPE__FAVORITE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + ContentProviderMegaMelodies.AUTHORITY + "." + TableFavorite.TABLE_NAME;
    public static final String CONTENT_ITEM_TYPE__FAVORITE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + ContentProviderMegaMelodies.AUTHORITY + "." + TableFavorite.TABLE_NAME;

    public static final String CONTENT_TYPE__NCT_SINGER = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + ContentProviderMegaMelodies.AUTHORITY + "." + TableNctSinger.TABLE_NAME;
    public static final String CONTENT_ITEM_TYPE__NCT_SINGER = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + ContentProviderMegaMelodies.AUTHORITY + "." + TableNctSinger.TABLE_NAME;

    public static final String CONTENT_TYPE__NCT_SONG = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + ContentProviderMegaMelodies.AUTHORITY + "." + TableNctSong.TABLE_NAME;
    public static final String CONTENT_ITEM_TYPE__NCT_SONG = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + ContentProviderMegaMelodies.AUTHORITY + "." + TableNctSong.TABLE_NAME;

    public static final String CONTENT_TYPE__PLAYLIST = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + ContentProviderMegaMelodies.AUTHORITY + "." + TablePlaylist.TABLE_NAME;
    public static final String CONTENT_ITEM_TYPE__PLAYLIST = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + ContentProviderMegaMelodies.AUTHORITY + "." + TablePlaylist.TABLE_NAME;

    public static final String CONTENT_TYPE__PLAYLIST_SONG = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + ContentProviderMegaMelodies.AUTHORITY + "." + TablePlaylistSong.TABLE_NAME;
    public static final String CONTENT_ITEM_TYPE__PLAYLIST_SONG = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + ContentProviderMegaMelodies.AUTHORITY + "." + TablePlaylistSong.TABLE_NAME;

    public static final String CONTENT_TYPE__ZING_SONG = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + ContentProviderMegaMelodies.AUTHORITY + "." + TableZingSong.TABLE_NAME;
    public static final String CONTENT_ITEM_TYPE__ZING_SONG = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + ContentProviderMegaMelodies.AUTHORITY + "." + TableZingSong.TABLE_NAME;

    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        ContentProviderMegaMelodies.uriMatcher.addURI(ContentProviderMegaMelodies.AUTHORITY, TableFavorite.TABLE_NAME, ContentProviderMegaMelodies.FAVORITES);
        ContentProviderMegaMelodies.uriMatcher.addURI(ContentProviderMegaMelodies.AUTHORITY, TableFavorite.TABLE_NAME + "/#", ContentProviderMegaMelodies.FAVORITE);

        ContentProviderMegaMelodies.uriMatcher.addURI(ContentProviderMegaMelodies.AUTHORITY, TableNctSinger.TABLE_NAME, ContentProviderMegaMelodies.NCT_SINGERS);
        ContentProviderMegaMelodies.uriMatcher.addURI(ContentProviderMegaMelodies.AUTHORITY, TableNctSinger.TABLE_NAME + "/#", ContentProviderMegaMelodies.NCT_SINGER);

        ContentProviderMegaMelodies.uriMatcher.addURI(ContentProviderMegaMelodies.AUTHORITY, TableNctSong.TABLE_NAME, ContentProviderMegaMelodies.NCT_SONGS);
        ContentProviderMegaMelodies.uriMatcher.addURI(ContentProviderMegaMelodies.AUTHORITY, TableNctSong.TABLE_NAME + "/#", ContentProviderMegaMelodies.NCT_SONG);

        ContentProviderMegaMelodies.uriMatcher.addURI(ContentProviderMegaMelodies.AUTHORITY, TablePlaylist.TABLE_NAME, ContentProviderMegaMelodies.PLAYLISTS);
        ContentProviderMegaMelodies.uriMatcher.addURI(ContentProviderMegaMelodies.AUTHORITY, TablePlaylist.TABLE_NAME + "/#", ContentProviderMegaMelodies.PLAYLIST);

        ContentProviderMegaMelodies.uriMatcher.addURI(ContentProviderMegaMelodies.AUTHORITY, TablePlaylistSong.TABLE_NAME, ContentProviderMegaMelodies.PLAYLIST_SONGS);
        ContentProviderMegaMelodies.uriMatcher.addURI(ContentProviderMegaMelodies.AUTHORITY, TablePlaylistSong.TABLE_NAME + "/#", ContentProviderMegaMelodies.PLAYLIST_SONG);

        ContentProviderMegaMelodies.uriMatcher.addURI(ContentProviderMegaMelodies.AUTHORITY, TableZingSong.TABLE_NAME, ContentProviderMegaMelodies.ZING_SONGS);
        ContentProviderMegaMelodies.uriMatcher.addURI(ContentProviderMegaMelodies.AUTHORITY, TableZingSong.TABLE_NAME + "/#", ContentProviderMegaMelodies.ZING_SONG);
    }

    @Override
    protected SQLiteOpenHelper createSQLiteOpenHelper() {
        return new HelperSQLiteOpen(this.getContext(), ContentProviderMegaMelodies.DATABASE__NAME, null, ContentProviderMegaMelodies.DATABASE__VERSION);
    }

    @Override
    public String getType(Uri pUri) {
        String type;

        int uriType = ContentProviderMegaMelodies.uriMatcher.match(pUri);
        switch (uriType) {
            case ContentProviderMegaMelodies.FAVORITES:
                type = ContentProviderMegaMelodies.CONTENT_TYPE__FAVORITE;
                break;
            case ContentProviderMegaMelodies.FAVORITE:
                type = ContentProviderMegaMelodies.CONTENT_ITEM_TYPE__FAVORITE;
                break;
            case ContentProviderMegaMelodies.NCT_SINGERS:
                type = ContentProviderMegaMelodies.CONTENT_TYPE__NCT_SINGER;
                break;
            case ContentProviderMegaMelodies.NCT_SINGER:
                type = ContentProviderMegaMelodies.CONTENT_ITEM_TYPE__NCT_SINGER;
                break;
            case ContentProviderMegaMelodies.NCT_SONGS:
                type = ContentProviderMegaMelodies.CONTENT_TYPE__NCT_SONG;
                break;
            case ContentProviderMegaMelodies.NCT_SONG:
                type = ContentProviderMegaMelodies.CONTENT_ITEM_TYPE__NCT_SONG;
                break;
            case ContentProviderMegaMelodies.PLAYLISTS:
                type = ContentProviderMegaMelodies.CONTENT_TYPE__PLAYLIST;
                break;
            case ContentProviderMegaMelodies.PLAYLIST:
                type = ContentProviderMegaMelodies.CONTENT_ITEM_TYPE__PLAYLIST;
                break;
            case ContentProviderMegaMelodies.PLAYLIST_SONGS:
                type = ContentProviderMegaMelodies.CONTENT_TYPE__PLAYLIST_SONG;
                break;
            case ContentProviderMegaMelodies.PLAYLIST_SONG:
                type = ContentProviderMegaMelodies.CONTENT_ITEM_TYPE__PLAYLIST_SONG;
                break;
            case ContentProviderMegaMelodies.ZING_SONGS:
                type = ContentProviderMegaMelodies.CONTENT_TYPE__ZING_SONG;
                break;
            case ContentProviderMegaMelodies.ZING_SONG:
                type = ContentProviderMegaMelodies.CONTENT_ITEM_TYPE__ZING_SONG;
                break;
            default:
                throw new IllegalArgumentException(String.format(ContentProviderMegaMelodies.ERROR_MESSAGE__UNKNOWN_URI, pUri));
        }

        return type;
    }

    @Override
    public Cursor query(Uri pUri, String[] pProjection, String pSelection, String[] pSelectionArgs, String pSortOrder) {
        Cursor cursor = null;
        String id = null;
        DalBase dalBase;

        int uriType = ContentProviderMegaMelodies.uriMatcher.match(pUri);
        switch (uriType) {
            case ContentProviderMegaMelodies.FAVORITE:
                id = pUri.getLastPathSegment();
            case ContentProviderMegaMelodies.FAVORITES:
                dalBase = new DalFavorite(this.mHelperSQLiteDatabase);
                break;
            case ContentProviderMegaMelodies.NCT_SINGER:
                id = pUri.getLastPathSegment();
            case ContentProviderMegaMelodies.NCT_SINGERS:
                dalBase = new DalNctSinger(this.mHelperSQLiteDatabase);
                break;
            case ContentProviderMegaMelodies.NCT_SONG:
                id = pUri.getLastPathSegment();
            case ContentProviderMegaMelodies.NCT_SONGS:
                dalBase = new DalNctSong(this.mHelperSQLiteDatabase);
                break;
            case ContentProviderMegaMelodies.PLAYLIST:
                id = pUri.getLastPathSegment();
            case ContentProviderMegaMelodies.PLAYLISTS:
                dalBase = new DalPlaylist(this.mHelperSQLiteDatabase);
                break;
            case ContentProviderMegaMelodies.PLAYLIST_SONG:
                id = pUri.getLastPathSegment();
            case ContentProviderMegaMelodies.PLAYLIST_SONGS:
                dalBase = new DalPlaylistSong(this.mHelperSQLiteDatabase);
                break;
            case ContentProviderMegaMelodies.ZING_SONG:
                id = pUri.getLastPathSegment();
            case ContentProviderMegaMelodies.ZING_SONGS:
                dalBase = new DalZingSong(this.mHelperSQLiteDatabase);
                break;
            default:
                throw new IllegalArgumentException(String.format(ContentProviderMegaMelodies.ERROR_MESSAGE__UNKNOWN_URI, pUri));
        }

        if (dalBase != null) {
            if (id != null) {
                if (UtilsString.isEmpty(pSelection)) {
                    pSelection = "";
                } else {
                    pSelection += " and ";
                }
                pSelection += dalBase.getTableName() + "." + TableBase.COLUMN_NAME___ID + " = " + id;
            }
            cursor = dalBase.select(pProjection, pSelection, pSelectionArgs, null, null, pSortOrder);
            cursor.setNotificationUri(this.getContext().getContentResolver(), pUri);
        }

        return cursor;
    }

    @Override
    public Uri insert(Uri pUri, ContentValues pValues) {
        Uri newDataUri = null;
        DalBase DalBase;

        int uriType = ContentProviderMegaMelodies.uriMatcher.match(pUri);
        switch (uriType) {
            case ContentProviderMegaMelodies.FAVORITES:
                DalBase = new DalFavorite(this.mHelperSQLiteDatabase);
                break;
            case ContentProviderMegaMelodies.NCT_SINGERS:
                DalBase = new DalNctSinger(this.mHelperSQLiteDatabase);
                break;
            case ContentProviderMegaMelodies.NCT_SONGS:
                DalBase = new DalNctSong(this.mHelperSQLiteDatabase);
                break;
            case ContentProviderMegaMelodies.PLAYLISTS:
                DalBase = new DalPlaylist(this.mHelperSQLiteDatabase);
                break;
            case ContentProviderMegaMelodies.PLAYLIST_SONGS:
                DalBase = new DalPlaylistSong(this.mHelperSQLiteDatabase);
                break;
            case ContentProviderMegaMelodies.ZING_SONGS:
                DalBase = new DalZingSong(this.mHelperSQLiteDatabase);
                break;
            default:
                throw new IllegalArgumentException(String.format(ContentProviderMegaMelodies.ERROR_MESSAGE__UNKNOWN_URI, pUri));
        }

        if (DalBase != null) {
            long newId = DalBase.insert(pValues);
            newDataUri = ContentUris.withAppendedId(pUri, newId);
            this.getContext().getContentResolver().notifyChange(pUri, null);
        }

        return newDataUri;
    }

    @Override
    public int update(Uri pUri, ContentValues pValues, String pSelection, String[] pSelectionArgs) {
        int affectedRows = 0;
        String id = null;
        DalBase dalBase;

        int uriType = ContentProviderMegaMelodies.uriMatcher.match(pUri);
        switch (uriType) {
            case ContentProviderMegaMelodies.FAVORITE:
                id = pUri.getLastPathSegment();
            case ContentProviderMegaMelodies.FAVORITES:
                dalBase = new DalFavorite(this.mHelperSQLiteDatabase);
                break;
            case ContentProviderMegaMelodies.NCT_SINGER:
                id = pUri.getLastPathSegment();
            case ContentProviderMegaMelodies.NCT_SINGERS:
                dalBase = new DalNctSinger(this.mHelperSQLiteDatabase);
                break;
            case ContentProviderMegaMelodies.NCT_SONG:
                id = pUri.getLastPathSegment();
            case ContentProviderMegaMelodies.NCT_SONGS:
                dalBase = new DalNctSong(this.mHelperSQLiteDatabase);
                break;
            case ContentProviderMegaMelodies.PLAYLIST:
                id = pUri.getLastPathSegment();
            case ContentProviderMegaMelodies.PLAYLISTS:
                dalBase = new DalPlaylist(this.mHelperSQLiteDatabase);
                break;
            case ContentProviderMegaMelodies.PLAYLIST_SONG:
                id = pUri.getLastPathSegment();
            case ContentProviderMegaMelodies.PLAYLIST_SONGS:
                dalBase = new DalPlaylistSong(this.mHelperSQLiteDatabase);
                break;
            case ContentProviderMegaMelodies.ZING_SONG:
                id = pUri.getLastPathSegment();
            case ContentProviderMegaMelodies.ZING_SONGS:
                dalBase = new DalZingSong(this.mHelperSQLiteDatabase);
                break;
            default:
                throw new IllegalArgumentException(String.format(ContentProviderMegaMelodies.ERROR_MESSAGE__UNKNOWN_URI, pUri));
        }

        if (dalBase != null) {
            if (id != null) {
                if (UtilsString.isEmpty(pSelection)) {
                    pSelection = "";
                } else {
                    pSelection += " and ";
                }
                pSelection += TableBase.COLUMN_NAME___ID + " = " + id;
            }
            affectedRows = dalBase.update(pValues, pSelection, pSelectionArgs);
            this.getContext().getContentResolver().notifyChange(pUri, null);
        }

        return affectedRows;
    }

    @Override
    public int delete(Uri pUri, String pSelection, String[] pSelectionArgs) {
        int affectedRows = 0;
        String id = null;
        DalBase dalBase;

        int uriType = ContentProviderMegaMelodies.uriMatcher.match(pUri);
        switch (uriType) {
            case ContentProviderMegaMelodies.FAVORITE:
                id = pUri.getLastPathSegment();
            case ContentProviderMegaMelodies.FAVORITES:
                dalBase = new DalFavorite(this.mHelperSQLiteDatabase);
                break;
            case ContentProviderMegaMelodies.NCT_SINGER:
                id = pUri.getLastPathSegment();
            case ContentProviderMegaMelodies.NCT_SINGERS:
                dalBase = new DalNctSinger(this.mHelperSQLiteDatabase);
                break;
            case ContentProviderMegaMelodies.NCT_SONG:
                id = pUri.getLastPathSegment();
            case ContentProviderMegaMelodies.NCT_SONGS:
                dalBase = new DalNctSong(this.mHelperSQLiteDatabase);
                break;
            case ContentProviderMegaMelodies.PLAYLIST:
                id = pUri.getLastPathSegment();
            case ContentProviderMegaMelodies.PLAYLISTS:
                dalBase = new DalPlaylist(this.mHelperSQLiteDatabase);
                break;
            case ContentProviderMegaMelodies.PLAYLIST_SONG:
                id = pUri.getLastPathSegment();
            case ContentProviderMegaMelodies.PLAYLIST_SONGS:
                dalBase = new DalPlaylistSong(this.mHelperSQLiteDatabase);
                break;
            case ContentProviderMegaMelodies.ZING_SONG:
                id = pUri.getLastPathSegment();
            case ContentProviderMegaMelodies.ZING_SONGS:
                dalBase = new DalZingSong(this.mHelperSQLiteDatabase);
                break;
            default:
                throw new IllegalArgumentException(String.format(ContentProviderMegaMelodies.ERROR_MESSAGE__UNKNOWN_URI, pUri));
        }

        if (dalBase != null) {
            if (id != null) {
                if (UtilsString.isEmpty(pSelection)) {
                    pSelection = "";
                } else {
                    pSelection += " and ";
                }
                pSelection += TableBase.COLUMN_NAME___ID + " = " + id;
            }
            affectedRows = dalBase.delete(pSelection, pSelectionArgs);
            this.getContext().getContentResolver().notifyChange(pUri, null);
        }

        return affectedRows;
    }
}
