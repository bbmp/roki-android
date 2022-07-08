package com.robam.roki.ui.widget.layout

import android.content.Context
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.RelativeLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.robam.roki.R
import com.xiaomi.push.it
import kotlinx.android.synthetic.main.item_nine_masking.view.*

class ItemNineMaskingView: RelativeLayout {

    constructor(context: Context):super(context){
        LayoutInflater.from(context).inflate(R.layout.item_nine_masking,this,true);


    }



     fun setTextNumber(number: Int,url:String){
//        text_image_view.text = "共"+number+"张"
//         Glide.with(context).load(url)
//             .error(context.resources.getDrawable(R.mipmap.activity_notification))
//             .apply(
//                 RequestOptions()
//                     .transforms(
//                         CenterCrop(), RoundedCorners(
//                             context.resources.getDimension(R.dimen.dp_8)
//                                 .toInt()
//                         )
//                     )
//             )
//             .into(nine_image_view)
    }
}