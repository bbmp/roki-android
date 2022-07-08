package com.robam.roki.ui.page.curve

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Rect
import android.os.Build
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager

import com.blankj.utilcode.util.GsonUtils.fromJson
import com.bumptech.glide.Glide
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.ChartTouchListener.ChartGesture
import com.github.mikephil.charting.listener.OnChartGestureListener
import com.github.mikephil.charting.utils.Transformer
import com.google.common.base.Objects
import com.google.common.eventbus.Subscribe
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.legent.ContextIniter.cx
import com.legent.VoidCallback
import com.legent.events.ActivityResultOnVideoEvent
import com.legent.plat.Plat
import com.legent.plat.io.device.msg.Msg
import com.legent.plat.pojos.device.IDevice
import com.legent.utils.EventUtils
import com.legent.utils.api.ToastUtils
import com.robam.common.events.NewSteamOvenOneAlarmEvent
import com.robam.common.events.SteamOvenOneStatusChangedEvent
import com.robam.common.events.StoveStatusChangedEvent
import com.robam.common.io.device.MsgKeys
import com.robam.common.io.device.MsgParams
import com.robam.common.io.device.MsgParamsNew
import com.robam.common.pojos.device.Stove.Stove
import com.robam.common.pojos.device.Stove.StoveStatus
import com.robam.common.pojos.device.integratedStove.IntegStoveStatus
import com.robam.common.pojos.device.steameovenone.AbsSteameOvenOneNew
import com.robam.common.pojos.device.steameovenone.SteamOvenOnePowerOnStatusNew
import com.robam.roki.R
import com.robam.roki.db.model.RecipeStepBean
import com.robam.roki.manage.AliyunOSSManager
import com.robam.roki.manage.ThreadPoolManager
import com.robam.roki.model.bean.LineChartDataBean
import com.robam.roki.net.OnRequestListener
import com.robam.roki.request.api.RecipeSuccessApi
import com.robam.roki.request.api.UploadFileApi
import com.robam.roki.request.bean.GosnAnalBean.DeviceParam
import com.robam.roki.request.bean.RecipeCurveSuccessBean
import com.robam.roki.request.bean.UpFileBean
import com.robam.roki.ui.activity3.AppActivity
import com.robam.roki.ui.adapter.IPickPicture
import com.robam.roki.ui.adapter.RecipeCurveDataAdapter
import com.robam.roki.ui.page.device.integratedStove.SteamOvenHelper
import com.robam.roki.ui.page.device.integratedStove.SteamOvenHelper.getSteamContent
import com.robam.roki.ui.page.recipedetail.RecipeDetailPage
import com.robam.roki.ui.widget.dialog.CookedFinishNewDialog
import com.robam.roki.utils.AlarmDataUtils
import com.robam.roki.utils.AlarmDataUtils.steamOvenOneAlarmStatus
import com.robam.roki.utils.PermissionsUtils
import com.robam.roki.utils.PickImageVideoHelper
import com.robam.roki.utils.audio.IOnTouchListener
import com.robam.roki.utils.audio.SimpleAudioPlayer
import com.robam.roki.utils.audio.player.PlayerCallback
import com.robam.roki.utils.audio.player.PlayerState
import com.robam.roki.utils.audio.player.RokiMediaPlayer
import com.robam.roki.utils.chart.ChartDataReviseUtil
import com.robam.roki.utils.chart.DynamicLineChartManager
import com.xiaomi.push.it
import com.xiasuhuei321.loadingdialog.view.LoadingDialog
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItemClickListener
import com.zlw.main.recorderlib.RecordManager
import com.zlw.main.recorderlib.recorder.RecordHelper
import com.zlw.main.recorderlib.recorder.listener.RecordStateListener
import kotlinx.android.synthetic.main.activity_recipe_success.*
import kotlinx.android.synthetic.main.include_title_bar.*
import kotlinx.android.synthetic.main.page_activity_webview.*
import org.json.JSONException
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.text.contains as contains1
import kotlin.text.isNotEmpty as isNotEmpty1
fun getTemp(funtionBeans: DeviceParam): String {
    return if (funtionBeans.mode == 14) {
        funtionBeans.setUpTemp + "℃ " + funtionBeans.setDownTemp + "℃ "
    } else {
        funtionBeans.setUpTemp + "℃ "
    }

}
fun getTime(time: Int): String {
    var totalTime = ""
    time.let { time ->
        totalTime = when {
            time > 3600 -> {
                (time / 3600).toString() + "h " + ((time % 3600) / 60) + "min " + ((time % 3600) % 60) + "s "
            }
            time in 60..3599 -> {
                if (time % 60 == 0) {
                    (time / 60).toString() + "min "
                } else {
                    (time / 60).toString() + "min " + (time % 60) + "s "
                }
            }
            else -> {
                (time).toString() + "s "
            }
        }
    }
    return totalTime
}
class CurveWorkBeanItem(
    val mode: Int,
    val modeName: String,
    val segNo: Int,
    val setDownTemp: Int,
    val setTime: Int,
    val setUpTemp: Int,
    val steamVolume:Int=0
) {
    fun exchange(): RecipeStepBean {

        var mRecipeStepBean = RecipeStepBean();
        mRecipeStepBean.work_mode = mode;
        mRecipeStepBean.steam_flow=steamVolume;
        Log.e("结果", "$steamVolume---蒸汽")
        if (mode==14) {
            mRecipeStepBean.temperature = setUpTemp;
            mRecipeStepBean.temperature2 = setDownTemp;
        }else{
            mRecipeStepBean.temperature = setUpTemp;
            mRecipeStepBean.temperature2 = setUpTemp;
        }

        mRecipeStepBean.time = setTime;

        return mRecipeStepBean

    }
}

class RecipeSuccessActivity : AppActivity(), IPickPicture, OnRequestListener {

    //private lateinit var mTest:TestAPi
    var mRecipeCurveSuccessBean: RecipeCurveSuccessBean? = null
    private var mTimerTask: TimerTask? = null
    private var mTimer: Timer? = null
    private lateinit var mRecipeSuccessApi: RecipeSuccessApi
    override fun getLayoutId(): Int = R.layout.activity_recipe_success
    private var oldDiff = 0
    private fun listenKeyFboardVisible() {
        var activityRoot: View = window.decorView ?: return
        pag_cooked_preview_img.viewTreeObserver
            .addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                private var r = Rect()
                override fun onGlobalLayout() {
                    activityRoot.getWindowVisibleDisplayFrame(r)
                    var diff = activityRoot.rootView.height - r.height();
                    //键盘是否弹出
                    var isOpen = (diff > 200);
                    if (diff != oldDiff) {
                        Log.d("keyboard", "keyboard open: $isOpen");
                        oldDiff = diff;


                    }
                    if (isOpen) {
                        page_btn_bottom_ll.visibility = View.GONE
                    } else {
                        page_btn_bottom_ll.visibility = View.VISIBLE
                    }
                }

            })

    }


    fun RecordAudioSuccess() {
        RecordManager.getInstance().setRecordResultListener { filePath ->
            Log.e(TAG, "ACTION_MOVE  stop update")
            mRecipeCurveDataAdapter.data[mPosAudio].voiceUrl = filePath.absolutePath
//            rl_audio_recorded_finish.visibility=View.VISIBLE
//           {
//                var url= AliyunOSSManager.getInstance().uploadFile(AliyunOSSManager.getOSSVoiceName("curve",System.currentTimeMillis().toString()),filePath.absolutePath)

//            }
        }
    }


    fun RecordAudioListener() {

        RecordManager.getInstance().setRecordStateListener(object : RecordStateListener {
            override fun onStateChange(p0: RecordHelper.RecordState?) {
                Log.e(TAG, "录制$mPosAudio-----$p0===${RecordHelper.RecordState.RECORDING}")

                p0?.let {
                    when (it) {
                        RecordHelper.RecordState.RECORDING -> {

                            Log.e(TAG, "RECORDING")

                        }
                        RecordHelper.RecordState.FINISH, RecordHelper.RecordState.IDLE -> {
                            Log.e(TAG, "---------$it$mPosAudio")


                        }
                        else -> {

                        }
                    }
                }
            }

            override fun onError(p0: String?) {

            }

        })
    }

    val REQUESTCODE = 0x123

    var deletePosition = -1;

    var iStove: Stove? = null
    var absSteameOvenOne: AbsSteameOvenOneNew? = null

    @Subscribe
    fun onEvent(event: StoveStatusChangedEvent) {
        iStove = event.pojo
        if (model != CURVE) {
            iStove?.let { absSteameOvenOne ->

                act_cooked_left_time_title.visibility = View.VISIBLE
                act_cooked_left_time.visibility = View.VISIBLE
                act_cooked_left_time_min.visibility = View.VISIBLE
                act_chart_top.visibility = View.INVISIBLE
                if ((absSteameOvenOne?.rightHead.level > 0)) {

                    drawPoint(event.pojo.rightTemp)
                    page_btn_cooked_save.visibility = View.GONE
                    act_recipe_pause_btn.visibility = View.GONE
                    act_end_recipe_btn.visibility = View.VISIBLE

                    val total: Short? = mRecipeCurveSuccessBean?.payload?.needTime?.toShort()
                    val outTime: Short? =
                        operationList.get(operationList.size - 1).markTime?.toShort()
//                    val outTime: Short = absSteameOvenOne?.rightHead.workTime
//                    var total = absSteameOvenOne.rightHead.time
                    act_cooked_left_time.text = ((total!! - outTime!!) / 60).toString()

                    mRecipeCurveSuccessBean?.payload?.stepList?.forEachIndexed { index, it ->
                        mRecipeCurveDataAdapter.data[index].isShow =
                            it.markTime?.toInt()
                                ?.minus(3) ?: 0 < (total!! - outTime!!) && (it.markTime?.toInt()
                                ?: 0) + 3 > (total!! - outTime!!)

                        if (mRecipeCurveDataAdapter.data[index].isShow) {
                            mRecipeCurveDataAdapter.notifyItemChanged(index)
                        }
                    }
                }
            }

        }
    }


    fun getLeftTime(outTime:Int):String{
        return if (outTime<=60){
            "$outTime s"
        }else if (outTime in 61..3600){
            (outTime/60).toString()+"min "+outTime%60+" s";
        }else{
            (outTime/3600).toString()+" h"+(outTime%3600)/60+" min"+(outTime%3600)%60+" s";
        }
    }

    var isWorked=false

    @Subscribe
    fun onEvent(event: SteamOvenOneStatusChangedEvent) {

        Log.e("工作前面", absSteameOvenOne?.workState.toString() + "---" + absSteameOvenOne?.guid?.guid)
        if (model != CURVE) {


            if (absSteameOvenOne == null || !Objects.equal(
                    absSteameOvenOne?.id,
                    event.pojo.id
                )
            ) {
                return
            }

            absSteameOvenOne = event.pojo as AbsSteameOvenOneNew

            absSteameOvenOne?.let { absSteameOvenOne ->
                Log.e(
                    "工作",
                    absSteameOvenOne?.workState.toString() + "---" + absSteameOvenOne?.guid?.guid
                )
                if (absSteameOvenOne?.workState == SteamOvenOnePowerOnStatusNew.finish ||
                    absSteameOvenOne?.workState == SteamOvenOnePowerOnStatusNew.Off && isWorked
                ) {
                    if (isFinishAct) {
                        setResult(RESULT_OK)
                    }
                    finish()
                    return
                }
                act_cooked_left_time_title.visibility = View.VISIBLE
                act_cooked_left_time.visibility = View.VISIBLE
                act_cooked_left_time_min.visibility = View.VISIBLE
                act_chart_top.visibility = View.INVISIBLE
                if ((absSteameOvenOne?.workState == SteamOvenOnePowerOnStatusNew.preWorkHeat ||
                            absSteameOvenOne?.workState == SteamOvenOnePowerOnStatusNew.Working)
                ) {


                    isWorked=true

                    drawPoint((event.pojo as AbsSteameOvenOneNew).curTemp)
                    page_btn_cooked_save.visibility = View.GONE
                    act_recipe_pause_btn.visibility = View.VISIBLE
                    act_end_recipe_btn.visibility = View.VISIBLE
                    val outTime: Int = absSteameOvenOne?.restTimeH * 256 + absSteameOvenOne.restTime
                    var total = absSteameOvenOne.setTimeH * 256 + absSteameOvenOne.setTime

                    act_cooked_left_time.text =getLeftTime(outTime)


                    mRecipeCurveSuccessBean?.payload?.stepList?.forEachIndexed { index, it ->
                        mRecipeCurveDataAdapter.data[index].isShow = it.markTime?.toInt()
                            ?.minus(3) ?: 0 < (total - outTime) && (it.markTime?.toInt()
                            ?: 0) + 3 > (total - outTime)
                        if (mRecipeCurveDataAdapter.data[index].isShow) {
                            if (mRecipeCurveDataAdapter.data[index]?.voiceUrl?.isNotEmpty1() == true) {
                                mRecipeCurveDataAdapter.data[index]?.voiceUrl?.let {
                                    if (!mRecipeCurveDataAdapter.data[index]?.isPlayed) {
                                        mRecipeCurveDataAdapter.data[index]?.isPlayed=true;
                                        playAudio(it)
                                    }
                                }
                            }
                            mRecipeCurveDataAdapter.notifyItemChanged(index)
                        }
                    }
                }
            }
        }
    }

    var pos = 0;
    var entryList = ArrayList<Entry>() //数据集合


    fun drawPoint(leftTemp: Short) {
        val bean = LineChartDataBean()
        bean.yValue = leftTemp.toFloat()
        //        bean.xValue = stoveHead.time;
        if (entryList.isEmpty()) {
            bean.xValue = 0f
        } else {
            bean.xValue = entryList.get(entryList.size - 1).x + 3
        }

//        dataBeanList.add(bean);
        entryList.add(Entry(bean.xValue, bean.yValue))
        pageAddEntry(entryList)
    }

    fun pageAddEntry(entryList: List<Entry>) {
        if (entryList.size > 2) {
            val bean = RecipeCurveSuccessBean.StepList()
            if (operationList.size == 0) {
                bean.description = "开始烹饪"
                bean.markTemp = entryList[0].y.toInt().toString() + ""
                bean.markTime = entryList[0].x.toInt().toString() + ""
                operationList.add(0, bean)
                appointList.add(Entry(entryList[0].x, 1F))
            } else if (operationList.size == 1) {
                bean.description = "结束烹饪"
                bean.markTemp = entryList[entryList.size - 1].y.toInt().toString() + ""
                bean.markTime = entryList[entryList.size - 1].x.toInt().toString() + ""
                appointList.add(Entry(entryList[entryList.size - 1].x, 2F))
                operationList.add(bean)
            } else {
                for (i in operationList.indices) {
                    if (operationList[i].description == "结束烹饪") {
                        operationList[i].markTemp =
                            entryList[entryList.size - 1].y.toInt().toString() + ""
                        operationList[i].markTime =
                            entryList[entryList.size - 1].x.toInt().toString() + ""
                        appointList[i].x = entryList[entryList.size - 1].x
                        break
                    }
                }
            }
            Collections.sort(appointList, object : Comparator<Entry?> {


                override fun compare(o1: Entry?, o2: Entry?): Int {
                    if (o1?.x!! > o2?.x!!) {
                        return 1
                    }
                    return if (o1.x < o2.x) {
                        -1
                    } else 0
                }
            })
//            dm.lineDataAppointSet(appointList)
        }
        //添加数据
        dm.addEntryWithMV(entryList[entryList.size - 1])
        val h: Highlight = cook_chart11.getHighlightByTouchPoint(
            cook_chart11.right.toFloat(),
            cook_chart11.top.toFloat()
        )
        cook_chart11.highlightValue(h, true)
    }

    private lateinit var simpleAudioPlayer: SimpleAudioPlayer
    var mPosAudio = 0

    private fun playAudio(path: String) {
        simpleAudioPlayer.play(path)
        simpleAudioPlayer.addCallback(object : PlayerCallback {
            override fun onPlay() {
                Log.e(TAG, "onPlay----")

            }

            override fun onProgress(progress: Int) {
                Log.e(TAG, "$progress----")
                mRecipeCurveDataAdapter.data[mPosAudio].playTime = progress
                mRecipeCurveDataAdapter.data[mPosAudio].status = 3
                runOnUiThread {
                    mRecipeCurveDataAdapter.notifyItemChanged(mPosAudio, "1")
                }

            }

            override fun onPause() {

            }

            override fun onStop() {
                Log.e(TAG, "onStop----")
            }
        })

    }

    override fun showDialog() {

        runOnUiThread {
            ld = LoadingDialog(this)
            ld.setLoadingText("上传中")
                .setInterceptBack(false)
                .closeSuccessAnim()
                .setRepeatCount(1)
                .show()
        }

    }

    var workTime = 0L

    var timestamp=System.currentTimeMillis();
    override fun initView() {

        mLineChartDataBean = ArrayList()
        AlarmDataUtils.init(this)
        simpleAudioPlayer = RokiMediaPlayer()
        mRecipeSuccessApi = RecipeSuccessApi(this)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(
                arrayOf(
                    Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ),
                0x11
            )
        }

        modelInit()

        img_back.setOnClickListener {

            finish()
        }
        pag_cooked_preview_img.setOnClickListener { choosePicture() }

        add_dished_prepare_by_step_page_rl.setOnClickListener {
            var mIntent = Intent(activity, DishesPreparedByStepActivity::class.java)
            mIntent.putExtra(
                DishesPreparedByStepActivity.UPDATECURVESTEPREQUEST,
                mRecipeCurveSuccessBean
            )
            mIntent.putExtra(MODEL, model)
            startActivityForResult(mIntent, REQUESTCODE)
        }

        if (model == CURVE) {
            val menuCreator = SwipeMenuCreator { _, rightMenu, _ ->
                val deleteItem = SwipeMenuItem(context)
                deleteItem
                    .setText("删除")
                    .setTextColor(Color.argb(0xff,0xff,0x25,0x25)) // 文字颜色。
                    .setTextSize(15) // 文字大小。
                    .setWidth(140).height = ViewGroup.LayoutParams.MATCH_PARENT
                rightMenu.addMenuItem(deleteItem)
            }
            page_cooked_data_list.setSwipeMenuCreator(menuCreator)
        }
        RecipeDetailPage.setStatusBarColor(activity, R.color.white)
        RecipeDetailPage.setLightStatusBar(
            activity, true
        )

        var timeRecord = 0L;

        mRecipeCurveDataAdapter = RecipeCurveDataAdapter(this, model,mRecipeCurveSuccessBean?.payload?.needTime, object : IOnTouchListener {
            override fun onTouch(pos: Int, v: View, event: MotionEvent, isPress: Boolean): Boolean {

                mPosAudio = pos
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        Log.e(TAG + "1", "ACTION_DOWN")
                        workTime = System.currentTimeMillis()
                        timeRecord = System.currentTimeMillis()
                    }
                    MotionEvent.ACTION_MOVE -> {

                        Log.e(
                            TAG + "1",
                            "ACTION_MOVE" + "---" + RecordManager.getInstance().state + "-----" + System.currentTimeMillis()
                        )
                        if (RecordManager.getInstance().state == RecordHelper.RecordState.STOP &&
                            RecordManager.getInstance().state != RecordHelper.RecordState.RECORDING
                            || RecordManager.getInstance().state == RecordHelper.RecordState.IDLE
                            && System.currentTimeMillis() - timeRecord > 300
                        ) {
                            RecordManager.getInstance().start();
                        }


                        if (RecordManager.getInstance().state == RecordHelper.RecordState.RECORDING) {
                            runOnUiThread {
                                mRecipeCurveDataAdapter.data[mPosAudio].status = 1;
                                mRecipeCurveDataAdapter.data[mPosAudio].time =
                                    (System.currentTimeMillis() - workTime).toInt();
                                Log.e(
                                    TAG,
                                    "ACTION_MOVE" + mRecipeCurveDataAdapter.data[mPosAudio].time
                                )
                                mRecipeCurveDataAdapter.notifyItemChanged(mPosAudio, "1")

                            }

                        }
                    }
                    MotionEvent.ACTION_UP -> {
                        Log.e(TAG + "1", "ACTION_UP")

                        timeRecord = System.currentTimeMillis()
                        if (RecordManager.getInstance().state == RecordHelper.RecordState.RECORDING) {
                            Log.e(TAG + "1", "停止")
                            RecordManager.getInstance().stop()
                            mRecipeCurveDataAdapter.data[mPosAudio].status = 2;
//                        Log.e(TAG, "---------$it"+mPosAudio+"---"+mCurveStepDtoList[mPosAudio].status);
                            mRecipeCurveDataAdapter.notifyItemChanged(mPosAudio, "1")
                        }
                    }

                }
                return true


            }

            override fun onClick(view: View, pos: Int) {


                mPosAudio = pos
                when (view.id) {
                    R.id.rl_audio_recorded_delete -> {


                        if (simpleAudioPlayer?.state == PlayerState.Playing) {
                            simpleAudioPlayer.stop()
                        }
                        if (RecordManager.getInstance().state == RecordHelper.RecordState.RECORDING) {
                            RecordManager.getInstance().stop();
                        }
                        mRecipeCurveDataAdapter.data[mPosAudio].time = 0
                        mRecipeCurveDataAdapter.data[mPosAudio].playTime = 0
                        mRecipeCurveDataAdapter.data[mPosAudio].voiceUrl = ""
                        mRecipeCurveDataAdapter.data[mPosAudio].status = 4
                        runOnUiThread {
                            mRecipeCurveDataAdapter.notifyItemChanged(mPosAudio, "1")
                        }
                    }
                    R.id.rl_audio_recorded_finish -> {

                        mRecipeCurveDataAdapter.data[mPosAudio].voiceUrl?.let { it1 -> playAudio(it1) }

                    }
                }
            }
        })


        var mLinearLayoutManager = LinearLayoutManager(context)
        mLinearLayoutManager.stackFromEnd = true;
        page_cooked_data_list.layoutManager = mLinearLayoutManager


        val mMenuItemClickListener =
            SwipeMenuItemClickListener { swipeMenuBridge ->
                // 任何操作必须先关闭菜单，否则可能出现Item菜单打开状态错乱。
                swipeMenuBridge.closeMenu()
                val adapterPosition = swipeMenuBridge.adapterPosition // RecyclerView的Item的position。
                when (swipeMenuBridge.position) {
                    0 -> {
                        this.deletePosition = adapterPosition

                        if (mRecipeCurveDataAdapter.data[adapterPosition].markName=="烹饪开始"||mRecipeCurveDataAdapter.data[adapterPosition].markName=="烹饪结束"||mRecipeCurveDataAdapter.data[adapterPosition].markName=="预热完成"){
                            ToastUtils.show("固定步骤不能删除",Toast.LENGTH_LONG)
                            return@SwipeMenuItemClickListener
                        }
                        mRecipeCurveDataAdapter.data[adapterPosition].curveStepId?.let {
                            mRecipeSuccessApi.delete(
                                R.id.page_cooked_data_list,
                                it
                            )
                        }


                        if (mRecipeCurveDataAdapter.data[adapterPosition].curveStepId == null) {
                            mRecipeCurveDataAdapter.data.removeAt(deletePosition)
                            appointList.removeAt(deletePosition)
                            mRecipeCurveDataAdapter.notifyDataSetChanged()
                            dm.lineDataAppointSet(appointList)
                            deletePosition = -1

                        }


                    }
                }
            }

        page_cooked_data_list.setSwipeMenuItemClickListener(mMenuItemClickListener)
        page_cooked_data_list.adapter = mRecipeCurveDataAdapter
        page_cooked_data_list.setItemViewCacheSize(1)

        mRecipeSuccessApi.query(R.layout.activity_recipe_success, curveId)
        listenKeyFboardVisible()




        dm = DynamicLineChartManager(cook_chart11, cx)
        dm.initLineDataSet("烹饪曲线", resources.getColor(R.color.line_chart_easy), entryList, true)
        dm.setChartAttribute(false, false)


        RecordAudioListener()
        RecordAudioSuccess()

    }

    override fun onDestroy() {
        super.onDestroy()
        //退出页面录音停止
        if (RecordManager.getInstance().state == RecordHelper.RecordState.RECORDING) {
            RecordManager.getInstance().stop();
        }
        if (simpleAudioPlayer != null && simpleAudioPlayer.state == PlayerState.Playing) {
            simpleAudioPlayer.stop()
        }

    }

    lateinit var dm: DynamicLineChartManager

    //蒸烤一体机报警接收事件
    @Subscribe
    fun onEvent(event: NewSteamOvenOneAlarmEvent) {
        Log.e("报警", "-------")
        val isNoShow = false
        if (event.steameOvenOne is AbsSteameOvenOneNew) {
            val steameOvenOne = event.steameOvenOne as AbsSteameOvenOneNew
            val alarms = event.alarmId
            steamOvenOneAlarmStatus(steameOvenOne, alarms, isNoShow)
        }

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
//

        Log.e(TAG, "onActivityResult1111$localClassName")
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUESTCODE) {
                Log.e(TAG, "onActivityResult")
                var mRecipeCurveSuccessBean =
                    data?.getSerializableExtra(DishesPreparedByStepActivity.UPDATECURVESTEBACK) as RecipeCurveSuccessBean
                if (mRecipeCurveSuccessBean.payload?.prepareStepList?.isNotEmpty() == true) {
                    Glide.with(context).load(R.mipmap.icon_has_step)
                        .into(add_dished_prepare_by_step_page_image)
                }

                this.mRecipeCurveSuccessBean = mRecipeCurveSuccessBean
            } else {
                EventUtils.postEvent(ActivityResultOnVideoEvent(requestCode, resultCode, data))
            }
        }
    }

    private val TAG = "RecipeSuccessActivity"

    override fun initData() {

        mUploadFileApi = UploadFileApi(this)

    }

//    private var mCurveStepDtoList: ArrayList<RecipeCurveSuccessBean.StepList> = ArrayList()


    var isClick = false

    companion object {
        var isFinishAct: Boolean = false
        fun show(context: Context, model: Int) {
            var intent = Intent(context, RecipeSuccessActivity::class.java)
            intent.putExtra(MODEL, model)
            context.startActivity(intent)
        }


        fun show(context: Activity, model: Int, uid: String, curveId: Long, isFinish: Boolean) {

            isFinishAct = isFinish
            var intent = Intent(context, RecipeSuccessActivity::class.java)

            intent.putExtra(GUID, uid)
            intent.putExtra(MODEL, model)
            intent.putExtra(CURVEID, curveId)
            if (isFinish) {
                context.startActivityForResult(intent, 0x123)
            } else {
                context.startActivity(intent)
            }
        }

        @JvmStatic
        fun show(
            context: Activity,
            model: Int,
            uid: String,
            curveId: Long,
            isFinish: Boolean,
            cookName: String
        ) {
            isFinishAct = isFinish
            var intent = Intent(context, RecipeSuccessActivity::class.java)
            intent.putExtra(GUID, uid)
            intent.putExtra(MODEL, model)
            intent.putExtra(CURVEID, curveId)
            intent.putExtra(COOK_NAME, cookName)
            if (isFinish) {
                context.startActivityForResult(intent, 0x123)
            } else {
                context.startActivity(intent)
            }
        }

        @JvmStatic
        fun show(context: Context, model: Int, curveId: Long) {
            var intent = Intent(context, RecipeSuccessActivity::class.java)
            intent.putExtra(MODEL, model)
            intent.putExtra(CURVEID, curveId)
            context.startActivity(intent)
        }


        @JvmStatic
        fun show(
            context: Context,
            model: Int,
            curveId: Long,
            operationList: ArrayList<RecipeCurveSuccessBean.StepList>
        ) {
            var intent = Intent(context, RecipeSuccessActivity::class.java)
            intent.putExtra(MODEL, model)
            intent.putExtra(CURVEID, curveId)
            intent.putExtra(CURVEIDLIST, operationList)

            context.startActivity(intent)
        }

        @JvmStatic
        fun show(
            context: Context,
            model: Int,
            curveId: Long,
            operationList: ArrayList<RecipeCurveSuccessBean.StepList>,
            uid: String?,
            entryList: ArrayList<Entry>
        ) {
            var intent = Intent(context, RecipeSuccessActivity::class.java)
            intent.putExtra(MODEL, model)
            if (uid != null)
                intent.putExtra(GUID, uid)
            intent.putExtra(CURVEID, curveId)
            intent.putExtra(CURVEIDLIST, operationList)
            intent.putExtra(ENTRYLIST, entryList)

            context.startActivity(intent)
        }

        @JvmStatic
        fun show(
            context: Context,
            model: Int,
            curveId: Long,
            operationList: ArrayList<RecipeCurveSuccessBean.StepList>,
            uid: String?,
            entryList: ArrayList<Entry>,
            appointList: ArrayList<Entry>
        ) {
            var intent = Intent(context, RecipeSuccessActivity::class.java)
            intent.putExtra(MODEL, model)
            if (uid != null)
                intent.putExtra(GUID, uid)
            intent.putExtra(CURVEID, curveId)
            intent.putExtra(CURVEIDLIST, operationList)
            intent.putExtra(ENTRYLIST, entryList)
            intent.putExtra(APPOINTLIST, appointList)
            context.startActivity(intent)
        }

        /**
         * 类型
         */
        //曲线
        val CURVE = 0;

        //烹饪 不可点击
        val RECIPEING = 1;
        val CURVEID = "curveId"
        val GUID = "uid"
        val DEVICE = "device"
        val MODEL = "model"
        val COOK_NAME = "cook_name"

        val CURVEIDLIST = "curveidlist"
        val ENTRYLIST = "entryList"
        val APPOINTLIST = "appointList"
    }

    private fun HStop() {
        var mCookedFinishNewDialog: CookedFinishNewDialog? = null
        mCookedFinishNewDialog = CookedFinishNewDialog(context) {
            if (iStove != null) {
                val status =
                    if (iStove!!.rightHead.status == StoveStatus.Off) StoveStatus.StandyBy else StoveStatus.Off
                iStove!!.setStoveStatus(false, 1, status, object : VoidCallback {
                    override fun onSuccess() {
                        mRecipeSuccessApi.setStatus(
                            R.id.page_cooked_data_list,
                            curveId.toInt(),
                            RecipeSuccessApi.END
                        )
                        finish()
                    }

                    override fun onFailure(t: Throwable) {}
                })
                if (iStove!!.rightHead.status == StoveStatus.Off) {
                    mRecipeSuccessApi.setStatus(
                        R.id.page_cooked_data_list,
                        curveId.toInt(),
                        RecipeSuccessApi.END
                    )
                    finish()
                }
            } else if (absSteameOvenOne != null) {
                absSteameOvenOne?.let { it ->
                    it.setSteamWorkStatus(
                        IntegStoveStatus.workCtrl_stop,
                        4.toShort(),
                        object : VoidCallback {
                            override fun onSuccess() {
                                mRecipeSuccessApi.setStatus(
                                    R.id.recipe_save_success,
                                    curveId.toInt(),
                                    RecipeSuccessApi.END
                                )
//                                finish()
                            }

                            override fun onFailure(t: Throwable) {}
                        })
                }
            }
            mCookedFinishNewDialog?.dismiss()
        }
        mCookedFinishNewDialog.create()
        mCookedFinishNewDialog.show()
    }

    var operationList: ArrayList<RecipeCurveSuccessBean.StepList> =
        ArrayList<RecipeCurveSuccessBean.StepList>()
    var curveId = 0L
    var guid = ""

    var model = 0

    private fun modelInit() {

        model = intent.getIntExtra(MODEL, CURVE)
        curveId = intent.getLongExtra(CURVEID, 190L)


        if (model == CURVE) {
            act_cooked_left_time_min.visibility = View.GONE
            act_cooked_left_time.visibility = View.GONE
            act_cooked_left_time_title.visibility = View.GONE
            act_chart_top.visibility = View.VISIBLE
        } else {
            act_cooked_left_time_min.visibility = View.VISIBLE
            act_cooked_left_time.visibility = View.VISIBLE
            act_cooked_left_time_title.visibility = View.VISIBLE
            act_chart_top.visibility = View.GONE
        }
        if (intent.hasExtra(GUID)) {
            guid = intent.getStringExtra(GUID)
        }

        if (intent.hasExtra(CURVEIDLIST)) {
            operationList =
                intent.getSerializableExtra(CURVEIDLIST) as ArrayList<RecipeCurveSuccessBean.StepList>
        }
//        if (intent.hasExtra(APPOINTLIST)) {
//            appointList =
//                intent.getSerializableExtra(APPOINTLIST) as ArrayList<Entry>
//        }
        page_edit_line_name.setText(mRecipeCurveSuccessBean?.payload?.name)
//        page_edit_line_name.addTextChangedListener(object : TextWatcher {
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//
//            }
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//
//            }
//
//            override fun afterTextChanged(s: Editable?) {
//                s?.length?.let {
//                    if (it > 10) {
//                        page_edit_line_name.background =
//                            resources.getDrawable(R.drawable.shape_input_curve_et_length_limit);
//
//                    } else {
//                        page_edit_line_name.background =
//                            resources.getDrawable(R.drawable.shape_input_curve_et);
//                    }
//
//                }
//
//            }
//
//        })
        if (model == CURVE) {
            tv_title.text = "烹饪完成"
            page_btn_cooked_save.visibility = View.VISIBLE
            act_end_recipe_btn.visibility = View.GONE
            act_recipe_pause_btn.visibility = View.GONE
            page_btn_cooked_save.setOnClickListener {

                ThreadPoolManager.getInstance().execute {
                    mRecipeCurveSuccessBean?.let { mRecipeCurve ->
                        if (TextUtils.isEmpty(page_edit_line_name.text)) {
                            ToastUtils.show("请输入曲线名称", Toast.LENGTH_LONG)
                            return@execute
                        }



                        mRecipeCurve.payload?.name = page_edit_line_name.text.toString()
                        mRecipeCurve.payload?.deviceGuid = guid
                        mRecipeCurve.payload?.stepList?.apply {
                            clear()
                            addAll(mRecipeCurveDataAdapter.data)
                        } ?: run {
                            mRecipeCurve.payload?.stepList = ArrayList()
                            mRecipeCurve.payload?.stepList?.clear()
                            mRecipeCurve.payload?.stepList?.addAll(mRecipeCurveDataAdapter.data)
                        }
                        if (mRecipeCurve.payload?.stepList?.isEmpty() == true) {
                            com.hjq.toast.ToastUtils.show("请至少添加一个步骤")
                            return@execute
                        }


                        if (model != CURVE){
                            mRecipeCurve.payload?.gmtCreate?.let {
                                mRecipeCurve.payload?.needTime=
                                    ((timestamp-it)/1000).toString()
                            }
                        }

                        Log.e("请求", mRecipeCurve.payload?.stepList?.toString())
                        mRecipeSuccessApi.submitCookingCurve(
                            R.id.page_btn_cooked_save,
                            mRecipeCurve
                        )
                    }
                }
            }
        } else {
            //还原
            page_btn_cooked_save.text = "结束烹饪"
            if (TextUtils.isEmpty(guid))
                return
            var iDevice: IDevice = Plat.deviceService.lookupChild(guid)
            if (iDevice is Stove) {
                iStove = iDevice
                act_recipe_pause_btn.visibility = View.GONE
            } else if (iDevice is AbsSteameOvenOneNew) {
                absSteameOvenOne = iDevice as AbsSteameOvenOneNew
            }


            page_edit_line_name.isEnabled = false
            page_cooked_preview_title_ctl.visibility = View.GONE
            var cookName: String = intent.getStringExtra(COOK_NAME)
            tv_title.text = cookName
            act_end_recipe_btn.setOnClickListener {
                HStop();
            }
            act_recipe_pause_btn.setOnClickListener {
                absSteameOvenOne?.let {
                    it.setSteamWorkStatus(
                        if (SteamOvenHelper.isPause(it?.workState))
                            IntegStoveStatus.workCtrl_continue else IntegStoveStatus.workCtrl_time_out,
                        4.toShort(),
                        object : VoidCallback {
                            override fun onSuccess() {
                                pauseAndContinue()
                            }

                            override fun onFailure(t: Throwable) {}
                        })
                }
            }


        }
    }

    fun startWork() {
        var controlWay: ArrayList<Short> = ArrayList()
        var tap: ArrayList<Short> = ArrayList()
        var temp: ArrayList<Int> = ArrayList()
        var time: ArrayList<Int> = ArrayList()
        if (iStove != null) {
            mRecipeCurveSuccessBean?.payload?.temperatureCurveParams?.let { it ->


                var data = it.substring(1, it.indexOf("}"))
                ChartDataReviseUtil.curveDataToLineFormat(data).filter { list ->
                    list.isBigPoint
                }.forEachIndexed { index, lineChartDataBean ->
                    ///温度不能从0开始所以第二个点开始输入
                    if (index >= 1) {
                        controlWay.add(1)
                        tap.add(lineChartDataBean.gear.toShort())
                        temp.add(lineChartDataBean.yValue.toInt())
                        time.add(lineChartDataBean.xValue.toInt())
                    }
                }
            }


            if (iStove != null) {
                iStove?.setAutoTemporarySetting(0, 1, object : VoidCallback {
                    override fun onSuccess() {
                        Log.e(TAG, "指令下发成功")
                        iStove?.setAutoTemporaryStep(0,
                            tap.size.toShort(),
                            controlWay,
                            tap,
                            temp,
                            time,
                            object : VoidCallback {
                                override fun onSuccess() {
                                    Log.e(TAG, "开启成功")
                                    page_btn_cooked_save.visibility = View.GONE
                                    act_end_recipe_btn.visibility = View.VISIBLE
                                    act_recipe_pause_btn.visibility = View.VISIBLE
                                }

                                override fun onFailure(t: Throwable?) {
                                    Log.e(TAG, "开启失败" + t.toString())
                                }

                            })
                    }

                    override fun onFailure(t: Throwable?) {

                    }
                })
            }
        } else if (absSteameOvenOne != null) {
            mRecipeCurveSuccessBean?.payload?.curveSettingParams?.let {

                mCurveWorkBean.let {


                    if (it != null && it.isNotEmpty()) {
                        try {
                            absSteameOvenOne?.let {
                                val msg: Msg = it?.newReqMsg(MsgKeys.setDeviceAttribute_Req)
                                msg.putOpt(
                                    MsgParams.ArgumentNumber,
                                    mCurveWorkBean.size * 5 + 3
                                )
                                msg.putOpt(MsgParamsNew.type, 2)
                                //一体机电源控制
                                msg.putOpt(MsgParamsNew.powerCtrlKey, 2)
                                msg.putOpt(MsgParamsNew.powerCtrlLength, 1)
                                msg.putOpt(MsgParamsNew.powerCtrl, 1)
                                //一体机工作控制
                                msg.putOpt(MsgParamsNew.workCtrlKey, 4)
                                msg.putOpt(MsgParamsNew.workCtrlLength, 1)
                                msg.putOpt(MsgParamsNew.workCtrl, 1)
                                //段数
                                msg.putOpt(MsgParamsNew.sectionNumberKey, 100)
                                msg.putOpt(MsgParamsNew.sectionNumberLength, 1)
                                msg.putOpt(MsgParamsNew.sectionNumber, mCurveWorkBean.size)
                                for (i in mCurveWorkBean.indices) {
                                    val bean: RecipeStepBean = mCurveWorkBean[i].exchange()
                                    //模式
                                    msg.putOpt(MsgParamsNew.modeKey + i, 101 + i * 10)
                                    msg.putOpt(MsgParamsNew.modeLength + i, 1)
                                    msg.putOpt(MsgParamsNew.mode + i, bean.work_mode)
                                    //温度上温度
                                    msg.putOpt(MsgParamsNew.setUpTempKey + i, 102 + i * 10)
                                    msg.putOpt(MsgParamsNew.setUpTempLength + i, 1)
                                    msg.putOpt(MsgParamsNew.setUpTemp + i, bean.temperature)


                                    //
                                    msg.putOpt(MsgParamsNew.setDownTempKey + i, 103 + i * 10)
                                    msg.putOpt(MsgParamsNew.setDownTempLength + i, 1)
                                    msg.putOpt(MsgParamsNew.setDownTemp + i, bean.temperature2)
                                    //时间
                                    val time = bean.time
                                    msg.putOpt(MsgParamsNew.setTimeKey + i, 104 + i * 10)
                                    msg.putOpt(MsgParamsNew.setTimeLength + i, 1)
                                    val lowTime =
                                        if (time > 255) (time and 0Xff).toShort() else time.toShort()
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
                                    //msg.putOpt(MsgParamsNew.setTime + i, bean.getTime()*60);
                                    //蒸汽量
                                    msg.putOpt(MsgParamsNew.steamKey + i, 106 + i * 10)
                                    msg.putOpt(MsgParamsNew.steamLength + i, 1)
                                    msg.putOpt(MsgParamsNew.steam + i, bean.steam_flow)
                                }
                                it.setSteamOvenOneMultiStepMode(msg, object : VoidCallback {
                                    override fun onSuccess() {

                                    }

                                    override fun onFailure(t: Throwable) {}
                                })
                            }
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                    }
                }

            }
        }

    }


    fun pauseAndContinue() {
        if (act_recipe_pause_btn.text == "暂停") {
            act_recipe_pause_btn.text = "继续"
            mRecipeSuccessApi.setStatus(
                R.id.ssdk_sina_web_title_id,
                curveId.toInt(),
                RecipeSuccessApi.PAUSE
            )


        } else {
            act_recipe_pause_btn.text = "暂停"
            mRecipeSuccessApi.setStatus(
                R.id.device_polling_task_id,
                curveId.toInt(),
                RecipeSuccessApi.CONTINUE
            )
        }
    }


    private lateinit var mRecipeCurveDataAdapter: RecipeCurveDataAdapter


    lateinit var pickHelper: PickImageVideoHelper

    override fun choosePic(pos: Int) {
        this.pos = pos
        pickHelper = PickImageVideoHelper(this, pickCallback, pickVideoCallback)
        choosePicture()
    }

    var fileType: String = ""
    var pickVideoCallback =
        PickImageVideoHelper.PickVideoCallbackTwo { bmp, path ->
            if (bmp == null) {
                return@PickVideoCallbackTwo
            }
            fileType = "3"
//            mUploadFileApi.uploadFile(R.id.about_version_code, path, fileType)
//            ThreadPoolManager.getInstance().execute{
//               var url= AliyunOSSManager.getInstance().uploadFile(AliyunOSSManager.getOSSVideoName("curve"+System.currentTimeMillis()),path)


            mRecipeCurveDataAdapter.data[pos].videoUrl = path
                runOnUiThread {
                    mRecipeCurveDataAdapter.data[pos].isShow=true
                    mRecipeCurveDataAdapter.notifyDataSetChanged()
                }

        }

    var pickCallback =
        PickImageVideoHelper.PickImgCallbackTwo { bmp ->
            if (bmp == null) {
                return@PickImgCallbackTwo
            }
            val fileDirPath: String? = context?.filesDir?.absolutePath
            val filePath = fileDirPath + File.separator +
                    SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA)
                        .format(System.currentTimeMillis()) + ".jpg"

            bitmap2Path(bmp, filePath)
//            fileType = "2"
//            ThreadPoolManager.getInstance().execute{
//                var url= AliyunOSSManager.getInstance().uploadFile(AliyunOSSManager.getOSSImgName("curve",System.currentTimeMillis().toString()),filePath)

            mRecipeCurveDataAdapter.data[pos].imageUrl = filePath;

            runOnUiThread {
                mRecipeCurveDataAdapter.data[pos].isShow = true;
                mRecipeCurveDataAdapter.notifyItemChanged(pos)
            }


//            }


//            mUploadFileApi.uploadFile(R.id.about_version_code, filePath, fileType)


    }


    private lateinit var mUploadFileApi: UploadFileApi
    private fun choosePicture() {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val selfPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            if (selfPermission == 0) {
                pickHelper.showPickDialog("")
            } else {
                PermissionsUtils.checkPermission(
                    this,
                    Manifest.permission.CAMERA,
                    PermissionsUtils.CODE_USER_INFO_SHARE
                )
            }
        } else {
            pickHelper.showPickDialog("")
        }
    }

    var appointList: ArrayList<Entry> = ArrayList()

    override fun onFailure(requestId: Int, requestCode: Int, msg: String?, data: Any?) {
        if (R.id.auto_work_layout == requestId) {
            ld.close()
        }
        Toast.makeText(context, "失败：$msg", Toast.LENGTH_LONG).show()
    }

    override fun onSaveCache(requestId: Int, requestCode: Int, paramObject: Any?) {

    }




    fun getSteam(funtionBeans: DeviceParam): String {
        var steam = ""
        if (funtionBeans.mode == null || funtionBeans.steamVolume == null) {
            return ""
        }
        if (funtionBeans.steamVolume.toShort() <= 0) {
            return ""
        }
        if (SteamOvenHelper.isShowSteam(funtionBeans.mode.toShort())) {
            steam = getSteamContent(funtionBeans.steamVolume.toShort())
        }
        return steam
    }

    lateinit var ld: LoadingDialog




    var totalTime=0
    lateinit var mLineChartDataBean: ArrayList<LineChartDataBean>
    lateinit var mCurveWorkBean: List<CurveWorkBeanItem>
    override fun onSuccess(requestId: Int, requestCode: Int, paramObject: Any?) {


        if (requestId == R.id.about_version_code) {
            if (paramObject is UpFileBean) {
                if (fileType == "2") {
                    mRecipeCurveDataAdapter.data[pos].imageUrl = paramObject.path;
                } else if (fileType == "3") {
                    mRecipeCurveDataAdapter.data[pos].voiceUrl = paramObject.path;
                }
                mRecipeCurveDataAdapter?.notifyItemChanged(pos)
            }
            return
        }

        if (requestId == R.id.page_cooked_data_list) {
            if (this.deletePosition != -1) {
                mRecipeCurveDataAdapter.data.removeAt(deletePosition)
                mRecipeCurveDataAdapter.notifyDataSetChanged()
                deletePosition = -1
            }
        }
        if (requestId == R.layout.activity_recipe_success) {
            if (paramObject is RecipeCurveSuccessBean) {
                mRecipeCurveSuccessBean = paramObject
                Log.e("onSuccess", mRecipeCurveSuccessBean.toString())
                mRecipeCurveSuccessBean?.let { it ->
                    with(it.payload) {
//                        Glide.with(this@RecipeSuccessActivity).load(imageCover).into(page_choose_pick_image)
//                        page_choose_pick_image_introduction.setText(introduction)
                        it.payload?.name?.let {
                            page_edit_line_name.setText(it)

                        }

                        if (this?.prepareStepList?.isNotEmpty() == true) {
                            Glide.with(context).load(R.mipmap.icon_has_step)
                                .into(add_dished_prepare_by_step_page_image)
                        }
                        if (it.payload?.prepareStepList?.isNotEmpty() == true) {

                            Glide.with(context).load(R.mipmap.icon_has_step)
                                .into(add_dished_prepare_by_step_page_image)
                        }
                        it.payload?.stepList?.let { list ->
                            if (operationList.isNotEmpty()) {
                                mRecipeCurveDataAdapter.data.addAll(operationList)
                            } else {
                                mRecipeCurveDataAdapter.data.addAll(list)
                            }
                            if (mRecipeCurveDataAdapter.data.isEmpty()) {
                                var bean = RecipeCurveSuccessBean.StepList()
                                bean.markTemp = entryList[0].y.toString()
                                bean.markTime = entryList[0].x.toString()
                                bean.markName = "烹饪开始"
                                mRecipeCurveDataAdapter.data.add(0, bean)
                                bean = RecipeCurveSuccessBean.StepList()
                                bean.markTemp = entryList[entryList.size - 1].y.toString()
                                bean.markTime = entryList[entryList.size - 1].x.toString()
                                bean.markName = "烹饪结束"
                                mRecipeCurveDataAdapter.data.add(bean)
                            }
                            mRecipeCurveDataAdapter.data.sortBy { it.markTime?.toInt() }
                            mRecipeCurveDataAdapter.data.forEach {

                                Log.e(TAG, it.markTime + "----")
                            }
//                                .sortWith(compareBy({ it.age }, { it.name }))
                            mRecipeCurveDataAdapter.notifyDataSetChanged()
                        } ?: run {
                            it.payload?.stepList = ArrayList()
                            if (operationList.isNotEmpty()) {
                                mRecipeCurveDataAdapter.data?.addAll(operationList)
                            }
                            mRecipeCurveDataAdapter.data.sortBy { it.markTime?.toInt() }

                            mRecipeCurveDataAdapter.data.forEach {

                                Log.e(TAG, it.markTime + "----")
                            }

//                            mRecipeCurveDataAdapter.notifyDataSetChanged()
                        }
                        mRecipeCurveDataAdapter.setNewInstance(mRecipeCurveDataAdapter.data)
                        page_edit_line_name.setText(this?.name)

                        this?.curveSettingParams?.let {
                            mCurveWorkBean = fromJson(
                                it,
                                object : TypeToken<List<CurveWorkBeanItem?>?>() {}.type
                            )

                            var time = 0
                            mCurveWorkBean.forEach { it ->
                                time += it.setTime
                            }

                            act_cooked_left_time.text = getTime(time).toString()
                        }

                        this?.temperatureCurveParams?.let {


                            var dataString = it?.substring(1, it.indexOf("}"))
                            mLineChartDataBean =
                                ChartDataReviseUtil.curveDataToLineFormat(dataString) as ArrayList<LineChartDataBean>
                            var entryList = ArrayList<Entry>()
                            //有传值
                            if (intent.hasExtra(ENTRYLIST)) {
                                entryList =
                                    intent.getSerializableExtra(ENTRYLIST) as ArrayList<Entry>
                            } else {
                                mLineChartDataBean.forEachIndexed { index, _ ->
//                                if (mLineChartDataBean[index].isBigPoint) {
//                                    appointList.add(Entry(mLineChartDataBean[index].xValue, 2f))
//                                }
                                    entryList.add(
                                        Entry(
                                            mLineChartDataBean[index].xValue,
                                            mLineChartDataBean[index].yValue
                                        )
                                    )
                                }
                            }

                            mRecipeCurveSuccessBean?.payload?.needTime =
                                entryList[entryList.size - 1].x.toInt().toString()

                            mRecipeCurveDataAdapter.time= mRecipeCurveSuccessBean?.payload?.needTime
                            var dm =
                                DynamicLineChartManager(cook_chart11, this@RecipeSuccessActivity)
                            dm.initLineDataSet(
                                "烹饪曲线",
                                resources.getColor(R.color.line_chart_easy),
                                entryList,
                                true
                            )
                            cook_chart11.zoom(3f,0f,0f,0f)
                            //有传值
                            if (intent.hasExtra(APPOINTLIST)) {
                                appointList.clear()
                                appointList =
                                    intent.getSerializableExtra(APPOINTLIST) as ArrayList<Entry>
                            } else {
                                if (mRecipeCurveSuccessBean?.payload?.stepList != null) {
                                    for (item in mRecipeCurveSuccessBean?.payload?.stepList!!) {
                                        //                        searchRecipeByWord(searchRecipeKeyWord);
                                        item.markTime?.toFloat()?.let { time ->
                                            appointList.add(Entry(time, 1f))
                                        }
                                    }
                                }
                                appointList.add(0, Entry(0f, 2f))
                                appointList.add(Entry(entryList[entryList.size - 1].x, 1f))
                            }
                            dm.lineDataAppointSet(appointList)
                            dm.setChartAttribute(false, false);
                            cook_chart11.notifyDataSetChanged()
                            //根据 mRecipeCurveSuccessBean.payload.curveSettingParams  中的  modeName 设置模式展示  act_cooked_mode_1 act_cooked_mode_2 act_cooked_mode_3
                            val type = object : TypeToken<List<DeviceParam?>?>() {}.type
                            if (mRecipeCurveSuccessBean?.payload?.curveSettingParams != null) {
                                val funtionBeans: List<DeviceParam> =
                                    Gson().fromJson(
                                        mRecipeCurveSuccessBean?.payload?.curveSettingParams,
                                        type
                                    )
                                var list:ArrayList<String> = ArrayList()
                                list.add("第一段")
                                list.add("第二段")
                                list.add("第三段")
                                if (funtionBeans.isNotEmpty() && model == CURVE) {
                                    rl_mutil_status.setTitleAndContent(list, funtionBeans)
                                }
//                                if (funtionBeans.isNotEmpty() && model == CURVE) {
//                                    when (funtionBeans.size) {
//                                        1 -> {
//                                            act_cooked_mode_1.visibility = View.VISIBLE
//                                            act_cooked_mode_1.text = funtionBeans[0].modeName
//                                            act_cooked_mode_1_time.visibility = View.VISIBLE
//                                            act_cooked_mode_1_time.text =
//                                                getSteam(funtionBeans[0]) + getTemp(funtionBeans[0]) + getTime(
//                                                    Integer.parseInt(funtionBeans[0].setTime)
//                                                )
//                                        }
//                                        2 -> {
//                                            act_cooked_mode_1.visibility = View.VISIBLE
//                                            act_cooked_mode_1.text = funtionBeans[0].modeName
//                                            act_cooked_mode_1_time.visibility = View.VISIBLE
//                                            act_cooked_mode_1_time.text =
//                                                getSteam(funtionBeans[0]) + getTemp(funtionBeans[0]) + getTime(
//                                                    Integer.parseInt(funtionBeans[0].setTime)
//                                                )
//                                            act_cooked_mode_2.visibility = View.VISIBLE
//                                            act_cooked_mode_2.text = funtionBeans[1].modeName
//                                            act_cooked_mode_2_time.visibility = View.VISIBLE
//                                            act_cooked_mode_2_time.text =
//                                                getSteam(funtionBeans[1]) + getTemp(funtionBeans[1]) + getTime(
//                                                    Integer.parseInt(funtionBeans[1].setTime)
//                                                )
//                                        }
//                                        3 -> {
//                                            act_cooked_mode_1.visibility = View.VISIBLE
//                                            act_cooked_mode_1.text = funtionBeans[0].modeName
//                                            act_cooked_mode_1_time.visibility = View.VISIBLE
//                                            act_cooked_mode_1_time.text =
//                                                getSteam(funtionBeans[0]) + getTemp(funtionBeans[0]) + getTime(
//                                                    Integer.parseInt(funtionBeans[0].setTime)
//                                                )
//                                            act_cooked_mode_2.visibility = View.VISIBLE
//                                            act_cooked_mode_2.text = funtionBeans[1].modeName
//                                            act_cooked_mode_2_time.visibility = View.VISIBLE
//                                            act_cooked_mode_2_time.text =
//                                                getSteam(funtionBeans[1]) + getTemp(funtionBeans[1]) + getTime(
//                                                    Integer.parseInt(funtionBeans[1].setTime)
//                                                )
//                                            act_cooked_mode_3.visibility = View.VISIBLE
//                                            act_cooked_mode_3.text = funtionBeans[2].modeName
//                                            act_cooked_mode_3_time.visibility = View.VISIBLE
//                                            act_cooked_mode_3_time.text =
//                                                getSteam(funtionBeans[2]) + getTemp(funtionBeans[2]) + getTime(
//                                                    Integer.parseInt(funtionBeans[2].setTime)
//                                                )
//                                        }
//                                        else -> {
//                                        }
//                                    }
//                                }
                            }
                            if (model == CURVE) {
//                                rl_add_step.visibility = View.VISIBLE
                                Log.e("宽度", cook_chart11.width.toString() + "---")
                                dm.setChartZ(
                                    entryList[entryList.size - 1].x.toInt(),
                                    cook_chart11.width
                                )
                                dm.setPointText(operationList)
                            }
                            var x_active = 0
                            var y_active = 0
                            cook_chart11.onChartGestureListener = object : OnChartGestureListener {
                                override fun onChartGestureStart(
                                    me: MotionEvent,
                                    lastPerformedGesture: ChartGesture
                                ) {
                                }

                                override fun onChartGestureMove(
                                    me: MotionEvent,
                                    lastPerformedGesture: ChartGesture
                                ) {
                                }

                                override fun onChartGestureEnd(
                                    me: MotionEvent,
                                    lastPerformedGesture: ChartGesture
                                ) {

                                    val transformer: Transformer =
                                        cook_chart11.getRendererXAxis().getTransformer()
                                    x_active = (transformer.getValuesByTouchPoint(
                                        cook_chart11.getCenter().getX(),
                                        cook_chart11.getCenter().getY()
                                    ).x + 0.5f).toInt()
//                                    y_active = (transformer.getValuesByTouchPoint(
//                                        cook_chart11.getCenter().getX(), cook_chart11.getCenter().getY()
//                                    ).y+0.5f).toInt()
                                    for (i in entryList.indices) {
                                        if (entryList[i].x.toInt() >= x_active) {
                                            y_active = entryList[i].y.toInt()
                                            break
                                        }
                                    }
                                    var timeStr = ""
                                    timeStr = if (x_active <= 0) {
                                        "0S"
                                    } else if (x_active > 0 && x_active < 60) {
                                        x_active.toString() + "S"
                                    } else {
                                        (x_active.toInt() / 60).toString() + "min" + x_active.toInt() % 60 + "S"
                                    }
                                    tv_add_step_time.text = timeStr
                                }

                                override fun onChartLongPressed(me: MotionEvent) {}
                                override fun onChartDoubleTapped(me: MotionEvent) {}
                                override fun onChartSingleTapped(me: MotionEvent) {}
                                override fun onChartFling(
                                    me1: MotionEvent,
                                    me2: MotionEvent,
                                    velocityX: Float,
                                    velocityY: Float
                                ) {
                                }

                                override fun onChartScale(
                                    me: MotionEvent,
                                    scaleX: Float,
                                    scaleY: Float
                                ) {
                                }

                                override fun onChartTranslate(
                                    me: MotionEvent,
                                    dX: Float,
                                    dY: Float
                                ) {
                                }
                            }
                            img_add_step.setOnClickListener {
                                var bean = RecipeCurveSuccessBean.StepList()
                                if (x_active < 0 || x_active > entryList[entryList.size - 1].x) {
                                    return@setOnClickListener
                                }
                                bean.markTemp = y_active.toString()
                                bean.markTime = x_active.toString()
                                bean.markName = "标记打点"
                                mRecipeCurveDataAdapter.data.add(bean)
                                appointList.add(Entry(x_active.toFloat(), 1f))

                                Collections.sort(
                                    mRecipeCurveDataAdapter.data
                                ) { o1: RecipeCurveSuccessBean.StepList, o2: RecipeCurveSuccessBean.StepList ->
                                    if (o1.markTime!!.toDouble() > o2.markTime!!.toDouble()) {
                                        return@sort 1
                                    }
                                    if (o1.markTime!!.toDouble() < o2.markTime!!.toDouble()) {
                                        return@sort -1
                                    }
                                    0
                                }



                                Collections.sort(
                                    appointList
                                ) { o1: Entry, o2: Entry ->
                                    if (o1.x > o2.x) {
                                        return@sort 1
                                    }
                                    if (o1.x < o2.x) {
                                        return@sort -1
                                    }
                                    0
                                }



                                Collections.sort(
                                    mRecipeCurveDataAdapter.data
                                ) { o1: RecipeCurveSuccessBean.StepList, o2: RecipeCurveSuccessBean.StepList ->
                                    if (o1.markTime!!.toDouble() > o2.markTime!!.toDouble()) {
                                        return@sort 1
                                    }
                                    if (o1.markTime!!.toDouble() < o2.markTime!!.toDouble()) {
                                        return@sort -1
                                    }
                                    0
                                }
                                operationList.add(bean)
                                mRecipeCurveDataAdapter.notifyDataSetChanged()
                                dm.lineDataAppointSet(appointList)
                            }
                        }
                    }
                }


                startWork()
            }
        } else if (requestId == R.id.recipe_save_success) {
            finish()

        } else if (requestId == R.id.page_btn_cooked_save) {

            showDialog()
            ThreadPoolManager.getInstance().execute {
//                mRecipeCurveSuccessBean?.let {


                mRecipeCurveDataAdapter.data.forEachIndexed { _, stepList ->

                    Log.e(
                        "路劲",
                        stepList.videoUrl + "---" + stepList.imageUrl + "----" + stepList.voiceUrl
                    )

                    if (stepList.videoUrl?.contains1("http") != true && stepList.videoUrl?.isNotEmpty1() == true) {
                        var url = AliyunOSSManager.getInstance()
                            .uploadFile(
                                AliyunOSSManager.getOSSVideoName(
                                    "curve"
                                ), stepList.videoUrl
                            )
                        stepList.videoUrl = url
                    } else if (stepList.voiceUrl?.contains1("http") != true && stepList.voiceUrl?.isNotEmpty1() == true) {
                        var url = AliyunOSSManager.getInstance()
                            .uploadFile(
                                AliyunOSSManager.getOSSVoiceName(
                                    "curve", System.currentTimeMillis().toString()
                                ), stepList.voiceUrl
                            )
                        stepList.voiceUrl = url

                    }

                    if (stepList.imageUrl?.contains1("http") != true) {
                        if (stepList.imageUrl?.isNotEmpty1() == true) {
                            var url = AliyunOSSManager.getInstance()
                                .uploadFile(
                                    AliyunOSSManager.getOSSImgName(
                                        "curve", System.currentTimeMillis().toString()
                                    ), stepList.imageUrl
                                )
                            stepList.imageUrl = url
                        }
                    }
                    Log.e(
                        "路劲1",
                        stepList.videoUrl + "---" + stepList.imageUrl + "----" + stepList.voiceUrl
                    )
                }

//                }


                mRecipeCurveSuccessBean?.let {
                    mRecipeSuccessApi.submitCookingStepCurve(
                        R.id.auto_work_layout,
                        it
                    )
                }
            }
        } else if (requestId == R.id.auto_work_layout) {
            Toast.makeText(this, "保存成功", Toast.LENGTH_LONG).show()

            if (model != CURVE)
                startActivity(Intent(this, CookingCurveListActivity::class.java))
            else
                startActivity(Intent(this, SaveCurveSuccessActivity::class.java))

            ld.close()
            if (isFinishAct) {
                setResult(RESULT_OK)
            }
            finish()

        } else if (requestId == R.id.page_cooked_data_list) {
            Log.e(TAG, "page_cooked_data_list" + "成功")
        } else if (requestId == R.id.device_polling_task_id) {
            Log.e(TAG, "page_cooked_data_list" + "暂停")
        } else if (requestId == R.id.ssdk_sina_web_title_id) {
            Log.e(TAG, "page_cooked_data_list" + "继续")
        }
    }


}


fun bitmap2Path(bitmap: Bitmap, path: String): String {
    try {
        var os = FileOutputStream(path);
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
        os.flush();
        os.close();
    } catch (e: Exception) {
        Log.e("TAG", "", e);
    }
    return path;
}