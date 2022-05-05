package com.robam.common.pojos;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Lists;

import java.io.Serializable;
import java.util.ArrayList;


/**
 * Created by r on 2016/12/29.
 */

public class DrinkingItem  implements Serializable {


    public ArrayList<DataInfo> dataInfo= Lists.newArrayList();

    @Override
    public String toString() {
        return dataInfo.toString();
    }
}
