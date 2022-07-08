package com.robam.roki.ui.widget.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout

class SteamFrameLayout: FrameLayout {

    constructor(context:Context):super(context)



//    override fun addViewInLayout(
//        child: View?,
//        index: Int,
//        params: ViewGroup.LayoutParams?
//    ): Boolean {
//        return super.addViewInLayout(child, index, params)
//    }



    constructor(context: Context, attrs: AttributeSet?):super(context,attrs)


    override fun addViewInLayout(
        child: View?,
        index: Int,
        params: ViewGroup.LayoutParams?
    ): Boolean {
        return super.addViewInLayout(child, index, params)
    }
    fun mAddViewInlayout(child: View?) {
        val index = this.childCount

        mAddViewInlayout(child, index)
    }


    fun mAddViewInlayout(child: View?, index: Int) {
        addViewInLayout(child, index, this.layoutParams)
    }
    override  fun addViewInLayout(
        child: View?,
        index: Int,
        params: ViewGroup.LayoutParams?,
        preventRequestLayout: Boolean
    ): Boolean {
        return super.addViewInLayout(child, index, params, preventRequestLayout)
    }
}