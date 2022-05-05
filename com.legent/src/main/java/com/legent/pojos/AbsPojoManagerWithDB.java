package com.legent.pojos;

import com.google.common.collect.Lists;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.Callable;


abstract public class AbsPojoManagerWithDB<Pojo extends IKeyPojo<ID>, ID> extends
        AbsPojoManager<Pojo, ID> {

    abstract protected Dao<Pojo, ID> getDao();

    @Override
    public long count() {
        try {
            return getDao().countOf();
        } catch (SQLException e) {
            onException(e);
        }
        return 0;
    }

    @Override
    public boolean containsId(ID id) {
        try {
            return getDao().idExists(id);
        } catch (SQLException e) {
            onException(e);
        }
        return false;
    }

    @Override
    public Pojo queryById(ID id) {
        try {
            return getDao().queryForId(id);
        } catch (SQLException e) {
            onException(e);
        }
        return null;
    }

    @Override
    public List<Pojo> queryAll() {
        try {
            return getDao().queryForAll();
        } catch (SQLException e) {
            onException(e);
        }
        return Lists.newArrayList();
    }

    @Override
    public boolean add(Pojo pojo) {
        try {
            getDao().createIfNotExists(pojo);
            pojo.init(cx);
            return true;
        } catch (SQLException e) {
            onException(e);
        }
        return false;
    }

    @Override
    public boolean delete(Pojo pojo) {
        try {
            getDao().delete(pojo);
            pojo.dispose();
            return true;
        } catch (SQLException e) {
            onException(e);
        }
        return false;
    }

    @Override
    public boolean update(Pojo pojo) {
        try {
            getDao().update(pojo);
            return true;
        } catch (SQLException e) {
            onException(e);
        }
        return false;
    }

    @Override
    public void batchAdd(final List<Pojo> list) {
        if (list == null || list.size() == 0)
            return;

        try {
            getDao().callBatchTasks(new Callable<Void>() {

                @Override
                public Void call() throws Exception {
                    for (Pojo pojo : list) {
                        getDao().create(pojo);
                        pojo.init(cx);
                    }

                    onCollectionChanged();
                    return null;
                }
            });
        } catch (Exception e) {
            onException(e);
        }
    }

    @Override
    public void batchDelete(final List<Pojo> list) {
        if (list == null || list.size() == 0)
            return;

        try {
            getDao().callBatchTasks(new Callable<Void>() {

                @Override
                public Void call() throws Exception {
                    for (Pojo pojo : list) {
                        getDao().delete(pojo);
                        pojo.dispose();
                    }

                    onCollectionChanged();
                    return null;
                }
            });
        } catch (Exception e) {
            onException(e);
        }
    }

    @Override
    public void batchUpdate(final List<Pojo> list) {
        if (list == null || list.size() == 0)
            return;

        try {
            getDao().callBatchTasks(new Callable<Void>() {

                @Override
                public Void call() throws Exception {
                    for (Pojo pojo : list) {
                        getDao().update(pojo);
                    }
                    return null;
                }
            });
        } catch (Exception e) {
            onException(e);
        }
    }

}
