package com.robam.common.services

import com.legent.Callback
import com.legent.services.AbsService
import com.robam.common.io.cloud.Reponses
import com.robam.common.io.cloud.Requests
import com.robam.common.io.cloud.RokiRestHelper

class CurveService: AbsService() {


    companion object{
    private val instance = CurveService()

    @Synchronized
    fun getInstance(): CurveService{
        return instance
    }

    }

    fun deleteCurve(id:Int,callback: Callback<Reponses.CurveStepDelete> ){
        RokiRestHelper.deleteCurveStep(id,object:Callback<Reponses.CurveStepDelete>{
            override fun onSuccess(result: Reponses.CurveStepDelete?) {
                callback.onSuccess(result)
            }
            override fun onFailure(t: Throwable?) {
                callback. onFailure(t)
            }
        })
    }


    fun updateCurve(cookingCurveDto: Requests.cookingCurveDto, callback: Callback<Reponses.CurveStepDelete> ){
        RokiRestHelper.updateCurveStep(cookingCurveDto,object :Callback<Reponses.CurveStepDelete>
        {
            override fun onSuccess(result: Reponses.CurveStepDelete?) {
                callback.onSuccess(result)
            }

            override fun onFailure(t: Throwable?) {
                callback.onFailure(t)
            }

        })


        
    }
}