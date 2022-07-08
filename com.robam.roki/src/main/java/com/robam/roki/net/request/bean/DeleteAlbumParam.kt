package com.robam.roki.net.request.bean

import com.legent.plat.Plat
import com.robam.roki.net.base.BaseParam

class DeleteAlbumParam(var albumId:Long,var userId:Long=Plat.accountService.currentUserId): BaseParam() {
}