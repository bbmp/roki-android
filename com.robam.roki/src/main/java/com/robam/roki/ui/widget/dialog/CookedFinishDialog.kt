package com.robam.roki.ui.widget.dialog

import android.content.Context
import android.os.CountDownTimer
import com.robam.roki.R
import kotlinx.android.synthetic.main.dialog_cooked_finish.*
interface ICookedFinish{

   public fun onEnd()

   public fun addTime();
}

class CookedFinishDialog(context: Context, var mICookedFinish: ICookedFinish) : BaseDialog(context) {
    override fun getLayoutId(): Int=R.layout.dialog_cooked_finish

    override fun initView() {
        countDownTimer.start()
        dialog_cooked_finish_ok.setOnClickListener {
            mICookedFinish.onEnd()
            dismiss()
        }
        dialog_cooked_finish_extra_time.setOnClickListener {
            var extraTimeDialog= ExtraTimeDialog(context,object :IChooseTime{
                override fun onTimeResult(time: Int) {

                }

            })
            extraTimeDialog.create()
            extraTimeDialog.show()
            dismiss()
        }
    }






    private var countDownTimer=object: CountDownTimer(60000,1000) {
        override fun onTick(millisUntilFinished: Long) {
            dialog_cooked_finish_ok.text = "知道了"+(millisUntilFinished/1000)+"s"
        }

        override fun onFinish() {
            dismiss()
        }


    }
}