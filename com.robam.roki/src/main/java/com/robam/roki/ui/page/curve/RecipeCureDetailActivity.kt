package com.robam.roki.ui.page.curve

import android.content.Intent
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.legent.plat.Plat
import com.legent.plat.events.PageBackEvent
import com.legent.plat.io.device.mqtt.MqttBus.MqttParams.deviceType
import com.legent.plat.pojos.device.IDevice
import com.legent.utils.api.ToastUtils
import com.robam.base.BaseDialog
import com.robam.common.pojos.device.fan.AbsFan
import com.robam.common.pojos.device.steameovenone.SteamOvenOnePowerOnStatusNew.finish
import com.robam.roki.R
import com.robam.roki.net.OnRequestListener
import com.robam.roki.request.api.CurveListApi
import com.robam.roki.request.bean.CurveDetailCookBean
import com.robam.roki.request.bean.Payload
import com.robam.roki.ui.PageArgumentKey.curveId
import com.robam.roki.ui.activity3.AppActivity
import com.robam.roki.ui.adapter.getTimeFormat
import com.robam.roki.ui.page.curve.RecipeSuccessActivity.Companion.CURVE
import com.robam.roki.ui.page.curve.RecipeSuccessActivity.Companion.show
import com.robam.roki.ui.page.recipedetail.RecipeDetailPage
import com.robam.roki.ui.widget.dialog.ChooseDeviceDialog
import com.robam.roki.utils.JsonUtils
import com.robam.roki.utils.audio.SimpleAudioPlayer
import com.robam.roki.utils.audio.player.PlayerCallback
import com.robam.roki.utils.audio.player.RokiMediaPlayer
import com.xiaomi.push.it
import kotlinx.android.synthetic.main.activity_recipe_cure_detail_acticity.*
import kotlinx.android.synthetic.main.activity_recipe_cure_detail_acticity.recipe_cure_detail_food_add
import kotlinx.android.synthetic.main.activity_recipe_cure_detail_acticity.recipe_cure_detail_food_list
import kotlinx.android.synthetic.main.activity_recipe_cure_detail_acticity.recipe_cure_detail_introduce
import kotlinx.android.synthetic.main.activity_recipe_cure_detail_acticity.recipe_cure_detail_sean_add
import kotlinx.android.synthetic.main.activity_recipe_cure_detail_acticity.recipe_cure_detail_sean_list
import kotlinx.android.synthetic.main.activity_recipe_cure_detail_acticity.recipe_cure_detail_step_list
import kotlinx.android.synthetic.main.activity_recipe_cure_detail_acticity.recipe_cure_detail_step_start_cook
import kotlinx.android.synthetic.main.include_title_bar.*
import kotlinx.android.synthetic.main.item_recipe_detail_food.view.*
import kotlinx.android.synthetic.main.item_recipe_detail_step.view.*
import kotlinx.android.synthetic.main.recipe_cure_cetail_1page.*
import kotlinx.android.synthetic.main.record_audio_view.view.*
import kotlinx.android.synthetic.main.widget_add_cooked_step_view.view.*


class RecipeCureDetailActivity : AppActivity(), OnRequestListener {
     companion object{
         val RECIPECUREDETAIL="RecipeCureDetail"


     }



    override fun getLayoutId(): Int =R.layout.activity_recipe_cure_detail_acticity

//     lateinit var btnNotDevice: View
     lateinit var baseDialog: BaseDialog
//     lateinit var tvCookName: TextView

    private lateinit var mCurveListApi: CurveListApi


    fun finishBack( mPageBackEvent: PageBackEvent){
        if (mPageBackEvent.pageName.equals("RecipeSuccessActivity"))
        finish()
    }
      var detail: Payload?=null
    override fun initView() {
        mCurveListApi= CurveListApi(this)
        detail=intent.getParcelableExtra(RECIPECUREDETAIL)
        img_back.setOnClickListener { finish() }
        tv_title.text=detail?.name
        img_edit.background = resources.getDrawable(R.mipmap.icon_edit_step)
        img_edit.setOnClickListener {
            detail?.id?.toLong()?.let { detailId ->
                var iDeviceWork: IDevice?=null
               var deviceType= JsonUtils.getJson2Map(detail?.deviceParams)["deviceType"]
                for (iDevice in Plat.deviceService.queryAll()) {
                    if (iDevice.dt.equals(deviceType)) {
                        iDeviceWork=iDevice
                    }
                    if (iDevice is AbsFan){
                        for (subDevice in iDevice.childList) {
                            if (subDevice.dt.equals(deviceType)){
                                iDeviceWork=iDevice
                            }
                        }
                    }
                }
                iDeviceWork?.guid?.guid?.let { guid ->
                       show(
                            this,
                            RecipeSuccessActivity.CURVE,
                           guid,
                           detailId,true
                        )
                    }
            }
//            var intent=Intent(context,RecipeSuccessActivity::class.java)
//            intent.putExtra(DishesPreparedByStepActivity.ID,detail?.id)
//            startActivity(intent)
        }

        RecipeDetailPage.setStatusBarColor(activity, R.color.white)
        RecipeDetailPage.setLightStatusBar(
            activity, true
        )
        //JsonUtils.getJson2Map(it)["categoryCode"].toString()
        //{"deviceType":"CQ920","deviceTypeName":"蒸烤一体机-CQ920","categoryCode":"RZKY"
        // ,"platformName":"蒸烤一体机通用","categoryName":"烤蒸一体机","platformCode":"ZKY01"}
        var totalTime=""
        detail?.needTime?.let { time->
            totalTime = when {
                time>3600 -> {
                    (time/3600).toString()+"h "+((time%3600)/60)+"min "+((time%3600)%60)+"s"
                }
                time in 60..3599 -> {
                    if (time%60==0){
                        (time / 60).toString() + "min "
                    }else {
                        (time / 60).toString() + "min " + (time % 60) + "s "
                    }
                }
                else -> {
                    (time).toString()+"s"
                }
            }
        }
        detail?.deviceParams?.let {
//            var diftext=""
//            if (detail?.difficulty!=null){
//            var diff = when {
//                detail?.difficulty==3 -> {
//                    "中等"
//                }
//                detail?.difficulty!! >3 -> {
//                    "困难"
//                }
//                else -> {
//                    "简单"
//                }
//            }
//                diftext=if (detail?.difficulty!=null) "难度:${diff}" else ""
//            }
//            recipe_cure_detail_introduce_device.text=
//                "设备:"+JsonUtils.getJson2Map(it)["deviceTypeName"]+"\n时间："+totalTime

            act_recipe_cure_detail_device_name.text="设备：${JsonUtils.getJson2Map(it)["deviceTypeName"]}"

            act_recipe_cure_detail_time_name.text="时间：$totalTime"

        }
//        initFood()
//        initSean(paramObject.getPayload().materialList)

//        initStep()
        Glide.with(context).load(detail?.imageCover)
            .apply( RequestOptions().transforms( CenterCrop(),  RoundedCorners(18))).error(R.mipmap.icon_recipe_default)
            .into(recipe_cure_detail_collection_image_cover)
//         Glide.with(context).load(detail?.imageCover).into(recipe_cure_detail_collection_image_cover)
        recipe_cure_detail_step_start_cook.setOnClickListener {


            var mDeviceDialog=  detail?.let { ChooseDeviceDialog(this, it)  }
            mDeviceDialog?.create()
            mDeviceDialog?.show()
        }
        recipe_cure_detail_food_add.visibility=View.GONE


    }
//http://roki.oss-cn-hangzhou.aliyuncs.com/device/local/video/cuvetStep1653474400990.mp4
    override fun onResume() {
        super.onResume()

        detail?.id?.let {
            mCurveListApi.getListDetail(R.layout.activity_recipe_cure_detail_acticity,
                    it
            )
        }
    }

    override fun initData() {

    }

    private fun initStep( mStepList:ArrayList<CurveDetailCookBean.PrepareStepList>?){
        var mFoodStepAdapter= mStepList?.let { FoodStepAdapter(it) }
        recipe_cure_detail_step_list.adapter=mFoodStepAdapter
        recipe_cure_detail_step_list.layoutManager= LinearLayoutManager(context)
    }
    private fun initFood( mFoodList:ArrayList<CurveDetailCookBean.MaterialList>?){

        var mFoodListInit:ArrayList<CurveDetailCookBean.MaterialList>? = ArrayList()
        mFoodList?.filter {
            it.materialType==3
        }?.forEach{
            mFoodListInit?.add(it)
        }
        var mFoodAdapter=FoodAdapter(mFoodListInit)
        recipe_cure_detail_food_list.adapter=mFoodAdapter
        recipe_cure_detail_food_list.layoutManager= LinearLayoutManager(context)

        recipe_cure_detail_food_add.setOnClickListener {

            var intent=Intent(context,FlavouringActivity::class.java)
            intent.putExtra(FlavouringActivity.IFACCESSORY,false)
            startActivity(intent)

        }
    }
    private fun initSean( materialList: ArrayList<CurveDetailCookBean.MaterialList>?) {
//        var mFoodList:ArrayList<CurveDetailCookBean.MaterialList> = ArrayList()
      var mSeanList:ArrayList<CurveDetailCookBean.MaterialList>? = ArrayList()
       materialList?.filter {
            it.materialType==1
        }?.forEach{
           mSeanList?.add(it)
       }

        var mFoodAdapter=FoodAdapter(mSeanList)
        recipe_cure_detail_sean_list.adapter=mFoodAdapter
        recipe_cure_detail_sean_list.layoutManager= LinearLayoutManager(context)
        recipe_cure_detail_sean_add.setOnClickListener {
//            mFoodAdapter.add()
            var intent=Intent(context,FlavouringActivity::class.java)
            intent.putExtra(FlavouringActivity.IFACCESSORY,true)
            startActivityForResult(intent,0x123)
//            UIService.getInstance().postPage(PageKey.RecipeStep)
        }

        recipe_cure_detail_sean_add.visibility=View.GONE

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode== RESULT_OK){
            if (requestCode==0x123){
                setResult(RESULT_OK)
                finish()
            }

        }

    }


     var mCurveDetailCookBean:CurveDetailCookBean?=null

    override fun onFailure(requestId: Int, requestCode: Int, msg: String?, data: Any?) {

    }

    override fun onSaveCache(requestId: Int, requestCode: Int, paramObject: Any?) {

    }

    override fun onSuccess(requestId: Int, requestCode: Int, paramObject: Any?) {


        if (requestId==R.layout.activity_recipe_cure_detail_acticity){
            if ( paramObject is CurveDetailCookBean){
                mCurveDetailCookBean=paramObject
                initSean(paramObject?.getPayload()?.materialList)
                initFood(paramObject?.getPayload()?.materialList)
                paramObject?.getPayload()?.prepareStepList?.let {
                    initStep(it)
                }?: run {
                    paramObject?.getPayload()?.prepareStepList = ArrayList()
                    initStep(paramObject?.getPayload()?.prepareStepList)
                }


//                recipe_cure_detail_introduce
                recipe_cure_detail_introduce.text=paramObject?.getPayload()?.introduction

                if (TextUtils.isEmpty(paramObject?.getPayload()?.imageCover)){
                    Glide.with(context).load(paramObject?.getPayload()?.imageUrl)
                        .apply(RequestOptions().transforms(CenterCrop(), RoundedCorners(10)))
                        .error(R.mipmap.icon_recipe_default)
                        .into(recipe_cure_detail_collection_image_cover)
                }else {
                    Glide.with(context).load(paramObject?.getPayload()?.imageCover)
                        .apply(RequestOptions().transforms(CenterCrop(), RoundedCorners(10)))
                        .error(R.mipmap.icon_recipe_default)
                        .into(recipe_cure_detail_collection_image_cover)
                }


               var diff = when {
                    paramObject.getPayload()?.difficulty==3 -> {
                        "中等"
                    }
                    paramObject.getPayload()?.difficulty!! >3 -> {
                        "困难"
                    }
                    else -> {
                        "简单"
                    }
                }
                var device=JsonUtils.getJson2Map(paramObject?.getPayload()?.deviceParams)["deviceTypeName"].toString()
                var totalTime=""
                paramObject?.getPayload()?.needTime?.let { time->
                    totalTime = when {
                        time>3600 -> {
                            (time/3600).toString()+"h "+((time%3600)/60)+"min "+((time%3600)%60)+"s"
                        }
                        time in 60..3599 -> {
                            if (time%60==0){
                                (time / 60).toString() + "min "
                            }else {
                                (time / 60).toString() + "min " + (time % 60) + "s "
                            }
                        }
                        else -> {
                            (time).toString()+"s"
                        }
                    }
                }

                act_recipe_cure_detail_device_name.text="设备：$device"

                act_recipe_cure_detail_time_name.text="时间：$totalTime"
//                recipe_cure_detail_introduce_device.text = "设备：$device\n时间：$totalTime"
            }
        }
    }


}





class FoodAdapter(var mFoodList:ArrayList<CurveDetailCookBean.MaterialList>?): RecyclerView.Adapter<FoodAdapter.FoodViewHolder>() {




    inner class FoodViewHolder(itemview: View): RecyclerView.ViewHolder(itemview)
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): FoodViewHolder {
        var mRootView=
            LayoutInflater.from(p0.context).inflate(R.layout.item_recipe_detail_food,p0,false)
        return FoodViewHolder(mRootView)
    }

    override fun onBindViewHolder(p0: FoodViewHolder, p1: Int) {
        with(p0.itemView) {
            item_recipe_detail_food_name.text = mFoodList?.get(p1)?.materialName+""
            item_recipe_detail_food_img.setOnClickListener {


            }


            item_recipe_detail_food_txt_amount.text =
                mFoodList?.get(p1)?.weight.toString()+mFoodList?.get(p1)?.unitName+""
        }
    }

    override fun getItemCount(): Int= mFoodList?.size!!

}


class FoodStepAdapter(var mDataList:ArrayList<CurveDetailCookBean.PrepareStepList>): RecyclerView.Adapter<FoodStepAdapter.FoodStepViewHolder>() {

    private  var simpleAudioPlayer: SimpleAudioPlayer= RokiMediaPlayer()
    inner class FoodStepViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview)



    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): FoodStepViewHolder {
        var mRootView =
            LayoutInflater.from(p0.context).inflate(R.layout.item_recipe_detail_step, p0, false)
        return FoodStepViewHolder(mRootView)
    }

    override fun onBindViewHolder(p0: FoodStepViewHolder, p1: Int) {
     with(p0.itemView){

         Glide.with(context).load(mDataList?.get(p1)?.imageUrl)
             .apply( RequestOptions().transforms( CenterCrop(),  RoundedCorners(10))).into(item_recipe_detail_step_img)
         item_recipe_detail_step_img.background=resources.getDrawable(R.drawable.shape_input_curve_et)

         if (!TextUtils.isEmpty(mDataList?.get(p1)?.voiceUrl)&&!TextUtils.isEmpty(mDataList?.get(p1)?.description)){
             item_add_cooked_change_view.setOnClickListener {
                 if (item_add_cooked_view_player_audio.visibility==View.VISIBLE){
                     item_add_cooked_view_player_audio.visibility=View.GONE
                     item_add_cooked_view_picture_txt.visibility=View.VISIBLE
                 }else{
                     item_add_cooked_view_picture_txt.visibility=View.GONE
                     item_add_cooked_view_player_audio.visibility=View.VISIBLE
                 }

                 item_add_cooked_view_player_audio.setOnClickListener {


                     simpleAudioPlayer.play(mDataList?.get(p1)?.voiceUrl)

                     simpleAudioPlayer.addCallback(object : PlayerCallback{
                         override fun onPlay() {

                         }

                         override fun onProgress(progress: Int) {
                             item_add_cooked_view_step_audio_time.text= getTimeFormat(progress)

                         }

                         override fun onPause() {

                         }

                         override fun onStop() {

                         }
                     })
                 }
             }
         }else{
             if (!TextUtils.isEmpty(mDataList?.get(p1)?.voiceUrl)){
                 item_add_cooked_view_picture_txt.visibility=View.GONE
                 item_add_cooked_view_player_audio.visibility=View.VISIBLE
                 Glide.with(context).load(R.mipmap.icon_audio_input)
                     .into(item_add_cooked_change_view)
                 item_add_cooked_view_player_audio.setOnClickListener {


                     simpleAudioPlayer.play(mDataList?.get(p1)?.voiceUrl)

                     simpleAudioPlayer.addCallback(object : PlayerCallback{
                         override fun onPlay() {

                         }

                         override fun onProgress(progress: Int) {
                             item_add_cooked_view_step_audio_time.text= getTimeFormat(progress)

                         }

                         override fun onPause() {

                         }

                         override fun onStop() {

                         }
                     })
                 }

             }
             if (!TextUtils.isEmpty(mDataList?.get(p1)?.description)){
                 item_add_cooked_view_player_audio.visibility=View.GONE
                 item_add_cooked_view_picture_txt.visibility=View.VISIBLE
                 Glide.with(context).load(R.mipmap.text_input_icon)
                     .into(item_add_cooked_change_view)

             }
         }
         if(!TextUtils.isEmpty(mDataList?.get(p1)?.description)){
             item_add_cooked_view_picture_txt.visibility=View.VISIBLE
             item_add_cooked_view_player_audio.visibility=View.GONE
         }
         item_add_cooked_view_picture_txt.text=mDataList?.get(p1)?.description
     }
    }

    override fun getItemCount(): Int = mDataList?.size
}

