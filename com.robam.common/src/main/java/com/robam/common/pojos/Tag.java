package com.robam.common.pojos;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.j256.ormlite.field.DatabaseField;
import com.legent.dao.DaoHelper;
import com.legent.pojos.AbsStorePojo;
import com.robam.common.services.SysCfgManager;

import java.util.List;

public class Tag extends AbsStorePojo<Long> implements Parcelable{

    @DatabaseField(id = true)
    @JsonProperty("id")
    public long id;

    @DatabaseField
    @JsonProperty("name")
    public String name;

    @DatabaseField
    @JsonProperty("imgUrl")
    public String imageUrl;

    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    protected Group group;

    /**
     * 本地存储的库版本号
     */
    @DatabaseField()
    public int version;

    // -------------------------------------------------------------------------------
    // -------------------------------------------------------------------------------
    public Tag() {
    }

    protected Tag(Parcel in) {
        this.id = in.readLong();
        this.name = in.readString();
        this.imageUrl = in.readString();
        this.version = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.name);
        dest.writeString(this.imageUrl);
        dest.writeInt(this.version);
    }
    public static final Parcelable.Creator<Tag> CREATOR = new Parcelable.Creator<Tag>() {
        public Tag createFromParcel(Parcel source) {
            return new Tag(source);
        }

        public Tag[] newArray(int size) {
            return new Tag[size];
        }
    };

    @Override
    public Long getID() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    //未调用
//    public boolean isNewest() {
//        int mainVer = SysCfgManager.getInstance().getLocalVersion();
//        return version >= mainVer;
//    }

    public Group getParent() {
        return group;
    }


    public void save2db(List<Recipe> books, List<Recipe3rd> thirdBooks) {

        if (books != null) {
            DaoHelper.deleteWhereEq(Tag_Recipe.class, Tag_Recipe.COLUMN_TAG_ID, id);
            Tag_Recipe tb;
            for (Recipe book : books) {
                book.save2db();

                tb = new Tag_Recipe(this, book);
                tb.save2db();
            }
        }

        // --------------------------------------------------------------------
        if (thirdBooks != null) {
            DaoHelper.deleteWhereEq(Tag_Recipe3rd.class,
                    Tag_Recipe3rd.COLUMN_TAG_ID, id);

            Tag_Recipe3rd tb;
            for (Recipe3rd book : thirdBooks) {
                book.save2db();

                tb = new Tag_Recipe3rd(this, book);
                tb.save2db();
            }
        }

        // --------------------------------------------------------------------
        version = SysCfgManager.getInstance().getCloudVersion();
        DaoHelper.update(this);
    }

}