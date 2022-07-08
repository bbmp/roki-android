package com.robam.roki.ui.view.nineview

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.robam.roki.R
import com.robam.roki.ui.widget.layout.ItemNineMaskingView
import com.xiaomi.push.it


interface Name {
    companion object {
        const val IMAGE_1 = "0"
        const val IMAGE_2 = "1"
        const val IMAGE_3 = "2"
        const val IMAGE_4 = "3"
        const val IMAGE_5 = "4"
        const val IMAGE_6 = "5"
        const val IMAGE_7 = "6"
        const val IMAGE_8 = "7"
        const val IMAGE_9 = "8"
    }
}

interface OnItemPictureClickListener {
    fun onItemPictureClick(
        itemPostion: Int,
        i: Int,
        url: String?,
        urlList: List<String?>?,
        imageView: ImageView?
    )
}

fun getNameByPosition(itemPosition: Int, i: Int): String? {
    var name = itemPosition.toString() + "_" + Name.IMAGE_1
    when (i) {
        0 -> name = itemPosition.toString() + "_" + Name.IMAGE_1
        1 -> name = itemPosition.toString() + "_" + Name.IMAGE_2
        2 -> name = itemPosition.toString() + "_" + Name.IMAGE_3
        3 -> name = itemPosition.toString() + "_" + Name.IMAGE_4
        4 -> name = itemPosition.toString() + "_" + Name.IMAGE_5
        5 -> name = itemPosition.toString() + "_" + Name.IMAGE_6
        6 -> name = itemPosition.toString() + "_" + Name.IMAGE_7
        7 -> name = itemPosition.toString() + "_" + Name.IMAGE_8
        8 -> name = itemPosition.toString() + "_" + Name.IMAGE_9
    }
    return name
}
class  NineCookedView : NineGridLayout {

    constructor(context: Context):super(context)

    constructor(context: Context,attr:AttributeSet):super(context,attr)
    fun setShowMore(isShowMore:Boolean){
        this.isShowMore=isShowMore;
    }

private var itemPosition: Int = 0
    override fun displayImage(position: Int,
                              imageView: View?,
                              url: String?) {
    if (context != null) {

        if (imageView is ImageView) {
            imageView.let {
                Glide.with(context).load(url)
                        .placeholder(R.mipmap.icon_recipe_default)
                    .error(context.resources.getDrawable(R.mipmap.activity_notification))
                    .apply(
                        RequestOptions()
                            .transforms(
                                CenterCrop(), RoundedCorners(
                                    context.resources.getDimension(R.dimen.dp_8)
                                        .toInt()
                                )
                            )
                    )
                    .into(it)
            }
        } else if (imageView is ItemNineMaskingView) {

            for (num in 0 until imageView.childCount) {
                if (imageView.getChildAt(num) is ImageView) {
                    imageView.getChildAt(num).let {
                        Glide.with(context).load(url)
                                .placeholder(R.mipmap.icon_recipe_default)
                            .error(context.resources.getDrawable(R.mipmap.activity_notification))
                            .apply(
                                RequestOptions()
                                    .transforms(
                                        CenterCrop(), RoundedCorners(
                                            context.resources.getDimension(R.dimen.dp_8)
                                                .toInt()
                                        )
                                    )
                            )
                            .into(it as ImageView)
                    }
                }

            }
        }
    }
    }

    fun setItemPosition(itemPosition: Int) {
        this.itemPosition = itemPosition
    }

    fun setListener(listener: OnItemPictureClickListener) {
        this.listener = listener
    }
    private var listener: OnItemPictureClickListener? = null
    override fun onClickImage(
        position: Int,
        url: String?,
        urlList: MutableList<String>?,
        imageView: ImageView?
    ) {
        listener?.onItemPictureClick(itemPosition, position, url, urlList, imageView)
    }


}