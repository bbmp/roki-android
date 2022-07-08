package com.robam.roki.ui.adapter

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Bitmap
import android.os.Build
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.robam.roki.R
import com.robam.roki.request.bean.RecipeCurveSuccessBean
import com.robam.roki.utils.PermissionsUtils
import com.robam.roki.utils.PickImageHelperTwo
import com.robam.roki.utils.audio.SimpleAudioRecorder
import com.robam.roki.utils.audio.recorder.AudioPCMRecorder
import com.robam.roki.utils.audio.recorder.RecordState
import com.robam.roki.utils.audio.recorder.RecorderCallback
import kotlinx.android.synthetic.main.widget_add_cooked_step_view.view.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import com.robam.roki.utils.audio.player.AudioPCMPlayer
import com.robam.roki.utils.audio.player.PlayerCallback


class DishedPrepareStepAdapter1(var mDataList:ArrayList<RecipeCurveSuccessBean.PrepareStepList>,
                               var mActivity: Activity,var mIChooseImage:IChooseImage) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    private  var simpleAudioPlayer= AudioPCMPlayer()

    lateinit var pickHelper: PickImageHelperTwo

    private var mRecorder: SimpleAudioRecorder = AudioPCMRecorder()



    override fun onCreateViewHolder(viewGroup: ViewGroup, p1: Int): DishedPrepareStepViewHolder {
        return DishedPrepareStepViewHolder(
            LayoutInflater.from(viewGroup.context).inflate(R.layout.widget_add_cooked_step_view, viewGroup, false))
    }


    private fun choosePicture(){


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mActivity.let {
                val selfPermission = ContextCompat.checkSelfPermission(it, Manifest.permission.CAMERA)
                if (selfPermission == 0) {
                    pickHelper.showPickDialog("")
                } else {
                    PermissionsUtils.checkPermission(
                        it,
                        Manifest.permission.CAMERA,
                        PermissionsUtils.CODE_USER_INFO_SHARE
                    )
                }


            }

        } else {
            pickHelper.showPickDialog("")
        }
    }

    private  val TAG = "DishedPrepareStepAdapte"
    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(p0: RecyclerView.ViewHolder, @SuppressLint("RecyclerView") p1:Int) {



        with(p0.itemView){
            if (!TextUtils.isEmpty(mDataList[p1].imageUrl)){
                Log.e("图片url",mDataList[p1].imageUrl+ "----")
                mActivity?.let {
                    Glide.with(it).load(mDataList[p1].imageUrl).into(widget_add_cooked_view_picture_view)
                    widget_add_cooked_view_picture_view_delete.visibility=View.VISIBLE
                    widget_add_cooked_view_picture_view_icon.visibility=View.INVISIBLE
                }
            }
            widget_add_cooked_view_picture_et.setText(mDataList[p1].description)
            widget_add_cooked_view_picture_et.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {

                }

                override fun onTextChanged(
                    s: CharSequence?,
                    start: Int,
                    before: Int,
                    count: Int
                ) {

                }

                override fun afterTextChanged(s: Editable?) {
                    mDataList[p1].description = s.toString()
                }

            })

            widget_add_cooked_view_picture_view_delete.setOnClickListener {
                widget_add_cooked_view_picture_view_delete.visibility = View.GONE

                Glide.with(mActivity).load(R.drawable.shape_image_bg)
                    .into(widget_add_cooked_view_picture_view)
                widget_add_cooked_view_picture_view_icon.visibility = View.VISIBLE
            }

            widget_add_cooked_view_picture_view.setOnClickListener {
                pickHelper = PickImageHelperTwo(
                    mActivity, object : PickImageHelperTwo.PickCallbackTwo {
                        override fun onPickComplete(bmp: Bitmap?) {
                            bmp?.let { mIChooseImage.choose(p1, it) }

                        }

                        override fun onPickComplete(bmp: String?) {

                        }


                    }
                )
                choosePicture()
            }

            widget_add_cooked_view_step.text = "步骤" + IntToSmallChineseNumber.ToCH(p1 + 1)


            ///有录音文件的时候
            if(!TextUtils.isEmpty(mDataList[p1].voiceUrl)){
                Log.e(TAG,mDataList[p1].voiceUrl)
                widget_add_cooked_view_picture_et.visibility=View.GONE
                widget_add_cooked_view_record_audio.visibility=View.GONE
                widget_add_cooked_view_record_audio.visibility=View.VISIBLE
                widget_add_cooked_change_view.setImageResource(R.mipmap.text_input_icon)
                widget_add_cooked_change_view.setOnClickListener {
                    if (widget_add_cooked_view_picture_et.visibility==View.GONE){
                        widget_add_cooked_view_picture_et.visibility=View.VISIBLE
                        widget_add_cooked_view_record_audio.visibility=View.GONE
                    }else{
                        widget_add_cooked_view_picture_et.visibility=View.GONE
                        widget_add_cooked_view_record_audio.visibility=View.VISIBLE
                    }



                }

                widget_add_cooked_view_record_audio.setOnClickListener {
                    mDataList[p1].voiceUrl?.let { it -> playAudio(it,widget_add_cooked_view_step_audio_time) }
                }
            }else {


                widget_add_cooked_change_view.setOnClickListener {

                    if (widget_add_cooked_view_picture_et.visibility != View.GONE) {
                        widget_add_cooked_view_picture_et.visibility = View.GONE
//                        widget_add_cooked_view_recorde_audio.visibility = View.VISIBLE
                        widget_add_cooked_change_view.setImageResource(R.mipmap.text_input_icon)
                    } else {
                        widget_add_cooked_change_view.setImageResource(R.mipmap.icon_audio_input)
                        widget_add_cooked_view_picture_et.visibility = View.VISIBLE
//                        widget_add_cooked_view_recorde_audio.visibility = View.GONE
                    }
                }
                val countDownTimer: CountDownTimer = object : CountDownTimer(1000, 1000) {
                    override fun onTick(millisUntilFinished: Long) {

                    }

                    override fun onFinish() {
                        startRecord(
                            widget_add_cooked_view_picture_et,
                            widget_add_cooked_view_record_audio,
                            p1
                        )
                    }
                }

                setOnTouchListener { _, event ->
                    when (event?.action) {
                        MotionEvent.ACTION_UP -> {
                            countDownTimer.cancel()
                            Log.e(TAG, "11ACTION_UP")
                            if (mRecorder.state == RecordState.Recording) {
                                stopRecord()
                            }
                        }
                        MotionEvent.ACTION_MOVE->{

                            Log.e(TAG, "11ACTION_MOVE")
                        }
                        MotionEvent.ACTION_DOWN -> {
                        Log.e(TAG, "11ACTION_DOWN")
                        if (mRecorder.state != RecordState.Recording) {
                            countDownTimer.start()
                        }
                    }
                        else -> {

                        }
                    }
                    true
                }
            }
        }
    }



    private fun stopRecord() {
        mRecorder.stop()
    }


    private fun playAudio(path:String,mTextTime:TextView){
        simpleAudioPlayer.play("/data/user/0/com.robam.roki/files/20211204104336.pcm")
        simpleAudioPlayer.addCallback(object: PlayerCallback {
            override fun onPlay() {
                Log.e(TAG, "onPlay----")

//                simpleAudioPlayer.duration
            }

            override fun onProgress(progress: Int) {
                Log.e(TAG, "$progress----")
                mTextTime.text=getTimeFormat(progress)
            }

            override fun onPause() {

            }

            override fun onStop() {
                Log.e(TAG, "onStop----")
            }
        })

    }

    fun  getTimeFormat(sec:Int):String{

        return   (sec/60).toString()+":"+(sec%60)


    }

    private fun startRecord( mRecorderText:TextView,mRlPlayerView:RelativeLayout,p1:Int) {
        Log.e(TAG, "startRecord")


        val fileDirPath: String = mActivity.filesDir.absolutePath
        val filePath = fileDirPath + File.separator +
                SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA)
                    .format(System.currentTimeMillis()) + ".pcm"
        Log.e(TAG, filePath)
        mRecorder.record(filePath)
        mRecorder.addCallback(object : RecorderCallback {
            var sec=0;
            override fun onRecord() {
                Log.e(TAG, "onRecord")
            }

            override fun onProgress(sec: Int) {
                Log.e(TAG, "onProgress"+sec)
                mRecorderText.text =getTimeFormat(sec)
                this.sec=sec
                if (sec > 60) {
                    mRecorder.stop()
                }
            }

            override fun onPause() {
                Log.e(TAG, "onPause")
            }

            override fun onStop() {

                Log.e(TAG, "onStop")
                Log.e(TAG,filePath+"----------")
                mIChooseImage.uploadFile(filePath,p1)




            }
        })
    }

    override fun getItemCount(): Int=mDataList.size


}