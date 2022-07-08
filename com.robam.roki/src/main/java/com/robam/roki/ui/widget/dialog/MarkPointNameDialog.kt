package com.robam.roki.ui.widget.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.robam.roki.R
import kotlinx.android.synthetic.main.dialog_make_point_name.*

class MarkPointNameDialog(context: Context,var content:String,var mIResult: IResult) :BaseDialog(context) {
    interface IResult {
        fun onName(name: String?)
    }

    private var mEditText: EditText? = null
    private var mBtnSave: Button? = null
    private var mBtnCancel: Button? = null


    override fun getLayoutId(): Int =R.layout.dialog_make_point_name



    override fun initView() {
        window?.setGravity(Gravity.BOTTOM)
        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        mBtnCancel = findViewById(R.id.dialog_btn_cancel)
        mEditText = findViewById(R.id.dialog_make_point_name_et)
        mBtnSave = findViewById(R.id.dialog_btn_save)
        mBtnCancel?.setOnClickListener(View.OnClickListener { v: View? -> dismiss() })
        mBtnSave?.setOnClickListener(View.OnClickListener { v: View? ->
            mIResult.onName(mEditText?.text.toString())
            dismiss()
        })
        dialog_make_point_name_et.setText(content.toString())

    }
}