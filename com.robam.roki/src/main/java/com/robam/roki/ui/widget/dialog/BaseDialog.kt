package com.robam.roki.ui.widget.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import com.robam.roki.R
import kotlinx.android.synthetic.main.view_connect_scan.*

abstract class BaseDialog(context:Context): Dialog(context,R.style.MyDialog){



    abstract fun getLayoutId():Int

    abstract fun initView()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        setCancelable(false)
//        =context.resources.getDrawable(R.drawable.shape_dialog_bg)
        window?.setGravity (Gravity.CENTER or Gravity.BOTTOM)
        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        initView()
    }
}