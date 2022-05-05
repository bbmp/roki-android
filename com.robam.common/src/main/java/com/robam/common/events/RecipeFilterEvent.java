package com.robam.common.events;

import java.util.List;

/**
 * Created by Rosicky on 16/1/19.
 */
public class RecipeFilterEvent {
    public List<Integer> source;
    public List<Integer> kitchen;
    public List<Integer> delicious;

    public RecipeFilterEvent(List<Integer> source, List<Integer> kitchen, List<Integer> delicious) {
        this.source = source;
        this.kitchen = kitchen;
        this.delicious = delicious;
    }

    public RecipeFilterEvent() {
    }

    public void setSource(List<Integer> source) {
        this.source = source;
    }

    public void setKitchen(List<Integer> kitchen) {
        this.kitchen = kitchen;
    }

    public void setDelicious(List<Integer> delicious) {
        this.delicious = delicious;
    }

    @Override
    public String toString() {
        return "RecipeFilterEvent{" +
                "source=" + source +
                ", kitchen=" + kitchen +
                ", delicious=" + delicious +
                '}';
    }
}
