package com.robam.roki.net.request.param

import com.legent.plat.Plat
import com.robam.roki.net.base.BaseParam

class AlumListParam(var cookBookId:Long,var userId:Long=Plat.accountService.currentUserId): BaseParam() {
}