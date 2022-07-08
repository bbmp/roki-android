package com.robam.roki.ui.widget.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import com.contrarywind.listener.OnItemSelectedListener
import com.robam.roki.R
import kotlinx.android.synthetic.main.dialog_choose_time_and_temperature.*
import kotlinx.android.synthetic.main.extra_time_dialog.*
import java.util.ArrayList


interface IChooseTime{
    fun onTimeResult(time:Int)
}

class ExtraTimeDialog(context:Context, var mIChooseTime:IChooseTime) :BaseDialog(context) {


    var pos=-1
    override fun getLayoutId(): Int {
     return R.layout.extra_time_dialog
    }

    override fun initView() {
        dialog_extra_time_txt_cancel.setOnClickListener {
            dismiss()
        }


        dialog_extra_time_txt_confirm.setOnClickListener {

            if (pos==-1) {
                Toast.makeText(context,"请选择加时的时间",Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            mIChooseTime.onTimeResult(pos)
            dismiss()
        }
        initTime();
    }



    private fun initTime() {
        val list: MutableList<String> = ArrayList()
        for (i in 0..60) {
            val itemData = "$i min"
            list.add(itemData)
        }
        dialog_extra_time_list.adapter=ArrayWheelAdapter(list)
        dialog_extra_time_list.setDividerColor(0xEFCE17)
        dialog_extra_time_list.setOnItemSelectedListener {
            pos=it
        }
    }
}