package com.legent.pojos;

import com.legent.IKey;

import java.util.List;

/**
 * 实体管理接口
 *
 * @param <T>
 * @param <ID>
 * @author sylar
 */
public interface IPojoManager<T extends IKey<ID>, ID> {

    long count();

    boolean containsId(ID id);

    T queryById(ID id);

    List<T> queryAll();

    boolean add(T t);

    boolean delete(T t);

    boolean update(T t);

    T getDefault();

    void setDefault(T t);

}