package com.robam.common.pojos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.j256.ormlite.field.DatabaseField;
import com.legent.dao.DaoHelper;
import com.legent.pojos.AbsStorePojo;

public class AdvertImage extends AbsStorePojo<Long> {

	public final static String FIELD_ID = "id";

	static final public String FIELD_isInAll = "isInAll";
	static final public String FIELD_isFavority = "isFavority";
	static final public String FIELD_isRecommend = "isRecommend";

	@DatabaseField(id = true, columnName = FIELD_ID)
	@JsonProperty("cookbookId")
	public long id;

	@DatabaseField
	@JsonProperty("description")
	public String desc;

	@DatabaseField
	@JsonProperty("image")
	public String imageUrl;

	@DatabaseField(columnName = FIELD_isFavority)
	public boolean isFavority;

	@DatabaseField(columnName = FIELD_isRecommend)
	public boolean isRecommend;

	@DatabaseField(columnName = FIELD_isInAll)
	public boolean isInAll;

	// -------------------------------------------------------------------------------
	//
	// -------------------------------------------------------------------------------

	@Override
	public Long getID() {
		return id;
	}

	@Override
	public String getName() {
		return desc;
	}

	public void updateField(String field, Object value) {
		if (!DaoHelper.isExists(this.getClass(), id)) {
			DaoHelper.createOrUpdate(this);
		}
		DaoHelper.setFieldWhereEq(this.getClass(), field, value, FIELD_ID, id);
		DaoHelper.refresh(this);
	}

}