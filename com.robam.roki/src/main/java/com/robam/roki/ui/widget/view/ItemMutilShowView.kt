package com.robam.roki.ui.widget.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import com.robam.roki.R
import com.robam.roki.request.bean.GosnAnalBean
import com.robam.roki.ui.page.curve.getTemp
import com.robam.roki.ui.page.curve.getTime
import kotlinx.android.synthetic.main.item_mutil_show_view.view.*

interface IItemMutilShowView{

    fun setTitleAndContent(title:ArrayList<String>, content: List<GosnAnalBean.DeviceParam>);



}
class ItemMutilShowView : RelativeLayout ,IItemMutilShowView{
    private fun initView(context: Context, attrs: AttributeSet?) {
        LayoutInflater.from(context).inflate(R.layout.item_mutil_show_view, this, true)

        item_mutil_title_1.setOnClickListener {

            item_mutil_content_1.text= content.get(0).modeName
            item_mutil_content_2.text= getTemp(content.get(0))
            item_mutil_content_3.text= getTime(  Integer.parseInt(content.get(0).setTime))

        }


        item_mutil_title_2.setOnClickListener {
            item_mutil_content_1.text= content.get(1).modeName
            item_mutil_content_2.text= getTemp(content.get(1))
            item_mutil_content_3.text= getTime(  Integer.parseInt(content.get(1).setTime))
        }


        item_mutil_title_3.setOnClickListener {
            item_mutil_content_1.text= content.get(2).modeName
            item_mutil_content_2.text= getTemp(content.get(2))
            item_mutil_content_3.text= getTime(  Integer.parseInt(content.get(2).setTime))

        }
    }

    constructor(context: Context) : super(context) {
        initView(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initView(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initView(context, attrs)
    }

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        initView(context, attrs)
    }

     var title: ArrayList<String> =ArrayList()
    var content: List<GosnAnalBean.DeviceParam> =ArrayList()
    override fun setTitleAndContent(title: ArrayList<String>, content:List<GosnAnalBean.DeviceParam>) {

        this.title=title
        this.content=content
        if (content.size==1){
            item_mutil_content_1.text = content.get(0).modeName
            item_mutil_content_2.text = getTemp(content.get(0))
            item_mutil_content_3.text =
                getTime(Integer.parseInt(content.get(0).setTime))
            item_mutil_content_1.visibility=View.VISIBLE
            item_mutil_content_2.visibility=View.VISIBLE
            item_mutil_content_3.visibility=View.VISIBLE

        }else {
            content.forEachIndexed { index, s ->
                when (index) {
                    0 -> {
                        item_mutil_title_1.visibility = View.VISIBLE
                        item_mutil_content_1.visibility = View.VISIBLE
                        item_mutil_title_1.text = title[index]
                    }
                    1 -> {
                        item_mutil_title_2.visibility = View.VISIBLE
                        item_mutil_content_2.visibility = View.VISIBLE
                        item_mutil_title_2.text = title[index]
                    }
                    2 -> {
                        item_mutil_title_3.visibility = View.VISIBLE
                        item_mutil_content_3.visibility = View.VISIBLE
                        item_mutil_title_3.text = title[index]


                    }
                }


            }
        }


    }


}