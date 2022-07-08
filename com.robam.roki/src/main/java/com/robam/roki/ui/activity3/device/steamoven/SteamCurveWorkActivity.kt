package com.robam.roki.ui.activity3.device.steamoven

import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.util.Log
import android.view.View
import android.widget.Toast
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.google.common.eventbus.Subscribe
import com.legent.Callback
import com.legent.ContextIniter.cx
import com.legent.VoidCallback
import com.legent.plat.pojos.RCReponse
import com.legent.services.TaskService
import com.legent.utils.EventUtils
import com.legent.utils.LogUtils
import com.legent.utils.api.ToastUtils
import com.robam.common.RobamApp
import com.robam.common.events.SteamOvenOneStatusChangedEvent
import com.robam.common.io.cloud.RokiRestHelper.cookingCurveUpdateCurveState
import com.robam.common.pojos.device.integratedStove.IntegStoveStatus
import com.robam.common.pojos.device.integratedStove.SteamOvenModeEnum
import com.robam.common.pojos.device.steameovenone.AbsSteameOvenOneNew
import com.robam.common.pojos.device.steameovenone.SteamOvenOnePowerOnStatusNew
import com.robam.roki.R
import com.robam.roki.model.bean.LineChartDataBean
import com.robam.roki.request.bean.RecipeCurveSuccessBean
import com.robam.roki.ui.activity3.device.base.DeviceBaseFuntionActivity
import com.robam.roki.ui.activity3.device.base.adapter.ImageSelectUtil
import com.robam.roki.ui.dialog.type.DialogType_FinishWork
import com.robam.roki.ui.form.SteamFinish
import com.robam.roki.ui.page.curve.RecipeSuccessActivity.Companion.CURVE
import com.robam.roki.ui.page.curve.RecipeSuccessActivity.Companion.show
import com.robam.roki.ui.page.device.integratedStove.SteamOvenHelper
import com.robam.roki.ui.page.device.steamovenone.AbsDeviceSteamOvenOne620Page
import com.robam.roki.utils.chart.DynamicLineChartManager
import kotlinx.android.synthetic.main.activity_steam_curve_work_h.*
import kotlinx.android.synthetic.main.activity_steam_curve_work_v.*
import kotlinx.android.synthetic.main.activity_steam_curve_work_v.act_steam_cook_chart
import kotlinx.android.synthetic.main.activity_steam_curve_work_v.act_steam_curve_item_model_content
import kotlinx.android.synthetic.main.activity_steam_curve_work_v.act_steam_curve_item_temp_content
import kotlinx.android.synthetic.main.activity_steam_curve_work_v.act_steam_curve_item_temp_content_down
import kotlinx.android.synthetic.main.activity_steam_curve_work_v.act_steam_curve_item_time_content
import kotlinx.android.synthetic.main.activity_steam_curve_work_v.act_steam_curve_work_time
import kotlinx.android.synthetic.main.activity_steam_curve_work_v.btn_left_work_finish
import kotlinx.android.synthetic.main.activity_steam_curve_work_v.item_mutil_title_bar
import kotlinx.android.synthetic.main.item_mutil_title_bar.view.*
import skin.support.content.res.SkinCompatResources
import java.util.*
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit

class SteamCurveWorkActivity : DeviceBaseFuntionActivity() {
    private val startStr = "烹饪开始"
    private val endStr = "烹饪结束"
    private val preStr = "预热完成"
    private val pointStr = "标记打点"
    //说话点位
    var appointList = ArrayList<Entry>()
    private var preheatTip: Int = 0
    var operationList = ArrayList<RecipeCurveSuccessBean.StepList>()
    var entryList = ArrayList<Entry>() //数据集合

    override fun getLayoutId(): Int {
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            activity.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
            return R.layout.activity_steam_curve_work_h
        } else if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            activity.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
            return R.layout.activity_steam_curve_work_v
        }

        return R.layout.activity_steam_curve_work_v
    }
//    override fun onConfigurationChanged(newConfig: Configuration) {
//   if (newConfig.orientation === Configuration.ORIENTATION_LANDSCAPE) {
//          setContentView(R.layout.activity_steam_curve_work_v)
//        } else {
//           setContentView(R.layout.activity_steam_curve_work_h)
//        }
//        super.onConfigurationChanged(newConfig)
//    }
      private var isIntoWorking: Boolean = false
    private var isLoadCurveData = false

    private var isCookingCurveUpdateCurveState = false
    override fun initView() {
        findViewById<View>(R.id.act_switch_scream).setOnClickListener {


            intent.putExtra(BUNDLE, bundle)
            requestedOrientation = if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED;

            } else {
                ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
            }
            if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                setContentView(R.layout.activity_steam_curve_work_v)

            } else {
                setContentView(R.layout.activity_steam_curve_work_h)
            }




        }


        btn_left_work_finish.setOnClickListener {
            val mDialogType_FinishWork = DialogType_FinishWork.createDialogType_FinishWork(
                context
            )
            mDialogType_FinishWork.bindAllListeners()
            mDialogType_FinishWork.setOnOkClickListener { v: View ->
                when (v.id) {
                    R.id.btn_finish -> {
                        mDialogType_FinishWork.dismiss()
                        finishWork("3")
                    }
                    R.id.btn_cancel -> mDialogType_FinishWork.dismiss()
                    else -> {}
                }
            }
            mDialogType_FinishWork.show()
        }

        findViewById<View>(R.id.act_steam_curve_make_step).setOnClickListener {
            onCurveMarkClicked()
        }

    }

    private fun finishWork(state: String) {
        if (steameOvenOne == null) {
            return
        }
        steameOvenOne.setSteamWorkStatus(
            IntegStoveStatus.workCtrl_stop,
            4.toShort(),
            object : VoidCallback {
                override fun onSuccess() {
                    with(operationList) {
                        sortWith(Comparator { o1: RecipeCurveSuccessBean.StepList, o2: RecipeCurveSuccessBean.StepList ->
                                        if (o1.markTime!!.toDouble() > o2.markTime!!.toDouble()) {
                                            return@Comparator 1
                                        }
                                        if (o1.markTime!!.toDouble() < o2.markTime!!.toDouble()) {
                                            return@Comparator -1
                                        }
                                        0
                                    })
                    }
                    cookingCurveUpdateCurveState(curveId.toString() + "", state)
                }

                override fun onFailure(t: Throwable) {}
            })
    }
    private val curveId: Long = 0
    private fun cookingCurveUpdateCurveState(id: String, state: String) {
        cookingCurveUpdateCurveState(id, state,
            object : Callback<RCReponse?> {
                override fun onSuccess(rcReponse: RCReponse?) {
                    var isPreHeat = false
                    if (steameOvenOne.workState == SteamOvenOnePowerOnStatusNew.preWorkHeat) {
                        isPreHeat = true
                    }
                    val finalIsPreHeat = isPreHeat
                    if (!finalIsPreHeat) {
                        show(
                            cx, CURVE, curveId,operationList,
                            steameOvenOne.getGuid().getGuid(), entryList, appointList
                        )
                    }
                    //                        ToastUtils.show("烹饪完成", Toast.LENGTH_LONG);
                    stopCountdown()
                    finish()
                    isCookingCurveUpdateCurveState = true
                }

                override fun onFailure(t: Throwable) {}
            })
    }

    lateinit var steameOvenOne: AbsSteameOvenOneNew

    fun onCurveMarkClicked() {
//        if (appointList.size() < 2||mAbsSteameOvenOneNew.workState == SteamOvenOnePowerOnStatusNew.preWorkHeat) {
//            return;
//        }
        if (appointList.size < 2) {
            return
        }
        ToastUtils.show("已成功标记该点", Toast.LENGTH_SHORT)
        addAppoint("", true)
    }

    fun setMultShow(sectionNumber: Short) {
        if (steameOvenOne.sectionNumber.toInt() == 2) {
            item_mutil_title_bar.mutil_step_3.visibility=View.GONE

        }
        if (sectionNumber.toInt() == 1) {
            item_mutil_title_bar.mutil_step_1.background= SkinCompatResources.getDrawable(
                RobamApp.getInstance(),
                R.drawable.shape_mutil_title_select_bg
            )
            item_mutil_title_bar.mutil_step_1.setTextColor(resources.getColor(R.color.steam_btn_right_text_color))
            item_mutil_title_bar.mutil_step_2.background= SkinCompatResources.getDrawable(
                RobamApp.getInstance(),
                R.drawable.shape_mutil_title_unselect_bg
            )
            item_mutil_title_bar.mutil_step_2.setTextColor(resources.getColor( R.color.white30))
            item_mutil_title_bar.mutil_step_3.background= SkinCompatResources.getDrawable(
                RobamApp.getInstance(),
                R.drawable.shape_mutil_title_select_bg
            )
            item_mutil_title_bar.mutil_step_3.setTextColor(resources.getColor( R.color.white30))
//
        } else if (sectionNumber.toInt() == 2) {
            item_mutil_title_bar.mutil_step_1.background= SkinCompatResources.getDrawable(
                RobamApp.getInstance(),
                R.drawable.shape_mutil_title_select_bg
            )
            item_mutil_title_bar.mutil_step_1.setTextColor(resources.getColor(R.color.steam_btn_right_text_color))
            item_mutil_title_bar.mutil_step_2.background= SkinCompatResources.getDrawable(
                RobamApp.getInstance(),
                R.drawable.shape_mutil_title_unselect_bg
            )
            item_mutil_title_bar.mutil_step_2.setTextColor(resources.getColor(R.color.steam_btn_right_text_color))
            item_mutil_title_bar.mutil_step_3.background= SkinCompatResources.getDrawable(
                RobamApp.getInstance(),
                R.drawable.shape_mutil_title_unselect_bg
            )
            item_mutil_title_bar.mutil_step_3.setTextColor(resources.getColor( R.color.white30))
        } else if (sectionNumber.toInt() == 3) {
            item_mutil_title_bar.mutil_step_1.background= SkinCompatResources.getDrawable(
                RobamApp.getInstance(),
                R.drawable.shape_mutil_title_select_bg
            )
            item_mutil_title_bar.mutil_step_1.setTextColor(resources.getColor(R.color.steam_btn_right_text_color))
            item_mutil_title_bar.mutil_step_2.background= SkinCompatResources.getDrawable(
                RobamApp.getInstance(),
                R.drawable.shape_mutil_title_select_bg
            )
            item_mutil_title_bar.mutil_step_2.setTextColor(resources.getColor(R.color.steam_btn_right_text_color))
            item_mutil_title_bar.mutil_step_3.background= SkinCompatResources.getDrawable(
                RobamApp.getInstance(),
                R.drawable.shape_mutil_title_select_bg
            )
            item_mutil_title_bar.mutil_step_3.setTextColor(resources.getColor( R.color.steam_btn_right_text_color))
        }
//        if (steameOvenOne.curSectionNbr.toInt() == 1) {
//            item_mutil_title_bar.mutil_step_1.background= SkinCompatResources.getDrawable(
//                RobamApp.getInstance(),
//                R.drawable.shape_mutil_title_select_bg
//            )
//            item_mutil_title_bar.mutil_step_2.background= SkinCompatResources.getDrawable(
//                RobamApp.getInstance(),
//                R.drawable.shape_mutil_title_unselect_bg
//            )
//            item_mutil_title_bar.mutil_step_3.background= SkinCompatResources.getDrawable(
//                RobamApp.getInstance(),
//                R.drawable.shape_mutil_title_unselect_bg
//            )
//        } else if (steameOvenOne.curSectionNbr.toInt() == 2) {
////            tvDotOne.setBackgroundResource(R.mipmap.icon_ok)
////            tvDotTwo.setBackgroundResource(R.drawable.shape_green_dot)
////            tvDotThree.setBackgroundResource(R.drawable.shape_grey_dot)
//        } else if (steameOvenOne.curSectionNbr.toInt() == 3) {
////            tvDotOne.setBackgroundResource(R.mipmap.icon_ok)
////            tvDotTwo.setBackgroundResource(R.mipmap.icon_ok)
////            tvDotThree.setBackgroundResource(R.drawable.shape_green_dot)
//        }
    }
//    public fun onEvent(event: SteamOvenOneStatusChangedEvent)
//    {
//
//
//        if (steameOvenOne == null || Objects.equals(steameOvenOne.id, event.pojo.id)) {
//            return;
//        }
//        val outTime: Int = steameOvenOne.restTimeH * 256 + steameOvenOne.restTime
//
//        if (outTime % 60 == 0) {
//            act_steam_curve_work_time.text = "剩余时间" + outTime / 60
//        } else {
//            act_steam_curve_work_time.text = "剩余时间" + (outTime / 60 + 1)
//        }
//        val setTime =
//            if ((steameOvenOne.setTimeH * 256 + steameOvenOne.setTime) / 60 == 0) "" else ((steameOvenOne.setTimeH * 256 + steameOvenOne.setTime) / 60).toString() + "min"
//
//        val sec =
//            if ((steameOvenOne.setTimeH * 256 + steameOvenOne.setTime) % 60 == 0) "" else (steameOvenOne.setTimeH * 256 + steameOvenOne.setTime) .rem( 60).toString() + "s"
//        //模式名称
//        act_steam_curve_item_model_content.text=
//            SteamOvenModeEnum.match(steameOvenOne.mode.toInt()).value
//
//        //时间
//        act_steam_curve_item_time_content.text=setTime + sec
//
//        //温度
//        act_steam_curve_item_temp_content.setText(steameOvenOne.setUpTemp.toString() + "℃")
//
//    }
//添加步骤点
private  fun addAppoint(appointName: String, iiCheck: Boolean) {
//    var appointName: String? = appointName
//    val bean = RecipeCurveSuccessBean.StepList()
//    if (appointList.size == 0) {
//        appointList.add(Entry(0, 2))
//        bean.markTemp = entryList.get(0).getY().toInt().toString() + ""
//        bean.markTime = entryList.get(0).getX().toInt().toString() + ""
//    } else {
//        appointList.add(Entry(entryList.get(entryList.size - 1).getX(), 1))
//        bean.markTemp = entryList.get(entryList.size - 1).getY().toInt().toString() + ""
//        bean.markTime = entryList.get(entryList.size - 1).getX().toInt().toString() + ""
//    }
//    dm.lineDataAppointSet(appointList)
//    if (TextUtils.isEmpty(appointName)) {
//        appointName = pointStr
//    }
//    bean.markName = appointName
//    operationList.add(bean)
//    if (iiCheck) {
//        cookingCurveMarkStep()
//    }
}

    private fun checkPreheat(): Boolean {
        if (operationList.size <= 2) return false
        for (i in operationList.indices) {
            if (operationList[i].markName == preStr) return true
        }
        return false
    }
    @Subscribe
    fun onEvent(event: SteamOvenOneStatusChangedEvent) {
        LogUtils.i("20180518", event.pojo.id)
        if (steameOvenOne == null || !Objects.equals(
                steameOvenOne.id,
                event.pojo.id
            )
        ) {
            return
        }
        steameOvenOne = event.pojo as AbsSteameOvenOneNew
        Log.e("结果onEvent", steameOvenOne.workState.toString() + "---")
        act_steam_curve_item_model_content.setText(SteamOvenModeEnum.match(steameOvenOne.mode.toInt()).value)
        val setTime =
            if ((steameOvenOne.setTimeH * 256 + steameOvenOne.setTime) / 60 == 0) "" else ((steameOvenOne.setTimeH * 256 + steameOvenOne.setTime) / 60).toString() + "min"
        val sec =
            if ((steameOvenOne.setTimeH * 256 + steameOvenOne.setTime) % 60 == 0) "" else (steameOvenOne.setTimeH * 256 + steameOvenOne.setTime).rem(60).toString() + "s"
        val outTime: Int = steameOvenOne.restTimeH * 256 + steameOvenOne.restTime
        if (outTime % 60 == 0) {
            act_steam_curve_work_time.setText("剩余时间" + outTime / 60)
        } else {
            act_steam_curve_work_time.setText("剩余时间" + (outTime / 60 + 1))
        }
        act_steam_curve_item_time_content.setText(setTime + sec)
        if (SteamOvenHelper.isMult(steameOvenOne.sectionNumber)) {
            item_mutil_title_bar.setVisibility(View.VISIBLE)
            setMultShow(steameOvenOne.curSectionNbr)
        } else {
            item_mutil_title_bar.setVisibility(View.INVISIBLE)
        }
        if (SteamOvenHelper.isShowSteam(steameOvenOne.mode)) {
            item_mutil_title_temp_down_title.visibility = View.VISIBLE
            if (item_mutil_title_temp_down_title != null) act_steam_curve_item_temp_content_down.visibility = View.VISIBLE
            act_steam_curve_item_temp_content_down.text = SteamOvenHelper.getSteamContent(steameOvenOne.steam)
        } else {
            item_mutil_title_temp_down_title.setVisibility(View.GONE)
            if (act_steam_curve_item_temp_content_down != null) act_steam_curve_item_temp_content_down.setVisibility(View.GONE)
        }
        if (SteamOvenModeEnum.match(steameOvenOne.mode.toInt()) == SteamOvenModeEnum.EXP) {

            item_mutil_title_temp_title.text = "上温度"
            act_steam_curve_item_temp_content_down.setText(steameOvenOne.setUpTemp.toString() + "℃")
            item_mutil_title_temp_down_title.text = "下温度"
            act_steam_curve_item_temp_content.text = steameOvenOne.setDownTemp.toString() + "℃"
            act_steam_curve_item_temp_content.visibility = View.VISIBLE
        } else {
            act_steam_curve_item_temp_content.setText(steameOvenOne.setUpTemp.toString() + "℃")
        }

        //预备 工作
        if (isLoadCurveData &&
            (steameOvenOne.workState == SteamOvenOnePowerOnStatusNew.preWorkHeat ||
                    steameOvenOne.workState == SteamOvenOnePowerOnStatusNew.Working)
        ) {
            if (steameOvenOne.workState == SteamOvenOnePowerOnStatusNew.Working) { //进入过工作状态
                isIntoWorking = true
                if (operationList.size >= 2 && !checkPreheat()) {
                    addAppoint(preStr, true)
                }

//                if (preheatTip == 1) {//从预热状态过来
//                    preheatTip++;
//                } else if (preheatTip == 0) {//直接进入工作状态
//
//                }
//                if (preheatTip == 2) {//添加预热完成点
//                    preheatTip++;
//                    addAppoint(preStr, true);
//                }
            }
            if (steameOvenOne.workState == SteamOvenOnePowerOnStatusNew.preWorkHeat) { //预热
                preheatTip = 1
            }
//            tvStatusName.setText("记录中")
            btn_right_work_suspend.text = "暂停"
            startCountdown()
            //完成
        } else if (steameOvenOne.workState == SteamOvenOnePowerOnStatusNew.finish) {
            if (!AbsDeviceSteamOvenOne620Page.isForeground(cx, "RecipeSuccessActivity")) {
                if (!isCookingCurveUpdateCurveState) {
                    cookingCurveUpdateCurveState(curveId.toString() , "4")
                }
            }
        } else if (steameOvenOne.workState == SteamOvenOnePowerOnStatusNew.workPauseSteam
            || steameOvenOne.workState == SteamOvenOnePowerOnStatusNew.preWorkHeatPause
        ) {
            stopCountdown()
//            tvStatusName.setText("暂停中")
            btn_right_work_suspend.setText("继续烹饪")
        } else if (steameOvenOne.workState == SteamOvenOnePowerOnStatusNew.Off ||
            steameOvenOne.workState == SteamOvenOnePowerOnStatusNew.Open
        ) {
            if (isIntoWorking && !AbsDeviceSteamOvenOne620Page.isForeground(
                    cx,
                    "RecipeSuccessActivity"
                )
            ) {
                if (!isCookingCurveUpdateCurveState) {
                    cookingCurveUpdateCurveState(curveId.toString(), "3")
                }
            }
            EventUtils.postEvent(SteamFinish())
            stopCountdown()
        }
    }
    override fun dealData() {}
    protected fun stopCountdown() {
        if (mFuture != null) {
            mFuture!!.cancel(true)
            mFuture = null
        }
    }
    protected var mFuture //计时器
            : ScheduledFuture<*>? = null
    /**
     * 开始计时
     */
    protected fun startCountdown() {
        if (mFuture != null) return
        mFuture = TaskService.getInstance().scheduleAtFixedRate({
            LogUtils.i("20220608", steameOvenOne.curTemp.toString() + "")
            drawPoint(steameOvenOne.curTemp)
        }, 2000, 2000, TimeUnit.MILLISECONDS)
    }
    private lateinit var dm: DynamicLineChartManager

    fun drawPoint(leftTemp: Short) {
        val bean = LineChartDataBean()
        bean.yValue = leftTemp.toFloat()
        if (entryList.isEmpty()) {
            bean.xValue = 0f
        } else {
            bean.xValue = entryList.get(entryList.size - 1).getX() + 2
        }
        entryList.add(Entry(bean.xValue, bean.yValue))
        pageAddEntry(entryList)
    }

    fun pageAddEntry(entryList: List<Entry>) {
        if (entryList.size > 2) {
            if (operationList.size == 0) {
                addAppoint(startStr, false)
            } else if (operationList.size == 1) {
                addAppoint(endStr, false)
            } else {
                for (i in operationList.indices) {
                    if (operationList[i].markName == endStr) {
                        operationList[i].markTemp =
                            entryList[entryList.size - 1].y.toInt().toString() + ""
                        operationList[i].markTime =
                            entryList[entryList.size - 1].x.toInt().toString() + ""
                        appointList[i].x = entryList[entryList.size - 1].x
                        break
                    }
                }
            }
            Collections.sort(appointList, object : Comparator<Entry> {


                override fun compare(o1: Entry, o2: Entry): Int {
                    if (o1?.x > o2?.x) {
                        return 1
                    }
                    return if (o1.x < o2.x) {
                        -1
                    } else 0
                }
            })
            operationList.sortWith(Comparator { o1: RecipeCurveSuccessBean.StepList, o2: RecipeCurveSuccessBean.StepList ->
                if (o1.markTime!!.toInt() > o2.markTime!!.toInt()) {
                    return@Comparator 1
                }
                if (o1.markTime!!.toInt() < o2.markTime!!.toInt()) {
                    return@Comparator -1
                }
                0
            })
            dm.lineDataAppointSet(appointList)
        }
        //添加数据
        dm.addEntryWithMV(entryList[entryList.size - 1])
        val h: Highlight = act_steam_cook_chart.getHighlightByTouchPoint(
            act_steam_cook_chart.getRight().toFloat(),
            act_steam_cook_chart.getTop().toFloat()
        )
        act_steam_cook_chart.highlightValue(h, true)
    }
}