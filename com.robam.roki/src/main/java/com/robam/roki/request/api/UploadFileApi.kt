package com.robam.roki.request.api

import com.aispeech.libbase.export.bean.CheckUpdateResultBean
import com.robam.roki.net.OnRequestListener
import com.robam.roki.net.base.BaseApi
import com.robam.roki.request.bean.ResultBean
import com.robam.roki.request.bean.UpFileBean
import com.robam.roki.request.param.UploadFileParam

class UploadFileApi(mOnRequestListener: OnRequestListener):BaseApi(mOnRequestListener) {
    //audio 4
    //image 1
    //type 1意见 2图片 3视频 4语音 10日志
    val url= "/rest/ops/api/file/upload"
    fun uploadFile(requestCode:Int, path:String, type:String="1"){
        var map:HashMap<String,Any> = HashMap()
        map["type"] = type
        map["path"] = path
        doFilePost(requestCode,url, map, UpFileBean::class.java)
    }
}