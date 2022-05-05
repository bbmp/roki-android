package com.legent.events;

/**
 * Created by sylar on 15/8/7.
 */
abstract public class AbsEvent<Pojo> {

    public Pojo pojo;

    public AbsEvent(Pojo pojo) {
        this.pojo = pojo;
    }
}