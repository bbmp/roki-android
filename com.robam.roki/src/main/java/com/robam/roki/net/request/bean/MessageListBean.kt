package com.robam.roki.net.request.bean

class MessageListBean(var rc:Int,var msg:String,
var totalSize:Int
,var totalPage:Int,var datas:ArrayList<Datas>) {


    class Datas(var readFlag:Boolean,var id:Int,var msgType:Int,
                var msgTitle:String,var msgSubTitle:String,var msgContent:String,
                var  msgAction:String,var msgScope:String,var fileKey:String,var createTime:String,
                var updateTime:Long,var userId:Long,var msgExtra:String){

    }
}