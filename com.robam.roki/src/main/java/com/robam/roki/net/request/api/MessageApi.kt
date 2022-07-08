package com.robam.roki.net.request.api

import com.legent.plat.Plat
import com.legent.plat.events.MessageEventNumber
import com.legent.utils.EventUtils
import com.legent.utils.api.PreferenceUtils
import com.robam.roki.net.OnRequestListener
import com.robam.roki.net.request.bean.MessageListBean
import com.robam.roki.net.request.bean.MessageUserUnreadBean
import com.robam.roki.net.request.bean.ResultBean
import com.robam.roki.net.request.param.MessageStatusChangeParam

val  SYSTEMMESSAGE=1;
val  DEVICEMESSAGE=2;
val  WORDSWRITTE=3
class MessageApi(mRequestListener: OnRequestListener): com.robam.roki.net.base.BaseApi(mRequestListener) {

    val messageStatusUrl="/rest/ops/api/systemMessage"
    fun messageStatusChange(mRequestId:Int, allSet:Boolean=false, ids:IntArray
                            , messageType:Int){
        doPost(mRequestId, messageStatusUrl,MessageStatusChangeParam(Plat.accountService.currentUserId,allSet,ids,messageType),ResultBean::class.java)
    }

    val unreadMessageUrl="/rest/ops/api/systemMessage"
    fun getUserUnreadMessage(mRequestId:Int){
        var request=HashMap<String,Any>()
        if (Plat.accountService.isLogon) {
            doGet(
                mRequestId,
                unreadMessageUrl + "/" + Plat.accountService.currentUserId,
                request,
                MessageUserUnreadBean::class.java
            )
        }else{
            EventUtils.postEvent(MessageEventNumber(0))
        }
    }

    val unreadMessagePageUrl="/rest/ops/api/systemMessage/page"

    fun getUserMessageList(mRequestId:Int,
                           messageType:Int,page:Int=0,size:Int=10){
        var request=HashMap<String,Any>()
        request["userId"] = Plat.accountService.currentUserId
        request["messageType"] = messageType
        request["page"] = page
        request["size"] = size
        doGet(mRequestId, unreadMessagePageUrl,request,
            MessageListBean::class.java)
    }
}