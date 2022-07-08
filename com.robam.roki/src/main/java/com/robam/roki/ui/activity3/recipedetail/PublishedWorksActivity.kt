package com.robam.roki.ui.activity3.recipedetail

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.legent.events.ActivityResultOnPageEvent
import com.legent.utils.EventUtils
import com.legent.utils.api.ToastUtils
import com.robam.roki.R
import com.robam.roki.net.OnRequestListener
import com.robam.roki.net.request.api.PublishApi
import com.robam.roki.ui.activity3.AppActivity
import com.robam.roki.ui.page.recipedetail.RecipeDetailPage
import com.robam.roki.utils.PermissionsUtils
import com.robam.roki.utils.PickImageHelperTwo
import kotlinx.android.synthetic.main.activity_published_works.*
import kotlinx.android.synthetic.main.include_title_bar.*
import kotlinx.android.synthetic.main.item_add_picture.view.*
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

fun bitmap2Path(bitmap: Bitmap, path:String ) :String{
    try {
        var  os =  FileOutputStream(path);
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
        os.flush();
        os.close();
    } catch (e: Exception) {
        Log.e("TAG", "", e);
    }
    return path;
}



class PublishedWorkViewHolder(view:View): RecyclerView.ViewHolder(view)
class PublishedWorkAdapter(var mList:ArrayList<String>, var activity: Activity): RecyclerView.Adapter<PublishedWorkViewHolder>() {
    /**
     * 图像处理回调
     */
    var pickCallback = object : PickImageHelperTwo.PickCallbackTwo{
        override fun onPickComplete(bmp: Bitmap?) {
            if (bmp == null) {
                return
            }
            val fileDirPath: String? = activity?.filesDir?.absolutePath
            val filePath = fileDirPath + File.separator +
                    SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA)
                        .format(System.currentTimeMillis()) + ".jpg"
            bitmap2Path(bmp,filePath)
            mList.reverse()
            mList.add(filePath)
            mList.reverse()

            activity.runOnUiThread {
                notifyDataSetChanged()
            }
        }

        override fun onPickComplete(filePath: String?) {

            mList.reverse()
            filePath?.let {
                mList.add(it)
            }

            mList.reverse()
            activity.runOnUiThread {
                notifyDataSetChanged()
            }
        }

        override fun onPickComplete(filePaths: ArrayList<String>?) {
            mList.reverse()
            filePaths?.let {
                mList.addAll(filePaths)
            }

            mList.reverse()
            activity.runOnUiThread {
                notifyDataSetChanged()
            }
        }
    }


    var pickHelper: PickImageHelperTwo? = null
    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): PublishedWorkViewHolder {
        return PublishedWorkViewHolder(LayoutInflater.from(p0.context).inflate(R.layout.item_add_picture,null)) }

    override fun onBindViewHolder(p0: PublishedWorkViewHolder, p1: Int) {


        with(p0.itemView){
            if (p1 == mList.size - 1 && mList[p1] == "") {
//                image_show_work.scaleType= ImageView.ScaleType.CENTER
//                var params =  image_show_work?.layoutParams as RelativeLayout.LayoutParams
                image_show_delete.visibility=View.GONE
                image_show_add.visibility = View.VISIBLE
                image_show_work.setImageDrawable(null)
//                params.width = context.resources.getDimension(R.dimen.dp_20).toInt()
//                params.height =context.resources.getDimension(R.dimen.dp_20).toInt()

//                image_show_work.layoutParams = params

//                Glide.with(context).load(R.mipmap.ic_device_add_night).transforms( CenterCrop(),  RoundedCorners(context.resources.getDimension(R.dimen.dp_8)
//                    .toInt())
//                ).into(image_show_work )
                image_show_add.setOnClickListener{
                    if (pickHelper == null) {
                        pickHelper = PickImageHelperTwo(activity, pickCallback)
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        val selfPermission =
                                ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA)
                        if (selfPermission == 0) {
                            pickHelper?.showPickDialog(PickImageHelperTwo.PHOTO_REQUEST_GALLERY + 1 , 10 - mList.size)
                        } else {
                            PermissionsUtils.checkPermission(activity, Manifest.permission.CAMERA, PermissionsUtils.CODE_USER_INFO_SHARE)
                        }
                    } else {
                        pickHelper?.showPickDialog(PickImageHelperTwo.PHOTO_REQUEST_GALLERY+1 , 10 - mList.size)
                    }
                }

//                var params1=  image_show_work?.layoutParams as RelativeLayout.LayoutParams;
//                params1.width = context.resources.getDimension(R.dimen.dp_97).toInt();
//                params1.height =context.resources.getDimension(R.dimen.dp_97).toInt();
//                item_add_picture.layoutParams = params1;

//                item_add_picture.setBackgroundDrawable(activity.getDrawable(R.drawable.shape_add_picture_bg))
//                item_add_picture.setOnClickListener {
//
//                    if (pickHelper == null) {
//                        pickHelper = PickImageHelperTwo(activity, pickCallback)
//                    }
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                        val selfPermission =
//                            ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA)
//                        if (selfPermission == 0) {
//                            pickHelper?.showPickDialog(PickImageHelperTwo.PHOTO_REQUEST_GALLERY + 1 , 10 - mList.size)
//                        } else {
//                            PermissionsUtils.checkPermission(activity, Manifest.permission.CAMERA, PermissionsUtils.CODE_USER_INFO_SHARE)
//                        }
//                    } else {
//                        pickHelper?.showPickDialog(PickImageHelperTwo.PHOTO_REQUEST_GALLERY+1 , 10 - mList.size)
//                    }
//                }
            }else{

                image_show_work.scaleType= ImageView.ScaleType.FIT_XY
//                item_add_picture.setBackgroundDrawable(null)
//                item_add_picture.setOnClickListener(null)
//                var params =  image_show_work?.layoutParams as RelativeLayout.LayoutParams;
//                params.width = context.resources.getDimension(R.dimen.dp_97).toInt();
//                params.height =context.resources.getDimension(R.dimen.dp_97).toInt();
//                image_show_work.layoutParams = params;
//                item_add_picture.layoutParams = params;
//                image_show_work.visibility=View.VISIBLE
                Glide.with(context).load(BitmapFactory.decodeFile(mList[p1])) .apply( RequestOptions()
                    .transforms( CenterCrop(),  RoundedCorners(context.resources.getDimension(R.dimen.dp_8)
                        .toInt())
                    )).into(p0.itemView.image_show_work )

                image_show_delete.visibility=View.VISIBLE
                image_show_add.visibility = View.GONE

                image_show_delete.setOnClickListener {
                    mList.removeAt(p1)
                    notifyDataSetChanged()
                }

                image_show_work.setOnClickListener {
                    val intent = Intent(
                            context,
                            ImagePreviewActivity::class.java
                    )
                  var showList = ArrayList<String>();
                    showList.addAll(mList)
                    showList.removeAt(showList.size -1 )
                    intent.putStringArrayListExtra(
                            "imageList",
                            mList as ArrayList<String?>?
                    )
                    intent.putExtra(ImagePreviewActivity.P.START_ITEM_POSITION, p1)
                    intent.putExtra(ImagePreviewActivity.P.START_IAMGE_POSITION, p1
                    )
                    context.startActivity(intent)
                }
            }
        }
    }

    override fun getItemCount(): Int=if(mList.size>8){
        9
    }else{
        mList.size
    }


}

/**
 * 晒作品 新作品页
 */
class PublishedWorksActivity : AppActivity(), OnRequestListener {



    companion object{
        @JvmStatic
      var  PUBLICRECIPEID="publicRecipeID"
    }

    lateinit var mPublishApi: PublishApi

    var mList:ArrayList<String> = ArrayList()


    lateinit var mPublishedWorkAdapter:PublishedWorkAdapter

    override fun getLayoutId(): Int =R.layout.activity_published_works

    override fun initView() {
        tv_title.text="新作品"
        tv_title.typeface = Typeface.defaultFromStyle(Typeface.BOLD);

        img_back.setOnClickListener {
            finish()
        }
        mList.add("")
        mPublishedWorkAdapter=PublishedWorkAdapter(mList,this);
        act_publish_work_image_list.layoutManager=GridLayoutManager(this,3)
        act_publish_work_image_list.adapter=mPublishedWorkAdapter

//        RecipeDetailPage.setStatusBarColor(activity, R.color.white)
//        RecipeDetailPage.setLightStatusBar(
//            activity, true
//        )
//        txt_edit.text = "发布"
//        txt_edit.setTextColor(Color.rgb(0x33,0x33,0x33))
//        txt_edit.setBackgroundDrawable(getDrawable(R.drawable.shape_show_confirm_bg))
//        var layoutParams=txt_edit.layoutParams as RelativeLayout.LayoutParams
//        layoutParams.width= context.resources.getDimension(R.dimen.dp_72).toInt()
//        layoutParams.height= context.resources.getDimension(R.dimen.dp_30).toInt()
//        txt_edit.layoutParams=layoutParams
//        txt_edit.textSize = context.resources.getDimension(R.dimen.sp_8)/2
//        txt_edit.setTypeface(null, Typeface.BOLD);
        btn_publish.setOnClickListener {


            var content=act_publish_list_edit.text.toString()
            if (content.isEmpty() && mList.isEmpty()){
                Toast.makeText(context,"先添加内容喔~",Toast.LENGTH_LONG).show()
                return@setOnClickListener;
            }

//            if (mList.isEmpty()){
//                Toast.makeText(context,"请选择照片",Toast.LENGTH_LONG).show()
//                return@setOnClickListener;
//            }
            btn_publish.isEnabled=false
            var mId=intent.getLongExtra(PUBLICRECIPEID,0)
            mPublishApi.submitPublish(R.id.img_thumbs_up_publish,
                mId,content,mList)

            ToastUtils.show("正在上传中···",Toast.LENGTH_LONG)
        }
    }

    override fun initData() {
        mPublishApi= PublishApi(this);

    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        EventUtils.postEvent(ActivityResultOnPageEvent(requestCode, resultCode, data))
//        super.onActivityResult(requestCode, resultCode, data)
//    }

    override fun onFailure(requestId: Int, requestCode: Int, msg: String?, data: Any?) {
        when(requestId){
            R.id.img_thumbs_up_publish->{
                btn_publish.isEnabled=true
                msg?.let {
                    ToastUtils.show(msg,Toast.LENGTH_LONG)
                }

            }

        }
    }

    override fun onSaveCache(requestId: Int, requestCode: Int, paramObject: Any?) {

    }

    override fun onSuccess(requestId: Int, requestCode: Int, paramObject: Any?) {

        when(requestId){
            R.id.img_thumbs_up_publish->{
                txt_edit.isEnabled=true

                ToastUtils.show("上传成功，审核通过后显示",Toast.LENGTH_LONG)
//                  EventUtils.postEvent(RecipteLoginEvent())
                  setResult(RESULT_OK)
                  finish()
            }

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        EventUtils.postEvent(ActivityResultOnPageEvent(requestCode, resultCode, data))
        super.onActivityResult(requestCode, resultCode, data)
    }
}