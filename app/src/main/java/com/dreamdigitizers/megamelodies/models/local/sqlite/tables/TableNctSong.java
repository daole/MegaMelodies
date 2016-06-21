package com.dreamdigitizers.megamelodies.models.local.sqlite.tables;

import android.database.sqlite.SQLiteDatabase;

import com.dreamdigitizers.androidbaselibrary.models.local.sqlite.tables.TableBase;

import java.util.ArrayList;
import java.util.List;

public class TableNctSong extends TableBase {
    public static final String TABLE_NAME = "nct_song";

    public static final String COLUMN_NAME__NAME = "name";
    public static final String COLUMN_NAME__URL = "url";

    public static final String COLUMN_NAME_ALIAS___ID = TableNctSong.TABLE_NAME + "_" + TableNctSong.COLUMN_NAME___ID;
    public static final String COLUMN_NAME_ALIAS__NAME = TableNctSong.TABLE_NAME + "_" + TableNctSong.COLUMN_NAME__NAME;
    public static final String COLUMN_NAME_ALIAS__URL = TableNctSong.TABLE_NAME + "_" + TableNctSong.COLUMN_NAME__URL;

    private static final String STATEMENT_CREATE = "CREATE TABLE `" + TableNctSong.TABLE_NAME + "` ("
            + "`" + TableNctSong.COLUMN_NAME___ID + "` TEXT NOT NULL PRIMARY KEY,"
            + "`" + TableNctSong.COLUMN_NAME__NAME + "` TEXT NOT NULL,"
            + "`" + TableNctSong.COLUMN_NAME__URL + "` TEXT NOT NULL"
            + ");";

    private static final String STATEMENT_UPGRADE = "";

    public static List<String> getColumns() {
        return TableNctSong.getColumns(false, true);
    }

    public static List<String> getColumns(boolean pIsIncludeTableName, boolean pIsIncludeIdColumn) {
        List<String> columns = new ArrayList<>();

        String tableName = "";
        if(pIsIncludeTableName) {
            tableName = TableNctSong.TABLE_NAME + ".";
        }

        if(pIsIncludeIdColumn) {
            columns.add(tableName + TableNctSong.COLUMN_NAME___ID + (pIsIncludeTableName ? (" as "  + TableNctSong.COLUMN_NAME_ALIAS___ID) : ""));
        }
        columns.add(tableName + TableNctSong.COLUMN_NAME__NAME + (pIsIncludeTableName ? (" as "  + TableNctSong.COLUMN_NAME_ALIAS__NAME) : ""));
        columns.add(tableName + TableNctSong.COLUMN_NAME__URL + (pIsIncludeTableName ? (" as "  + TableNctSong.COLUMN_NAME_ALIAS__URL) : ""));

        return columns;
    }

    public static void onCreate(SQLiteDatabase pSQLiteDatabase) {
        pSQLiteDatabase.execSQL(TableNctSong.STATEMENT_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase pSQLiteDatabase, int pOldVersion, int pNewVersion) {
    }
}
