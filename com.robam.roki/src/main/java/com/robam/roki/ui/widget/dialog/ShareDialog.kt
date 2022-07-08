package com.robam.roki.ui.widget.dialog

import android.app.Dialog
import android.content.Context
import android.text.TextUtils
import android.widget.Toast
import com.robam.roki.R
import kotlinx.android.synthetic.main.dialog_share_phone.*

open class ShareDialog(context: Context, var mResult:MarkPointNameDialog.IResult): BaseDialog(context) {


    override fun getLayoutId(): Int {
        return R.layout.dialog_share_phone
    }

    override fun initView() {

        dialog_share_cancel.setOnClickListener {
            dismiss()
        }

        dialog_share_confirm.setOnClickListener {
            var content:String?=dialog_share_et.text.toString()


            if (TextUtils.isEmpty(content)){
                Toast.makeText(context,"请输入手机号",Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            content?.let {
                    mResult.onName(it.toString())
                    dismiss()
                    return@let
                }


        }

        }



}