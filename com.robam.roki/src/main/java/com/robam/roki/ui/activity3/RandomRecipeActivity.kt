package com.robam.roki.ui.activity3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.robam.roki.R

class RandomRecipeActivity : AppActivity() {


    override fun getLayoutId(): Int =R.layout.activity_random_recipe

    override fun initView() {

        findViewById<ImageView>(R.id.img_back).setOnClickListener {
            finish()
        }

    }

    override fun initData() {

    }
}