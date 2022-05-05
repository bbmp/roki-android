package com.legent.plat.events;

/**
 * Created by rent on 2016/9/6.
 */

public class RecipePraiseEvent {
    public String id;
    public boolean isPraised;

    public  RecipePraiseEvent(String id, boolean isPraised) {
        this.id = id;
        this.isPraised = isPraised;
    }
}
