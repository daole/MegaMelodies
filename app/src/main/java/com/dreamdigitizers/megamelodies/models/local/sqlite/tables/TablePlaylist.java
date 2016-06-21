package com.dreamdigitizers.megamelodies.models.local.sqlite.tables;

import android.database.sqlite.SQLiteDatabase;

import com.dreamdigitizers.androidbaselibrary.models.local.sqlite.tables.TableBase;

import java.util.ArrayList;
import java.util.List;

public class TablePlaylist extends TableBase {
    public static final String TABLE_NAME = "playlist";

    public static final String COLUMN_NAME__NAME = "name";

    public static final String COLUMN_NAME_ALIAS___ID = TablePlaylist.TABLE_NAME + "_" + TablePlaylist.COLUMN_NAME___ID;
    public static final String COLUMN_NAME_ALIAS__NAME = TablePlaylist.TABLE_NAME + "_" + TablePlaylist.COLUMN_NAME__NAME;

    private static final String STATEMENT_CREATE = "CREATE TABLE `" + TablePlaylist.TABLE_NAME + "` ("
            + "`" + TablePlaylist.COLUMN_NAME___ID + "` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,"
            + "`" + TablePlaylist.COLUMN_NAME__NAME + "` TEXT NOT NULL"
            + ");";

    private static final String STATEMENT_UPGRADE = "";

    public static List<String> getColumns() {
        return TablePlaylist.getColumns(false, true);
    }

    public static List<String> getColumns(boolean pIsIncludeTableName, boolean pIsIncludeIdColumn) {
        List<String> columns = new ArrayList<>();

        String tableName = "";
        if(pIsIncludeTableName) {
            tableName = TablePlaylist.TABLE_NAME + ".";
        }

        if(pIsIncludeIdColumn) {
            columns.add(tableName + TablePlaylist.COLUMN_NAME___ID + (pIsIncludeTableName ? (" as "  + TablePlaylist.COLUMN_NAME_ALIAS___ID) : ""));
        }
        columns.add(tableName + TablePlaylist.COLUMN_NAME__NAME + (pIsIncludeTableName ? (" as "  + TablePlaylist.COLUMN_NAME_ALIAS__NAME) : ""));

        return columns;
    }

    public static void onCreate(SQLiteDatabase pSQLiteDatabase) {
        pSQLiteDatabase.execSQL(TablePlaylist.STATEMENT_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase pSQLiteDatabase, int pOldVersion, int pNewVersion) {
    }
}
