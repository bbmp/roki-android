package com.robam.roki.request.bean

class CurveDetailCookBean {
    private var msg: String? = null
    private var payload: Payload? = null
    private var rc = 0
    fun setMsg(msg: String?) {
        this.msg = msg
    }

    fun getMsg(): String? {
        return msg
    }

    fun setPayload(payload: Payload?) {
        this.payload = payload
    }

    fun getPayload(): Payload? {
        return payload
    }

    fun setRc(rc: Int) {
        this.rc = rc
    }

    fun getRc(): Int {
        return rc
    }
    class StepList {
        var curveCookbookId = 0
        var curveStageParams: String? = null
        var curveStepId = 0
        var description: String? = null
        var imageUrl: String? = null
        var markName: String? = null
        var markTime: String? = null
        var voiceUrl: String? = null
    }



    class PrepareStepList {
        var curvePrepareStepId = 0
        var description: String? = null
        var imageUrl: String? = null
        var no = 0
        var voiceUrl: String? = null
    }


    class MaterialList {
        var id = 0
        var materialId = 0

        var materialName: String? = null
        var unitName: String? = null
        var materialType = 0
        var sourceType = 0
        var unitId = 0
        var weight = 0


    }

    class Payload {
        var curveCookbookId = 0
        var deviceCategoryCode: String? = null
        var deviceParams: String? = null
        var devicePlatformCode: String? = null
        var deviceTypeCode: String? = null
        var difficulty = 0
        var imageCover: String? = null
        var imageUrl:String?=null
        var curveSettingParams:String?=null
        var introduction: String? = null
        var materialList: ArrayList<MaterialList>? = null
        var name: String? = null
        var needTime = 0
        var prepareStepList: ArrayList<PrepareStepList>? = null
        var stepList: ArrayList<StepList>? = null
        var temperatureCurveParams: String? = null
        var time = 0
    }
}
