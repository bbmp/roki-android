package com.robam.roki.net.request.api

import com.legent.plat.Plat
import com.robam.roki.net.OnRequestListener
import com.robam.roki.net.base.BaseApi
import com.robam.roki.net.request.bean.AlumListBean
import com.robam.roki.net.request.bean.DeleteAlbumParam
import com.robam.roki.net.request.bean.ResultBean
import com.robam.roki.net.request.param.AlumListParam
import com.robam.roki.net.request.param.PraiseParam
import com.robam.roki.net.request.param.SubmitPublishParam
import com.robam.roki.net.request.param.UserListParam

class PublishApi(mRequestListener: OnRequestListener): BaseApi(mRequestListener) {
    companion object {
        const val getUserList = 100
    }

    val submitUrl="/zuul/rest/cks/api/cooking/album/add"

    val updateUrl="/rest/cks/api/cooking/img/upload"


    val deleteUrl="/rest/cks/api/cooking/album/delete"


    val praiseUrl="/rest/cks/api/cooking/album/point-praise"


    val noPraiseUrl= "/rest/cks/api/cooking/album/cancel-point-praise"


//    {’userId’: '用户ID’, 'albumId’: '作品ID’}
    fun noPraise(mRequestId:Int, albumId:Long){

        doPost(mRequestId,noPraiseUrl,PraiseParam(albumId=albumId),ResultBean::class.java)
    }
    fun submitPublish(mRequestId:Int, cookBookId:Long,  content:String, fileList:List<String>){
         var request: HashMap<String,Any> = HashMap()
        request["cookBookId"]=cookBookId
        request["fileList"]= fileList

        request["content"]=content

        request["userId"]=Plat.accountService.currentUserId

        doFilePost(mRequestId,submitUrl, request,
            ResultBean::class.java)
    }


    fun praisePublic(mRequestId:Int, albumId:Long){
        doPost(mRequestId,praiseUrl, PraiseParam(albumId=albumId),ResultBean::class.java)
    }


    fun deletePublic(mRequestId:Int,id:Long){
       doPost(mRequestId,deleteUrl, DeleteAlbumParam(id),ResultBean::class.java)
    }



    val getListALLUrl="/rest/cks/api/cooking/album/cookbook/get"


    fun getList(mRequestId:Int,cookBookId:Long){
        doPost(mRequestId,getListALLUrl, AlumListParam(cookBookId), AlumListBean::class.java)
    }



    val getUserListUrl="/rest/cks/api/cooking/album/my/get"
    fun getUserList(mRequestId:Int){


        doPost(mRequestId,getUserListUrl, UserListParam(Plat.accountService.currentUserId), AlumListBean::class.java)
    }
}