package com.example.textviewtext

import android.graphics.Paint

class PaintUtils {

    companion object {
        @JvmStatic
        fun getTheTextNeedWidth(thePaint: Paint, text: String): Int {
            val widths = FloatArray(text.length)
            thePaint.getTextWidths(text, widths)
            val length = widths.size
            var nowLength = 0
            for (i in 0 until length) {
               nowLength += widths[i].toInt()
            }
            return nowLength
        }
    }
}