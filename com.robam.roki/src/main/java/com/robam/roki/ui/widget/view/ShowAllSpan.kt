package com.example.textviewtext


import android.content.Context
import android.graphics.Color
import com.example.textviewtext.ShowAllSpan.OnAllSpanClickListener
import android.text.style.ClickableSpan
import android.text.TextPaint
import android.view.View
import com.robam.roki.R

class ShowAllSpan(
    private val context: Context,
    private val clickListener: OnAllSpanClickListener?
) : ClickableSpan() {
    private var isPressed = false
    override fun onClick(widget: View) {
        clickListener?.onClick(widget)
    }

    fun setPressed(pressed: Boolean) {
        isPressed = pressed
    }

    interface OnAllSpanClickListener {
        fun onClick(widget: View?)
    }

    override fun updateDrawState(ds: TextPaint) {

       ds.bgColor = context.resources.getColor(R.color.transparent)

        ds.color = Color.rgb(0x47, 0x8B, 0xF3)
        ds.textSize=context.resources.getDimension(R.dimen.sp_12)
        ds.isUnderlineText = false
    }
}