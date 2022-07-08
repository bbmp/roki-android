package com.robam.roki.ui.widget.dialog

import android.content.Context
import android.graphics.Color
import android.util.Log
import com.robam.roki.R
import com.robam.roki.listener.OnItemSelectedListenerPosition
import com.robam.roki.model.helper.HelperRikaData
import com.robam.roki.ui.page.device.steamovenone.steamovenone620.MicroWave620Page
import com.xiaomi.push.it
import kotlinx.android.synthetic.main.picker_time_dialog.*

interface IPickerTime{
    fun onData(min:Int,sex:Int)
}
class PickerTimeDialog(var time:String,context:Context,var mIPickerTime: IPickerTime): BaseDialog(context) {


    val mMinList: MutableList<String> = ArrayList()
    val mSecondList: MutableList<String> = ArrayList()
    override fun getLayoutId(): Int=R.layout.picker_time_dialog


    var min=51
    var sec=31
    override fun initView() {



        var minTemp=time.toInt()/60;
        var secTemp=time.toInt()%60;


        for (ins in 0..minTemp){
            mMinList.add("$ins"+"min")
        }

        for (ins in 0..secTemp){
            mSecondList.add("$ins"+"s")
        }
        picker_time_dialog_wheel_view_min.setItems(mMinList)
        picker_time_dialog_wheel_view_min.setInitPosition(minTemp)
//        picker_time_dialog_wheel_view_min.setCurrentPosition(minTemp)


        picker_time_dialog_wheel_view_min.setDividerColor(Color.parseColor("#e1e1e1"))
        picker_time_dialog_wheel_view_min.setListenerPosition(OnItemSelectedListenerPosition { position1: Int ->
            min= position1
        })

        picker_time_dialog_wheel_view_second.setItems(mSecondList)
       picker_time_dialog_wheel_view_second.setInitPosition(secTemp)
//         picker_time_dialog_wheel_view_second.setCurrentPosition(secTemp)

        picker_time_dialog_wheel_view_second.setDividerColor(Color.parseColor("#e1e1e1"))
        picker_time_dialog_wheel_view_second.setListenerPosition(OnItemSelectedListenerPosition { position1: Int ->
            sec= position1
        })


//        picker_time_dialog_wheel_view_second.adapter=ArrayWheelAdapter(mSecondList)
//        picker_time_dialog_wheel_view_second.setOnItemSelectedListener {
//            sec=it
//        }

        picker_time_dialog_wheel_view_save.setOnClickListener {
            mIPickerTime.onData(min,sec)
            dismiss()
        }


        picker_time_dialog_wheel_view_cancel.setOnClickListener {

            dismiss()
        }
    }
}