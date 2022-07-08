package com.robam.roki.request.api

import com.aispeech.libbase.export.bean.CheckUpdateResultBean
import com.robam.roki.net.OnRequestListener
import com.robam.roki.net.base.BaseApi
import com.robam.roki.request.bean.FlavouringArray
import com.robam.roki.request.bean.FlavouringBean
import com.robam.roki.request.bean.ResultBean
import com.robam.roki.request.bean.UnitListBean
import com.robam.roki.request.param.UnitTypeParam

class FlavouringApi(mOnRequestListener:OnRequestListener ):BaseApi(mOnRequestListener) {

    val flavorUrl="/rest/cks/api/curve_cookbook/findMaterialByName"


    val unitListUrl=
            "/rest/cks/api/unit/list"
    fun getSearch( mRequestId:Int,name:String,ifAccessory:Boolean=false){

//        ?name=%E7%8C%AA%E8%82%89&&ifAccessory=false
        var map:HashMap<String,Any> = HashMap()
        map["name"] = name
        map["ifAccessory"]=ifAccessory



        doGet(mRequestId,flavorUrl,map, FlavouringBean::class.java)
    }


    fun getAllType(mRequestId:Int,type:String="1"){

        doPost(mRequestId,unitListUrl,UnitTypeParam(type), UnitListBean::class.java)

    }
}