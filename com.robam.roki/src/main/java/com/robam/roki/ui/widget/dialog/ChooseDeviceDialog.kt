package com.robam.roki.ui.widget.dialog

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.GsonUtils
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.legent.Callback
import com.legent.plat.Plat
import com.legent.plat.constant.IPlatRokiFamily
import com.legent.plat.pojos.device.IDevice
import com.legent.utils.LogUtils
import com.legent.utils.api.ToastUtils
import com.robam.common.io.device.MsgParamsNew.type
import com.robam.common.pojos.device.Pot.Pot
import com.robam.common.pojos.device.Stove.Stove
import com.robam.common.pojos.device.fan.AbsFan
import com.robam.common.pojos.device.rika.RikaModel
import com.robam.common.pojos.device.steameovenone.AbsSteameOvenOneNew
import com.robam.roki.R
import com.robam.roki.request.bean.GosnAnalBean
import com.robam.roki.request.bean.Payload
import com.robam.roki.ui.Helper
import com.robam.roki.ui.form.RecipePotActivity
import com.robam.roki.ui.form.RecipeRRQZActivity
import com.robam.roki.ui.page.curve.CurveWorkBeanItem
import com.robam.roki.ui.page.curve.RecipeSuccessActivity
import com.robam.roki.ui.page.device.steamovenone.steamovenone920.WorkBeforeCheck
import com.robam.roki.utils.DeviceSelectUtils
import com.robam.roki.utils.JsonUtils
import kotlinx.android.synthetic.main.activity_recipe_success.*
import kotlinx.android.synthetic.main.dialog_choose_devive.*
import kotlinx.android.synthetic.main.item_choose_device_dialog_item.view.*

interface IDeviceChoose{
    fun onData(id:Int)
}

class ChooseDeviceListAdapter(var mList:List<IDevice>, var mClick: IDeviceChoose): RecyclerView.Adapter<ChooseDeviceListViewHolder>() {

    var selectPosition=-1;
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ChooseDeviceListViewHolder {


        return ChooseDeviceListViewHolder(LayoutInflater.from(p0.context).inflate(R.layout.item_choose_device_dialog_item,p0,false))

    }

    override fun onBindViewHolder(p0: ChooseDeviceListViewHolder, p1: Int) {
        with(p0.itemView) {
            if (p1 === selectPosition) {
                item_choose_dialog_point.setImageResource(R.mipmap.privacy_selected_3x)
            } else {
                item_choose_dialog_point.setImageResource(R.mipmap.icon_choice_multi_nomal)
            }

            item_choose_dialog_device_txt.text = mList[p1].dispalyType.toString()
            item_choose_dialog_name_txt.text = mList[p1].categoryName
            setOnClickListener {
                selectPosition = p1;
                mClick.onData(p1)
                notifyDataSetChanged()

            }
        }
    }
    override fun getItemCount(): Int=mList.size
}

class ChooseDeviceListViewHolder(item:View): RecyclerView.ViewHolder(item)
class ChooseDeviceDialog(context: Context,var mPayload: Payload)  :BaseDialog(context){
    override fun getLayoutId(): Int=R.layout.dialog_choose_devive

    private  val TAG = "ChooseDeviceDialog"
    var mId=-1
    override fun initView() {
        dialog_choose_device_cancel.setOnClickListener {
            dismiss()
        }

        Log.e(TAG,mPayload.toString())
        var mSteamoven = ArrayList<IDevice>()
        mPayload.deviceParams?.let {
            var deviceType=JsonUtils.getJson2Map(it)["deviceType"]
            for (iDevice in Plat.deviceService.queryAll()) {
                Log.e(TAG, iDevice.dt.toString() + "---")
                if (iDevice.dt.equals(deviceType)) {
                    Log.e(TAG, iDevice.deviceType.toString() + "---")
                    mSteamoven.add(iDevice)
                }
                if (iDevice is AbsFan){
                    for (subDevice in iDevice.childList) {
                        Log.e(TAG, subDevice.dt.toString() + "---")
                        if (subDevice.dt.equals(deviceType)){
                            mSteamoven.add(subDevice)
                        }
                    }
                }
            }
            dialog_device_choose_list.layoutManager = LinearLayoutManager(context)
            dialog_device_choose_list.adapter =
                ChooseDeviceListAdapter(mSteamoven, object : IDeviceChoose {
                    override fun onData(id: Int) {

                        dialog_choose_device_confirm.isEnabled = true
                        mId = id
                    }
                })
            dialog_choose_device_confirm.setOnClickListener {
                if (mId == -1) {
                    Toast.makeText(context, "请选择", Toast.LENGTH_LONG).show()
                    return@setOnClickListener
                }




                if(mSteamoven[mId] is Stove){
                    val stove: Stove = Plat.deviceService.lookupChild(mSteamoven[mId].guid.guid);
                   if(stove.dt.equals(IPlatRokiFamily.R9B010)&&stove.rightHead.level > 0) {
                       Toast.makeText(context, "炉头被占用", Toast.LENGTH_LONG).show()
                       return@setOnClickListener
                   }
//                   mPayload.curveStageParams
                    Helper.newStoveSelectAllOffTips(context, stove, object : Callback<Int?> {
                        override fun onSuccess(result: Int?) {
                                var cookName: String? =mPayload.name
                                if (cookName != null) {
                                    RecipeSuccessActivity.show(
                                        scanForActivity(context)  as Activity,
                                        RecipeSuccessActivity.RECIPEING,
                                        mSteamoven[mId].guid.guid,
                                        mPayload.id.toLong(),
                                        false,
                                        cookName
                                    )
                                    dismiss()
                                }
                        }
                        override fun onFailure(t: Throwable) {}
                    })
                }
                if(mSteamoven[mId] is AbsSteameOvenOneNew){
                    var cookName: String? =mPayload.name
                    if (cookName != null) {

                        if (!mSteamoven[mId].isConnected){
                            ToastUtils.show("设备未在线",Toast.LENGTH_SHORT)
                            return@setOnClickListener
                        }
                        mPayload.curveSettingParams?.let {

                            val type = object : TypeToken<List<GosnAnalBean.DeviceParam?>?>() {}.type
                            val funtionBeans: List<GosnAnalBean.DeviceParam> = Gson().fromJson(
                                    mPayload.curveSettingParams,
                                    type
                                )


                            var time=0;
                            funtionBeans.forEachIndexed { index, deviceParam ->
                                time+=deviceParam.setTime.toInt()

                            }

                            if (time<60){
                                ToastUtils.show("工作时间不能少于一分钟",Toast.LENGTH_LONG)
                                return@setOnClickListener
                            }
                        }

                        if (mSteamoven[mId]  is AbsSteameOvenOneNew){
                            var mAbs:AbsSteameOvenOneNew=mSteamoven[mId]  as AbsSteameOvenOneNew
                            if (!WorkBeforeCheck(mAbs.mode.toInt(),mAbs)){
                                return@setOnClickListener
                            }
                        }

                        RecipeSuccessActivity.show(
                            scanForActivity(context)  as Activity,
                            RecipeSuccessActivity.RECIPEING,
                            mSteamoven[mId].guid.guid,
                            mPayload.id.toLong(),
                            false,
                            cookName
                        )
                    }
                    dismiss()
                }

            }

        }
    }

    private fun scanForActivity(cont: Context?): Activity? {
        if (cont == null) return null else if (cont is Activity) return cont else if (cont is ContextWrapper) return scanForActivity(
            cont.baseContext
        )
        return null
    }
}


