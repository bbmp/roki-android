package com.robam.roki.utils.audio

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.RelativeLayout
import com.bumptech.glide.Glide
import com.robam.common.RobamApp
import com.robam.roki.R
import com.robam.roki.manage.AliyunOSSManager
import com.robam.roki.manage.ThreadPoolManager
import com.robam.roki.request.bean.RecipeCurveSuccessBean
import com.robam.roki.ui.adapter.DialogAdapter.mList
import com.robam.roki.ui.adapter.getTimeFormat
import com.robam.roki.utils.audio.player.PlayerCallback
import com.robam.roki.utils.audio.player.PlayerState
import com.robam.roki.utils.audio.player.RokiMediaPlayer
import com.robam.roki.utils.audio.recorder.AudioPCMRecorder
import com.xiaomi.push.it
import com.zlw.main.recorderlib.RecordManager
import com.zlw.main.recorderlib.recorder.RecordHelper
import com.zlw.main.recorderlib.recorder.listener.RecordStateListener
import kotlinx.android.synthetic.main.record_audio_view.*
import kotlinx.android.synthetic.main.record_audio_view.view.*
import kotlinx.android.synthetic.main.widget_add_cooked_step_view.view.*
import java.util.*


interface IRecordAudioText{

    fun setRecordDate(mCurveStepDoList: RecipeCurveSuccessBean.StepList,pos:Int,boolean: Boolean,mIOnTouchListener:IOnTouchListener);

}

interface IOnTouchListener{
    fun onTouch(pos:Int,v:View , event:MotionEvent,isPress: Boolean=true):Boolean

    fun onClick(view: View,pos: Int)
}
class RecordAudioAndTextView:RelativeLayout,IRecordAudioText {




    constructor(context: Context):super(context){
        initView(context)
    }



    var isPress=true;
    var isAudioChange=false
    public fun setViewAI(){
        if (isAudioChange){

            Glide.with(context).load(R.mipmap.icon_audio_input)
                .into(record_model_chang_img)

            audio_input_rl.visibility=View.GONE
            rl_audio_recorded_finish.visibility=View.GONE
            record_audio_view_info_img.visibility=View.GONE
            press_record_audio_view_info.visibility=View.GONE
            text_input_et.visibility=View.VISIBLE
        }else{

            if (TextUtils.isEmpty( mCurveStepDoList.voiceUrl)) {
                audio_input_rl.visibility=View.VISIBLE
            }else{
                rl_audio_recorded_finish.visibility = View.VISIBLE
            }
            Glide.with(context).load(R.mipmap.text_input_icon)
                .into(record_model_chang_img)


            record_audio_view_info_img.visibility=View.VISIBLE
            press_record_audio_view_info.visibility=View.VISIBLE
            text_input_et.visibility=View.GONE
        }
        isAudioChange=!isAudioChange
    }


    @SuppressLint("ClickableViewAccessibility")
    private fun initView(context: Context) {

        this.mContext=context
        LayoutInflater.from(context).inflate(R.layout.record_audio_view,this,true)
    }

    fun deleteView(){
        if (TextUtils.isEmpty(mCurveStepDoList.voiceUrl)) {
            if (audio_input_rl.visibility == View.VISIBLE || rl_audio_recorded_finish.visibility == View.VISIBLE) {
                audio_input_rl.visibility = View.GONE
                text_input_et.visibility = View.VISIBLE
                rl_audio_recorded_finish.visibility = View.GONE
                record_audio_view_info_img.visibility = View.VISIBLE
                press_record_audio_view_info.visibility = View.VISIBLE
                Glide.with(context).load(R.mipmap.icon_audio_input)
                    .into(record_model_chang_img)
            } else {
                rl_audio_recorded_finish.visibility = View.GONE
                audio_input_rl.visibility = View.VISIBLE
                text_input_et.visibility = View.GONE
                press_record_audio_view_info.visibility = View.GONE
                record_audio_view_info_img.visibility = View.GONE
                Glide.with(context).load(R.mipmap.text_input_icon)
                    .into(record_model_chang_img)
            }
        } else {
            if (audio_input_rl.visibility == View.VISIBLE || rl_audio_recorded_finish.visibility == View.VISIBLE) {
                audio_input_rl.visibility = View.GONE
                text_input_et.visibility = View.VISIBLE
                rl_audio_recorded_finish.visibility = View.GONE
                record_audio_view_info_img.visibility = View.VISIBLE
                press_record_audio_view_info.visibility = View.VISIBLE
                Glide.with(context).load(R.mipmap.icon_audio_input)
                    .into(record_model_chang_img)
            } else {
                rl_audio_recorded_finish.visibility = View.VISIBLE
                audio_input_rl.visibility = View.GONE
                text_input_et.visibility = View.GONE
                press_record_audio_view_info.visibility = View.GONE
                record_audio_view_info_img.visibility = View.GONE
                Glide.with(context).load(R.mipmap.text_input_icon)
                    .into(record_model_chang_img)
            }
        }
    }


     lateinit var mContext:Context
    private  val TAG = "RecordAudioView"


    constructor(context:Context , attrs: AttributeSet):super(context, attrs, 0){
        initView(context)
    }
    constructor(context:Context , attrs: AttributeSet,defStyleAttr:Int):super(context, attrs, defStyleAttr){
        initView(context)
    }

    constructor(context:Context , attrs: AttributeSet,defStyleAttr:Int,defStyleRes:Int):super(context, attrs, defStyleAttr,defStyleRes){
        initView(context)
    }

     var timer:Timer?= Timer()

    lateinit var mCurveStepDoList: RecipeCurveSuccessBean.StepList


    var pos:Int = 0
    @SuppressLint("ClickableViewAccessibility")
    override fun setRecordDate(mCurveStepDoList: RecipeCurveSuccessBean.StepList, pos:Int, isEdit:Boolean,mIOnTouchListener:IOnTouchListener) {

        Log.e("位置", "$pos----")
        this.mCurveStepDoList=mCurveStepDoList

        if (isEdit){  // 只读模式
            Log.e(TAG,"结果"+"---"+mCurveStepDoList.description.toString())
            text_input_et.isEnabled=false
            if (!TextUtils.isEmpty(mCurveStepDoList.description)&&!TextUtils.isEmpty(mCurveStepDoList.voiceUrl)){
                record_model_chang_img.visibility=View.VISIBLE
                rl_audio_recorded_delete.visibility=View.GONE

                if (record_model_chang_img.tag!=null&&record_model_chang_img.tag is OnClickListener) {
                    record_model_chang_img.setOnClickListener(record_model_chang_img.tag as OnClickListener)
                }else{
                    var mClickChange= OnClickListener {
                        if (TextUtils.isEmpty(mCurveStepDoList.voiceUrl)) {
                            if (audio_input_rl.visibility == View.VISIBLE || rl_audio_recorded_finish.visibility == View.VISIBLE) {
                                audio_input_rl.visibility = View.GONE
                                text_input_et.visibility = View.VISIBLE
                                rl_audio_recorded_finish.visibility = View.GONE
                                record_audio_view_info_img.visibility = View.VISIBLE
                                press_record_audio_view_info.visibility = View.VISIBLE
                                Glide.with(context).load(R.mipmap.icon_audio_input)
                                    .into(record_model_chang_img)
                            } else {
                                rl_audio_recorded_finish.visibility = View.GONE
                                audio_input_rl.visibility = View.VISIBLE
                                text_input_et.visibility = View.GONE
                                press_record_audio_view_info.visibility = View.GONE
                                record_audio_view_info_img.visibility = View.GONE
                                Glide.with(context).load(R.mipmap.text_input_icon)
                                    .into(record_model_chang_img)
                            }
                        } else {
                            if (audio_input_rl.visibility == View.VISIBLE || rl_audio_recorded_finish.visibility == View.VISIBLE) {
                                audio_input_rl.visibility = View.GONE
                                text_input_et.visibility = View.VISIBLE
                                rl_audio_recorded_finish.visibility = View.GONE
                                record_audio_view_info_img.visibility = View.VISIBLE
                                press_record_audio_view_info.visibility = View.VISIBLE
                                Glide.with(context).load(R.mipmap.icon_audio_input)
                                    .into(record_model_chang_img)
                            } else {
                                rl_audio_recorded_finish.visibility = View.VISIBLE
                                audio_input_rl.visibility = View.GONE
                                text_input_et.visibility = View.GONE
                                press_record_audio_view_info.visibility = View.GONE
                                record_audio_view_info_img.visibility = View.GONE
                                Glide.with(context).load(R.mipmap.text_input_icon)
                                    .into(record_model_chang_img)
                            }
                        }
                    }
                    record_model_chang_img.setOnClickListener (mClickChange)
                    record_model_chang_img.tag=mClickChange
                }

                Log.e(TAG,"结果111"+"---"+mCurveStepDoList.description.toString())
                text_input_et.setText(mCurveStepDoList.description.toString())

            }else {
                record_model_chang_img.visibility=View.GONE
                if (!TextUtils.isEmpty(mCurveStepDoList.description)) {
                    text_input_et.isEnabled = false
                    text_input_et.visibility=View.VISIBLE
                    record_model_chang_img.visibility = View.GONE
                    text_input_et.setText(mCurveStepDoList.description.toString())
                }

                if (!TextUtils.isEmpty(mCurveStepDoList.voiceUrl)) {
                    audio_input_rl.visibility=View.GONE
                    rl_audio_recorded_delete.visibility=View.GONE
                    rl_audio_recorded_finish.visibility=View.VISIBLE
                    rl_audio_recorded_finish.setOnClickListener {
                        mIOnTouchListener.onClick(it, pos)
                    }
                    record_model_chang_img.visibility = View.GONE
                }
            }





        }else {
            if (!TextUtils.isEmpty(mCurveStepDoList.description)) {
                text_input_et.setText(mCurveStepDoList.description)
            } else {
                text_input_et.visibility = View.GONE
            }




            if (!TextUtils.isEmpty(mCurveStepDoList.voiceUrl)) {
                text_input_et.visibility = View.GONE
                audio_input_rl.visibility = View.VISIBLE
                rl_audio_recorded_finish.visibility = View.VISIBLE
                Glide.with(context).load(R.mipmap.text_input_icon)
                    .into(record_model_chang_img)
                isPress = false
                rl_audio_recorded_finish.setOnClickListener {
                    mIOnTouchListener.onClick(it, pos)
                }
            } else {
                text_input_et.visibility = View.VISIBLE
                audio_input_rl.visibility = View.GONE
                rl_audio_recorded_finish.visibility = View.GONE
            }
            record_model_chang_img.setOnClickListener {

                if (TextUtils.isEmpty(mCurveStepDoList.voiceUrl)) {
                    setViewAI()
                } else {
                    if (audio_input_rl.visibility == View.VISIBLE || rl_audio_recorded_finish.visibility == View.VISIBLE) {
                        audio_input_rl.visibility = View.GONE
                        text_input_et.visibility = View.VISIBLE
                        rl_audio_recorded_finish.visibility = View.GONE
                        record_audio_view_info_img.visibility = View.VISIBLE
                        press_record_audio_view_info.visibility = View.VISIBLE
                        Glide.with(context).load(R.mipmap.icon_audio_input)
                            .into(record_model_chang_img)
                    } else {
                        rl_audio_recorded_finish.visibility = View.VISIBLE
                        audio_input_rl.visibility = View.GONE
                        text_input_et.visibility = View.GONE
                        press_record_audio_view_info.visibility = View.GONE
                        record_audio_view_info_img.visibility = View.GONE
                        Glide.with(context).load(R.mipmap.text_input_icon)
                            .into(record_model_chang_img)
                    }
                }


            }

            if (rl_audio_recorded_delete.tag!=null&&rl_audio_recorded_delete.tag is OnClickListener){
                rl_audio_recorded_delete.setOnClickListener(rl_audio_recorded_delete.tag as OnClickListener)
            }else {
                var mCLick = OnClickListener { mIOnTouchListener.onClick(it, pos) }

                rl_audio_recorded_delete.tag = mCLick
                rl_audio_recorded_delete.setOnClickListener(mCLick)
            }


            if (text_input_et.tag != null && text_input_et.tag is TextWatcher) {
                text_input_et.setText(mCurveStepDoList.description)
            } else {
                text_input_et.setText(mCurveStepDoList.description)
                val textWatcher: TextWatcher = object : TextWatcher {
                    override fun beforeTextChanged(
                        s: CharSequence,
                        start: Int,
                        count: Int,
                        after: Int
                    ) {
                    }

                    override fun onTextChanged(
                        s: CharSequence,
                        start: Int,
                        before: Int,
                        count: Int
                    ) {
                    }

                    override fun afterTextChanged(s: Editable) {
                        mCurveStepDoList.description = s.toString()
                    }
                }
                text_input_et.addTextChangedListener(textWatcher)
                text_input_et.tag = textWatcher
            }


            rl_audio_recorded_finish.setOnClickListener {
                mIOnTouchListener.onClick(it, pos)
            }


            if (audio_input_rl.tag!=null&&audio_input_rl.tag is OnTouchListener){
                audio_input_rl.setOnTouchListener(audio_input_rl.tag as OnTouchListener)
            }else {
                var mTouch = OnTouchListener { v, event ->
                    Log.e("位置1",pos.toString()+"")
                    mIOnTouchListener.onTouch(pos, v, event, isPress) }
                audio_input_rl.tag = mTouch
                audio_input_rl.setOnTouchListener(mTouch)
                text_input_et.isEnabled = true;

            }

        }



    }
}