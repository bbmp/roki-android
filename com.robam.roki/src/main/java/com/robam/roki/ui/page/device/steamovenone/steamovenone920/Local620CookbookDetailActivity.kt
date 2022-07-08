package com.robam.roki.ui.page.device.steamovenone.steamovenone920

import android.content.Intent
import android.util.Log
import com.google.common.eventbus.Subscribe
import com.legent.plat.Plat
import com.legent.plat.events.PageBackEvent
import com.robam.common.pojos.device.steameovenone.AbsSteameOvenOneNew
import com.robam.roki.R
import com.robam.roki.ui.PageArgumentKey
import com.robam.roki.ui.activity3.AppActivity
import com.robam.roki.ui.page.device.oven.CookBookTag
import kotlinx.android.synthetic.main.activity_local620_cookbook_detail.*
import kotlinx.android.synthetic.main.page_local_cookbook_610_detail.*
import java.util.ArrayList

class Local620CookbookDetailActivity : AppActivity() {


    override fun getLayoutId(): Int =R.layout.activity_local620_cookbook_detail

    override fun initView() {
        var bundle=intent.extras
        frag_cookbook_detail.arguments=bundle
    }

    override fun initData() {
        frag_cookbook_detail.iv_back.setOnClickListener {
            finish()
        }

    }

    @Subscribe
    fun finishPage(mPageBackEvent:PageBackEvent){
        if (mPageBackEvent.pageName.equals("Local620CookbookDetailPage")){
            finish()
        }
    }
}