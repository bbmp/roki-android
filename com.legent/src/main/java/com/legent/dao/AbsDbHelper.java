package com.legent.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.legent.IDispose;

abstract public class AbsDbHelper extends OrmLiteSqliteOpenHelper implements
        IDispose {

    protected static final String TAG = DaoHelper.TAG;

    public AbsDbHelper(Context cx, String dbName, int dbVersion) {
        super(cx, dbName, null, dbVersion);
    }

    @Override
    public void dispose() {
        this.close();
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        Log.d(TAG, "DB begin create:" + getDatabaseName());
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        Log.d(TAG, String.format("DB Upgrade. oldVersion:%s  newVersion:%s", oldVersion, newVersion));
    }
}
