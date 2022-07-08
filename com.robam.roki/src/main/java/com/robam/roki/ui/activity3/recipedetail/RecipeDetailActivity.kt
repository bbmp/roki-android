package com.robam.roki.ui.activity3.recipedetail

import android.os.Bundle
import com.legent.events.AppVisibleEvent
import com.legent.plat.events.FloatHelperEvent
import com.legent.utils.EventUtils
import com.legent.utils.LogUtils
import com.robam.roki.R
import com.robam.roki.ui.PageArgumentKey
import com.robam.roki.ui.activity3.AppActivity
import kotlinx.android.synthetic.main.activity_recipe_detail.*
import kotlinx.android.synthetic.main.page_recipe_detail_3.*

class RecipeDetailActivity : AppActivity() {


    override fun getLayoutId(): Int =R.layout.activity_recipe_detail

    override fun initView() {
        val bd = Bundle()
        bd.putInt("pageKey", 0)
        bd.putLong(PageArgumentKey.BookId, intent.getLongExtra(PageArgumentKey.Id,0))
        recipe_detail_frg.arguments=bd
//        RecipeDetailPage.setStatusBarColor(activity, R.color.black)
//        RecipeDetailPage.setLightStatusBar(
//            activity, false
//        )

        recipe_detail_frg.imgreturn.setOnClickListener {
            finish()
        }
//        recipe_detail_frg.rvRecipeDetailAdapter


    }


    override fun onPause() {
        super.onPause()
        EventUtils.postEvent(AppVisibleEvent(false))

    }
//    override fun onDestroy() {
//        super.onDestroy()
//
//
//            EventUtils.postEvent(AppVisibleEvent(false))
////            EventUtils.postEvent(FloatHelperEvent(true, 99))
//
//    }

    override fun initData() {

    }
}