package com.robam.roki.request.param

import com.robam.roki.net.base.BaseParam

class ShareParam(var userId:String,var curveCookbookId:Int) : BaseParam() {
}

class ShareMutilParam(var userId:Long) : BaseParam() {
}