package com.robam.common.events;

/**
 * Created by Administrator on 2017/4/24.
 */
public class DeleteUserControlEvent {
    private  long       userId;
    private  String     absFanID;

    public DeleteUserControlEvent(long userId, String absFanID) {
        this.userId = userId;
        this.absFanID = absFanID;
    }

    public long getUserId() {
        return userId;
    }

    public String getAbsFanID() {
        return absFanID;
    }

}
