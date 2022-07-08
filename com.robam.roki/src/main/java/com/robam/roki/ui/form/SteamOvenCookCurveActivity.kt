package com.robam.roki.ui.form

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.google.common.eventbus.Subscribe
import com.legent.plat.events.PageBackEvent
import com.legent.ui.ext.BaseActivity
import com.legent.utils.EventUtils
import com.robam.roki.ui.PageKey

/**
 * DeviceSteamOvenOneCurvePage
 */

class SteamFinish{

}
class SteamOvenCookCurveActivity : BaseActivity() {
    override fun onKeyDown_Back() {
        EventUtils.postEvent(PageBackEvent("SteamOvenCookCurveActivityBack"))
    }

    var boolean=false;
    @Subscribe
    fun finis1h(pageBackEvent:PageBackEvent ){
        if (pageBackEvent.pageName.equals("SteamOvenCookCurveActivityBack")) {
            finish()

        }

    }

    override fun onDestroy() {
        super.onDestroy()

    }

    override fun createFormKey(): String {
        return PageKey.SteamOvenCookCurve
    }

    @Subscribe
    fun onFinish(steamFinish: SteamFinish){
        this.finish()
    }


//    var mSteamOvenOneNew:SteamOvenOneNew

//    @Subscribe
//    public fun onEvent(event: SteamOvenOneStatusChangedEvent)
//    {
//        if (mSteamOvenOneNew == null || !Objects.equal(mSteamOvenOneNew.getID(), event.pojo.id)) {
//            return
//        }
//        mSteamOvenOneNew = event.pojo as SteamOvenOne
//    }

    companion object {
        fun start(atv: Activity) {
            atv.startActivity(Intent(atv, SteamOvenCookCurveActivity::class.java))
            atv.finish()
        }

        @JvmStatic
        fun start(atv: Activity, bd: Bundle?) {
            atv.startActivity(
                Intent(atv, SteamOvenCookCurveActivity::class.java)
                    .putExtras(bd!!)
            )
        }
    }
}