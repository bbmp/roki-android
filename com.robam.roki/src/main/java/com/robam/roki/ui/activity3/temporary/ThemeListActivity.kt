package com.robam.roki.ui.activity3.temporary

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.legent.events.AppVisibleEvent
import com.legent.plat.events.FloatHelperEvent
import com.legent.ui.UIService
import com.legent.utils.EventUtils
import com.legent.utils.LogUtils
import com.robam.roki.R
import com.robam.roki.ui.PageArgumentKey
import com.robam.roki.ui.PageKey
import com.robam.roki.ui.activity3.AppActivity
import com.robam.roki.ui.activity3.recipedetail.ImagePreviewActivity
import kotlinx.android.synthetic.main.activity_recipe_detail.*
import kotlinx.android.synthetic.main.activity_recipe_detail.recipe_detail_frg
import kotlinx.android.synthetic.main.activity_theme_list.*
import kotlinx.android.synthetic.main.fragment_theme_recipe_list_page.*
import kotlinx.android.synthetic.main.page_recipe_detail_3.*
import kotlinx.android.synthetic.main.page_recipe_detail_3.imgreturn

class ThemeListActivity : AppActivity() {


    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(
                    context,
                    ThemeListActivity::class.java
            ))
        }
    }

    override fun getLayoutId(): Int =R.layout.activity_theme_list

    override fun initView() {


        theme_list_fragment.iv_theme_recipe_back.setOnClickListener {
            finish()
        }

    }

    override fun initData() {
        UIService.getInstance().postPage(PageKey.ThemeRecipeListPage);
    }
}