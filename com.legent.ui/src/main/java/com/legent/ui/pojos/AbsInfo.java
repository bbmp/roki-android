package com.legent.ui.pojos;

import android.content.Context;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.legent.pojos.AbsKeyPojo;
import com.legent.utils.api.ResourcesUtils;

/**
 * Created by sylar on 15/6/3.
 */
public class AbsInfo extends AbsKeyPojo<String> {

    @JsonProperty("id")
    protected String id;

    @JsonProperty("name")
    protected String name;

    @JsonProperty("clazz")
    protected String clazz;

    @JsonProperty("iconRes")
    protected String iconRes;

    @Override
    public String getID() {
        return id;
    }

    @Override
    public String getName() {
        return ResourcesUtils.getStringOrFromRes(name);
    }

    public String getClazz() {
        return clazz;
    }

    public int getIconResId(Context cx) {
        return ResourcesUtils.getResId(iconRes);
    }

    protected Object getReflectObj() {

        Preconditions.checkState(!Strings.isNullOrEmpty(clazz),
                "clazz is null");

        Object obj = null;
        try {
            Class<?> c = Class.forName(clazz);
            obj = c.newInstance();
        } catch (Exception e) {
        }

        return obj;
    }
}
