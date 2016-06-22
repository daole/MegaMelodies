package com.dreamdigitizers.megamelodies.models.local.sqlite.tables;

import android.database.sqlite.SQLiteDatabase;

import com.dreamdigitizers.androidbaselibrary.models.local.sqlite.tables.TableBase;

import java.util.ArrayList;
import java.util.List;

public class TablePlaylistSong extends TableBase {
    public static final String TABLE_NAME = "playlist_song";

    public static final String COLUMN_NAME__PLAYLIST_ID = "playlist_id";
    public static final String COLUMN_NAME__NCT_SONG_ID = "nct_song_id";
    public static final String COLUMN_NAME__ZING_SONG_ID = "zing_song_id";

    public static final String COLUMN_NAME_ALIAS___ID = TablePlaylistSong.TABLE_NAME + "_" + TablePlaylistSong.COLUMN_NAME___ID;
    public static final String COLUMN_NAME_ALIAS__PLAYLIST_ID = TablePlaylistSong.TABLE_NAME + "_" + TablePlaylistSong.COLUMN_NAME__PLAYLIST_ID;
    public static final String COLUMN_NAME_ALIAS__NCT_SONG_ID = TablePlaylistSong.TABLE_NAME + "_" + TablePlaylistSong.COLUMN_NAME__NCT_SONG_ID;
    public static final String COLUMN_NAME_ALIAS__ZING_SONG_ID = TablePlaylistSong.TABLE_NAME + "_" + TablePlaylistSong.COLUMN_NAME__ZING_SONG_ID;

    private static final String STATEMENT_CREATE = "CREATE TABLE `" + TablePlaylistSong.TABLE_NAME + "` ("
            + "`" + TablePlaylistSong.COLUMN_NAME___ID + "` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,"
            + "`" + TablePlaylistSong.COLUMN_NAME__PLAYLIST_ID + "` INTEGER NOT NULL,"
            + "`" + TablePlaylistSong.COLUMN_NAME__NCT_SONG_ID + "` TEXT,"
            + "`" + TablePlaylistSong.COLUMN_NAME__ZING_SONG_ID + "` TEXT,"
            + "UNIQUE (`" + TablePlaylistSong.COLUMN_NAME__PLAYLIST_ID + "`, `" + TablePlaylistSong.COLUMN_NAME__NCT_SONG_ID + "`),"
            + "UNIQUE (`" + TablePlaylistSong.COLUMN_NAME__PLAYLIST_ID + "`, `" + TablePlaylistSong.COLUMN_NAME__ZING_SONG_ID + "`)"
            + ");";

    private static final String STATEMENT_UPGRADE = "";

    public static List<String> getColumns() {
        return TablePlaylistSong.getColumns(false, true);
    }

    public static List<String> getColumns(boolean pIsIncludeTableName, boolean pIsIncludeIdColumn) {
        List<String> columns = new ArrayList<>();

        String tableName = "";
        if(pIsIncludeTableName) {
            tableName = TablePlaylistSong.TABLE_NAME + ".";
        }

        if(pIsIncludeIdColumn) {
            columns.add(tableName + TablePlaylistSong.COLUMN_NAME___ID + (pIsIncludeTableName ? (" as "  + TablePlaylistSong.COLUMN_NAME_ALIAS___ID) : ""));
        }
        columns.add(tableName + TablePlaylistSong.COLUMN_NAME__PLAYLIST_ID + (pIsIncludeTableName ? (" as "  + TablePlaylistSong.COLUMN_NAME_ALIAS__PLAYLIST_ID) : ""));
        columns.add(tableName + TablePlaylistSong.COLUMN_NAME__NCT_SONG_ID + (pIsIncludeTableName ? (" as "  + TablePlaylistSong.COLUMN_NAME_ALIAS__NCT_SONG_ID) : ""));
        columns.add(tableName + TablePlaylistSong.COLUMN_NAME__ZING_SONG_ID + (pIsIncludeTableName ? (" as "  + TablePlaylistSong.COLUMN_NAME_ALIAS__ZING_SONG_ID) : ""));

        return columns;
    }

    public static void onCreate(SQLiteDatabase pSQLiteDatabase) {
        pSQLiteDatabase.execSQL(TablePlaylistSong.STATEMENT_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase pSQLiteDatabase, int pOldVersion, int pNewVersion) {
    }
}
