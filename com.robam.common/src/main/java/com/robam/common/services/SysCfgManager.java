package com.robam.common.services;

import com.j256.ormlite.dao.Dao;
import com.legent.plat.Plat;
import com.legent.dao.DaoHelper;
import com.legent.pojos.AbsPojoManagerWithDB;
import com.robam.common.pojos.SysCfg;

public class SysCfgManager extends AbsPojoManagerWithDB<SysCfg, Long> {

    static private SysCfgManager instance = new SysCfgManager();

    synchronized static public SysCfgManager getInstance() {
        return instance;
    }

    int cloudVersion = 1;

    private SysCfgManager() {
    }

    @Override
    protected Dao<SysCfg, Long> getDao() {
        return DaoService.getInstance().getDao(SysCfg.class);
    }

    public boolean isNewest() {
        int localVer = getLocalVersion();
        return localVer >= cloudVersion;
    }

    public int getCloudVersion() {
        return cloudVersion;
    }

    public int getLocalVersion() {
        long userId = getUserId();
        try {
            if (!DaoHelper.isExists(SysCfg.class, userId))
                return -1;

            SysCfg sys = DaoHelper.getById(SysCfg.class, userId);
            return sys.version;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public void setLocalVersion(int version) {
        cloudVersion = version;

        SysCfg sys = new SysCfg();
        sys.id = getUserId();
        sys.version = version;
        DaoHelper.createOrUpdate(sys);

    }

    private long getUserId() {
        return Plat.accountService.getCurrentUserId();
    }
}
