package com.legent.pojos;

import com.legent.IDispose;
import com.legent.Initialization;

/**
 * 普通实体对象接口,无唯一标识
 */
public interface IPojo extends Initialization, IDispose, IJsonPojo, IDbPojo {
}