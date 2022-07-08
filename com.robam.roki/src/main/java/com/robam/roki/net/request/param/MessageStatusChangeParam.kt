package com.robam.roki.net.request.param

import com.robam.roki.net.base.BaseParam
//"allSet": false,
//"ids": [
//0
//],
//"messageType": 0,
//"userId": 0
class MessageStatusChangeParam(var userId :Long,var allSet:Boolean=false,var ids:IntArray
,var messageType:Int):BaseParam()