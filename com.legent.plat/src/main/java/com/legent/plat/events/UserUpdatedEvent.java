package com.legent.plat.events;

import com.legent.events.AbsEvent;
import com.legent.plat.pojos.User;

/**
 * 用户信息变更事件
 *
 * @author sylar
 */
public class UserUpdatedEvent extends AbsEvent<User> {

    public UserUpdatedEvent(User pojo) {
        super(pojo);
    }
}
