package com.robam.roki.ui.adapter

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Bitmap
import android.os.*
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

import com.robam.roki.R
import com.robam.roki.request.bean.RecipeCurveSuccessBean
import com.robam.roki.utils.PermissionsUtils
import com.robam.roki.utils.PickImageHelperTwo
import kotlinx.android.synthetic.main.widget_add_cooked_step_view.view.*
import com.robam.roki.utils.audio.IOnTouchListener


object IntToSmallChineseNumber {
    fun ToCH(intInput: Int): String {
        val si = intInput.toString()
        var sd = ""
        if (si.length == 1) // 個
        {
            sd += GetCH(intInput)
            return sd
        } else if (si.length == 2) // 十
        {
            sd += if (si.substring(0, 1) == "1") "十" else GetCH(intInput / 10) + "十"
            sd += ToCH(intInput % 10)
        } else if (si.length == 3) // 百
        {
            sd += GetCH(intInput / 100) + "百"
            if ((intInput % 100).toString().length < 2) sd += "零"
            sd += ToCH(intInput % 100)
        } else if (si.length == 4) // 千
        {
            sd += GetCH(intInput / 1000) + "千"
            if ((intInput % 1000).toString().length < 3) sd += "零"
            sd += ToCH(intInput % 1000)
        } else if (si.length == 5) // 萬
        {
            sd += GetCH(intInput / 10000) + "万"
            if ((intInput % 10000).toString().length < 4) sd += "零"
            sd += ToCH(intInput % 10000)
        }
        return sd
    }

    private fun GetCH(input: Int): String {
        var sd = ""
        when (input) {
            1 -> sd = "一"
            2 -> sd = "二"
            3 -> sd = "三"
            4 -> sd = "四"
            5 -> sd = "五"
            6 -> sd = "六"
            7 -> sd = "七"
            8 -> sd = "八"
            9 -> sd = "九"
            else -> {
            }
        }
        return sd
    }
}

class DishedPrepareStepViewHolder(view: View) : RecyclerView.ViewHolder(view)

interface IChooseImage {
    fun uploadFile(path: String, pos: Int)
    fun choose(pos: Int, bitmao: Bitmap)

}

class DishedPrepareStepAdapter(
    var mActivity: Activity, var mIChooseImage: IChooseImage,var mIOnTouchListener:IOnTouchListener
) : BaseQuickAdapter<RecipeCurveSuccessBean.PrepareStepList, BaseViewHolder>(R.layout.widget_add_cooked_step_view) {
    lateinit var pickHelper: PickImageHelperTwo
    private fun choosePicture() {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mActivity.let {
                val selfPermission =
                    ContextCompat.checkSelfPermission(it, Manifest.permission.CAMERA)
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
    override fun convert(
        holder: BaseViewHolder,
        item: RecipeCurveSuccessBean.PrepareStepList,
        payloads: List<Any>
    ) {
        Log.e(TAG,"-----"+item.status+" "+holder.layoutPosition)
            if (payloads != null) {
                with(holder.itemView) {
                when (item.status) {
                    1 -> {
                        Glide.with(context).asGif().load(R.mipmap.audio_recorde_gif)
                            .into(widget_add_cooked_view_recorde)
                        widget_add_cooked_view_recorde.visibility = View.VISIBLE
                        widget_add_cooked_view_step_audio_icon_delete
                        widget_add_cooked_view_step_audio_time.text=  getTimeFormat(item.time / 1000)
                        widget_add_cooked_view_play_audio.visibility=View.GONE
                        widget_add_cooked_view_record_audio.visibility=View.VISIBLE
                        widget_add_cooked_view_picture_et.visibility=View.GONE
                    }
                    2 -> {
                        widget_add_cooked_view_picture_et.visibility=View.GONE
                        widget_add_cooked_view_play_audio.visibility=View.VISIBLE
                        widget_add_cooked_view_record_audio.visibility=View.GONE
                        widget_add_cooked_view_play_audio.setOnClickListener {
                            mIOnTouchListener.onClick(it, holder.layoutPosition)
                        }
                    }
                    3 -> {
                        widget_add_cooked_view_step_audio_icon_delete_play_audio.visibility=View.VISIBLE
                        widget_add_cooked_view_play_audio.visibility=View.VISIBLE
                        widget_add_cooked_view_record_audio.visibility=View.GONE
                        widget_add_cooked_view_step_audio_time_play_audio.text=getTimeFormat(item.playTime)
                        widget_add_cooked_view_play_audio.setOnClickListener {
                            mIOnTouchListener.onClick(it, holder.layoutPosition)
                        }
                    }
                    4 -> {
                        item.voiceUrl=null
                        item.playTime=0
                        item.time=0
                        widget_add_cooked_view_record_audio.visibility=View.VISIBLE
                        widget_add_cooked_view_play_audio.visibility=View.GONE
                        widget_add_cooked_view_step_audio_time.text="按住输入语音"
                        widget_add_cooked_view_recorde.visibility = View.GONE
                        widget_add_cooked_view_picture_et.visibility=View.GONE
                        widget_add_cooked_view_record_audio.setOnTouchListener { v, event ->
                            return@setOnTouchListener  mIOnTouchListener.onTouch(holder.layoutPosition,v,event)
                        }

                    }
                }
                }
            } else {
                super.convert(holder, item, payloads)
            }

//        super.convert(holder, item, payloads)
    }







//    private fun stopRecord() {
//        mRecorder.stop()
//    }



    override fun convert(holder: BaseViewHolder, item: RecipeCurveSuccessBean.PrepareStepList) {
        with(holder.itemView) {

            if (!TextUtils.isEmpty(item.imageUrl)) {
               Log.e("图片url", item.imageUrl + "----")
                Glide.with(mActivity).load(item.imageUrl)
                    .into(widget_add_cooked_view_picture_view)
                widget_add_cooked_view_picture_view_delete.visibility = View.VISIBLE
                widget_add_cooked_view_picture_view_icon.visibility = android.view.View.INVISIBLE

            }
            if (widget_add_cooked_view_picture_et.tag!=null&&widget_add_cooked_view_picture_et.tag is TextWatcher){
                widget_add_cooked_view_picture_et.addTextChangedListener(widget_add_cooked_view_picture_et.tag as TextWatcher)
            }else {
                widget_add_cooked_view_picture_et.setText(item.description)
                var watcher=object : TextWatcher {
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
                        item.description = s.toString()
                    }

                }
                widget_add_cooked_view_picture_et.addTextChangedListener(watcher)
            }


            if (!TextUtils.isEmpty(item.description)) {
                widget_add_cooked_view_record_audio.visibility = View.GONE
                widget_add_cooked_view_picture_et.visibility = View.VISIBLE
            }
            widget_add_cooked_view_picture_view_delete.setOnClickListener {
                widget_add_cooked_view_picture_view_delete.visibility = View.GONE
                Glide.with(mActivity).load(R.drawable.shape_image_bg)
                    .into(widget_add_cooked_view_picture_view)
                widget_add_cooked_view_picture_view_icon.visibility = View.VISIBLE
                item.imageUrl = null
            }
            widget_add_cooked_view_picture_view.setOnClickListener {
                pickHelper = PickImageHelperTwo(
                    mActivity, object : PickImageHelperTwo.PickCallbackTwo {
                        override fun onPickComplete(bmp: Bitmap?) {
                            bmp?.let { mIChooseImage.choose(holder.absoluteAdapterPosition, it) }
                        }
                        override fun onPickComplete(bmp: String?) {}
                    }
                )
                choosePicture()
            }
            widget_add_cooked_view_step.text = "步骤" + IntToSmallChineseNumber.ToCH(holder.absoluteAdapterPosition + 1)

            widget_add_cooked_change_view.setOnClickListener {
                if (widget_add_cooked_view_picture_et.visibility == View.GONE) {
                    widget_add_cooked_view_picture_et.visibility = View.VISIBLE
                    widget_add_cooked_view_record_audio.visibility = View.GONE
                    widget_add_cooked_view_play_audio.visibility= View.GONE
                    widget_add_cooked_change_view.setImageResource(com.robam.roki.R.mipmap.text_input_icon)
                } else {
                    widget_add_cooked_view_picture_et.visibility = View.GONE
                    if (item.voiceUrl?.isNotEmpty() == true) {
                        widget_add_cooked_view_step_audio_time.text =
                            context.getString(com.robam.roki.R.string.widget_click_player)
                        widget_add_cooked_view_play_audio.setOnClickListener {
                            mIOnTouchListener.onClick(it, holder.absoluteAdapterPosition)
                        }
                        widget_add_cooked_view_play_audio.visibility = View.VISIBLE
                        widget_add_cooked_view_record_audio.visibility = View.GONE
                    } else {
                        widget_add_cooked_view_step_audio_time.text =
                            context.getString(R.string.widget_press_input_audio)
                        widget_add_cooked_view_record_audio.setOnTouchListener { view, event ->
                            return@setOnTouchListener mIOnTouchListener.onTouch(
                                holder.absoluteAdapterPosition,
                                view,
                                event
                            )
                        }
                        widget_add_cooked_view_picture_et.visibility = View.GONE
                        widget_add_cooked_view_play_audio.visibility = View.GONE
                        widget_add_cooked_view_record_audio.visibility = View.VISIBLE
                        widget_add_cooked_change_view.setImageResource(com.robam.roki.R.mipmap.icon_audio_input)

                    }
                }


                }

            if (!TextUtils.isEmpty(item.voiceUrl)) {
                widget_add_cooked_view_record_audio.visibility= View.GONE
                widget_add_cooked_view_play_audio.visibility= View.VISIBLE
                widget_add_cooked_view_play_audio.setOnClickListener {
                    mIOnTouchListener.onClick(it,holder.absoluteAdapterPosition)
                }

            }else {
                widget_add_cooked_view_play_audio.visibility= View.GONE
                widget_add_cooked_view_record_audio.visibility= View.VISIBLE
                widget_add_cooked_view_record_audio.setOnTouchListener { v, event ->
                    return@setOnTouchListener  mIOnTouchListener.onTouch(holder.absoluteAdapterPosition,v,event)
                }
            }

            widget_add_cooked_view_step_audio_icon_delete_play_audio.setOnClickListener {
                mIOnTouchListener.onClick(it,holder.absoluteAdapterPosition)
            }


        }
    }
}


fun getTimeFormat(sec: Int): String {
    Log.e("时间格式", "秒$sec")
    return (sec / 60).toString() + ":" + (sec % 60)
}