package com.robam.roki.net.base

import android.os.Parcel
import android.os.Parcelable

 open class BaseBean(var rc:String?="0", var msg:String?="") : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString()
    ) {
    }



    companion object CREATOR : Parcelable.Creator<BaseBean> {
        override fun createFromParcel(parcel: Parcel): BaseBean {
            return BaseBean(parcel)
        }

        override fun newArray(size: Int): Array<BaseBean?> {
            return arrayOfNulls(size)
        }
    }

     override fun describeContents(): Int {
         TODO("Not yet implemented")
     }

     override fun writeToParcel(dest: Parcel?, flags: Int) {
         TODO("Not yet implemented")
     }


 }