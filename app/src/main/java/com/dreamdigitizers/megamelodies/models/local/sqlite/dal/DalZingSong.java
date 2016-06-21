package com.dreamdigitizers.megamelodies.models.local.sqlite.dal;


import com.dreamdigitizers.androidbaselibrary.models.local.sqlite.dal.DalBase;
import com.dreamdigitizers.androidbaselibrary.models.local.sqlite.helpers.HelperSQLiteDatabase;
import com.dreamdigitizers.megamelodies.models.local.sqlite.tables.TableZingSong;

import java.util.Arrays;
import java.util.List;

public class DalZingSong extends DalBase {
    public DalZingSong(HelperSQLiteDatabase pHelperSQLiteDatabase) {
        super(pHelperSQLiteDatabase);
    }

    @Override
    public String getTableName() {
        return TableZingSong.TABLE_NAME;
    }

    @Override
    public boolean checkColumns(String[] pProjection) {
        if(pProjection == null) {
            return false;
        }
        List<String> columns = TableZingSong.getColumns();
        return columns.containsAll(Arrays.asList(pProjection));
    }
}
