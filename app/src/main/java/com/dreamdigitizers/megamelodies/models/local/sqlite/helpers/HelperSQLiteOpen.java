package com.dreamdigitizers.megamelodies.models.local.sqlite.helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.dreamdigitizers.megamelodies.models.local.sqlite.tables.TableNctSinger;
import com.dreamdigitizers.megamelodies.models.local.sqlite.tables.TableNctSong;
import com.dreamdigitizers.megamelodies.models.local.sqlite.tables.TablePlaylist;
import com.dreamdigitizers.megamelodies.models.local.sqlite.tables.TablePlaylistSong;
import com.dreamdigitizers.megamelodies.models.local.sqlite.tables.TableZingSong;

public class HelperSQLiteOpen extends SQLiteOpenHelper {
    public HelperSQLiteOpen(Context pContext, String pDatabaseName, SQLiteDatabase.CursorFactory pCursorFactory, int pDatabaseVersion) {
        super(pContext, pDatabaseName, pCursorFactory, pDatabaseVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase pSQLiteDatabase) {
        TableNctSinger.onCreate(pSQLiteDatabase);
        TableNctSong.onCreate(pSQLiteDatabase);
        TablePlaylist.onCreate(pSQLiteDatabase);
        TablePlaylistSong.onCreate(pSQLiteDatabase);
        TableZingSong.onCreate(pSQLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase pSQLiteDatabase, int pOldVersion, int pNewVersion) {
        TableNctSinger.onUpgrade(pSQLiteDatabase, pOldVersion, pNewVersion);
        TableNctSong.onUpgrade(pSQLiteDatabase, pOldVersion, pNewVersion);
        TablePlaylist.onUpgrade(pSQLiteDatabase, pOldVersion, pNewVersion);
        TablePlaylistSong.onUpgrade(pSQLiteDatabase, pOldVersion, pNewVersion);
        TableZingSong.onUpgrade(pSQLiteDatabase, pOldVersion, pNewVersion);
    }
}
