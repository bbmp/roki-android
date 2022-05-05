package com.legent.pojos;


import com.legent.dao.DaoHelper;
import com.legent.utils.LogUtils;


/**
 * Created by sylar on 15/7/7.
 */
abstract public class AbsStorePojo<ID> extends AbsKeyPojo<ID> implements IStorePojo<ID> {

    @Override
    public void save2db() {
        if (DaoHelper.isExists(getClass(), getID())) {
            LogUtils.i("20180319","here up");
            DaoHelper.update(this);
        } else {
            DaoHelper.create(this);
            LogUtils.i("20180319","here cr");
        }
        DaoHelper.refresh(this);
    }

    @Override
    public void delete(long id) {
        DaoHelper.delete(this);
        DaoHelper.refresh(this);
    }
}
