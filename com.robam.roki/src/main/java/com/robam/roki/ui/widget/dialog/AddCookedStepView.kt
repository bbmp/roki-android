package com.robam.roki.ui.widget.dialog

import android.content.Context
import android.content.Intent
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import com.blankj.utilcode.util.ThreadUtils.runOnUiThread
import com.bumptech.glide.Glide
import com.robam.roki.R
import com.robam.roki.utils.IChoosePicture
import com.robam.roki.utils.IResultPicture
import com.robam.roki.utils.ImagePickerUtil
import kotlinx.android.synthetic.main.widget_add_cooked_step_view.view.*

interface IAddCookedStep{
    fun setChoosePickInterface(mIResultPicture:IChoosePicture)

}

class AddCookedStepView: RelativeLayout, IAddCookedStep, IResultPicture {





    private lateinit var mContext:Context

    constructor(context:Context,fragment:Fragment):super(context){
        init(context,fragment)
    }


    private var isAudio=false;


    private fun init(context: Context,fragment:Fragment) {

        this.mContext=context
        LayoutInflater.from(context).inflate(R.layout.widget_add_cooked_step_view,this,true);
        widget_add_cooked_view_picture_view.setOnClickListener {
            mIChoosePicture.choosePicture(fragment,this)
        }

        add_cooked_step_view_delete.setOnClickListener {
            removeView(this)
        }


        widget_add_cooked_change_view.setOnClickListener {


            if (isAudio){
                widget_add_cooked_change_view.visibility= View.GONE
            }else{
//                widget_add_cooked_view_picture_audio.visibility= View.GONE

            }
            isAudio=!isAudio



        }


    }

    lateinit var mIChoosePicture:IChoosePicture

    override fun onFilePath(path: String?) {

        Glide.with(mContext).load(path).into(widget_add_cooked_view_picture_view)
    }

    override fun setChoosePickInterface(mIChoosePicture: IChoosePicture) {
        this.mIChoosePicture=mIChoosePicture
    }
}