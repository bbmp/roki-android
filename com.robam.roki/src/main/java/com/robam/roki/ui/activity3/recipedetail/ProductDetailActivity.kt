package com.robam.roki.ui.activity3.recipedetail

import android.content.Intent
import android.widget.ImageView
import com.legent.VoidCallback
import com.legent.ui.ext.dialogs.ProgressDialogHelper
import com.legent.utils.EventUtils
import com.legent.utils.api.ToastUtils
import com.robam.common.events.ParaiseEvent
import com.robam.common.pojos.CookAlbum
import com.robam.common.services.CookbookManager
import com.robam.roki.R
import com.robam.roki.factory.RokiDialogFactory
import com.robam.roki.net.OnRequestListener
import com.robam.roki.net.request.api.PublishApi
import com.robam.roki.ui.PageArgumentKey
import com.robam.roki.ui.activity3.AppActivity
import com.robam.roki.ui.activity3.stampToDate
import com.robam.roki.ui.view.nineview.OnItemPictureClickListener
import com.robam.roki.utils.DialogUtil
import kotlinx.android.synthetic.main.activity_product_detail.*
import kotlinx.android.synthetic.main.include_title_bar.*

class ProductDetailActivity : AppActivity(), OnRequestListener {

    companion object{
        @JvmStatic
        var COOKALBUM="CookAlbum"
    }
    override fun getLayoutId(): Int =R.layout.activity_product_detail
    fun onDelete() {
        val deleteDialog = RokiDialogFactory.createDialogByType(this, DialogUtil.DIALOG_TYPE_26)
        deleteDialog.setTitleText("已经上传的作品删除后，不可恢复")
        deleteDialog.setOkBtn(R.string.ok_btn) {
            deleteDialog.dismiss()
            ProgressDialogHelper.setRunning(this, true)
            CookbookManager.getInstance().removeCookAlbum(mCookAlbum.id, object : VoidCallback {
                override fun onSuccess() {
                    ProgressDialogHelper.setRunning(context, false)
                    EventUtils.postEvent(ParaiseEvent())
                    finish()
                }

                override fun onFailure(t: Throwable) {
                    ProgressDialogHelper.setRunning(context, false)
                    ToastUtils.showThrowable(t)
                }
            })
        }
        deleteDialog.setCancelBtn(R.string.can_btn) { deleteDialog.dismiss() }
        deleteDialog.setCancelable(false)
        deleteDialog.show();

    }
    lateinit var mCookAlbum:CookAlbum
    lateinit var mPublishedApi:PublishApi

    override fun onDestroy() {
        super.onDestroy()
        EventUtils.unregist(this)
    }
    override fun initView() {

        mCookAlbum= intent.getSerializableExtra(COOKALBUM) as CookAlbum
        mPublishedApi= PublishApi(this)
        EventUtils.regist(this)
        img_back.setOnClickListener {
            finish()
        }


        txt_edit.text = "删除"

//        txt_edit.setTextColor(Color.rgb(0x66,0x66,0x66))
        txt_edit.setOnClickListener {
            onDelete()
        }
//        com.robam.roki.ui.page.recipedetail.RecipeDetailPage.setStatusBarColor(
//            activity,
//            R.color.white
//        )
//        com.robam.roki.ui.page.recipedetail.RecipeDetailPage.setLightStatusBar(
//            activity, true
//        )


        act_product_name.setOnClickListener {
            val intent =
                Intent(context, RecipeDetailActivity::class.java)
            intent.putExtra(
                PageArgumentKey.Id,
                mCookAlbum.bookId
            )
            context.startActivity(intent)
        }
        tv_title.text=mCookAlbum.bookName
        act_product_content.text = mCookAlbum.desc

        act_product_name.text="#"+mCookAlbum.bookName

        image_product_has_thumbs_up.isSelected = mCookAlbum.hasPraised

        image_product_has_thumbs_up_counter.setOnClickListener {

            if (image_product_has_thumbs_up.isSelected){
                mPublishedApi.noPraise(
                    R.id.device_polling_task_id,
                    mCookAlbum.id
                )
            }else {
                mPublishedApi.praisePublic(
                    R.id.img_thumbs_up_publish,
                    mCookAlbum.id
                )
            }
        }
        image_product_has_thumbs_up.setOnClickListener {

            if (image_product_has_thumbs_up.isSelected){
                mPublishedApi.noPraise(
                    R.id.device_polling_task_id,
                    mCookAlbum.id
                )
            }else {
                mPublishedApi.praisePublic(
                    R.id.img_thumbs_up_publish,
                    mCookAlbum.id
                )
            }
        }

        image_product_has_thumbs_up_counter.text=mCookAlbum.praiseCount.toString()

        act_product_nine.setShowMore(false)
        act_product_nine.setListener(object :OnItemPictureClickListener {

            override fun onItemPictureClick(
                itemPostion: Int,
                position: Int,
                url: String?,
                urlList: List<String?>?,
                imageView: ImageView?
            ) {
                            val intent = Intent(
                context,
                ImagePreviewActivity::class.java
            )
            intent.putStringArrayListExtra(
                "imageList",
                urlList as ArrayList<String?>?
            )
            intent.putExtra(ImagePreviewActivity.P.START_ITEM_POSITION, itemPostion)
            intent.putExtra(ImagePreviewActivity.P.START_IAMGE_POSITION, position)
//            ActivityOptions compat = ActivityOptions.makeSceneTransitionAnimation(getContext(), imageView, imageView.getTransitionName());
            context.startActivity(intent)
            }
        })
        act_product_date.text = stampToDate(mCookAlbum.createdTime,"yyyy-MM-dd")
        act_product_nine.setItemPosition(0)
        act_product_nine.setSpacing(15f)
        if (mCookAlbum.url != null) {
            act_product_nine.setUrlList(getListData(mCookAlbum.url))
        }

        act_product_nine
    }

    override fun initData() {

    }

    override fun onFailure(requestId: Int, requestCode: Int, msg: String?, data: Any?) {

    }

    override fun onSaveCache(requestId: Int, requestCode: Int, paramObject: Any?) {

    }

    override fun onSuccess(requestId: Int, requestCode: Int, paramObject: Any?) {
      when(requestId){
          R.id.img_thumbs_up_publish -> {
              EventUtils
                  .postEvent(ParaiseEvent())
              image_product_has_thumbs_up.isSelected=!image_product_has_thumbs_up.isSelected
              if (image_product_has_thumbs_up.isSelected){
                  image_product_has_thumbs_up_counter.text=(image_product_has_thumbs_up_counter.text.toString().toInt()+1).toString()
              }else{
                  image_product_has_thumbs_up_counter.text= (image_product_has_thumbs_up_counter.text.toString().toInt()-1).toString()
              }
          }

          R.id.device_polling_task_id->{
              EventUtils
                  .postEvent(ParaiseEvent())
              image_product_has_thumbs_up.isSelected=!image_product_has_thumbs_up.isSelected
              if (image_product_has_thumbs_up.isSelected){
                  image_product_has_thumbs_up_counter.text=(image_product_has_thumbs_up_counter.text.toString().toInt()+1).toString()
              }else{
                  image_product_has_thumbs_up_counter.text= (image_product_has_thumbs_up_counter.text.toString().toInt()-1).toString()
              }
          }
      }
    }
}
