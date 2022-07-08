package com.robam.roki.ui.page.device.steamovenone.steamovenone920

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.fastjson.JSON
import com.bumptech.glide.Glide
import com.hjq.toast.ToastUtils
import com.legent.VoidCallback
import com.legent.plat.io.device.msg.Msg
import com.legent.plat.pojos.device.IDevice
import com.legent.plat.services.DeviceService
import com.legent.ui.UIService
import com.robam.common.io.device.MsgKeys
import com.robam.common.io.device.MsgParams
import com.robam.common.io.device.MsgParamsNew
import com.robam.common.pojos.device.integratedStove.SteamOvenFaultEnum
import com.robam.common.pojos.device.integratedStove.SteamOvenModeEnum
import com.robam.common.pojos.device.steameovenone.AbsSteameOvenOneNew
import com.robam.roki.R
import com.robam.roki.db.model.RecipeStepBean
import com.robam.roki.manage.AliyunOSSManager
import com.robam.roki.manage.ThreadPoolManager
import com.robam.roki.net.OnRequestListener
import com.robam.roki.net.request.bean.MultiStepDto
import com.robam.roki.net.request.bean.MutilStepItem
import com.robam.roki.net.request.bean.RecipeDetailBean
import com.robam.roki.request.api.RecipeApi
import com.robam.roki.request.param.MaterialDtoList
import com.robam.roki.request.param.RecipeSaveParam
import com.robam.roki.request.param.StepDtoList
import com.robam.roki.ui.activity3.AppActivity
import com.robam.roki.ui.page.device.integratedStove.SteamOvenHelper
import com.robam.roki.ui.page.recipedetail.RecipeDetailPage
import com.xiasuhuei321.loadingdialog.view.LoadingDialog
import kotlinx.android.synthetic.main.activity_recipe_preview.*
import kotlinx.android.synthetic.main.include_title_bar.*
import kotlinx.android.synthetic.main.item_material_preview.view.*
import kotlinx.android.synthetic.main.item_step_preview.view.*
import org.json.JSONException


class StepMaterialHolder(view: View) : RecyclerView.ViewHolder(view)


class StepAdapter(var mList:List<StepDtoList>):RecyclerView.Adapter<StepMaterialHolder>(){
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): StepMaterialHolder {
        return StepMaterialHolder(LayoutInflater.from(p0.context).inflate(R.layout.item_step_preview,p0,false))
    }

    override fun onBindViewHolder(p0: StepMaterialHolder, p1: Int) {
        with(p0.itemView){
            item_step_number.text="步骤"+(p1+1)+"/"+mList.size

            Glide.with(context).load(mList.get(p1).stepImg).error(R.mipmap.icon_recipe_default).into(item_step_image)
            item_step_description.text=mList.get(p1).description
        }
    }

    override fun getItemCount(): Int=mList.size

}


class MaterialAdapter(var mList:List<MaterialDtoList>):RecyclerView.Adapter<StepMaterialHolder>(){
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): StepMaterialHolder {
        return StepMaterialHolder(LayoutInflater.from(p0.context).inflate(R.layout.item_material_preview,p0,false))
    }

    override fun onBindViewHolder(p0: StepMaterialHolder, p1: Int) {
      with(p0.itemView){
          item_material_name.text=mList.get(p1).materialName
          item_material_number.text=mList.get(p1).weight.toFloat().toInt().toString()+" "+mList.get(p1).unitName
      }
    }

    override fun getItemCount(): Int=mList.size

}
class RecipePreviewActivity : AppActivity(), OnRequestListener {

    private lateinit var  mRecipeApi: RecipeApi

    companion object{
        @JvmStatic
        var RECIPEPREVIEW="recipebean"
        @JvmStatic
        var ISCUSTOMRECIPE="iscustomrecipe"



        var GUIDDEVICE="guidDevice"
        @JvmStatic
        var ID="id"
    }
    override fun getLayoutId(): Int=R.layout.activity_recipe_preview

     var mRecipeDetailBean:RecipeDetailBean?=null
     var guidDevice:String=""
    var materialDtoList:ArrayList<MaterialDtoList> = ArrayList()
    var stepDtoList:ArrayList<StepDtoList> = ArrayList()

    lateinit var mMaterialAdapter:MaterialAdapter
     var multiStep:MutilStepItem?=null
    lateinit var mStepAdapter:StepAdapter
    lateinit var ld:LoadingDialog

    private fun WorkBeforeCheck(mSteamModel: Int): Boolean {
        val faultEnum = SteamOvenFaultEnum.match(mAbsSteameOvenOneNew.faultCode.toInt())
        if (SteamOvenFaultEnum.NO_FAULT != faultEnum && mAbsSteameOvenOneNew.faultCode.toInt() != 11) {
            if (faultEnum != null) {
                ToastUtils.show(SteamOvenFaultEnum.match(mAbsSteameOvenOneNew.faultCode.toInt()).value)
            } else {
                ToastUtils.show("设备端故障未处理，请及时处理")
            }
            return false
        }
        if (SteamOvenHelper.isWork2(mAbsSteameOvenOneNew.workState)) {
            ToastUtils.show("设备已占用")
            return false
        }
        //门已打开 而且不能开门工作
        if (!SteamOvenHelper.isDoorState(mAbsSteameOvenOneNew.doorState) && !SteamOvenHelper.isOpenDoorWork(
                SteamOvenModeEnum.match(mSteamModel)
            )
        ) {
            ToastUtils.show("门未关好，请检查并确认关好门")
            return false
        }
        /**
         * 判断是否需要水
         */
        if (SteamOvenHelper.isWater(SteamOvenModeEnum.match(mSteamModel))) {
            if (SteamOvenHelper.isDescale(mAbsSteameOvenOneNew.descaleFlag)) {
                ToastUtils.show("设备需要除垢后才能继续工作，请先除垢")
                return false
            }
            if (!SteamOvenHelper.isWaterBoxState(mAbsSteameOvenOneNew.waterBoxState)) {
                ToastUtils.show("水箱已弹出，请检查水箱状态")
                return false
            }
            if (!SteamOvenHelper.isWaterLevelState(mAbsSteameOvenOneNew.waterLevelState)) {
                ToastUtils.show("水箱缺水，请加水")
                return false
            }
        }
        return true
    }


    override fun showDialog(){

        runOnUiThread {
            ld= LoadingDialog(this)
            ld.setLoadingText("上传中")

                .setInterceptBack(false)

                .closeSuccessAnim()

                .setRepeatCount(1)
                .show()
        }

    }

    lateinit var mAbsSteameOvenOneNew:AbsSteameOvenOneNew
    override fun initView() {

        img_back.setOnClickListener { finish() }
        tv_title.text = getString(R.string.preview)
        RecipeDetailPage.setStatusBarColor(activity, R.color.white)
        RecipeDetailPage.setLightStatusBar(
            activity, true
        )

        mRecipeApi = RecipeApi(this)



            if (!intent.getBooleanExtra(ISCUSTOMRECIPE,false)) {
            var mRecipePreview =
                intent.getSerializableExtra(CustomizeRecipeOrPreviewActivity.RECIPEDETAILBEAN) as RecipeSaveParam
            var mMultiStepDto =
                intent.getSerializableExtra(RECIPEPREVIEW) as ArrayList<MultiStepDto>
            act_preview_name.text = mRecipePreview.name
            Glide.with(context).load(mRecipePreview.coverImg).into(act_preview_cover_image)
                if (mRecipePreview.deviceTypeName.contains("920"))
            act_preview_device_name.text ="蒸烤一体机·CQ920"
                var time = 0;


                    mMultiStepDto.forEach {
                        time += it.time
                    }


             var timeText=   if (time>=3600) {
                    "${time/3600}小时${time%3600}分钟"
                }else if (time in 60..3599) {
                 if (time%60==0){
                     "${time/60}分钟"
                 }else {
                     "${time/60}分钟${time%60}秒"
                 }

                }else{
                 "${time/60}分钟"
                }
            act_preview_time.text = timeText



            act_pre_material_rl.layoutManager = LinearLayoutManager(this)
            materialDtoList.addAll( mRecipePreview.materialDtoList)
            mMaterialAdapter =MaterialAdapter(materialDtoList)
            act_pre_material_rl.adapter = mMaterialAdapter


                act_recycle_recipe_step.layoutManager = LinearLayoutManager(this)
                stepDtoList.addAll(mRecipePreview.stepDtoList)
                mStepAdapter=StepAdapter(stepDtoList)
                act_recycle_recipe_step.adapter=mStepAdapter


            act_preview_start.setOnClickListener {
                showDialog()
                ThreadPoolManager.getInstance().execute {
                    var url = AliyunOSSManager.getInstance().uploadFile(
                        AliyunOSSManager.getOSSImgName(
                            "cover",
                            System.currentTimeMillis().toString()
                        ), mRecipePreview.coverImg
                    )
                    mRecipePreview.coverImg=url

                    mRecipePreview.stepDtoList.forEach {
                        var path = AliyunOSSManager.getOSSImgName(
                            "920step",
                            System.currentTimeMillis().toString()
                        )
                        it.stepImg = AliyunOSSManager.getInstance().uploadFile(
                            path, it.stepImg
                        )


                    }
                    mRecipeApi.setSaveRecipeList(R.layout.activity_recipe_preview, mRecipePreview)
                }
            }
        }else{
            var id=intent.getIntExtra(ID,1)
                mRecipeApi.getRecipeDetail(R.id.act_preview_name,id)
                guidDevice=intent.getStringExtra(GUIDDEVICE)
                act_preview_start.text="启动"
                act_preview_start.setOnClickListener {
                    mAbsSteameOvenOneNew = DeviceService.getInstance().queryById<IDevice>(guidDevice) as AbsSteameOvenOneNew
                    var  multiStep = JSON.parseArray(mRecipeDetailBean?.payload?.multiStep,
                        MutilStepItem::class.java)
                    if ( multiStep != null && multiStep.size != 0) {
                        try {
                            val msg: Msg = mAbsSteameOvenOneNew.newReqMsg(MsgKeys.setDeviceAttribute_Req)
                            msg.putOpt(MsgParams.ArgumentNumber, multiStep.size * 5 + 3)
                            msg.putOpt(MsgParamsNew.type, 2)
                            msg.putOpt(MsgParamsNew.powerCtrlKey, 2)
                            msg.putOpt(MsgParamsNew.powerCtrlLength, 1)
                            msg.putOpt(MsgParamsNew.powerCtrl, 1)
                            msg.putOpt(MsgParamsNew.workCtrlKey, 4)
                            msg.putOpt(MsgParamsNew.workCtrlLength, 1)
                            msg.putOpt(MsgParamsNew.workCtrl, 1)
                            msg.putOpt(MsgParamsNew.sectionNumberKey, 100)
                            msg.putOpt(MsgParamsNew.sectionNumberLength, 1)
                            msg.putOpt(MsgParamsNew.sectionNumber, multiStep.size)
                            for (i in multiStep.indices) {
                                val bean: RecipeStepBean = multiStep[i].exchange()
                                if (!WorkBeforeCheck(bean.work_mode)){
                                    return@setOnClickListener
                                }
                                msg.putOpt(MsgParamsNew.modeKey + i, 101 + i * 10)
                                msg.putOpt(MsgParamsNew.modeLength + i, 1)
                                msg.putOpt(MsgParamsNew.mode + i, bean.work_mode)
                                msg.putOpt(MsgParamsNew.setUpTempKey + i, 102 + i * 10)
                                msg.putOpt(MsgParamsNew.setUpTempLength + i, 1)
                                msg.putOpt(MsgParamsNew.setUpTemp + i, bean.temperature)


                                msg.putOpt(MsgParamsNew.setDownTempKey + i, 103 + i * 10)
                                msg.putOpt(MsgParamsNew.setDownTempLength + i, 1)
                                msg.putOpt(MsgParamsNew.setDownTemp + i, bean.temperature2)
                                val time = bean.time
                                msg.putOpt(MsgParamsNew.setTimeKey + i, 104 + i * 10)
                                msg.putOpt(MsgParamsNew.setTimeLength + i, 1)
                                val lowTime = if (time > 255) (time and 0Xff).toShort() else time.toShort()
                                if (time <= 255) {
                                    msg.putOpt(MsgParamsNew.setTime0b + i, lowTime)
                                } else {
                                    msg.putOpt(MsgParamsNew.setTimeKey + i, 104 + i * 10)
                                    msg.putOpt(MsgParamsNew.setTimeLength + i, 2)
                                    val ltime = (time and 0xff).toShort()
                                    msg.putOpt(MsgParamsNew.setTime0b + i, ltime)
                                    val htime = (time shr 8 and 0Xff).toShort()
                                    msg.putOpt(MsgParamsNew.setTime1b + i, htime)
                                }
                                msg.putOpt(MsgParamsNew.steamKey + i, 106 + i * 10)
                                msg.putOpt(MsgParamsNew.steamLength + i, 1)
                                msg.putOpt(MsgParamsNew.steam + i, bean.steam_flow)
                            }
                            mAbsSteameOvenOneNew.setSteamOvenOneMultiStepMode(msg, object :
                                VoidCallback {
                                override fun onSuccess() {
                                          UIService.getInstance().popBack()
                                        if (mAbsSteameOvenOneNew.guid.guid.contains("920")) {
                                            query(mAbsSteameOvenOneNew,activity)

                                    }
                                }
                                override fun onFailure(t: Throwable) {

                                }
                            })
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                    }
                }

        }

    }

    override fun initData() {

    }

    override fun onFailure(requestId: Int, requestCode: Int, msg: String?, data: Any?) {
        if (requestId==R.layout.activity_recipe_preview){
            ld.close()
        }
    }

    override fun onSaveCache(requestId: Int, requestCode: Int, paramObject: Any?) {

    }

    override fun onSuccess(requestId: Int, requestCode: Int, paramObject: Any?) {

        if (requestId==R.layout.activity_recipe_preview){
            ToastUtils.show("保存成功")
            ld.close()
            setResult(RESULT_OK)
            finish()
        }else if (requestId==R.id.act_preview_name){
            paramObject?.let {
                if (paramObject is RecipeDetailBean){
                   this.mRecipeDetailBean=paramObject
                    paramObject.payload.apply {
                        act_preview_name.text = name
                        Glide.with(context).load(coverImg).error(R.mipmap.icon_recipe_default).into(act_preview_cover_image)
                        act_pre_material_rl.layoutManager = LinearLayoutManager(context)
                        if (materialDtoList==null){
                            materialDtoList=ArrayList()
                        }
                        mMaterialAdapter =MaterialAdapter(materialDtoList)
                         act_pre_material_rl.adapter = mMaterialAdapter

                        act_recycle_recipe_step.layoutManager = LinearLayoutManager(context)
                        if (stepDtoList==null){
                            stepDtoList=ArrayList()
                        }



                        var time = 0;
                        var multiStep= JSON.parseArray(mRecipeDetailBean?.payload?.multiStep, MutilStepItem::class.java)
                        multiStep.forEach {
                            time += it.time?.toInt() ?: 0;
                        }
                        var timeText=   if (time>=3600) {
                            "${time / 3600}小时${time % 36000}分钟"
                        }else if (time in 60..3599) {
                            if (time%60==0){
                                "${time/60}分钟"
                            }else {
                                "${time/60}分钟${time%60}秒"
                            }
                        }else{
                            "${time / 60}分钟"
                        }
                        act_preview_time.text = timeText
                        act_preview_name.text = mRecipeDetailBean?.payload?.name
                        mStepAdapter=StepAdapter(stepDtoList)
                        act_recycle_recipe_step.adapter=mStepAdapter





                    }
                }
            }

        }
    }
}