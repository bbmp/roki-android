package com.legent.ui.pojos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.legent.pojos.IJsonPojo;
import com.legent.ui.AbsLoader;
import com.legent.ui.ILayoutLoader;

/**
 * Created by sylar on 15/6/3.
 */
public class FormInfo implements IJsonPojo {

    @JsonProperty("id")
    public String id;

    @JsonProperty("homePage")
    public String homePageKey;

    @JsonProperty("loader")
    protected String loaderImp;

    ILayoutLoader loader = null;

    public ILayoutLoader getLoader() {

        if (loader == null) {

            Preconditions.checkState(!Strings.isNullOrEmpty(loaderImp),
                    "loaderImp is null");

            try {
                Class<?> c = Class.forName(loaderImp);
                loader = (ILayoutLoader) c.newInstance();
                if (loader instanceof AbsLoader) {
                    ((AbsLoader) loader).setKey(id);
                }
            } catch (Exception e) {
            }
        }

        Preconditions.checkNotNull(loader, "loader is null");
        return loader;
    }
}
