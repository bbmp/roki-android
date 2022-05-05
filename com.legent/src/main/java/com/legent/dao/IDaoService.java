package com.legent.dao;

import com.j256.ormlite.dao.Dao;

public interface IDaoService {
    String getDbName();

    int getDbVersion();

    AbsDbHelper getDbHelper();

    <T, ID> Dao<T, ID> getDao(Class<T> clazz);
}