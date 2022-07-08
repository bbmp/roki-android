package com.robam.roki.ui.widget.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.robam.roki.R
import com.robam.roki.ui.widget.pickrecycleview.adapter.ScrollPickerAdapter
import kotlinx.android.synthetic.main.dialog_choose_time_and_temperature.*
import java.util.ArrayList
interface ITimeAndTemperature{
    fun onData(righetData:Int,leftData:Int)
}
class DialogChooseTimeAndTemperature(context: Context,var type:Int,var  mITimeAndTemperature:ITimeAndTemperature) : BaseDialog(context) {




    private var left=100;
    private var right=100;
    val mRightList: MutableList<String> = ArrayList()
    val mLeftList: MutableList<String> = ArrayList()


    override fun getLayoutId(): Int {
       return R.layout.dialog_choose_time_and_temperature
    }

    override fun initView() {
        if (type==1){

            for (i in 0..200) {
                val itemData = "$i ℃"
                mRightList.add(itemData)
            }


            for (i in 0..60) {
                val itemData = "$i min"
                mLeftList.add(itemData)
            }

        }else if (type==2){

            for (i in 0..60) {
                val itemData = "$i min"
                mRightList.add(itemData)
            }


            for (i in 0..60) {
                val itemData = "$i s"
                mLeftList.add(itemData)
            }

        }




        dialog_choose_time_min.adapter=ArrayWheelAdapter(mRightList)
        dialog_choose_time_min.setOnItemSelectedListener {
           this.right=it


        }
        dialog_choose_time_sec.adapter=ArrayWheelAdapter(mLeftList)
        dialog_choose_time_sec.setOnItemSelectedListener {
            this.left=it
        }
        dialog_choose_time_save.setOnClickListener {
            mITimeAndTemperature.onData(right,left)
            dismiss()
        }
        dialog_choose_time_cancel.setOnClickListener {
            dismiss()
        }
    }


}