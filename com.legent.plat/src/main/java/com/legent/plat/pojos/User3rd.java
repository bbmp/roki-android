package com.legent.plat.pojos;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.legent.pojos.AbsKeyPojo;


public class User3rd extends AbsKeyPojo<String> implements Parcelable {

	@JsonProperty("id")
	public String id;

	@JsonProperty("nickname")
	public String name;

	public User3rd() {
	}

	public User3rd(String id, String name) {
		this.id = id;
		this.name = name;
	}

	@Override
	public String getID() {
		return id;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.id);
		dest.writeString(this.name);
	}

	private User3rd(Parcel in) {
		this.id = in.readString();
		this.name = in.readString();
	}

	public static final Parcelable.Creator<User3rd> CREATOR = new Parcelable.Creator<User3rd>() {
		public User3rd createFromParcel(Parcel source) {
			return new User3rd(source);
		}

		public User3rd[] newArray(int size) {
			return new User3rd[size];
		}
	};
}
