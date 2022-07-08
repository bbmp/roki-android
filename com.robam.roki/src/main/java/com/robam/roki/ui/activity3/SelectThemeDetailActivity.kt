package com.robam.roki.ui.activity3

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.legent.ui.UIService
import com.robam.roki.R
import com.robam.roki.ui.PageArgumentKey
import com.robam.roki.ui.PageKey
import kotlinx.android.synthetic.main.activity_select_theme_detail.*
import kotlinx.android.synthetic.main.fragment_select_theme_detail_page.*

class SelectThemeDetailActivity : AppActivity() {


    override fun getLayoutId(): Int =R.layout.activity_select_theme_detail

    override fun initView() {
        val bd = Bundle()
        bd.putInt(PageArgumentKey.ThemeType, intent.getIntExtra(PageArgumentKey.ThemeType,0))
        bd.putLong(PageArgumentKey.Id, intent.getLongExtra(PageArgumentKey.Id,0))
        select_detail_theme_frg.arguments = bd
//        UIService.getInstance().postPage(PageKey.SelectThemeDetailPage, bd)


        select_detail_theme_frg.tv_select_theme_back.setOnClickListener {
            finish()
        }
    }

    override fun initData() {
    }
}