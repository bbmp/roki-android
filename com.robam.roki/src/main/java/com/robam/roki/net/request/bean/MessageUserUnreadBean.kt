package com.robam.roki.net.request.bean

import com.robam.common.pojos.PayLoad

class MessageUserUnreadBean(var msg:String,var rc:Int,
                            var payload: PayLoad
) {
    inner class PayLoad(var chatUnReadCount:Int,var deviceUnReadCount:Int,
                  var sysUnReadCount:Int,var totalUnReadCount:Int)

}

