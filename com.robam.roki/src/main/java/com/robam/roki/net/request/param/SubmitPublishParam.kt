package com.robam.roki.net.request.param

import com.legent.plat.Plat
import com.legent.plat.PlatApp
import com.robam.roki.net.base.BaseParam

class SubmitPublishParam( var cookBookId:Long, var content:String,var fileList:List<String>,
                          var userId:Long) :BaseParam(){
}