package com.dreamdigitizers.megamelodies.models.local.sqlite.tables;

import android.database.sqlite.SQLiteDatabase;

import com.dreamdigitizers.androidbaselibrary.models.local.sqlite.tables.TableBase;

import java.util.ArrayList;
import java.util.List;

public class TableFavorite extends TableBase {
    public static final String TABLE_NAME = "favorite";

    public static final String COLUMN_NAME__NCT_SONG_ID = "nct_song_id";
    public static final String COLUMN_NAME__ZING_SONG_ID = "zing_song_id";

    public static final String COLUMN_NAME_ALIAS___ID = TableFavorite.TABLE_NAME + "_" + TableFavorite.COLUMN_NAME___ID;
    public static final String COLUMN_NAME_ALIAS__NCT_SONG_ID = TableFavorite.TABLE_NAME + "_" + TableFavorite.COLUMN_NAME__NCT_SONG_ID;
    public static final String COLUMN_NAME_ALIAS__ZING_SONG_ID = TableFavorite.TABLE_NAME + "_" + TableFavorite.COLUMN_NAME__ZING_SONG_ID;

    private static final String STATEMENT_CREATE = "CREATE TABLE `" + TableFavorite.TABLE_NAME + "` ("
            + "`" + TableFavorite.COLUMN_NAME___ID + "` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,"
            + "`" + TableFavorite.COLUMN_NAME__NCT_SONG_ID + "` TEXT UNIQUE,"
            + "`" + TableFavorite.COLUMN_NAME__ZING_SONG_ID + "` TEXT UNIQUE"
            + ");";

    private static final String STATEMENT_UPGRADE = "";

    public static List<String> getColumns() {
        return TableFavorite.getColumns(false, true);
    }

    public static List<String> getColumns(boolean pIsIncludeTableName, boolean pIsIncludeIdColumn) {
        List<String> columns = new ArrayList<>();

        String tableName = "";
        if(pIsIncludeTableName) {
            tableName = TableFavorite.TABLE_NAME + ".";
        }

        if(pIsIncludeIdColumn) {
            columns.add(tableName + TableFavorite.COLUMN_NAME___ID + (pIsIncludeTableName ? (" as "  + TableFavorite.COLUMN_NAME_ALIAS___ID) : ""));
        }
        columns.add(tableName + TableFavorite.COLUMN_NAME__NCT_SONG_ID + (pIsIncludeTableName ? (" as "  + TableFavorite.COLUMN_NAME_ALIAS__NCT_SONG_ID) : ""));
        columns.add(tableName + TableFavorite.COLUMN_NAME__ZING_SONG_ID + (pIsIncludeTableName ? (" as "  + TableFavorite.COLUMN_NAME_ALIAS__ZING_SONG_ID) : ""));

        return columns;
    }

    public static void onCreate(SQLiteDatabase pSQLiteDatabase) {
        pSQLiteDatabase.execSQL(TableFavorite.STATEMENT_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase pSQLiteDatabase, int pOldVersion, int pNewVersion) {
    }
}
