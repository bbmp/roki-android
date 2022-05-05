package com.legent.pojos;

/**
 * json+db 对象实体接口
 */
public interface IStorePojo<ID> extends IKeyPojo<ID> {
    void save2db();

    void delete(long id);
}
