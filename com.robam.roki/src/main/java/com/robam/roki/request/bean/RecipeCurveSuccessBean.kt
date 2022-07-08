/**
 * Copyright 2021 json.cn
 */
package com.robam.roki.request.bean

import com.robam.roki.net.base.BaseParam
import java.io.Serializable


class RecipeCurveSuccessBean:BaseParam(),Serializable{
    var msg: String? = null
    var payload: Payload? = null
    var rc = 0


    class Payload:Serializable,BaseParam() {
        var deviceGuid:String?=null
        var userId:String?=null
        var curve_stage_params:String?=null
        var curveCookbookId: String? = null
        var deviceCategoryCode: String? = null
        var deviceParams: String? = null
        var devicePlatformCode: String? = null
        var deviceTypeCode: String? = null
        var difficulty: String?=null
        var imageCover: String? = null
        var introduction: String? = null
        var materialList: ArrayList<MaterialList>? = null
        var name: String? = null
        var needTime: String? = null
        var prepareStepList: ArrayList<PrepareStepList>? = null
        var stepList: ArrayList<StepList>? = null
        var temperatureCurveParams: String? = null
        var time: String? = null
        var curveSettingParams:String?=null
        var gmtCreate:Long=0

    }

    class MaterialList(var materialType:Int?=null):Serializable {
        var units: ArrayList<Units>? = ArrayList()
        var id : String? = null

        var materialName: String? = null
        var unitName: String? = null
        var sourceType = 0
        var unitId:String? = null
        var weight = 0
    }
    class StepList(var isShow:Boolean=false):Serializable {
        var curveCookbookId:String? = null
        var curveStageParams: String? = null
        var curveStepId :String? = null
        var description: String? = null
        var imageUrl: String? = null
        var videoUrl:String?=null
        var markName: String? = null
        var markTime: String? = null
        var markTemp:String?=null
        var voiceUrl: String? = null
        var time:Int=0;
        var playTime:Int=0;
        var status:Int=1;

        var isPlayed=false
        override fun toString(): String {
            return "StepList(isShow=$isShow, curveCookbookId=$curveCookbookId, curveStageParams=$curveStageParams, curveStepId=$curveStepId, description=$description, imageUrl=$imageUrl, markName=$markName, markTime=$markTime, markTemp=$markTemp, voiceUrl=$voiceUrl)"
        }


    }


    class PrepareStepList:Serializable {
        var curvePrepareStepId:String? = null
        var description: String? = null
        var imageUrl: String? = null
        var no:String? = null
        var voiceUrl: String? = null
        var time=0;
        var playTime=0;
        var status=1;
    }
}