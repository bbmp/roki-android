package com.robam.roki.request.api


import com.legent.plat.Plat
import com.robam.roki.net.OnRequestListener
import com.robam.roki.net.base.BaseApi
import com.robam.roki.request.bean.RecipeCurveSuccessBean
import com.robam.roki.request.bean.ResultBean
import com.robam.roki.request.bean.SubmitCurveBean
import com.robam.roki.request.param.CookStatusParam
import com.robam.roki.request.param.RecipeSuccessParam

class RecipeSuccessApi(mOnRequestListener: OnRequestListener): BaseApi(mOnRequestListener) {

    val queryUrl= "/rest/cks/api/curve_cookbook/v2/cooking_curve/query"
    val submitUrl="/rest/cks/api/curve_cookbook/v2/cooking_curve/update"


    val cookingStateUrl="/rest/cks/api/curve_cookbook/v2/cooking_curve/updateCurveState"

    val submitStepUrl="/rest/cks/api/curve_cookbook/v2/cooking_curve_step/update"
    val deleteUrl="/rest/cks/api/curve_cookbook/v2/cooking_curve_step/delete"
    fun query(layoutId:Int,id:Long){
        doPost(layoutId,queryUrl,RecipeSuccessParam(id),
            RecipeCurveSuccessBean::class.java)
    }
    fun submitCookingStepCurve(layoutId:Int, mRecipeCurveSuccessBean: RecipeCurveSuccessBean){
        mRecipeCurveSuccessBean.payload?.userId=Plat.accountService.currentUserId.toString()
        mRecipeCurveSuccessBean.payload?.temperatureCurveParams=null
        doPost(layoutId,submitStepUrl,mRecipeCurveSuccessBean.payload,
            SubmitCurveBean::class.java)
    }
    fun submitCookingCurve(layoutId:Int, mRecipeCurveSuccessBean: RecipeCurveSuccessBean){
        mRecipeCurveSuccessBean.payload?.userId=Plat.accountService.currentUserId.toString()
        mRecipeCurveSuccessBean.payload?.temperatureCurveParams=null
        doPost(layoutId,submitUrl,mRecipeCurveSuccessBean.payload,
            SubmitCurveBean::class.java)
    }

    fun delete(layoutId:Int,id:String){
        doPost(layoutId, "$deleteUrl/$id",RecipeSuccessParam(id.toLong()),
            ResultBean::class.java) }


    companion object {
        val CONTINUE = 1
        val END = 3
        val PAUSE = 2
    }
    /**
     *  state 取值 1-继续；2-暂停；3-结束, 格式:{’id’: 1, 'state’: 2}
     */
    fun setStatus(layoutId:Int,id:Int,state:Int){

        doPost(layoutId,cookingStateUrl, CookStatusParam(id,state),
                ResultBean::class.java)

    }
}