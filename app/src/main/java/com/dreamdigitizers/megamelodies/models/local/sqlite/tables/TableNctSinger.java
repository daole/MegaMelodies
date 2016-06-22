package com.dreamdigitizers.megamelodies.models.local.sqlite.tables;

import android.database.sqlite.SQLiteDatabase;

import com.dreamdigitizers.androidbaselibrary.models.local.sqlite.tables.TableBase;

import java.util.ArrayList;
import java.util.List;

public class TableNctSinger extends TableBase {
    public static final String TABLE_NAME = "nct_singer";

    public static final String COLUMN_NAME__NCT_SONG_ID = "nct_song_id";
    public static final String COLUMN_NAME__NAME = "name";
    public static final String COLUMN_NAME__URL = "url";

    public static final String COLUMN_NAME_ALIAS___ID = TableNctSinger.TABLE_NAME + "_" + TableNctSinger.COLUMN_NAME___ID;
    public static final String COLUMN_NAME_ALIAS__NCT_SONG_ID = TableNctSinger.TABLE_NAME + "_" + TableNctSinger.COLUMN_NAME__NCT_SONG_ID;
    public static final String COLUMN_NAME_ALIAS__NAME = TableNctSinger.TABLE_NAME + "_" + TableNctSinger.COLUMN_NAME__NAME;
    public static final String COLUMN_NAME_ALIAS__URL = TableNctSinger.TABLE_NAME + "_" + TableNctSinger.COLUMN_NAME__URL;

    private static final String STATEMENT_CREATE = "CREATE TABLE `" + TableNctSinger.TABLE_NAME + "` ("
            + "`" + TableNctSinger.COLUMN_NAME___ID + "` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,"
            + "`" + TableNctSinger.COLUMN_NAME__NCT_SONG_ID + "` TEXT NOT NULL,"
            + "`" + TableNctSinger.COLUMN_NAME__NAME + "` TEXT NOT NULL,"
            + "`" + TableNctSinger.COLUMN_NAME__URL + "` TEXT NOT NULL"
            + ");";

    private static final String STATEMENT_UPGRADE = "";

    public static List<String> getColumns() {
        return TableNctSinger.getColumns(false, true);
    }

    public static List<String> getColumns(boolean pIsIncludeTableName, boolean pIsIncludeIdColumn) {
        List<String> columns = new ArrayList<>();

        String tableName = "";
        if(pIsIncludeTableName) {
            tableName = TableNctSinger.TABLE_NAME + ".";
        }

        if(pIsIncludeIdColumn) {
            columns.add(tableName + TableNctSinger.COLUMN_NAME___ID + (pIsIncludeTableName ? (" as "  + TableNctSinger.COLUMN_NAME_ALIAS___ID) : ""));
        }
        columns.add(tableName + TableNctSinger.COLUMN_NAME__NCT_SONG_ID + (pIsIncludeTableName ? (" as "  + TableNctSinger.COLUMN_NAME_ALIAS__NCT_SONG_ID) : ""));
        columns.add(tableName + TableNctSinger.COLUMN_NAME__NAME + (pIsIncludeTableName ? (" as "  + TableNctSinger.COLUMN_NAME_ALIAS__NAME) : ""));
        columns.add(tableName + TableNctSinger.COLUMN_NAME__URL + (pIsIncludeTableName ? (" as "  + TableNctSinger.COLUMN_NAME_ALIAS__URL) : ""));

        return columns;
    }

    public static void onCreate(SQLiteDatabase pSQLiteDatabase) {
        pSQLiteDatabase.execSQL(TableNctSinger.STATEMENT_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase pSQLiteDatabase, int pOldVersion, int pNewVersion) {
    }
}
