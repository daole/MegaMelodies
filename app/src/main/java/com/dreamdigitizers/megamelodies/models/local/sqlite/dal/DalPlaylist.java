package com.dreamdigitizers.megamelodies.models.local.sqlite.dal;

import android.database.Cursor;

import com.dreamdigitizers.androidbaselibrary.models.local.sqlite.dal.DalBase;
import com.dreamdigitizers.androidbaselibrary.models.local.sqlite.helpers.HelperSQLiteDatabase;
import com.dreamdigitizers.megamelodies.models.local.sqlite.tables.TableNctSinger;
import com.dreamdigitizers.megamelodies.models.local.sqlite.tables.TableNctSong;
import com.dreamdigitizers.megamelodies.models.local.sqlite.tables.TablePlaylist;
import com.dreamdigitizers.megamelodies.models.local.sqlite.tables.TablePlaylistSong;
import com.dreamdigitizers.megamelodies.models.local.sqlite.tables.TableZingSong;

import java.util.Arrays;
import java.util.List;

public class DalPlaylist extends DalBase {
    private static final String JOIN_CLAUSE;
    static {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(TablePlaylist.TABLE_NAME);
        stringBuilder.append(" join ");
        stringBuilder.append(TablePlaylistSong.TABLE_NAME);
        stringBuilder.append(" on ");
        stringBuilder.append(TablePlaylist.TABLE_NAME);
        stringBuilder.append(".");
        stringBuilder.append(TablePlaylist.COLUMN_NAME___ID);
        stringBuilder.append(" = ");
        stringBuilder.append(TablePlaylistSong.TABLE_NAME);
        stringBuilder.append(".");
        stringBuilder.append(TablePlaylistSong.COLUMN_NAME__PLAYLIST_ID);

        stringBuilder.append(" left join ");
        stringBuilder.append(TableNctSong.TABLE_NAME);
        stringBuilder.append(" on ");
        stringBuilder.append(TablePlaylistSong.TABLE_NAME);
        stringBuilder.append(".");
        stringBuilder.append(TablePlaylistSong.COLUMN_NAME__NCT_SONG_ID);
        stringBuilder.append(" = ");
        stringBuilder.append(TableNctSong.TABLE_NAME);
        stringBuilder.append(".");
        stringBuilder.append(TableNctSong.COLUMN_NAME___ID);

        stringBuilder.append(" left join ");
        stringBuilder.append(TableNctSinger.TABLE_NAME);
        stringBuilder.append(" on ");
        stringBuilder.append(TableNctSong.TABLE_NAME);
        stringBuilder.append(".");
        stringBuilder.append(TableNctSong.COLUMN_NAME___ID);
        stringBuilder.append(" = ");
        stringBuilder.append(TableNctSinger.TABLE_NAME);
        stringBuilder.append(".");
        stringBuilder.append(TableNctSinger.COLUMN_NAME__NCT_SONG_ID);

        stringBuilder.append(" left join ");
        stringBuilder.append(TableZingSong.TABLE_NAME);
        stringBuilder.append(" on ");
        stringBuilder.append(TablePlaylistSong.TABLE_NAME);
        stringBuilder.append(".");
        stringBuilder.append(TablePlaylistSong.COLUMN_NAME__ZING_SONG_ID);
        stringBuilder.append(" = ");
        stringBuilder.append(TableZingSong.TABLE_NAME);
        stringBuilder.append(".");
        stringBuilder.append(TableZingSong.COLUMN_NAME___ID);

        JOIN_CLAUSE = stringBuilder.toString();
    }

    public DalPlaylist(HelperSQLiteDatabase pHelperSQLiteDatabase) {
        super(pHelperSQLiteDatabase);
    }

    @Override
    public Cursor select(String[] pProjection, String pWhereClause, String[] pWhereArgs, String pGroupBy, String pHaving, String pSortOrder, boolean pIsCloseOnEnd) {
        this.mSQLiteQueryBuilder.setTables(DalPlaylist.JOIN_CLAUSE);
        Cursor cursor = this.mHelperSQLiteDatabase.select(this.mSQLiteQueryBuilder, pProjection, pWhereClause, pWhereArgs, pGroupBy, pHaving, pSortOrder, null, false, pIsCloseOnEnd);
        return  cursor;
    }

    @Override
    public String getTableName() {
        return TablePlaylist.TABLE_NAME;
    }

    @Override
    public boolean checkColumns(String[] pProjection) {
        if(pProjection == null) {
            return false;
        }
        List<String> columns = TablePlaylist.getColumns();
        return columns.containsAll(Arrays.asList(pProjection));
    }

}
