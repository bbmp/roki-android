package com.legent.plat.pojos.dictionary.msg;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.legent.pojos.AbsKeyPojo;

/**
 * Created by sylar on 15/9/5.
 */
public class MsgIndex extends AbsKeyPojo<String> {
    @JsonProperty
    public String deviceTypeId;

    @JsonProperty
    public String templateId;


    @Override
    public String getID() {
        return deviceTypeId;
    }

    @Override
    public String getName() {
        return deviceTypeId;
    }


    //===========================================================================================

    //===========================================================================================

}
