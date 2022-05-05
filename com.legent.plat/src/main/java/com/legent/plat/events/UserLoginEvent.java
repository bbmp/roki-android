package com.legent.plat.events;

import com.legent.events.AbsEvent;
import com.legent.plat.pojos.User;

/**
 * 用户登录事件
 *
 * @author sylar
 */
public class UserLoginEvent extends AbsEvent<User> {

    public UserLoginEvent(User pojo) {
        super(pojo);
    }

}
