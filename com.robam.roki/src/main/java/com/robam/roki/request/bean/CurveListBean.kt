package com.robam.roki.request.bean

import android.os.Parcel
import android.os.Parcelable

class Payload() :Parcelable{
    var id = 0
    var name: String? = null
    var briefDesc: String? = null
    var introduction: String? = null
    var deviceGuid: String? = null
    var userId: String? = null
    var needTime = 0
    var difficulty = 0
    var imageCover: String? = null
    var collectCount = 0
    var viewCount = 0
    var grounding = false
    var recommend = false
    var deviceCategoryCode: String? = null
    var devicePlatformCode: String? = null
    var deviceTypeCode: String? = null
    var deviceParams: String? = null
    var curveOssKey: String? = null
    var temperatureCurveParams: String? = null
    var curveState: String? = null
    var recordStartDate: String? = null
    var recordEndDate: String? = null
    var time:Int=0
    var curveStageParams: String? = null
    var dataStatus = 0
    var gmtCreate: String? = null
    var gmtModified: String? = null
    var curveSettingParams:String?=null

    constructor(parcel: Parcel) : this() {
        id = parcel.readInt()
        name = parcel.readString()
        briefDesc = parcel.readString()
        introduction = parcel.readString()
        deviceGuid = parcel.readString()
        userId = parcel.readString()
        needTime = parcel.readInt()
        difficulty = parcel.readInt()
        imageCover = parcel.readString()
        collectCount = parcel.readInt()
        viewCount = parcel.readInt()
        grounding = parcel.readByte() != 0.toByte()
        recommend = parcel.readByte() != 0.toByte()
        deviceCategoryCode = parcel.readString()
        devicePlatformCode = parcel.readString()
        deviceTypeCode = parcel.readString()
        deviceParams = parcel.readString()
        curveOssKey = parcel.readString()
        temperatureCurveParams = parcel.readString()
        curveState = parcel.readString()
        recordStartDate = parcel.readString()
        recordEndDate = parcel.readString()
        time = parcel.readInt()
        curveStageParams = parcel.readString()
        dataStatus = parcel.readInt()
        gmtCreate = parcel.readString()
        gmtModified = parcel.readString()
        curveSettingParams = parcel.readString()
    }


    override fun toString(): String {
        return "Payload(id=$id, name=$name, briefDesc=$briefDesc, introduction=$introduction, deviceGuid=$deviceGuid, userId=$userId, needTime=$needTime, difficulty=$difficulty, imageCover=$imageCover, collectCount=$collectCount, viewCount=$viewCount, grounding=$grounding, recommend=$recommend, deviceCategoryCode=$deviceCategoryCode, devicePlatformCode=$devicePlatformCode, deviceTypeCode=$deviceTypeCode, deviceParams=$deviceParams, curveOssKey=$curveOssKey, temperatureCurveParams=$temperatureCurveParams, curveState=$curveState, recordStartDate=$recordStartDate, recordEndDate=$recordEndDate, time=$time, curveStageParams=$curveStageParams, dataStatus=$dataStatus, gmtCreate=$gmtCreate, gmtModified=$gmtModified)"
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(name)
        parcel.writeString(briefDesc)
        parcel.writeString(introduction)
        parcel.writeString(deviceGuid)
        parcel.writeString(userId)
        parcel.writeInt(needTime)
        parcel.writeInt(difficulty)
        parcel.writeString(imageCover)
        parcel.writeInt(collectCount)
        parcel.writeInt(viewCount)
        parcel.writeByte(if (grounding) 1 else 0)
        parcel.writeByte(if (recommend) 1 else 0)
        parcel.writeString(deviceCategoryCode)
        parcel.writeString(devicePlatformCode)
        parcel.writeString(deviceTypeCode)
        parcel.writeString(deviceParams)
        parcel.writeString(curveOssKey)
        parcel.writeString(temperatureCurveParams)
        parcel.writeString(curveState)
        parcel.writeString(recordStartDate)
        parcel.writeString(recordEndDate)
        parcel.writeInt(time)
        parcel.writeString(curveStageParams)
        parcel.writeInt(dataStatus)
        parcel.writeString(gmtCreate)
        parcel.writeString(gmtModified)
        parcel.writeString(curveSettingParams)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Payload> {
        override fun createFromParcel(parcel: Parcel): Payload {
            return Payload(parcel)
        }

        override fun newArray(size: Int): Array<Payload?> {
            return arrayOfNulls(size)
        }
    }


}
class CurveListBean():Parcelable {
    private var rc = 0
    private var msg: String? = null
    private var payload: List<Payload>? = null

    constructor(parcel: Parcel) : this() {
        rc = parcel.readInt()
        msg = parcel.readString()
        payload = parcel.createTypedArrayList(Payload.CREATOR)
    }

    fun setRc(rc: Int) {
        this.rc = rc
    }

    fun getRc(): Int {
        return rc
    }

    fun setMsg(msg: String?) {
        this.msg = msg
    }

    fun getMsg(): String? {
        return msg
    }

    fun setPayload(payload: List<Payload>?) {
        this.payload = payload
    }

    fun getPayload(): List<Payload>? {
        return payload
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(rc)
        parcel.writeString(msg)
        parcel.writeTypedList(payload)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CurveListBean> {
        override fun createFromParcel(parcel: Parcel): CurveListBean {
            return CurveListBean(parcel)
        }

        override fun newArray(size: Int): Array<CurveListBean?> {
            return arrayOfNulls(size)
        }
    }
}