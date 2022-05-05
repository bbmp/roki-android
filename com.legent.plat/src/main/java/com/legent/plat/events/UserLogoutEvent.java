package com.legent.plat.events;

import com.legent.events.AbsEvent;
import com.legent.plat.pojos.User;

/**
 * 用户注销登录事件
 *
 * @author sylar
 */
public class UserLogoutEvent extends AbsEvent<User> {


    public UserLogoutEvent(User pojo) {
        super(pojo);
    }
}