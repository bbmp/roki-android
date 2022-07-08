package com.robam.roki.ui.page.curve

import android.content.Intent
import com.legent.ui.UIService
import com.robam.roki.R
import com.robam.roki.ui.PageKey
import com.robam.roki.ui.activity3.AppActivity
import com.robam.roki.ui.page.recipedetail.RecipeDetailPage
import kotlinx.android.synthetic.main.activity_save_curve_success.*


import kotlinx.android.synthetic.main.include_title_bar.*

class SaveCurveSuccessActivity : AppActivity() {


    override fun getLayoutId(): Int=R.layout.activity_save_curve_success

    override fun initView() {
        tv_title.text="保存烹饪曲线"
        img_back.setOnClickListener {
            finish()
//            UIService.getInstance().popBack()
        }
        RecipeDetailPage.setStatusBarColor(activity, R.color.white)
        RecipeDetailPage.setLightStatusBar(
            activity, true
        )

        recipe_save_success_curve_list.setOnClickListener {

              startActivity(Intent(this,CookingCurveListActivity::class.java))
            finish()
//            UIService.getInstance().postPage(PageKey.CookingCurveList)
        }
    }

    override fun initData() {

    }
}