package com.legent;

/**
 * 唯一标识符(ID)接口
 *
 * @author sylar
 */
public interface IKey<ID> {
    /**
     * 获取唯一标识符
     *
     * @return
     */
    ID getID();
}

