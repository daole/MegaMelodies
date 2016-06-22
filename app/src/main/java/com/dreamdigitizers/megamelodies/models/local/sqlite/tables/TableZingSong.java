package com.dreamdigitizers.megamelodies.models.local.sqlite.tables;

import android.database.sqlite.SQLiteDatabase;

import com.dreamdigitizers.androidbaselibrary.models.local.sqlite.tables.TableBase;

import java.util.ArrayList;
import java.util.List;

public class TableZingSong extends TableBase {
    public static final String TABLE_NAME = "zing_song";

    public static final String COLUMN_NAME__NAME = "name";
    public static final String COLUMN_NAME__ARTIST = "artist";
    public static final String COLUMN_NAME__IS_FAVORITE = "is_favorite";

    public static final String COLUMN_NAME_ALIAS___ID = TableZingSong.TABLE_NAME + "_" + TableZingSong.COLUMN_NAME___ID;
    public static final String COLUMN_NAME_ALIAS__NAME = TableZingSong.TABLE_NAME + "_" + TableZingSong.COLUMN_NAME__NAME;
    public static final String COLUMN_NAME_ALIAS__ARTIST = TableZingSong.TABLE_NAME + "_" + TableZingSong.COLUMN_NAME__ARTIST;
    public static final String COLUMN_NAME_ALIAS__IS_FAVORITE = TableZingSong.TABLE_NAME + "_" + TableZingSong.COLUMN_NAME__IS_FAVORITE;

    private static final String STATEMENT_CREATE = "CREATE TABLE `" + TableZingSong.TABLE_NAME + "` ("
            + "`" + TableZingSong.COLUMN_NAME___ID + "` TEXT NOT NULL PRIMARY KEY,"
            + "`" + TableZingSong.COLUMN_NAME__NAME + "` TEXT NOT NULL,"
            + "`" + TableZingSong.COLUMN_NAME__ARTIST + "` TEXT NOT NULL,"
            + "`" + TableZingSong.COLUMN_NAME__IS_FAVORITE + "` NUMERIC NOT NULL"
            + ");";

    private static final String STATEMENT_UPGRADE = "";

    public static List<String> getColumns() {
        return TableZingSong.getColumns(false, true);
    }

    public static List<String> getColumns(boolean pIsIncludeTableName, boolean pIsIncludeIdColumn) {
        List<String> columns = new ArrayList<>();

        String tableName = "";
        if(pIsIncludeTableName) {
            tableName = TableZingSong.TABLE_NAME + ".";
        }

        if(pIsIncludeIdColumn) {
            columns.add(tableName + TableZingSong.COLUMN_NAME___ID + (pIsIncludeTableName ? (" as "  + TableZingSong.COLUMN_NAME_ALIAS___ID) : ""));
        }
        columns.add(tableName + TableZingSong.COLUMN_NAME__NAME + (pIsIncludeTableName ? (" as "  + TableZingSong.COLUMN_NAME_ALIAS__NAME) : ""));
        columns.add(tableName + TableZingSong.COLUMN_NAME__ARTIST + (pIsIncludeTableName ? (" as "  + TableZingSong.COLUMN_NAME_ALIAS__ARTIST) : ""));
        columns.add(tableName + TableZingSong.COLUMN_NAME__IS_FAVORITE + (pIsIncludeTableName ? (" as "  + TableZingSong.COLUMN_NAME_ALIAS__IS_FAVORITE) : ""));

        return columns;
    }

    public static void onCreate(SQLiteDatabase pSQLiteDatabase) {
        pSQLiteDatabase.execSQL(TableZingSong.STATEMENT_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase pSQLiteDatabase, int pOldVersion, int pNewVersion) {
    }
}
