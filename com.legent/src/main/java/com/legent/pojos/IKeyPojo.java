package com.legent.pojos;

import com.legent.IKeyName;

/**
 * 普通实体对象接口，有唯一标识
 *
 * @param <ID> 标识类型
 * @author sylar
 */
public interface IKeyPojo<ID> extends IPojo, IKeyName<ID> {
}