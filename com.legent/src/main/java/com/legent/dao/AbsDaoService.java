package com.legent.dao;

import com.google.common.base.Splitter;
import com.google.common.collect.Maps;
import com.j256.ormlite.dao.Dao;
import com.legent.services.AbsService;
import com.legent.utils.api.PackageUtils;

import java.util.List;
import java.util.Map;

/**
 * Created by sylar on 15/7/6.
 */
abstract public class AbsDaoService extends AbsService implements IDaoService {

    private static final String TAG = DaoHelper.TAG;

    protected AbsDbHelper dbHelper;
    protected Map<String, Dao<?, ?>> daos = Maps.newHashMap();

    public AbsDaoService() {
        DaoHelper.init(this);
    }

    @Override
    public String getDbName() {
        List<String> strings = Splitter.on(".").splitToList(cx.getPackageName());
        return strings.get(strings.size() - 1);
    }

    @Override
    public int getDbVersion() {
        return PackageUtils.getVersionCode(cx);
    }

    @Override
    public <T, ID> Dao<T, ID> getDao(Class<T> clazz) {
        if (dbHelper == null) {
            dbHelper = getDbHelper();
            daos.clear();
        }
        String key = clazz.getName();
        if (daos.containsKey(key)) {
            return (Dao<T, ID>) daos.get(key);
        } else {
            try {
                Dao<T, ID> dao = dbHelper.getDao(clazz);
                daos.put(key, dao);
                return dao;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }
}
