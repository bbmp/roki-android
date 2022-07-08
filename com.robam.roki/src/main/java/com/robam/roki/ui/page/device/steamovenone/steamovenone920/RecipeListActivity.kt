package com.robam.roki.ui.page.device.steamovenone.steamovenone920


import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hjq.toast.ToastUtils
import com.legent.Callback
import com.legent.VoidCallback
import com.legent.plat.io.device.msg.Msg
import com.legent.plat.pojos.device.IDevice
import com.legent.plat.services.DeviceService
import com.robam.common.io.cloud.RokiRestHelper
import com.robam.common.io.device.MsgKeys
import com.robam.common.io.device.MsgParams
import com.robam.common.io.device.MsgParamsNew
import com.robam.common.pojos.device.integratedStove.SteamOvenFaultEnum
import com.robam.common.pojos.device.integratedStove.SteamOvenModeEnum
import com.robam.common.pojos.device.steameovenone.AbsSteameOvenOneNew
import com.robam.roki.R
import com.robam.roki.db.model.RecipeStepBean
import com.robam.roki.net.OnRequestListener
import com.robam.roki.net.request.bean.Data
import com.robam.roki.net.request.bean.MultiStepDto
import com.robam.roki.net.request.bean.RecipeRecordListBean
import com.robam.roki.request.api.RecipeApi
import com.robam.roki.ui.PageArgumentKey
import com.robam.roki.ui.activity3.device.base.DeviceBaseFuntionActivity
import com.robam.roki.ui.activity3.stampToDate
import com.robam.roki.ui.form.SteamOvenCookCurveActivity.Companion.start
import com.robam.roki.ui.page.device.integratedStove.SteamOvenHelper
import com.robam.roki.ui.page.device.integratedStove.SteamOvenHelper.getSteamContent
import com.robam.roki.ui.page.device.integratedStove.SteamOvenHelper.isShowSteam
import com.robam.roki.ui.page.recipedetail.RecipeDetailPage
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItemClickListener
import kotlinx.android.synthetic.main.activity_recipte_list.*
import kotlinx.android.synthetic.main.include_title_bar.*
import kotlinx.android.synthetic.main.item_d610_recipe_step.view.*
import kotlinx.android.synthetic.main.item_recipte_list.view.*
import org.json.JSONException


class RecipeViewHolder(view: View) : RecyclerView.ViewHolder(view)
 fun WorkBeforeCheck(mSteamModel: Int,mAbsSteamOvenOneNew:AbsSteameOvenOneNew): Boolean {
    val faultEnum = SteamOvenFaultEnum.match(mAbsSteamOvenOneNew.faultCode.toInt())
    if (SteamOvenFaultEnum.NO_FAULT != faultEnum && mAbsSteamOvenOneNew.faultCode.toInt() != 11) {
        if (faultEnum != null) {
            ToastUtils.show(SteamOvenFaultEnum.match(mAbsSteamOvenOneNew.faultCode.toInt()).value)
        } else {
            ToastUtils.show("设备端故障未处理，请及时处理")
        }
        return false
    }
    if (SteamOvenHelper.isWork2(mAbsSteamOvenOneNew.workState)) {
        ToastUtils.show("设备已占用")
        return false
    }
    //门已打开 而且不能开门工作
    if (!SteamOvenHelper.isDoorState(mAbsSteamOvenOneNew.doorState) && !SteamOvenHelper.isOpenDoorWork(
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
        if (SteamOvenHelper.isDescale(mAbsSteamOvenOneNew.descaleFlag)) {
            ToastUtils.show("设备需要除垢后才能继续工作，请先除垢")
            return false
        }
        if (!SteamOvenHelper.isWaterBoxState(mAbsSteamOvenOneNew.waterBoxState)) {
            ToastUtils.show("水箱已弹出，请检查水箱状态")
            return false
        }
        if (!SteamOvenHelper.isWaterLevelState(mAbsSteamOvenOneNew.waterLevelState)) {
            ToastUtils.show("水箱缺水，请加水")
            return false
        }
    }
    return true
}
class RecipeItemListAdapter(var mDataList: ArrayList<MultiStepDto>) : RecyclerView.Adapter<RecipeViewHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RecipeViewHolder {
        return RecipeViewHolder(LayoutInflater.from(p0.context).inflate(R.layout.item_steam_add_step, p0, false))
    }

    override fun onBindViewHolder(p0: RecipeViewHolder, p1: Int) {

        with(p0.itemView) {

            //鲜嫩蒸 100℃ 20min
            if (mDataList.size == 1) {
                tv_number.visibility = View.GONE
            } else {
                tv_number.text = (p1+1).toString()
            }
            var steam=""



            if (isShowSteam(mDataList[p1].modelCode.toShort())&& mDataList[p1].steamQuantity?.toShort()!! >0)
             steam=" "+ mDataList[p1].steamQuantity?.toShort()?.let { getSteamContent(it) }.toString()+" "

            Log.e("蒸汽",steam+"---"+ mDataList[p1].steamQuantity)

            if (mDataList[p1].modelCode == "14"){
                tv_item_name.text =
                    mDataList.get(p1).modelName + steam +" "+mDataList.get(p1).temperature + "℃  "+ mDataList.get(p1).downTemperature + "℃ " + mDataList.get(
                        p1
                    ).time / 60 + "min"
            }else {
                tv_item_name.text =
                    mDataList.get(p1).modelName + steam+" "+ mDataList.get(p1).temperature + "℃ " + mDataList.get(
                        p1
                    ).time / 60 + "min"

            }
        }
    }
    override fun getItemCount(): Int = mDataList.size
}
//第一个list的adapter
class RecipeAdapter(var mDataList: ArrayList<Data>, var isChoose: Boolean, var click: View.OnClickListener) : RecyclerView.Adapter<RecipeViewHolder>() {

    var selectPosition = -1;

    fun getPosition(): Int = selectPosition
    fun setPosition(pos: Int) {
        selectPosition = if (selectPosition == pos) {
            -1
        } else {
            pos
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RecipeViewHolder {
        return RecipeViewHolder(LayoutInflater.from(p0.context).inflate(R.layout.item_recipte_list, p0, false))
    }

    private val TAG = "RecipeListActivity"

    override fun onBindViewHolder(p0: RecipeViewHolder, p1: Int) {

        with(p0.itemView) {
            var time=stampToDate(mDataList[p1].createDateTime,"yyyy-MM-dd HH:mm:ss")
            item_recipe_txt_date.text = time.subSequence(0,"yyyy-MM-dd".length)
            item_recipe_txt_date_min.text =time.substring("yyyy-MM-dd ".length,"yyyy-MM-dd HH:mm:ss".length)
            item_recipe_check_box_title.setOnClickListener {
                setPosition(p1)
                click.onClick(it)
            }
            item_recipe_list_txt_save.setOnClickListener {
                val mIntent = Intent(context, CustomizeRecipeOrPreviewActivity::class.java)
                mIntent.putExtra(CustomizeRecipeOrPreviewActivity.MULTISTEPDTO, mDataList[p1].id)
                mIntent.putExtra(PageArgumentKey.Guid, mDataList[p1].deviceGuid)
                mIntent.putExtra(CustomizeRecipeOrPreviewActivity.RECIPEBEAN, mDataList[p1].multiStepDtoList)
                mIntent.putExtra(CustomizeRecipeOrPreviewActivity.CREATEDATE, mDataList[p1].createDateTime)
                context.startActivity(mIntent)
            }

//            if (mDataList[p1].multiStepDtoList != null && mDataList[p1].multiStepDtoList.size > 0) {
//                Log.e(TAG, "multiStepDtoList" + mDataList[p1].multiStepDtoList.size + "----" + p1)
                item_recipe_list_item_show_rv.layoutManager = LinearLayoutManager(context)
                item_recipe_list_item_show_rv.adapter = RecipeItemListAdapter(mDataList[p1].multiStepDtoList)
//            }else{
//                item_recipe_list_item_show_rv.visibility=View.GONE
//            }

            if (isChoose) {
                item_recipe_list_txt_save.visibility = View.INVISIBLE
            }

            if (p1 === selectPosition) {
                item_recipe_check_box_title.setImageResource(R.mipmap.privacy_selected_3x)
            } else {
                item_recipe_check_box_title.setImageResource(R.mipmap.icon_choice_multi_nomal)
            }
        }
    }
    override fun getItemCount(): Int = mDataList.size
}

class RecipeListActivity : DeviceBaseFuntionActivity(), OnRequestListener {
    //    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView
//    }
    var mDataList: ArrayList<Data> = ArrayList();
    var adapterPosition = 0
    lateinit var mRecipeApi: RecipeApi
    override fun getLayoutId(): Int = R.layout.activity_recipte_list

    var deviceGuid: String? = ""

    companion object {
        @JvmStatic
        val ISCHOOSE = "ischoose"

    }
    lateinit var mAbsSteamOvenOneNew:AbsSteameOvenOneNew
    var ischoose = false;

    lateinit var mRecipeAdapter: RecipeAdapter
    override fun initView() {
        deviceGuid = intent.getStringExtra(PageArgumentKey.Guid)
        RecipeDetailPage.setStatusBarColor(activity, R.color.white)
        RecipeDetailPage.setLightStatusBar(
                activity, true
        )
        ischoose = intent.getBooleanExtra(ISCHOOSE, false)
        if (ischoose) {
            btn_automatic.text = "确定"
            btn_automatic.setOnClickListener {
                var intent = Intent()
                var data = mDataList[mRecipeAdapter.getPosition()]
                intent.putExtra(CustomizeRecipeOrPreviewActivity.MULTISTEPDTO,data)
                setResult(RESULT_OK, intent)
                finish()
            }
        } else {
            btn_automatic.setOnClickListener {
                mAbsSteamOvenOneNew = DeviceService.getInstance().queryById<IDevice>(deviceGuid) as AbsSteameOvenOneNew
                var position = mRecipeAdapter.getPosition()
                var mData = mDataList[position]



                if (mData.multiStepDtoList != null && mData.multiStepDtoList.size != 0) {
                    try {
                        val msg: Msg = mAbsSteamOvenOneNew.newReqMsg(MsgKeys.setDeviceAttribute_Req)
                        //                msg.putOpt(MsgParams.TerminalType, steameOvenOne.terminalType);
//                msg.putOpt(UserId, steameOvenOne.getSrcUser());
//                msg.putOpt(MsgParams.categoryCode , IntegratedStoveConstant.STEAME_OVEN_ONE);
                        msg.putOpt(MsgParams.ArgumentNumber, mData.multiStepDtoList.size * 5 + 3)
                        msg.putOpt(MsgParamsNew.type, 2)
                        //一体机电源控制
                        msg.putOpt(MsgParamsNew.powerCtrlKey, 2)
                        msg.putOpt(MsgParamsNew.powerCtrlLength, 1)
                        msg.putOpt(MsgParamsNew.powerCtrl, 1)
                        //一体机工作控制
                        msg.putOpt(MsgParamsNew.workCtrlKey, 4)
                        msg.putOpt(MsgParamsNew.workCtrlLength, 1)
                        msg.putOpt(MsgParamsNew.workCtrl, 1)
                        //预约时间
//                msg.putOpt(MsgParamsNew.setOrderMinutesKey, 5);
//                msg.putOpt(MsgParamsNew.setOrderMinutesLength, 1);
//                msg.putOpt(MsgParamsNew.setOrderMinutes, 0);
                        //段数
                        msg.putOpt(MsgParamsNew.sectionNumberKey, 100)
                        msg.putOpt(MsgParamsNew.sectionNumberLength, 1)
                        msg.putOpt(MsgParamsNew.sectionNumber, mData.multiStepDtoList.size)
                        //                msg.putOpt(MsgParamsNew.sectionNumber, recipeStepList.size() ) ;

//                        for (i in mData.multiStepDtoList.indices) {
//                            val bean: RecipeStepBean = mData.multiStepDtoList[i].exchange()
//
//                            //exp  长度+1
//                            if (bean.work_mode==14){
//                                msg.putOpt(MsgParams.ArgumentNumber, msg.getInt(MsgParams.ArgumentNumber)+1)
//                            }
//                        }
                        for (i in mData.multiStepDtoList.indices) {


                            val bean: RecipeStepBean = mData.multiStepDtoList[i].exchange()

                            if (!WorkBeforeCheck(bean.work_mode,mAbsSteamOvenOneNew)){
                                return@setOnClickListener
                            }
                            //模式
                            msg.putOpt(MsgParamsNew.modeKey + i, 101 + i * 10)
                            msg.putOpt(MsgParamsNew.modeLength + i, 1)
                            msg.putOpt(MsgParamsNew.mode + i, bean.work_mode)


                            msg.putOpt(MsgParamsNew.setUpTempKey + i, 102 + i * 10)
                            msg.putOpt(MsgParamsNew.setUpTempLength + i, 1)
                            msg.putOpt(MsgParamsNew.setUpTemp + i, bean.temperature)

                            //温度下温度
                            msg.putOpt(MsgParamsNew.setDownTempKey + i, 103 + i * 10)
                            msg.putOpt(MsgParamsNew.setDownTempLength + i, 1)
                            msg.putOpt(MsgParamsNew.setDownTemp + i, bean.temperature2)


                            //时间
                            val time = bean.time
                            msg.putOpt(MsgParamsNew.setTimeKey + i, 104 + i * 10)
                            msg.putOpt(MsgParamsNew.setTimeLength + i, 1)
                            val lowTime = if (time > 255) (time and 0Xff).toShort() else time.toShort()
                            //                    final short lowTime = time > 255 ? (short) (time & 0Xff):(short)time;
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
                            //  msg.putOpt(MsgParamsNew.setTime + i, bean.getTime()*60);
                            //蒸汽量
                            msg.putOpt(MsgParamsNew.steamKey + i, 106 + i * 10)
                            msg.putOpt(MsgParamsNew.steamLength + i, 1)
                            msg.putOpt(MsgParamsNew.steam + i, bean.steam_flow)
                        }
                        mAbsSteamOvenOneNew.setSteamOvenOneMultiStepMode(msg, object : VoidCallback {
                            override fun onSuccess() {
                                if (mAbsSteamOvenOneNew.guid.guid.contains("920")) {
                                    query(mAbsSteamOvenOneNew, activity)
                                }

                                finish()
                            }
                            override fun onFailure(t: Throwable) {}
                        })
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }

            }

            val menuCreator = SwipeMenuCreator { _, rightMenu, _ ->
                val deleteItem = SwipeMenuItem(context)
                deleteItem
                    .setBackground(resources.getDrawable(R.drawable.layer_list_delete_drawable))
                    .setTextColor(Color.WHITE) // 文字颜色。
                    .setTextSize(15) // 文字大小。
                    .setWidth(140).height = ViewGroup.LayoutParams.MATCH_PARENT
                rightMenu.addMenuItem(deleteItem)
            }
            recipe_list.setSwipeMenuCreator(menuCreator)

            val mMenuItemClickListener =
                SwipeMenuItemClickListener { swipeMenuBridge ->
                    // 任何操作必须先关闭菜单，否则可能出现Item菜单打开状态错乱。
                    swipeMenuBridge.closeMenu()
                    adapterPosition = swipeMenuBridge.adapterPosition // RecyclerView的Item的position。
                    val id1 = mRecipeAdapter.mDataList[adapterPosition].id
                    mRecipeApi.detailRecipeMultiUrl(mRecipeApi.DELRECIPEMULT , id1)
                }

            recipe_list.setSwipeMenuItemClickListener(mMenuItemClickListener)
        }
        mRecipeApi = RecipeApi(this)
        btn_automatic.isEnabled = false

        //添加recycleView侧滑删除



        recipe_list.layoutManager = LinearLayoutManager(context)
        img_back.setOnClickListener {
            finish()
        }

        tv_title.text = "烹饪记录"
        mRecipeAdapter = RecipeAdapter(mDataList, ischoose) {
            when (it.id) {
                R.id.item_recipe_check_box_title -> {
                    btn_automatic.isEnabled = true
                }
            }
        }
        recipe_list.adapter = mRecipeAdapter
        deviceGuid?.let {
            mRecipeApi.getRecordList(R.layout.activity_recipte_list, it)
        }


    }

    override fun initData() {

    }

    override fun dealData() {

    }

    override fun onFailure(requestId: Int, requestCode: Int, msg: String?, data: Any?) {
        if (requestId == mRecipeApi.DELRECIPEMULT){
            ToastUtils.show("删除成功")
        }
    }

    override fun onSaveCache(requestId: Int, requestCode: Int, paramObject: Any?) {

    }

    override fun onSuccess(requestId: Int, requestCode: Int, paramObject: Any?) {
        if (requestId == R.layout.activity_recipte_list) {
            paramObject?.let {
                if (paramObject is RecipeRecordListBean) {
                    mDataList.addAll(paramObject.datas)
                    mRecipeAdapter.notifyDataSetChanged()


                }
            }
        }else if (requestId == mRecipeApi.DELRECIPEMULT){
            mDataList.removeAt(adapterPosition)
            mRecipeAdapter.notifyDataSetChanged()
            ToastUtils.show("删除成功")
        }
    }
}


fun query(device: IDevice, activity: Activity) {

//    //mGuid 暂时写死241
    RokiRestHelper.cookingCurvequery(device.guid.guid,
            object : Callback<com.robam.common.io.cloud.Reponses.CookingCurveQueryRes> {
                override fun onSuccess(rcReponse: com.robam.common.io.cloud.Reponses.CookingCurveQueryRes) {

                    Log.d("20211129", rcReponse.toString())
                    if (rcReponse.payload == null) {
                        cookingCurveSave(device, activity)
                    } else {
                        val curveId = rcReponse.payload.curveCookbookId.toLong()
                        val bd = Bundle()
                        bd.putString(PageArgumentKey.Guid, device.guid.guid)
                        //                            bd.putInt(PageArgumentKey.HeadId,headId);
                        bd.putLong(PageArgumentKey.curveId, curveId)
                        //                            UIService.getInstance().postPage(PageKey.DeviceStoveCurve,bd);
//                            CookCurveActivity.start(activity,bd);
                        start(activity, bd)

                        activity.finish()

                    }
                }

                override fun onFailure(t: Throwable) {
                    activity.finish()


                }
            })
}

private fun cookingCurveSave(device: IDevice, activity: Activity) {
    val deviceGuid: String = device.getGuid().getGuid() // stove.getGuid().getGuid();
    val id = 0
    val model = ""
    val setTemp = ""
    val setTime = ""
    RokiRestHelper.cookingCurveSave(deviceGuid, id, model, setTemp, setTime, 0,

            object : Callback<com.robam.common.io.cloud.Reponses.CookingCurveSaveRes> {
                override fun onSuccess(rcReponse: com.robam.common.io.cloud.Reponses.CookingCurveSaveRes) {

                    Log.d("20211129", rcReponse.toString())
                    if (rcReponse.payload != 0L) {
                        val curveId = rcReponse.payload
                        val bd = Bundle()
                        bd.putString(PageArgumentKey.Guid, deviceGuid)
                        //                            bd.putInt(PageArgumentKey.HeadId,headId);
                        bd.putLong(PageArgumentKey.curveId, curveId)
                        //                            UIService.getInstance().postPage(PageKey.DeviceStoveCurve,bd);
//                            CookCurveActivity.start(activity,bd);
                        start(activity, bd)

                        activity.finish()
                    }
                }

                override fun onFailure(t: Throwable) {
                    activity.finish()
                    ToastUtils.show(t.message)
                }
            })
}
