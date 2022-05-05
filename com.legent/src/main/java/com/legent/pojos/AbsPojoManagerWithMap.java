package com.legent.pojos;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

abstract public class AbsPojoManagerWithMap<Pojo extends IKeyPojo<ID>, ID> extends
		AbsPojoManager<Pojo, ID> {

	protected Map<ID, Pojo> map = Maps.newLinkedHashMap();

	@Override
	public long count() {
		return map.size();
	}

	@Override
	public boolean containsId(ID id) {
		return map.containsKey(id);
	}

	@Override
	public Pojo queryById(ID id) {
		return map.get(id);
	}

	@Override
	public List<Pojo> queryAll() {
		return Lists.newArrayList(map.values());
	}

	@Override
	public boolean add(Pojo pojo) {
		checkPojo(pojo);
		if (containsPojo(pojo))
			return false;

		map.put(pojo.getID(), pojo);
		pojo.init(cx);

		onPojoAdded(pojo);
		return true;
	}

	@Override
	public boolean delete(Pojo pojo) {
		checkPojo(pojo);
		if (!containsPojo(pojo))
			return false;

		map.remove(pojo.getID());
		pojo.dispose();

		onPojoDeleted(pojo);
		return true;
	}

	@Override
	public boolean update(Pojo pojo) {
		checkPojo(pojo);
		if (!containsPojo(pojo))
			return false;

		map.put(pojo.getID(), pojo);
		onPojoUpdated(pojo);
		return true;
	}

	@Override
	public void clearAll() {
		for (Pojo pojo : map.values()) {
			pojo.dispose();
		}

		map.clear();
		onCollectionChanged();
	}

}
