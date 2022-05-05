package com.legent.plat.pojos.dictionary;

import android.content.Context;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.legent.plat.Plat;
import com.legent.pojos.AbsPojo;


/**
 * Created by sylar on 15/7/30.
 */
public class CommOpt extends AbsPojo {

    @JsonProperty()
    public String appType;

    @JsonProperty(defaultValue = "2000")
    public long pollingPeriodInFront;

    @JsonProperty(defaultValue = "30000")
    public long pollingPeriodInBack;

    @JsonProperty()
    public String implAppChannel;

    @JsonProperty()
    public String implDeviceFactory;

    @JsonProperty()
    public String implAppNoticeReceiver;

    @JsonProperty()
    public String implAppOAuthService;


    @Override
    public void init(Context cx, Object... params) {
        super.init(cx, params);

        Preconditions.checkState(!Strings.isNullOrEmpty(appType), "appType must be none-null");
        Preconditions.checkState(appType.length() == 5, "appType length must equae 5");

        Plat.deviceService.setPolltingPeriod(pollingPeriodInFront,pollingPeriodInBack);
    }
}
