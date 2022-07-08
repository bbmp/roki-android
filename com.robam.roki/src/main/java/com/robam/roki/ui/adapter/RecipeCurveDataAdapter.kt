package com.robam.roki.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.robam.common.pojos.device.microwave.MicroWaveModeName.model
import com.robam.roki.R
import com.robam.roki.request.bean.RecipeCurveSuccessBean
import com.robam.roki.ui.dialog.ImageShareDialog.Companion.url
import com.robam.roki.ui.page.curve.RecipeSuccessActivity
import com.robam.roki.ui.widget.dialog.*
import com.robam.roki.utils.audio.IOnTouchListener
import com.robam.roki.utils.audio.RecordAudioAndTextView
import com.xiaomi.push.fa
import kotlinx.android.synthetic.main.item_cooked_data.view.*
import kotlinx.android.synthetic.main.record_audio_view.*
import kotlinx.android.synthetic.main.record_audio_view.view.*

interface IPickPicture{
    fun choosePic(pos:Int)
}
/**
 * 烹饪曲线列表
 */
class RecipeCurveDataAdapter(var choosePic:IPickPicture,var model: Int,var time:String?=null,var mIOnTouchListener: IOnTouchListener) :
    BaseQuickAdapter<RecipeCurveSuccessBean.StepList, BaseViewHolder>(R.layout.item_cooked_data) {
    private  val TAG = "RecipeCurveDataAdapter"


    override fun convert(
        holder: BaseViewHolder,
        item: RecipeCurveSuccessBean.StepList,
        payloads: List<Any>
    ) {
         Log.e(TAG,"-----"+item.status+" "+holder.absoluteAdapterPosition+"--"+item.time+"---"+item.playTime)
        if (payloads!=null){
            with(holder.itemView){
                    when (item.status) {
                        1 -> {
                            item_cooked_add_audio.rl_audio_recorded_finish.visibility = VISIBLE
                            item_cooked_add_audio.press_record_audio_view_info.visibility = GONE
                            item_cooked_add_audio.record_audio_view_info_img.visibility = GONE
                            item_cooked_add_audio.audio_input_rl.visibility = VISIBLE
                            Glide.with(context).asGif().load(R.mipmap.audio_recorde_gif)
                                .into(item_cooked_add_audio.recorde_audio_view_state)
                            item_cooked_add_audio.recorde_audio_view_time.text =
                                getTimeFormat(item.time / 1000)
                        }
                        2 -> {
                            item_cooked_add_audio.audio_input_rl.visibility= GONE
                            item_cooked_add_audio.rl_audio_recorded_finish.visibility= VISIBLE
                            Glide.with(context).load(R.mipmap.icon_audio_image)
                                .into(item_cooked_add_audio.recorde_audio_view_state)
                        }
                        3 -> {
                            recorde_audio_view_time.text = getTimeFormat(item.playTime)
                        }
                        else -> {
                            item.playTime=0
                            item.time=0
                            item_cooked_add_audio.rl_audio_recorded_finish.visibility= GONE
                            item_cooked_add_audio.audio_input_rl.visibility= VISIBLE
                            press_record_audio_view_info.visibility=View.VISIBLE
                            record_audio_view_info_img.visibility=View.VISIBLE
//                          item_cooked_add_audio.item_cooked_add_audio.setViewAI()
//                            item_cooked_add_audio.deleteView()
                        }
                    }
                }

        }else {
            super.convert(holder, item, payloads)
        }
    }

    override fun convert(holder: BaseViewHolder, item: RecipeCurveSuccessBean.StepList) {

        with(holder.itemView){
            var righetData=item.markTemp;
         item_time_page.text = item_time_page.text.toString()+" "+"${item.markTemp}℃"

//             String.format("%02d",(item.markTime?.toInt()?.div(60) ?: 0) )+":"+
//                         String.format("%02d",item.markTime?.toInt()
//                 ?.rem(60))
            item_cooked_time.text= item_time_page.text.toString()

            item_cooked_temp.text= "温度\n$righetData℃"

            item_cooked_add_audio.setRecordDate(
                item,
                holder.layoutPosition,
                model == RecipeSuccessActivity.RECIPEING
            ,mIOnTouchListener)



            item_mark_visual.setOnClickListener {
                if (item?.isShow){
                    Glide.with(context).load(R.mipmap.icon_steam_cruve_show).into(item_mark_visual)
                    ll_show_data_introduce_view.visibility= VISIBLE
                }else{
                    Glide.with(context).load(R.mipmap.icon_close_info).into(item_mark_visual)
                    ll_show_data_introduce_view.visibility=GONE
                }
                item?.isShow=!item?.isShow
            }

            if (item?.isShow){
                Glide.with(context).load(R.mipmap.icon_show_info).into(item_mark_visual)
                ll_show_data_introduce_view.visibility= VISIBLE
            }else{
                Glide.with(context).load(R.mipmap.icon_close_info).into(item_mark_visual)
                ll_show_data_introduce_view.visibility=GONE
            }
            if (model== RecipeSuccessActivity.CURVE) {
//                item_edit_target_img.visibility=VISIBLE
                item_cooked_add_pic_close.visibility=GONE
                item_mark_point_txt.setOnClickListener {
                    val markPointNameDialog = MarkPointNameDialog(
                        context, item_mark_point_txt.text?.toString()+"",object : MarkPointNameDialog.IResult {
                            override fun onName(name: String?) {
                                if (name != null) {
                                    item.markName = name
                                    item_mark_point_txt.text = name
                                }
                            }
                        })


                    markPointNameDialog.create()
                    markPointNameDialog.show()
                }

//                item_edit_target_img.setOnClickListener {
//                    val markPointNameDialog = MarkPointNameDialog(
//                        context,item_mark_point_txt.text?.toString()+"", object : MarkPointNameDialog.IResult {
//                            override fun onName(name: String?) {
//                                if (name != null) {
//                                    item.markName = name
//                                    item_mark_point_txt.text = name
//                                }
//                            }
//                        })
//
//
//                    markPointNameDialog.create()
//                    markPointNameDialog.show()
//
//                }


                item_cooked_time.setOnClickListener {
                    var mPickerTimeDialog =
                        time?.let { it1 ->
                            PickerTimeDialog(it1,context, object : IPickerTime {
                                override fun onData(min: Int, sec: Int) {
                                    item.markTime = (min * 60 + sec).toString()
                                    item_cooked_time.text = String.format("%02d",min) +":"+
                                            String.format("%02d",sec);
                                }
                            })
                        }
                    mPickerTimeDialog?.create()
                    mPickerTimeDialog?.show()
                }
//                item_edit_target_img
//                if (item.markName=="烹饪开始"||item.markName=="烹饪结束"||item.markName=="预热完成"){
//                    item_edit_target_img.visibility=GONE
//                    item_mark_point_txt.setOnClickListener(null)
//                    item_cooked_time.setOnClickListener(null)
//                }

//                item_cooked_temp.setOnClickListener {
//                    var dialog =
//                        DialogChooseTimeAndTemperature(context, 1, object : ITimeAndTemperature {
//                            override fun onData(righetData: Int, leftData: Int) {
//                                item.markTemp = righetData.toString()
//                                item_cooked_temp.text = "温度\n$righetData℃";
//                            }
//                        })
//                    dialog.create()
//                    dialog.show()
//                }

                item_cooked_add_pic_close.setOnClickListener {
                    item_cooked_add_pic_close.visibility = GONE
                    item_cooked_add_pic_show_all.visibility = GONE
                    item_time_and_temper_rl.setOnClickListener {
                        choosePic.choosePic(holder.absoluteAdapterPosition)
                    }
                }
                item_time_and_temper_rl.setOnClickListener {
                    choosePic.choosePic(holder.absoluteAdapterPosition)
                }
                var pos = holder.position
//                if (pos==0||pos==data.size-1||(item.markName!=null&&(item.markName?.contains("预热") == true))){
//                    item_mark_point_txt.isEnabled=false
//                    item_edit_target_img.visibility=GONE
//                }
                item_mark_point_txt.text = item.markName
            }else{
//                item_edit_target_img.visibility=View.INVISIBLE
                item_mark_point_txt.isEnabled=false
            }


            if (!TextUtils.isEmpty(item.imageUrl)||
                !TextUtils.isEmpty(item.videoUrl)) {
                item_time_and_temper_rl.setOnClickListener(null)
                item_cooked_add_pic_show_all.visibility=VISIBLE
                if(model== RecipeSuccessActivity.CURVE)
                item_cooked_add_pic_close.visibility= VISIBLE
                item_cooked_add_pic_show_all.background=resources.getDrawable(R.drawable.shape_curve_item_bg)
                item.imageUrl?.let {
                    Glide.with(context).load(it).apply( RequestOptions()
                        .transforms( CenterCrop(),  RoundedCorners(10)
                        )).into(item_cooked_add_pic_show_all)
                }
                item.videoUrl?.let {
                    Glide.with(context)
                        .setDefaultRequestOptions(
                            RequestOptions()
                                .frame(1000000)
                                .centerCrop()
                        )
                        .load(it)
                        .into(item_cooked_add_pic_show_all)
                }
            }


        }

        }
    }