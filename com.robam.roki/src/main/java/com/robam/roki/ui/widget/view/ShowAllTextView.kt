package com.example.textviewtext

import android.content.Context
import com.example.textviewtext.PaintUtils.Companion.getTheTextNeedWidth

import androidx.appcompat.widget.AppCompatTextView
import com.example.textviewtext.ShowAllSpan.OnAllSpanClickListener
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ClickableSpan
import android.view.MotionEvent
import android.text.Spannable
import android.text.Selection
import android.util.AttributeSet
import android.widget.TextView
import java.lang.Exception

class ShowAllTextView : AppCompatTextView {
    /**全文按钮点击事件 */
    private var onAllSpanClickListener: OnAllSpanClickListener? = null
    var maxShowLines = 0 //最大显示行数

    constructor(context: Context?) : super(context!!) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(
        context!!, attrs
    ) {
    }

    /**调用此方法才有效果 */
    fun setMaxLineText(text: CharSequence?) {
        super.setText(text)
        post { addEllipsisAndAllAtEnd() }
    }

    /**调用此方法才有效果 */
    fun setMaxLineText(resId: Int) {
        setMaxLineText(context.resources.getText(resId))
    }

    /**超过规定行数时, 在文末添加 "...全文" */
    private fun addEllipsisAndAllAtEnd() {
        if (maxShowLines in 1 until lineCount) {
            try {
                if (layout.lineCount>maxShowLines){
                    this.text = text.subSequence(0, layout.getLineEnd(maxShowLines-1))
                }
                val sb = SpannableString("\n查看全文")
                sb.setSpan(
                    ShowAllSpan(context, onAllSpanClickListener),
                    0,
                    sb.length,
                    Spanned.SPAN_INCLUSIVE_INCLUSIVE
                )
                this.append(sb)
            } catch (e: Exception) {
            }
        }
    }

    fun setOnAllSpanClickListener(onAllSpanClickListener: OnAllSpanClickListener?) {
        this.onAllSpanClickListener = onAllSpanClickListener
    }

    //实现span的点击
    private var mPressedSpan: ClickableSpan? = null
    private var result = false
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val text = text
        val spannable = Spannable.Factory.getInstance().newSpannable(text)
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                mPressedSpan = getPressedSpan(this, spannable, event)
                result = if (mPressedSpan != null) {
                    if (mPressedSpan is ShowAllSpan) {
                        (mPressedSpan as ShowAllSpan).setPressed(true)
                    }
                    Selection.setSelection(
                        spannable,
                        spannable.getSpanStart(mPressedSpan),
                        spannable.getSpanEnd(mPressedSpan)
                    )
                    true
                } else {
                    false
                }
            }
            MotionEvent.ACTION_MOVE -> {
                val mClickSpan = getPressedSpan(this, spannable, event)
                if (mPressedSpan != null && mPressedSpan !== mClickSpan) {
                    if (mPressedSpan is ShowAllSpan) {
                        (mPressedSpan as ShowAllSpan).setPressed(false)
                    }
                    mPressedSpan = null
                    Selection.removeSelection(spannable)
                }
            }
            MotionEvent.ACTION_UP -> {
                if (mPressedSpan != null) {
                    if (mPressedSpan is ShowAllSpan) {
                        (mPressedSpan as ShowAllSpan).setPressed(false)
                    }
                    mPressedSpan!!.onClick(this)
                }
                mPressedSpan = null
                Selection.removeSelection(spannable)
            }
        }
        return result
    }

    private fun getPressedSpan(
        textView: TextView,
        spannable: Spannable,
        event: MotionEvent
    ): ClickableSpan? {
        var mTouchSpan: ClickableSpan? = null
        var x = event.x.toInt()
        var y = event.y.toInt()
        x -= textView.totalPaddingLeft
        x += textView.scrollX
        y -= textView.totalPaddingTop
        y += textView.scrollY
        val layout = layout
        val line = layout.getLineForVertical(y)
        val off = layout.getOffsetForHorizontal(line, x.toFloat())
        val spans = spannable.getSpans(off, off, ShowAllSpan::class.java)
        if (spans != null && spans.isNotEmpty()) {
            mTouchSpan = spans[0]
        }
        return mTouchSpan
    }
}