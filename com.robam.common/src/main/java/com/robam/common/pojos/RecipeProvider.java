package com.robam.common.pojos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.j256.ormlite.field.DatabaseField;
import com.legent.pojos.AbsStorePojo;

public class RecipeProvider extends AbsStorePojo<Long> {

	@DatabaseField(id = true)
	@JsonProperty("id")
	public long id;

	@DatabaseField
	@JsonProperty("name")
	public String name;

	@DatabaseField
	@JsonProperty("logoUrl")
	public String logoUrl;

	@Override
	public Long getID() {
		return id;
	}

	@Override
	public String getName() {
		return name;
	}

}
