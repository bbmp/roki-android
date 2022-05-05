package com.robam.common.pojos;

import com.j256.ormlite.field.DatabaseField;
import com.legent.pojos.AbsStorePojo;

/**
 * Tag-Recipe 映射关系表
 * 
 * @author sylar
 * 
 */
public class Tag_Recipe extends AbsStorePojo<Long> {

	public final static String COLUMN_TAG_ID = "tag_id";
	public final static String COLUMN_BOOK_ID = "book_id";

	@DatabaseField(generatedId = true)
	private long id;

	@DatabaseField(foreign = true, columnName = COLUMN_TAG_ID)
	protected Tag tag;

	@DatabaseField(foreign = true, columnName = COLUMN_BOOK_ID)
	protected Recipe book;

	public Tag_Recipe() {
	}

	public Tag_Recipe(Tag tag, Recipe book) {
		this.tag = tag;
		this.book = book;
	}

	@Override
	public Long getID() {
		return id;
	}

	@Override
	public String getName() {
		return String.valueOf(id);
	}


}
