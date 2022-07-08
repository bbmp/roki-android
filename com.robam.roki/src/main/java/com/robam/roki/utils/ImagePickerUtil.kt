package com.robam.roki.utils

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.provider.DocumentsContract
import android.provider.MediaStore
import androidx.fragment.app.Fragment

interface IChoosePicture{

    fun onActivityResult(requestCode:Int,resultCode:Int,data:Intent?)


    fun choosePicture(mFragment: Fragment,mIResultPicture:IResultPicture);



}


interface IResultPicture{
    fun onFilePath(path:String?)
}
class ImagePickerUtil:IChoosePicture, PickImageHelperTwo.PickCallbackTwo {

    lateinit var pickHelper: PickImageHelperTwo
    val IMAGE_REQUEST_CODE=0x124
    override fun onActivityResult(requestCode: Int,
                                  resultCode: Int,
                                  data: Intent?) {

        if (resultCode!=RESULT_OK){
            return
        }else{
            when(requestCode){
                IMAGE_REQUEST_CODE->{
                    data?.let { handleImageOnKitKat(it) }
                }

            }
        }



    }

    lateinit var mIResultPicture:IResultPicture
    lateinit var mContext:Context
    override fun choosePicture(mFragment: Fragment,mIResultPicture:IResultPicture) {
        this.mIResultPicture=mIResultPicture;



        mFragment.context?.let {
            this.mContext=it
        }
        pickHelper=PickImageHelperTwo(mFragment.activity,this)



        val intent = Intent("android.intent.action.GET_CONTENT")
        intent.type = "image/*";
        mFragment.startActivityForResult(intent,IMAGE_REQUEST_CODE)
    }
    private fun getImagePath(uri: Uri, selection: String?): String? {
        var path: String? = null
        //通过Uri和selection来获取真实的图片路径
        val cursor: Cursor? =mContext.contentResolver.query(uri, null, selection, null, null)
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA))
            }
            cursor.close()
        }
        return path
    }

    var imagePath:String?=""

    private fun handleImageOnKitKat(data: Intent) {
        data.data?.let { uri->


            if (DocumentsContract.isDocumentUri(mContext, uri)) {
                //如果是document类型的Uri，则通过document id处理
                val docId: String = DocumentsContract.getDocumentId(uri)
                if ("com.android.providers.media.documents" == uri.authority) {
                    val id = docId.split(":").toTypedArray()[1] //解析出数字格式的id
                    val selection = MediaStore.Images.Media._ID + "=" + id
                    imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection)
                } else if ("com.android.providers.downloads.documents" == uri.authority) {
                    val contentUri: Uri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public downloads"),
                        java.lang.Long.valueOf(docId)
                    )
                    imagePath = getImagePath(contentUri, null)
                }
            } else if ("content".equals(uri.getScheme(), ignoreCase = true)) {
                //如果是file类型的Uri，直接获取图片路径即可
                imagePath = getImagePath(uri, null)
            } else if ("file".equals(uri.getScheme(), ignoreCase = true)) {
                //如果是file类型的Uri，直接获取图片路径即可
                imagePath = uri.path
            }
            mIResultPicture.onFilePath(imagePath) //根据图片路径显示图片

        }

    }

    override fun onPickComplete(bmp: Bitmap?) {

    }

    override fun onPickComplete(bmp: String?) {
    }
}