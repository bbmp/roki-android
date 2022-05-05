package com.legent.ui.pojos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Preconditions;
import com.legent.ui.DefaultMenuListener;
import com.legent.ui.IMenuListener;

/**
 * Created by sylar on 15/6/3.
 */
public class MenuInfo extends AbsInfo {

    @JsonProperty("valid")
    public boolean valid = true;

    IMenuListener listener;

    public IMenuListener getMenuListener() {
        if (clazz == null)
            return DefaultMenuListener.getInstance();

        if (listener == null) {
            listener = (IMenuListener) getReflectObj();
            Preconditions.checkNotNull(listener, "invalid MenuInfo:"
                    + clazz);
        }

        return listener;
    }
}
