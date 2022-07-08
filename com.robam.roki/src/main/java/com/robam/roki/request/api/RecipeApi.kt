package com.robam.roki.request.api

import com.robam.common.pojos.Ingredient
import com.robam.roki.net.OnRequestListener
import com.robam.roki.net.base.BaseApi
import com.robam.roki.net.base.BaseBean
import com.robam.roki.net.request.bean.RecipeCustomBean
import com.robam.roki.net.request.bean.RecipeDetailBean
import com.robam.roki.net.request.bean.RecipeRecordListBean
import com.robam.roki.net.request.bean.ResultBean
import com.robam.roki.request.param.RecipeRerordListParam
import com.robam.roki.request.param.RecipeSaveParam
import com.robam.roki.utils.StringUtil


class RecipeApi(mOnRequestListener: OnRequestListener) : BaseApi(mOnRequestListener) {
    val DELRECIPEMULT = 1001;

    var findRecordList="/rest/cks/api/customize/multi"
    var saveRecipeList="/rest/cks/api/customize/multi/cookbook"
    var detailUrl= "/rest/cks/api/customize/cookbook/"


    var detailRecipeUrl="/rest/cks/api/customize/cookbook/page"

    /**
     * 删除烹饪记录的多段记录（路径参数）
     */
    var delRecipeMultiUrl="/rest/cks/api/customize/multi/%s"

    fun getRecordList(mRequestId:Int,deviceGuid:String){

        var request= HashMap<String,Any>()
        request.put("deviceGuid",deviceGuid)
        doGet(mRequestId,findRecordList, request, RecipeRecordListBean::class.java)
    }
    fun setSaveRecipeList(mRequestId:Int,mRecipeSaveParam:RecipeSaveParam){
        doPost(mRequestId,saveRecipeList, mRecipeSaveParam,ResultBean::class.java)
    }

    fun getRecipeDetail(mRequestId:Int,id:Int){
        var request= HashMap<String,Any>()
        doGet(mRequestId,detailUrl+id,request, RecipeDetailBean::class.java)
    }

    fun getRecipeCustomList(mRequestId:Int, page:Int=0, pageSize :Int=10,name:String,deviceGuid:String){
        var request:HashMap<String,Any> = HashMap()
        request["page"] = page;
        request["pageSize"] = pageSize;
        request["name"] = name;
        request["deviceGuid"] = deviceGuid;
        doGet(mRequestId,detailRecipeUrl,request,RecipeCustomBean::class.java)

    }

    /**
     * 删除烹饪记录的多段记录()
     */
    fun detailRecipeMultiUrl(mRequestId:Int, id:Int){
        var request:HashMap<String,Any> = HashMap()
//        request["id"] = id;
        val format = String.format(delRecipeMultiUrl, id)
        doDel(mRequestId,format,request,BaseBean::class.java)

    }


}