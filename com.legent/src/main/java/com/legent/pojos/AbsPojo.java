package com.legent.pojos;


import com.legent.utils.JsonUtils;

/**
 * Created by sylar on 15/7/7.
 */
abstract public class AbsPojo extends AbsObject implements IPojo {

    @Override
    public String toString() {
        try {
            return JsonUtils.pojo2Json(this);
        } catch (Exception e) {
            return super.toString();
        }
    }

}
