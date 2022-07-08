package com.robam.roki.request.param

import com.legent.plat.Plat
import com.robam.roki.net.base.BaseParam
import com.robam.roki.request.bean.Units
import java.io.Serializable

data class RecipeSaveParam(var coverImg:String?
,var materialDtoList:ArrayList<MaterialDtoList>,var name:String
,var refMultiId:Int,var stepDtoList:ArrayList<StepDtoList>
,var deviceTypeName:String,var userId:Long=Plat.accountService.currentUserId): BaseParam() ,Serializable{


}

data class MaterialDtoList(var id:Int?,var materialName:String
     ,var sourceType:Int,var unitId:Int,var unitName:String,var weight:String): Serializable {
           var mUnitsList:ArrayList<Units>? = ArrayList()
}

data class StepDtoList(var description:String="",var id:Int?=1,var no:Int=1,var stepImg:String?=""):Serializable{

}