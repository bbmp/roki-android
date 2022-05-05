package com.robam.common.pojos;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class TagValue {

    private List<TagsTagBean> tags;

    public static class TagsTagBean implements Parcelable {
        /**
         * cookbookTagId : null
         * name : 热门推荐
         * type : -1
         */

        private Long cookbookTagId;
        private String name;
        private Integer type;

        protected TagsTagBean(Parcel in) {
            name = in.readString();
            if (in.readByte() == 0) {
                type = null;
            } else {
                type = in.readInt();
            }
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(name);
            if (cookbookTagId != null) {
                dest.writeLong((Long) cookbookTagId);
            }
            if (type == null) {
                dest.writeByte((byte) 0);
            } else {
                dest.writeByte((byte) 1);
                dest.writeInt(type);
            }
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<TagsTagBean> CREATOR = new Creator<TagsTagBean>() {
            @Override
            public TagsTagBean createFromParcel(Parcel in) {
                return new TagsTagBean(in);
            }

            @Override
            public TagsTagBean[] newArray(int size) {
                return new TagsTagBean[size];
            }
        };

        public Integer getType() {
            return type;
        }

        public String getName() {
            return name;
        }

        public Long getCookbookTagId() {
            return (Long) cookbookTagId;
        }
    }

    public List<TagsTagBean> getTags() {
        return tags;
    }
}
