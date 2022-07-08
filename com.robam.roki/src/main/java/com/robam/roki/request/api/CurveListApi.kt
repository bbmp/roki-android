package com.robam.roki.request.api

import com.legent.plat.Plat
import com.robam.roki.net.OnRequestListener
import com.robam.roki.net.base.BaseApi
import com.robam.roki.net.base.BaseBean
import com.robam.roki.request.bean.CurveDetailCookBean
import com.robam.roki.request.bean.CurveListBean
import com.robam.roki.request.bean.ResultBean
import com.robam.roki.request.bean.ShareBean
import com.robam.roki.request.param.*
import kotlin.collections.HashMap

class CurveListApi( mRequestListener: OnRequestListener): BaseApi(mRequestListener) {
    public final val DEL_CURVE = 1000
    public final val CURVE_MARK_STEP = 1001

    val curveListUrl="/rest/cks/api/curve_cookbook/v2/cooking_curve/queryByUserId";



    val curveCookDetailUrl=
            "/rest/cks/api/curve_cookbook/v2/cooking_curve/query"

    val deleteCurveUrl="/rest/cks/api/curve_cookbook/queryCurveCookbooks";

    val UserPhoneUrl="/rest/ums/api/findUserInfoByPhone"

    val shareUrl=
            "/rest/cks/api/curve_cookbook/shareCurveCookbook"
    val shareUrlMutil=
        "/rest/cks/api/multi/share/"
    /**
     * 删除菜谱烹饪曲线【烹饪步骤】路径参数
     */
//    val delCurveUrl= "/rest/cks/api/curve_cookbook/deleteCurveCookbook/%s"
    val delCurveUrl= "/rest/cks/api/curve_cookbook/deleteCurveCookbook"

    var cookingCurveMarkStep = "/rest/cks/api/curve_cookbook/v2/cooking_curve/markStep" //更新标记步骤

    fun getList(requestId:Int,userId:String= Plat.accountService.currentUserId.toString()){
        doPost(requestId,curveListUrl, QueryCurveParam(userId), CurveListBean::class.java)
    }


    fun getListDetail(requestId:Int, id: Int){
        doPost(requestId,curveCookDetailUrl, CookDetailParam(id), CurveDetailCookBean::class.java)
    }

    fun deleteCurveCookbook(requestId:Int,curveCookbookId:String,userId:String){
        var map:HashMap<String,Any> = HashMap()
        map["userId"] = userId
        map["curveCookbookId"]=curveCookbookId
        doGet(requestId,deleteCurveUrl, map, ResultBean::class.java)
    }

    fun searchPhone(requestId:Int,phone:String){
        var map:HashMap<String,Any> = HashMap()
        map["phone"] = phone
//        map["curveCookbookId"]=curveCookbookId
        doGet(requestId,UserPhoneUrl,map, ShareBean::class.java)
    }

    fun shareCookBook(requestId:Int, curveCookbookId:Int,userId: String){
        doPost(requestId,shareUrl, ShareParam(userId
            ,curveCookbookId),ResultBean::class.java)
    }


    fun shareMutilList(requestId:Int, id:Int,userId: Long){



        doPost(requestId,shareUrlMutil+id, ShareMutilParam(userId),ResultBean::class.java)
    }
    /**
     * 删除烹饪记录的多段记录()
     */
    fun delCurve(mRequestId:Int, id:Int , userId:Int){
        var request:HashMap<String,Any> = HashMap()
//        request["id"] = id;
//        val format = String.format(delCurveUrl, id)
//        doDel(mRequestId,format,request, BaseBean::class.java)
        request["curveCookbookId"]=id
        request["userId"]=userId
        doGet(mRequestId , delCurveUrl , request , BaseBean::class.java)
    }

    fun cookingCurveMarkStep(requestId:Int, id:String, stepDtoList:List<RequestsParam.CookingCurveMarkStepList>,){
        doPost(requestId,cookingCurveMarkStep, RequestsParam.CookingCurveMarkStepRequest(id,stepDtoList), RequestsParam.CookingCurveMarkStepRequest::class.java)
    }

}