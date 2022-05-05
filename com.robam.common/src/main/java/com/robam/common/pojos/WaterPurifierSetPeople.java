package com.robam.common.pojos;

/**
 * Created by Administrator on 2016/12/30.
 */

public class WaterPurifierSetPeople {
    public Boolean flag;
    public String memberCount;
    public WaterPurifierSetPeople(){

    }
    public WaterPurifierSetPeople(WaterPurifierSetPeople msg){
       this.flag=msg.getFlag();
        this.memberCount=msg.getMemberCount();
    }

    public void setFlag(Boolean flag){
        this.flag=flag;
    }

    public Boolean getFlag(){
        return flag;
    }

    public void setMemberCount(String memberCount){
        this.memberCount=memberCount;
    }

    public String getMemberCount(){
        return memberCount;
    }
}
