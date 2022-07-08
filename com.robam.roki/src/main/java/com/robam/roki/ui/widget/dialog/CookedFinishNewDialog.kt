package com.robam.roki.ui.widget.dialog

import android.app.Dialog
import android.content.Context
import android.view.View
import com.robam.roki.R
import kotlinx.android.synthetic.main.common_dialog_layout_finish_work.*

class CookedFinishNewDialog(context: Context,var click:View.OnClickListener) :BaseDialog(context) {
    override fun getLayoutId(): Int= R.layout.common_dialog_layout_finish_work

    override fun initView() {


        btn_finish.setOnClickListener (click)


        btn_cancel.setOnClickListener {
            dismiss()
        }

    }
}