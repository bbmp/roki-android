package com.robam.common.pojos.device.microwave;

/**
 * Created by Rent on 2016/6/13.
 */
public class MicroWaveWheelMsg {
    public MicroWaveWheelMsg(){

    }
    public MicroWaveWheelMsg(MicroWaveWheelMsg msg){
        this.fire=msg.getFire();
        this.time=msg.getTime();
        this.model=msg.getModel();
        this.weight=msg.getWeight();
    }
    @Override
    public String toString() {
        return "MicroWaveWheelMsg{" +
                "model=" + model +
                ", time=" + time +
                ", fire=" + fire +
                '}';
    }
    private short model;
    private short time;
    private short fire;
    private short weight;

    public short getWeight() {
        return weight;
    }

    public void setWeight(short weight) {
        this.weight = weight;
    }

    public short getModel() {
        return model;
    }

    public void setModel(short model) {
        this.model = model;
    }

    public short getTime() {
        return time;
    }

    public void setTime(short time) {
        this.time = time;
    }

    public short getFire() {
        return fire;
    }

    public void setFire(short fire) {
        this.fire = fire;
    }
}
