package com.robam.roki.ui.dialog

import android.app.Dialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.os.Environment
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import cn.sharesdk.dingding.friends.Dingding
import cn.sharesdk.sina.weibo.SinaWeibo
import cn.sharesdk.tencent.qq.QQ
import cn.sharesdk.wechat.friends.Wechat
import cn.sharesdk.wechat.moments.WechatMoments
import com.legent.ui.ext.dialogs.AbsDialog
import com.legent.utils.EventUtils
import com.legent.utils.api.ToastUtils
import com.legent.utils.api.ViewUtils
import com.robam.roki.R
import com.robam.roki.model.helper.ShareHelper
import com.robam.roki.net.OnRequestListener
import com.robam.roki.request.api.CurveListApi
import com.robam.roki.request.bean.ShareBean
import com.robam.roki.ui.mdialog.RecipteMutiShareDialog
import com.robam.roki.ui.widget.dialog.MarkPointNameDialog
import com.robam.roki.ui.widget.dialog.ShareDialog
import kotlinx.android.synthetic.main.dialog_cookbook_theme_share.imgFriend
import kotlinx.android.synthetic.main.dialog_cookbook_theme_share.imgMoment
import kotlinx.android.synthetic.main.dialog_cookbook_theme_share.imgQQ
import kotlinx.android.synthetic.main.dialog_cookbook_theme_share.imgQQzone
import kotlinx.android.synthetic.main.dialog_cookbook_theme_share.img_Dingding
import kotlinx.android.synthetic.main.dialog_cookbook_theme_share.img_weibo
import kotlinx.android.synthetic.main.dialog_cookbook_theme_share.ll_copy_url
import kotlinx.android.synthetic.main.dialog_cookbook_theme_share.ll_save_url
import kotlinx.android.synthetic.main.dialog_cookbook_theme_share.tv_close
import kotlinx.android.synthetic.main.dialog_cookbook_theme_share_muti.*
import java.io.*

fun copyAssetGetFilePath(assetsFilename: String,mContext:Context): String? {
    try {
        val filesDir: File = mContext.filesDir
        if (!filesDir.exists()) {
            filesDir.mkdirs()
        }
        val outFile = File(filesDir, assetsFilename)
//        val outFilename = outFile.absolutePath

        if (!outFile.exists()) {
            val res = outFile.createNewFile()
            if (!res) {

                return null
            }
        }
        val `is`: InputStream = mContext.getAssets().open(assetsFilename)
        val fos = FileOutputStream(outFile)
        val buffer = ByteArray(1024)
        var byteCount: Int
        while (`is`.read(buffer).also { byteCount = it } != -1) {
            fos.write(buffer, 0, byteCount)
        }
        fos.flush()
        `is`.close()
        fos.close()
        return outFile.path
    } catch (e: IOException) {
        e.printStackTrace()
    }
    return null
}

class FreshAlbum(var path:String)
public class ScreenshotUtil {
    /**
     * 截取指定activity显示内容
     * 需要读写权限
     */

    companion object {
        public fun saveScreenshotFromActivity(activity: Context, dialog: Dialog) {
            var view = dialog.window?.decorView;
            view?.setDrawingCacheEnabled(true);
            var bitmap = view?.getDrawingCache();
            if (bitmap != null) {
                saveImageToGallery(bitmap, activity)
            };
        }

        /**
         * 截取指定View显示内容
         * 需要读写权限
         */
        public fun saveScreenshotFromView(view: View, context: Context) {
            view.setDrawingCacheEnabled(true);
            var bitmap = view.getDrawingCache();
            saveImageToGallery(bitmap, context);
            //回收资源
            view.setDrawingCacheEnabled(false);
            view.destroyDrawingCache();
        }

        /**
         * 保存图片至相册
         * 需要读写权限
         */
        private fun saveImageToGallery(bmp: Bitmap, context: Context) {
            var appDir = File(getDCIM());
            if (!appDir.exists()) {
                appDir.mkdir();
            }
            var fileName = System.currentTimeMillis().toString() + ".jpg";
            var file = File(appDir, fileName);
            try {
                var fos = FileOutputStream(file);
                bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.flush();
                fos.close();
            } catch (e: FileNotFoundException) {
                e.printStackTrace();
            } catch (e: IOException) {
                e.printStackTrace();
            }
            EventUtils.postEvent(FreshAlbum(getDCIM()))
            // 通知图库更新
//            context.sendBroadcast(
//                Intent(
//                    Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
//                    Uri.parse("file://" + getDCIM())
//                )
//            );
        }

        /**
         * 获取相册路径
         */
        private fun getDCIM(): String {
            if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
                return "";
            }
            var path = Environment.getExternalStorageDirectory().getPath() + "/dcim/";
            if (File(path).exists()) {
                return path;
            }
            path = Environment.getExternalStorageDirectory().getPath() + "/DCIM/";
            var file = File(path);
            if (!file.exists()) {
                if (!file.mkdirs()) {
                    return "";
                }
            }
            return path;
        }
    }
}
class ImageShareDialog(context:Context) : AbsDialog(context,R.style.Theme_Dialog_kit_share),
    OnRequestListener {
    override fun getViewResId(): Int =  R.layout.dialog_cookbook_theme_share_muti

    private lateinit var mCurListApi: CurveListApi

   lateinit var  mRecipteMutiShareDialog: RecipteMutiShareDialog


    public fun setDialog(mRecipteMutiShareDialog: RecipteMutiShareDialog):ImageShareDialog{
        this.mRecipteMutiShareDialog=mRecipteMutiShareDialog
        return this;
    }
    init {
        ViewUtils.setBottmScreen(cx, this)
        mCurListApi=CurveListApi(this)
    }






    companion object{

        var url=""
        var title=""
        lateinit var mImageShareDialog:ImageShareDialog
        var id=0L
        @JvmStatic
        fun show(cx: Context,url:String,title:String,id:Long): ImageShareDialog{
            mImageShareDialog = ImageShareDialog(cx)
            mImageShareDialog.show()
            this.url=url
            this.title=title
            this.id=id;
            return mImageShareDialog
        }
    }

    fun saveBitmapFile(bitmap: Bitmap): String? {
        val file =
            File(Environment.getExternalStorageDirectory().toString() + "/pic/01.jpg") //将要保存图片的路径
        try {
            val bos = BufferedOutputStream(FileOutputStream(file))
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos)
            bos.flush()
            bos.close()
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            return file.absolutePath
        }
    }
//    var imagePath=""
    fun shareImg(platKey:String){


    var path=copyAssetGetFilePath("share_image.png",context)

        ShareHelper.shareWebFromUrlLocal(context,platKey,"多段烹饪",title+url,url,path)
//
//    (cx, platKey,
//            mRecipteMutiShareDialog.saveMyBitmap()?.let { saveBitmapFile(it) });
//        when {
//            Objects.equal(platKey, WechatMoments.NAME) -> {
//                ShareHelper.shareImageFromLocal(cx, platKey, path)
//            }
//            Objects.equal(platKey, Wechat.NAME) -> {
//                ShareHelper.shareMiniprogram(cx, platKey, title, imgSmall, webUrl, wxPath, desc)
//            }
//            Objects.equal(platKey, QQ.NAME) -> {
//                ShareHelper.shareWebByQQ(cx, "", path)
//            }
//            Objects.equal(platKey, SinaWeibo.NAME) -> {
//                ShareHelper.shareSinaWeiboImg(cx, title, path)
//            }
//            Objects.equal(platKey, Dingding.NAME) -> {
//                ShareHelper.shareDingding(cx, title, path, webUrl, desc)
//            }
//        }
    }
    override fun initView(view: View?) {
        super.initView(view)


        imgFriend.setOnClickListener {
            shareImg(Wechat.NAME)
        }


        imgMoment.setOnClickListener {
            shareImg(WechatMoments.NAME)

        }

        img_Dingding.setOnClickListener {
            shareImg(Dingding.NAME)

        }


        img_weibo.setOnClickListener {
            shareImg(SinaWeibo.NAME)

        }

        imgQQ.setOnClickListener {
            shareImg(QQ.NAME)
        }

        imgQQzone.setOnClickListener {
            shareImg(QQ.NAME)
        }

        ll_copy_url.setOnClickListener {
            copyUrl()
        }
        riko_suer.setOnClickListener {
            var mShareDialog= ShareDialog(context,object : MarkPointNameDialog.IResult{
                override fun onName(name: String?) {
                    name?.let { it1 ->
                        mCurListApi.searchPhone(R.layout.dialog_cookbook_theme_share_muti,
                            it1
                        )
                    }
                }
            })
            mShareDialog.create()
            mShareDialog.show()
        }

        ll_save_url.setOnClickListener {
            mRecipteMutiShareDialog?.window?.decorView?.let { it1 ->
                ScreenshotUtil.saveScreenshotFromView(
                    it1,context)
            }
            dismiss()
        }
        tv_close.setOnClickListener {
             dismiss()
        }
    }




    fun getCachePath(context: Context): String {
        val state = Environment.getExternalStorageState()
        var path = ""
        if (state != null && state == Environment.MEDIA_MOUNTED) {
            if (Build.VERSION.SDK_INT >= 8) {
                val file = context.externalCacheDir
                if (file != null) {
                    path = file.absolutePath
                }
                if (TextUtils.isEmpty(path)) {
                    path = Environment.getExternalStorageDirectory()
                        .absolutePath
                }
            } else {
                path = Environment.getExternalStorageDirectory()
                    .absolutePath
            }
        } else if (context.cacheDir != null) {
            path = context.cacheDir.absolutePath
        }
        return path
    }

    //将连接copy到剪切板
    private fun copyUrl() {

//        if (!TextUtils.isEmpty(mPwd)) {
        val cm = cx.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        // 创建普通字符型ClipData
        val mClipData = ClipData.newPlainText("Label", url)
        // 将ClipData内容放到系统剪贴板里。
        cm.setPrimaryClip(mClipData)
        ToastUtils.showShort("复制成功")

//        }
    }

    override fun onFailure(requestId: Int, requestCode: Int, msg: String?, data: Any?) {
        if (requestId==R.layout.item_delele){
            ToastUtils.show("分享失败", Toast.LENGTH_SHORT)
            dismiss()
        }
    }

    override fun onSaveCache(requestId: Int, requestCode: Int, paramObject: Any?) {

    }

    override fun onSuccess(requestId: Int, requestCode: Int, paramObject: Any?) {
      if (requestId==R.layout.dialog_cookbook_theme_share_muti){
          paramObject?.let {
              if (paramObject is ShareBean) {
                  if (!TextUtils.isEmpty(paramObject.userId)) {
                      mCurListApi.shareMutilList(
                          R.layout.item_delele,
                          id.toInt(),
                          paramObject.userId.toLong()
                      )
                  }else{
                      ToastUtils.show("用户不存在",Toast.LENGTH_LONG)
                  }
              }
          }

      }else if (requestId==R.layout.item_delele){
          ToastUtils.show("分享成功", Toast.LENGTH_SHORT)
          dismiss()
      }
    }

}