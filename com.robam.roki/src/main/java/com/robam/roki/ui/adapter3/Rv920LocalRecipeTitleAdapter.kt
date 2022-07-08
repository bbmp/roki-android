package com.robam.roki.ui.adapter3

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.robam.roki.R

import com.robam.roki.ui.adapter.QuickAdapter
import kotlinx.android.synthetic.main.item_title_recipe_title.view.*

class Rv920LocalRecipeTitleAdapter : BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_title_recipe_title) {
    override fun convert(holder: BaseViewHolder, item: String) {
        with(holder.itemView){
            item_title_recipe_txt.text=item;

        }

    }
}