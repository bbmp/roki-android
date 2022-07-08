package com.robam.roki.net.request.bean

import com.robam.common.pojos.CookAlbum
import com.xiaomi.push.co

class AlumListBean(
    var msg: String? = null,
    var albumList: List<AlbumList>? = null,
    var hasPrevious: String? = null,
    var hasNext: String? = null,
    var rc: String? = null)


class AlbumList(var id:String,var imgUrl:String,var praiseCount:String,
                var content:String,var hasPraised:Boolean,var cookbookId:String,var ownerId:String,
                var  ownerFigureUrl:String,var ownerName:String,var cookbookName:String,
                var createdTime:String,var cookingAlbumFileDtoList:ArrayList<CookingAlbumFileDtoList>){

    fun ex(): CookAlbum{
        var mCookAlbum=CookAlbum();
        mCookAlbum.id=this.id.toLong();
        if (cookingAlbumFileDtoList!=null&&cookingAlbumFileDtoList.size>0) {
            if (cookingAlbumFileDtoList?.get(0)?.fileUrl.isNotEmpty()) {
                mCookAlbum.imgUrl = cookingAlbumFileDtoList?.get(0)?.fileUrl;
            }
        }else{
            mCookAlbum.imgUrl=""
        }
        mCookAlbum.bookName=cookbookName
        mCookAlbum.hasPraised=hasPraised;
        mCookAlbum.ownerId=ownerId.toLong()
        mCookAlbum.ownerName=ownerName
        mCookAlbum.bookId= cookbookId.toLong()
        mCookAlbum.desc=content
        mCookAlbum.praiseCount=praiseCount.toInt()
        mCookAlbum.createdTime=createdTime
        mCookAlbum.url=ArrayList()
        cookingAlbumFileDtoList?.let {

            it.forEach {
                mCookAlbum.url.add(it.fileUrl)

            }
        }
        return mCookAlbum;
    }
}


class CookingAlbumFileDtoList(var fileUrl:String,var verifyFlag:String)
