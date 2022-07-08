package com.robam.roki.net.request.bean

import com.legent.plat.Plat
import com.robam.roki.db.model.RecipeStepBean
import java.io.Serializable

data class RecipeRecordListBean(
    var datas: List<Data>,
    var hasNext: Boolean,
    var msg: String,
    var rc: Int,
    var totalPage: Int,
    var totalSize: Int
):Serializable

 data class Data(var createDateTime: String=System.currentTimeMillis().toString(),
    var deviceGuid: String="",
    var id: Int=1,
    var multiStepDtoList: ArrayList<MultiStepDto> = ArrayList(),
    var name: String="测试",
    var userId: Int=Plat.accountService.currentUserId.toInt()
):Serializable

data class MultiStepDto(
    var downTemperature: Int,
    var modelCode: String,
    var modelName: String,
    var no: Int,
    var steamQuantity: String?=null,
    var temperature: Int,
    var time: Int,
    var upTemperature: Int,

):Serializable{


    fun exchange(): RecipeStepBean {
        var mRecipeStepBean=RecipeStepBean()
        mRecipeStepBean.work_mode=modelCode.toInt()
        mRecipeStepBean.temperature=temperature
        mRecipeStepBean.temperature2=downTemperature
        mRecipeStepBean.time=time
        steamQuantity?.toInt()?.let {
            mRecipeStepBean.steam_flow=it;
        }

        return mRecipeStepBean

    }
}