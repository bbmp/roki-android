package com.robam.roki.net.request.param

import com.legent.plat.Plat
import com.robam.roki.net.base.BaseParam

//{’userId’: '用户ID’, 'albumId’: '作品ID’}
class PraiseParam(var userId:Long=Plat.accountService.currentUserId, var albumId:Long):BaseParam() {
}