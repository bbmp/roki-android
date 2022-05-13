package com.legent.plat.pojos;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;
import com.google.common.base.Strings;
import com.legent.pojos.AbsKeyPojo;
import com.legent.utils.graphic.BitmapUtils;
import com.legent.utils.security.MD5Utils;

import java.util.Date;

public class User extends AbsKeyPojo<Long> implements Parcelable {

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
    /**
     * 用户ID
     */
    @JsonProperty("id")
    public long id;
    /**
     * 用户昵称
     */
    @JsonProperty("nickname")
    public String nickname;
    /**
     * 性别
     */
    @JsonProperty("gender")
    public boolean gender;
    /**
     * email
     */
    @JsonProperty("email")
    public String email;
    /**
     * 手机号码
     */
    @JsonProperty("phone")
    public String phone;
    /**
     * 用户口令(已加密)
     */
    @JsonProperty("password")
    public String password;
    /**
     * 用户头像(url形式)
     */
    @JsonProperty("figureUrl")
    public String figureUrl;

    @JsonProperty("binded")
    public boolean binded;
    @JsonProperty("birthday")
    public Date birthday ;
    @JsonProperty("wxNickname")
    public String wxNickname ;
    @JsonProperty("appleIdBinded")
    public boolean appleIdBinded;
    @JsonProperty("sex")
    public String sex ;
    /**
     * 绑定的第三方平台账户
     */
  /*  @JsonProperty("thirdBind")
    public Map<Integer, User3rd> mapThirdParty;*/
    @JsonProperty("thirdBind")
    public ThirdInfo thirdInfos;

    public String TGT, ST;

    public int loginPlatId;

    public String authorization;
    public User() {

    }

    public void setPassword(String password) {
        this.password = password;
    }

    private User(Parcel in) {
        this.id = in.readLong();
        this.nickname = in.readString();
        this.gender = in.readByte() != 0;
        this.email = in.readString();
        this.phone = in.readString();
        this.password = in.readString();
        this.figureUrl = in.readString();
        this.binded = in.readByte() != 0;
        this.wxNickname = in.readString();
        this.appleIdBinded = in.readByte() != 0;
        this.sex = in.readString();
      //  Log.i("20171027", "id:" + id + " name:" + name + " binded:" + binded);
        //this.thirdInfos = in.readArrayList(ArrayList.class.getClassLoader());
        this.thirdInfos = in.readParcelable(ThirdInfo.class.getClassLoader());
    //    Log.i("20171027", "thirdInfos " + thirdInfos);
        //    this.mapThirdParty = in.readHashMap(HashMap.class.getClassLoader());
        this.TGT = in.readString();
        this.ST = in.readString();
        this.loginPlatId = in.readInt();
    }

    static public String figure2String(Bitmap bmp) {
        String str = BitmapUtils.toBase64(bmp);
        return str;
    }

    static public String encryptPassword(String rawPwd) {
        return MD5Utils.Md5(rawPwd);
    }

    static public String getEmptyPwd() {
        return MD5Utils.Md5("");
    }

    @Override
    public Long getID() {
        return id;
    }

    @Override
    public String getName() {
        return nickname;
    }

    @Override
    public void setName(String name) {
        this.nickname = name;
    }

    public String getAccount() {
        return !Strings.isNullOrEmpty(phone) ? phone : email;
    }

    public String getPassword(){
        return password;
    }

    public String getPhone() {
        if (!Strings.isNullOrEmpty(phone) ){

            return phone.replaceAll("(\\d{3})\\d{4}(\\d{4})","$1****$2");
        }
        return "未绑定";
    }
    public String getPhone2() {
        if (!Strings.isNullOrEmpty(phone) ){

            return phone;
        }
        return "未绑定";
    }
    /* public User3rd getUser3rd(int platId) {
        if (mapThirdParty != null && mapThirdParty.containsKey(platId)) {
            User3rd u = mapThirdParty.get(platId);
            return u;
        } else
            return null;

    }

    public void bind3rd(int platId, User3rd user) {
        if (mapThirdParty == null) {
            mapThirdParty = Maps.newHashMap();
        }
        mapThirdParty.put(platId, user);
    }

    public void unbind3rd(int platId) {
        if (mapThirdParty != null) {
            mapThirdParty.remove(platId);
        }
    }*/

    public boolean hasPassword() {
        if (password == null){
            return false;
        }
        String emptyPwd = getEmptyPwd();
        boolean hasPwd = !Objects.equal(password, emptyPwd);
        return hasPwd;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.nickname);
        dest.writeByte(gender ? (byte) 1 : (byte) 0);
        dest.writeString(this.email);
        dest.writeString(this.phone);
        dest.writeString(this.password);
        dest.writeString(this.figureUrl);
        dest.writeByte(binded ? (byte) 1 : (byte) 0);
        dest.writeParcelable(this.thirdInfos, 0);
        dest.writeString(this.wxNickname);
        dest.writeByte(appleIdBinded ? (byte) 1 : (byte) 0);
        dest.writeString(this.sex);
        //  dest.writeMap(this.mapThirdParty);
        dest.writeString(this.TGT);
        dest.writeString(this.ST);
        dest.writeInt(this.loginPlatId);
    }
}
