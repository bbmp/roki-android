package com.robam.roki.net.request.bean

import com.robam.roki.db.model.RecipeStepBean
import com.robam.roki.request.param.MaterialDtoList
import com.robam.roki.request.param.StepDtoList
import com.robam.roki.ui.PageArgumentKey.time
import com.robam.roki.ui.page.device.steamovenone.CurveCookBooksVView.temperature
import java.io.Serializable

/**
 * {
 * "msg": "string",
 * "payload": {
 * "coverImg": "string",
 * "materialDtoList": [
 * {
 * "id": 0,
 * "materialId": 0,
 * "materialName": "string",
 * "sourceType": 0,
 * "unitId": 0,
 * "unitName": "string",
 * "weight": 0
 * }
 * ],
 * "name": "string",
 * "refMultiId": 0,
 * "stepDtoList": [
 * {
 * "description": "string",
 * "id": 0,
 * "no": 0,
 * "stepImg": "string"
 * }
 * ]
 * },
 * "rc": -1
 * }
 */
class RecipeDetailBean(var msg:String,var rc:Int,var payload:Payload):Serializable{
    inner class Payload(var coverImg:String,var materialDtoList: ArrayList<MaterialDtoList>,
                  var  name:String,var refMultiId:Int,var stepDtoList: ArrayList<StepDtoList>,var deviceTypeName:String,var multiStep:String):Serializable{

    }




}
class MutilStepItem(){


     var downTemperature: String?=null
    var modelCode: String?=null
    var modelName: String?=null
    var no: String?=null
    var temperature: String?=null
    var time: String?=null
    var upTemperature: String?=null

    var steamQuantity: String?=null


    fun exchange(): RecipeStepBean {

        var mRecipeStepBean= RecipeStepBean()

        mRecipeStepBean.work_mode= modelCode?.toInt()!!
        if (mRecipeStepBean.work_mode==14) {
            mRecipeStepBean.temperature = upTemperature?.toInt()!!
            mRecipeStepBean.temperature2= downTemperature?.toInt()!!
        } else {
            mRecipeStepBean.temperature = temperature?.toInt()!!
            mRecipeStepBean.temperature2= temperature?.toInt()!!
        }

        mRecipeStepBean.time= time?.toInt()!!
        mRecipeStepBean.steam_flow= steamQuantity?.toInt()!!


        return mRecipeStepBean

    }
}


