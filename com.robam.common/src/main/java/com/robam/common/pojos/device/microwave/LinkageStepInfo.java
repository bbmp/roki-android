package com.robam.common.pojos.device.microwave;

/**
 * Created by Administrator on 2016/6/22.
 */
public class LinkageStepInfo {
    public short getLink_time() {
        return link_time;
    }

    public void setLink_time(short link_time) {
        this.link_time = link_time;
    }

    public short getLink_fire() {
        return link_fire;
    }

    public void setLink_fire(short link_fire) {
        this.link_fire = link_fire;
    }

    public short getLink_model() {
        return link_model;
    }

    public void setLink_model(short link_model) {
        this.link_model = link_model;
    }

    short link_model = 0;
    short link_fire = 0;
    short link_time = 0;

    public short getLink_step() {
        return link_step;
    }

    public void setLink_step(short link_step) {
        this.link_step = link_step;
    }

    short link_step=0;
}
