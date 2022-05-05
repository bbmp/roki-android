package com.robam.common.events;

/**
 * @ClassName: CmccEvent
 * @Description:
 * @Author: Hxw
 * @CreateDate: 2021/3/30 14:44
 */
public class CmccEvent {
    /**
     * 一键登录鉴权是否成功
     */
    public boolean isLoginAuth;

    public CmccEvent(boolean isLoginAuth) {
        this.isLoginAuth = isLoginAuth;
    }
}
