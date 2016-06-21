package com.dreamdigitizers.megamelodies.models.local.sqlite.dal;

import com.dreamdigitizers.androidbaselibrary.models.local.sqlite.dal.DalBase;
import com.dreamdigitizers.androidbaselibrary.models.local.sqlite.helpers.HelperSQLiteDatabase;
import com.dreamdigitizers.megamelodies.models.local.sqlite.tables.TableNctSong;

import java.util.Arrays;
import java.util.List;

public class DalNctSong extends DalBase {
    public DalNctSong(HelperSQLiteDatabase pHelperSQLiteDatabase) {
        super(pHelperSQLiteDatabase);
    }

    @Override
    public String getTableName() {
        return TableNctSong.TABLE_NAME;
    }

    @Override
    public boolean checkColumns(String[] pProjection) {
        if(pProjection == null) {
            return false;
        }
        List<String> columns = TableNctSong.getColumns();
        return columns.containsAll(Arrays.asList(pProjection));
    }
}
