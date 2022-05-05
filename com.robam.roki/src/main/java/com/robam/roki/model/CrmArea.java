package com.robam.roki.model;

/**
 * Created by sylar on 15/8/5.
 */
public class CrmArea {
    public PCR province;
    public PCR city;
    public PCR county;

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        if (province != null)
            sb.append(province.toString());
        if (city != null)
            sb.append(city.toString());
        if (county != null)
            sb.append(county.toString());
        return sb.toString();
    }

}
