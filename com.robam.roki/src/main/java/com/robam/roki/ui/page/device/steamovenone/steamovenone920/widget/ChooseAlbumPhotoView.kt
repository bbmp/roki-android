package com.robam.roki.ui.page.device.steamovenone.steamovenone920.widget

import android.Manifest
import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.text.TextUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.robam.roki.R
import com.robam.roki.ui.page.curve.bitmap2Path
import com.robam.roki.utils.PermissionsUtils
import com.robam.roki.utils.PickImageHelperTwo
import com.xiaomi.push.it
import kotlinx.android.synthetic.main.view_choose_album_photo.view.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

/**
 * 照片选择的view
 */

interface  IHasChoosePic{
    fun setImage(path:String)
}
class ChooseAlbumPhotoView : RelativeLayout, PickImageHelperTwo.PickCallbackTwo {
    lateinit var pickHelper: PickImageHelperTwo
    constructor(context: Context):super(context){
        initView(null,0);
    }

    var mIHasChoosePic:IHasChoosePic?=null


    fun setIHasChoosePic(mIHasChoosePic:IHasChoosePic?){
        this.mIHasChoosePic=mIHasChoosePic
    }


    constructor(context: Context,attrs: AttributeSet):super(context,attrs){
        initView(attrs,0);
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr:Int):super(context,attrs){
        initView(attrs,defStyleAttr);
    }

    fun setContext(context:String="上传一张成品图作为封面吧"){
        view_choose_text_description.text=context;
    }

    private fun initView(attrs: AttributeSet?,defStyleAttr:Int ) {
        LayoutInflater.from(context).inflate(R.layout.view_choose_album_photo,this)
        pickHelper=PickImageHelperTwo(context as Activity?,this)
        view_choose_album.setOnClickListener {
            takePhoto()

        }

        img_recipe_icon_delete.setOnClickListener {
            view_choose_image_bg.visibility=View.GONE
            img_recipe_icon_delete.visibility=View.GONE
            view_choose_album.visibility=View.VISIBLE
            view_choose_text_description.visibility=View.VISIBLE
            this.filePath=""
        }

    }

    fun takePhoto(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val selfPermission =
                ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
            if (selfPermission == 0) {
                pickHelper.showPickDialog("")
            } else {
                PermissionsUtils.checkPermission(
                    context,
                    Manifest.permission.CAMERA,
                    PermissionsUtils.CODE_USER_INFO_SHARE
                )
            }
        } else {
           pickHelper.showPickDialog("")
        }
    }

    fun setDelete(){
        img_recipe_icon_delete.visibility=View.GONE
    }

    fun getImagePath()=filePath
    var filePath:String=""
    override fun onPickComplete(bmp: Bitmap?) {
        view_choose_album.visibility= View.GONE
        view_choose_text_description.visibility= View.GONE
        val fileDirPath: String? = context?.filesDir?.absolutePath
        this.filePath= fileDirPath + File.separator +
                SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA)
                    .format(System.currentTimeMillis()) + ".jpg"
        bmp?.let { bitmap2Path(it, filePath) }
        img_recipe_icon_delete.visibility=View.VISIBLE
        view_choose_image_bg.visibility=View.VISIBLE

        Glide.with(context).load(bmp).into(view_choose_image_bg)
        mIHasChoosePic?.setImage(filePath)
    }

    fun setImageview(filePath:String,isPreview:Boolean=false){
        if(filePath?.isNotEmpty()){
            this.filePath=filePath;
            view_choose_album.visibility= View.GONE
            view_choose_text_description.visibility= View.GONE
            view_choose_album.isEnabled=true
            img_recipe_icon_delete.visibility = View.VISIBLE
            view_choose_image_bg.visibility=View.VISIBLE
            Glide.with(context).load(filePath).error(R.mipmap.icon_recipe_default).into(view_choose_image_bg)
            mIHasChoosePic?.setImage(filePath)
        }
    }
    override fun onPickComplete(filePath: String?) {
       Glide.with(context).load(filePath).into(view_choose_image_bg)

        view_choose_image_bg.visibility=View.VISIBLE
        view_choose_album.visibility= View.GONE
        view_choose_text_description.visibility= View.GONE

        img_recipe_icon_delete.visibility=View.VISIBLE
        if (filePath != null) {
            this.filePath=filePath
        };

    }
}