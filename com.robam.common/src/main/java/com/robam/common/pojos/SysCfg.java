package com.robam.common.pojos;

import com.j256.ormlite.field.DatabaseField;
import com.legent.pojos.AbsStorePojo;

/**
 * 系统参数
 * 
 * @author sylar
 * 
 */
public class SysCfg extends AbsStorePojo<Long> {

	/**
	 * 以用户id标明存储数据的服务对象
	 */
	@DatabaseField(id = true)
	public long id;

	/**
	 * 本地存储的库版本号
	 */
	@DatabaseField()
	public int version;

	@Override
	public Long getID() {
		return id;
	}

	@Override
	public String getName() {
		return String.valueOf(version);
	}


}