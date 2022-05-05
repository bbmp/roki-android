package com.legent.dao;

import android.util.Log;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.j256.ormlite.stmt.Where;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;



/**
 * 须先调用 init(IDaoService daoService) 初始化
 */
public class DaoHelper {

    public static final String TAG = "dao";
    private static IDaoService daoService;

    public static void init(IDaoService daoService) {
        DaoHelper.daoService = daoService;
    }

    static public <T> void refresh(T t) {

        try {
            getDao(t).refresh(t);
        } catch (Exception e) {
            onException(e);
        }
    }

    static public <T> long countOf(Class<T> clazz) {

        try {
            return getDao(clazz).countOf();
        } catch (Exception e) {
            onException(e);
            return -1;
        }
    }


    static public <T> boolean isExists(Class<T> clazz, Object id) {
        try {
            return getDao(clazz).idExists(id);
        } catch (SQLException e) {
            onException(e);
            return false;
        }
    }


    static public <T> void create(T t) {
        try {
            getDao(t).create(t);
        } catch (SQLException e) {
            onException(e);
        }
    }

    static public <T> void createOrUpdate(T t) {

        try {
            getDao(t).createOrUpdate(t);
        } catch (SQLException e) {
            onException(e);
        }
    }


    static public <T> void createIfNotExists(T t) {

        try {
            getDao(t).createIfNotExists(t);
        } catch (SQLException e) {
            onException(e);
        }
    }

    static public <T> void update(T t) {
        try {
            getDao(t).update(t);
        } catch (SQLException e) {
            onException(e);
        }
    }

    static public <T> void delete(T t) {
        try {
            getDao(t).delete(t);
        } catch (SQLException e) {
            onException(e);
        }
    }

    static public <T> void deleteById(Class<T> clazz, Object id) {
        try {
            getDao(clazz).deleteById(id);
        } catch (SQLException e) {
            onException(e);
        }
    }


    static public <T> void deleteList(Class<T> clazz, Collection<T> list) {
        try {
            getDao(clazz).delete(list);
        } catch (SQLException e) {
            onException(e);
        }
    }

    static public <T> void deleteAll(Class<T> clazz) {
        try {
            Dao<T, ?> dao = getDao(clazz);
            dao.delete(dao.queryForAll());
        } catch (SQLException e) {
            onException(e);
        }
    }

    static public <T> void deleteWhereEq(Class<T> clazz, String field,
                                         Object value) {
        try {
            DeleteBuilder<T, ?> db = getDao(clazz).deleteBuilder();
            db.where().eq(field, value);
            db.delete();

        } catch (Exception e) {
            onException(e);
        }

    }

    static public <T> void deleteWhereEqAnd(Class<T> clazz,
                                            String[] whereFields, Object[] whereValues) {
        Preconditions.checkNotNull(whereFields);
        Preconditions.checkNotNull(whereValues);
        Preconditions.checkState(whereFields.length == whereValues.length);

        try {
            DeleteBuilder<T, ?> db = getDao(clazz).deleteBuilder();
            Where<T, ?> wh = db.where();

            int len = whereFields.length;
            for (int i = 0; i < len; i++) {
                wh.eq(whereFields[i], whereValues[i]);
                if (i < len - 1) {
                    wh.and();
                }
            }

            db.delete();
        } catch (Exception e) {
            onException(e);
        }

    }

    static public <T> T getTopOne(Class<T> clazz) {
        List<T> list = getTop(clazz, 1);
        if (list != null && list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }

    static public <T> List<T> getTop(Class<T> clazz, int count) {
        List<T> list = getAll(clazz);
        if (list != null && list.size() > 0) {
            return list.subList(0, Math.min(count, list.size()));
        } else {
            return Lists.newArrayList();
        }
    }

    static public <T> List<T> getAll(Class<T> clazz) {
        try {
            return getDao(clazz).queryForAll();
        } catch (SQLException e) {
            onException(e);
            return Lists.newArrayList();
        }

    }

    static public <T> List<T> getPage(Class<T> clazz, int start, int num) {
        try {
            QueryBuilder builder = getDao(clazz).queryBuilder();
            builder.offset(start);//表示查询的起始位置
            builder.limit(num);//表示总共获取的对象数量
            return builder.query();
        } catch (SQLException e) {
            onException(e);
            return Lists.newArrayList();
        }
    }

    static public <T> T getById(Class<T> clazz, Object id) {

        T t = null;
        try {
            t = getDao(clazz).queryForId(id);
        } catch (SQLException e) {
            onException(e);
        }
        return t;
    }

    static public <T> Iterator<T> iterator(Class<T> clazz) {
        try {
            return getDao(clazz).queryBuilder().iterator();
        } catch (Exception e) {
            onException(e);
            return null;
        }
    }

    static public <T> List<T> getPageByWhereEq(Class<T> clazz, Class clazzfg, String field,
                                               Object value, int start, int num) {
        try {
            QueryBuilder<T, ?> mainQuerybuild = getDao(clazz).queryBuilder();
            QueryBuilder forginqueryBuilder = getDao(clazzfg).queryBuilder();
            forginqueryBuilder.where().eq(field, value);
            return null;
        } catch (Exception e) {
            onException(e);
            return Lists.newArrayList();
        }
    }

    static public <T> List<T> getWhereEq(Class<T> clazz, String field,
                                         Object value) {

        try {
            QueryBuilder<T, ?> qb = getDao(clazz).queryBuilder();
            qb.where().eq(field, value);
            List<T> list = qb.query();
            return list;
        } catch (Exception e) {
            onException(e);
            return Lists.newArrayList();
        }

    }

    static public <T> List<T> getWhereEqAnd(Class<T> clazz, String[] fields,
                                            Object[] values) {
        Preconditions.checkNotNull(fields);
        Preconditions.checkNotNull(values);
        Preconditions.checkState(fields.length == values.length);

        try {
            QueryBuilder<T, ?> qb = getDao(clazz).queryBuilder();
            Where<T, ?> wh = qb.where();

            int len = fields.length;
            for (int i = 0; i < len; i++) {
                wh.eq(fields[i], values[i]);
                if (i < len - 1) {
                    wh.and();
                }
            }
            List<T> list = qb.query();
            return list;

        } catch (Exception e) {
            onException(e);
            return Lists.newArrayList();
        }

    }

    static public <T> List<T> getWhereLike(Class<T> clazz, String field,
                                           Object value) {

        try {
            QueryBuilder<T, ?> qb = getDao(clazz).queryBuilder();
            qb.where().like(field, "%" + value + "%");
            List<T> list = qb.query();
            return list;
        } catch (Exception e) {
            onException(e);
            return Lists.newArrayList();
        }

    }

    static public <T> void setField(Class<T> clazz, String field, Object value) {

        try {
            UpdateBuilder<T, ?> ub = getDao(clazz).updateBuilder();
            ub.updateColumnValue(field, value);
            ub.update();
        } catch (Exception e) {
            onException(e);
        }

    }

    static public <T> void setFieldWhereEq(Class<T> clazz, String setField,
                                           Object setValue, String whereField, Object whereValue) {

        try {
            UpdateBuilder<T, ?> ub = getDao(clazz).updateBuilder();
            ub.updateColumnValue(setField, setValue).where()
                    .eq(whereField, whereValue);
            ub.update();
        } catch (Exception e) {
            onException(e);
        }

    }

    static public <T> void setFieldWhereEqAnd(Class<T> clazz, String setField,
                                              Object setValue, String[] whereFields, Object[] whereValues) {

        Preconditions.checkNotNull(whereFields);
        Preconditions.checkNotNull(whereValues);
        Preconditions.checkState(whereFields.length == whereValues.length);

        try {
            UpdateBuilder<T, ?> ub = getDao(clazz).updateBuilder();
            Where<T, ?> wh = ub.updateColumnValue(setField, setValue).where();

            int len = whereFields.length;
            for (int i = 0; i < len; i++) {
                wh.eq(whereFields[i], whereValues[i]);
                if (i < len - 1) {
                    wh.and();
                }
            }

            ub.update();
        } catch (Exception e) {
            onException(e);
        }

    }


    // --------------------------------------------------------------------------------------------------
    //private 
    // --------------------------------------------------------------------------------------------------

    static private <T, ID> Dao<T, ID> getDao(T t) {
        Class<T> clazz = getClass(t);
        return getDao(clazz);
    }

    static public <T, ID> Dao<T, ID> getDao(Class<T> clazz) {
        return daoService.getDao(clazz);
    }

    static private <T> Class<T> getClass(T t) {
        Class<T> clazz = (Class<T>) t.getClass();
        return clazz;
    }


    private static void onException(Exception e) {
        if (e != null)
            Log.e(TAG, e.getMessage());
    }

}
