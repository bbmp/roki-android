package com.robam.roki.ui.activity3.recipedetail

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.textviewtext.ShowAllSpan
import com.legent.plat.Plat
import com.legent.utils.api.ToastUtils
import com.robam.roki.R
import com.robam.roki.net.OnRequestListener
import com.robam.roki.net.request.api.PublishApi
import com.robam.roki.net.request.bean.AlbumList
import com.robam.roki.net.request.bean.AlumListBean
import com.robam.roki.net.request.bean.CookingAlbumFileDtoList
import com.robam.roki.ui.activity3.AppActivity
import com.robam.roki.ui.activity3.recipedetail.PublishedWorksActivity.Companion.PUBLICRECIPEID
import com.robam.roki.ui.activity3.stampToDate
import com.robam.roki.ui.page.login.helper.CmccLoginHelper
import com.robam.roki.ui.view.nineview.OnItemPictureClickListener
import kotlinx.android.synthetic.main.activity_publish_list.*
import kotlinx.android.synthetic.main.dialog_delete_publie.*
import kotlinx.android.synthetic.main.include_title_bar.*
import kotlinx.android.synthetic.main.item_show_cook.view.*
import skin.support.content.res.SkinCompatResources

fun getList(mList: java.util.ArrayList<CookingAlbumFileDtoList>?): MutableList<String>? {
    val imageList: java.util.ArrayList<String> = java.util.ArrayList<String>()
     if (mList != null) {
         for (cookingAlbumFileDtoList in mList) {
             imageList.add(cookingAlbumFileDtoList.fileUrl)
         }
     }
    return imageList
}

@JvmName("getList1")
fun getListData(mList: ArrayList<String>): MutableList<String> {
    val imageList: java.util.ArrayList<String> = java.util.ArrayList<String>()
    mList?.forEach {
        imageList.add(it)
    }
    return imageList
}



class DeletePublicDialog(context: Context,var click:View.OnClickListener):Dialog(context){


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE);//需要在设置内容之前定义

        setContentView(R.layout.dialog_delete_publie)
        window?.setBackgroundDrawableResource(android.R.color.transparent);
        var attr = window?.attributes;
        if (attr != null) {
            attr.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            attr.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            attr.gravity = Gravity.CENTER;//设置dialog 在布局中的位置
        }
        setCanceledOnTouchOutside(false);
        dialog_delete_cancel.setOnClickListener {
            dismiss()
        }

        dialog_delete_ok.setOnClickListener{
            dismiss()
            click.onClick(it);
        }
    }



}
class PublishedWorksHolder(view: View): RecyclerView.ViewHolder(view)
interface IClick{
    fun onItem(view:View,text:TextView,pos:Int)
}
class PublishedWorksAdapter(var mList:ArrayList<AlbumList>, var mIClick:IClick,var mainActivity: Activity) : RecyclerView.Adapter<PublishedWorksHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): PublishedWorksHolder {
        return PublishedWorksHolder(LayoutInflater.from(p0.context).inflate(R.layout.item_show_cook,null))
    }
    override fun onBindViewHolder(p0: PublishedWorksHolder, p1: Int) {
        with(p0.itemView){
            mList[p1].apply {

                item_name_show_cook.text=ownerName
                val color = SkinCompatResources.getInstance().getTargetResId(context, R.color.text_color_net_err)
                if (color != 0) item_show_cooked_txt_context.setTextColor(context.resources.getColor(color))
                ownerId.let {
                    if (Plat.accountService.currentUserId.equals(it)){
                        item_nine_image_delete.visibility=View.INVISIBLE
                        item_show_cooked_txt_context.maxShowLines=9999
                        item_show_cooked_txt_context.setMaxLineText(content)
                    } else{

                        item_show_cooked_txt_context.maxShowLines=4
                        item_show_cooked_txt_context.setMaxLineText(content)

                        item_show_cooked_txt_context.setOnAllSpanClickListener(object : ShowAllSpan.OnAllSpanClickListener{
                            override fun onClick(widget: View?) {
                                item_show_cooked_txt_context.maxShowLines=9999
                                item_show_cooked_txt_context.setMaxLineText(content)
                            }

                        })
                    }

                    if (Plat.accountService.currentUserId==ownerId.toLong()){
                        item_nine_image_delete.visibility=View.VISIBLE
                    }else{
                        item_nine_image_delete.visibility=View.GONE
                    }


                }

                item_time_show_cook.visibility=View.VISIBLE
                item_time_show_cook.text=stampToDate(createdTime,"yyyy-MM-dd")



                    item_nine_image_delete.setOnClickListener{
                        if (Plat.accountService.isLogon) {
                       var  mDeletePublicDialog=DeletePublicDialog(context){

                               mIClick.onItem(item_nine_image_delete,item_name_show_cook_num,p1)



                        }
                        mDeletePublicDialog.create()
                        mDeletePublicDialog.show()
                        } else {
                            mainActivity.finish()
                            CmccLoginHelper.getInstance().toLogin()
                        }
                    }
                Glide.with(context).load(ownerFigureUrl).error(R.mipmap.headportrait_qs).into(item_head_image)

                img_thumbs_up_publish.isSelected = hasPraised

                item_name_show_cook_num.text=praiseCount
                img_thumbs_up_publish.setOnClickListener{


                    if (Plat.accountService.isLogon){
                        mIClick.onItem(it, item_name_show_cook_num, p1)
                    }else {
                        mainActivity.finish()
                        CmccLoginHelper.getInstance().toLogin()
                    }
                }
                item_nine_image.setListener(object :OnItemPictureClickListener{
                    override fun onItemPictureClick(itemPostion: Int, item: Int, url: String?, urlList: List<String?>?, imageView: ImageView?) {


//                        if (itemPostion)

                        val intent = Intent(
                            context,
                            ImagePreviewActivity::class.java
                        )
                        intent.putStringArrayListExtra(
                            "imageList",
                            urlList as java.util.ArrayList<String?>?
                        )
                        intent.putExtra(ImagePreviewActivity.P.START_ITEM_POSITION, itemPostion)
                        intent.putExtra(ImagePreviewActivity.P.START_IAMGE_POSITION, item)
//                        var compat = ActivityOptions.makeSceneTransitionAnimation(mainActivity, imageView, imageView?.transitionName);
                        context.startActivity(intent)


                    }
                })
                item_nine_image.setItemPosition(p1)
                item_nine_image.setSpacing(15f)
                item_nine_image.setUrlList(getList(mList[p1].cookingAlbumFileDtoList))


            }

        }
    }

    override fun getItemCount(): Int=mList.size


}

/**
 * 全部作品也
 */
class PublishListActivity : AppActivity(), OnRequestListener {

    private lateinit var thumbImag:ImageView
    private lateinit var thumbTextView:TextView
    companion object{
        @JvmStatic
        var RECIPEID="recipeID";
        @JvmStatic
        var RECIPENAME="recipeName";
    }
    lateinit var mPublishedWorksAdapter:PublishedWorksAdapter

    override fun getLayoutId(): Int =R.layout.activity_publish_list
    var reciptId=0L

    var mPos:Int=0

    private lateinit var mPublishApi: PublishApi
    var mListAlbumList:ArrayList<AlbumList> = ArrayList()
    override fun initView() {

         reciptId=intent.getLongExtra(RECIPEID,0);
        var reciptName=intent.getStringExtra(RECIPENAME);
        img_back.setOnClickListener {
            finish()
        }

        mPublishApi= PublishApi(this)
        tv_title.text=reciptName

        tv_title.typeface = Typeface.defaultFromStyle(Typeface.BOLD);

//        RecipeDetailPage.setStatusBarColor(activity, R.color.white)
//        RecipeDetailPage.setLightStatusBar(
//            activity, true
//        )
        mPublishedWorksAdapter= PublishedWorksAdapter(mListAlbumList,object :IClick{

            override fun onItem(view: View, text:TextView,pos: Int) {
                when(view.id){
                R.id.item_nine_image_delete->{
                     mPos=pos
                    mPublishApi.deletePublic(R.id.item_nine_image_delete,mListAlbumList[pos].id.toLong())
                }
                R.id.img_thumbs_up_publish->{
                    thumbImag=view as ImageView
                    thumbTextView=text


                    if (thumbImag.isSelected){
                        mPublishApi.noPraise(
                            R.id.device_polling_task_id,
                            mListAlbumList[pos].id.toLong()
                        )
                    }else {
                        mPublishApi.praisePublic(
                            R.id.img_thumbs_up_publish,
                            mListAlbumList[pos].id.toLong()
                        )
                    }
                }
            }
            }
        },this)
        act_publish_list.adapter=mPublishedWorksAdapter
        act_publish_list.layoutManager=LinearLayoutManager(this)

        act_btn_show_your_work_rl.setOnClickListener {
            if (Plat.accountService.isLogon) {
                jump();
            } else {
                finish()
                CmccLoginHelper.getInstance().toLogin()
            }



        }
    }

    fun jump(){
        val intent = Intent(context, PublishedWorksActivity::class.java)
        intent.putExtra(PUBLICRECIPEID,reciptId)
        startActivityForResult(intent,0x11)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode==0x11&&resultCode== RESULT_OK){
            initData()


            if (Plat.accountService.isLogon){
                jump()
            }

        }
    }

    override fun initData() {

        mPublishApi.getList(R.layout.activity_publish_list,reciptId)

    }

    override fun onFailure(requestId: Int, requestCode: Int, msg: String?, data: Any?) {

        msg?.let {
            ToastUtils.show(it,Toast.LENGTH_LONG)
        }
    }

    override fun onSaveCache(requestId: Int, requestCode: Int, paramObject: Any?) {

    }

    override fun onSuccess(requestId: Int, requestCode: Int, paramObject: Any?) {

        when (requestId) {
            R.id.img_thumbs_up_publish -> {
                thumbImag.isSelected=!thumbImag.isSelected
                if (thumbImag.isSelected){
                    thumbTextView.text=(thumbTextView.text.toString().toInt()+1).toString()
                }else{
                    thumbTextView.text= (thumbTextView.text.toString().toInt()-1).toString()
                }
            }

            R.id.device_polling_task_id->{
                thumbImag.isSelected=!thumbImag.isSelected
                if (thumbImag.isSelected){
                    thumbTextView.text=(thumbTextView.text.toString().toInt()+1).toString()
                }else{
                    thumbTextView.text= (thumbTextView.text.toString().toInt()-1).toString()
                }
            }
            R.layout.activity_publish_list -> {
                paramObject?.let {
                    if (paramObject is AlumListBean){
                        paramObject.albumList?.let {
                            mListAlbumList.clear()
                            mListAlbumList.addAll(it)
                            mPublishedWorksAdapter.notifyDataSetChanged()
                        }?: run {
                            act_publish_list.visibility=View.INVISIBLE
                        }
                    }
                }

            }
            R.id.item_nine_image_delete -> {
                Toast.makeText(this,"删除成功",Toast.LENGTH_LONG).show()
                mListAlbumList.removeAt(mPos)
                mPublishedWorksAdapter.notifyDataSetChanged()
            }
        }
    }
}