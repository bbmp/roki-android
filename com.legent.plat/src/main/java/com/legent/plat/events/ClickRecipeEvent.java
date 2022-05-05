package com.legent.plat.events;

/**
 * Created by Administrator on 2019/7/23.
 */

public class ClickRecipeEvent {
    private String position;

    public ClickRecipeEvent(String position) {
        this.position = position;
    }

    public String getPosition() {
        return position;
    }
}
