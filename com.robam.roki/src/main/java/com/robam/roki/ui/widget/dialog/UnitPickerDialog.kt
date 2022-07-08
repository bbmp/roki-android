package com.robam.roki.ui.widget.dialog

import android.content.Context
import android.util.Log
import com.robam.roki.R
import com.robam.roki.request.bean.Units
import kotlinx.android.synthetic.main.extra_time_dialog.*
import kotlinx.android.synthetic.main.extra_time_dialog.dialog_extra_time_list
import kotlinx.android.synthetic.main.unit_picker_dialog.*
import java.util.ArrayList
interface UnitCheck{
    fun choose(pos:Int)
}
class UnitPickerDialog(mContext:Context,var onResult:UnitCheck,var data:List<Units>?):BaseDialog(mContext) {
    override fun getLayoutId(): Int=R.layout.unit_picker_dialog

    override fun initView() {

        initUnit()

    }

    var pos=-1
    private  val TAG = "UnitPickerDialog"
    private fun initUnit() {
        val list: MutableList<String> = ArrayList()
        unit_picker_dialog_wheel_btn_confirm.setOnClickListener {
            if (pos!=-1)
            onResult.choose(pos)
            dismiss()
        }
        unit_picker_dialog_wheel_btn_cancel.setOnClickListener {
            dismiss()
        }

        if (data==null||data?.isEmpty() == true){
            return
        }

        Log.e(TAG,data?.toString())
        data?.let {  dataList->
            for (datum in dataList) {
                datum?.name?.let { list.add(it) }
            }
        }

        unit_picker_dialog_wheel_view.adapter=ArrayWheelAdapter(list)
        unit_picker_dialog_wheel_view.setDividerColor(0xEFCE17)
        unit_picker_dialog_wheel_view.setOnItemSelectedListener {
          pos=it

        }

    }
}