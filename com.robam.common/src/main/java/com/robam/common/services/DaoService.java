package com.robam.common.services;

import com.legent.dao.AbsDaoService;
import com.legent.dao.AbsDbHelper;

/**
 * Created by sylar on 15/7/7.
 */
public class DaoService extends AbsDaoService {

    private static DaoService instance = new DaoService();

    public synchronized static DaoService getInstance() {
        return instance;
    }

    private DaoService() {
    }

    public AbsDbHelper getCurrentDbHelper() {
        return dbHelper;
    }

    public void switchUser() {
        if (true)
            return;
        daos.clear();

        if (dbHelper != null) {
            //周定钧（不让数据库每次退出就关闭（让数据库游标每次使用完关闭即可）。否则会导致第二次登录时报尝试打开已关闭数据库的崩溃）
            dbHelper.close();
            dbHelper = null;
        }

        dbHelper = getDbHelper();
    }

    @Override
    public AbsDbHelper getDbHelper() {
        String dbName = getDbName();
        int dbVer = getDbVersion();
        DbHelper helper = DbHelper.newHelper(cx, dbName, dbVer);
        return helper;
    }


    @Override
    public String getDbName() {
        //long curUserId = Plat.accountService.getCurrentUserId();
        return String.format("%s_%s", super.getDbName(), "0");
    }

}
