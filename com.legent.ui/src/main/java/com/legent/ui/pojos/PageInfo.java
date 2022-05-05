package com.legent.ui.pojos;

import android.content.Context;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Preconditions;
import com.legent.ui.IPage;
import com.legent.utils.api.ResourcesUtils;

/**
 * Created by sylar on 15/6/3.
 */
public class PageInfo extends AbsInfo {

    @JsonProperty
    protected String animInRes;

    @JsonProperty
    protected String animOutRes;

    public IPage getPage() {
        IPage page = (IPage) getReflectObj();
        Preconditions.checkNotNull(page, "invalid PageInfo:" + clazz);

        page.setPageKey(id);
        page.setPageTitle(getName());
        return page;
    }

    public int getAnimInResId(Context cx) {
        return ResourcesUtils.getResId(animInRes);
    }

    public int getAnimOutResId(Context cx) {
        return ResourcesUtils.getResId(animOutRes);
    }
}
