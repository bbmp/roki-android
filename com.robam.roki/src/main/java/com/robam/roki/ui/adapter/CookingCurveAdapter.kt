package com.robam.roki.ui.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.gson.JsonObject
import com.legent.plat.Plat
import com.legent.utils.api.ToastUtils
import com.robam.roki.R
import com.robam.roki.request.bean.Payload
import com.robam.roki.ui.activity3.stampToDate
import com.robam.roki.ui.helper3.DeviceNameHelper
import com.robam.roki.ui.page.curve.RecipeCureDetailActivity

import com.robam.roki.ui.widget.dialog.ChooseDeviceDialog
import com.robam.roki.ui.widget.dialog.IDeviceChoose
import com.robam.roki.ui.widget.dialog.MarkPointNameDialog


import com.robam.roki.ui.widget.dialog.ShareDialog
import com.robam.roki.utils.JsonUtils
import com.xiaomi.push.it
import kotlinx.android.synthetic.main.item_cooking_curve.view.*
interface IShare{
    fun share(userId:String,curveCookbookId:Int)
}
class CookingCurveAdapter(var mContext:Activity, var mList:ArrayList<Payload>, var mIShare:IShare) :

    RecyclerView.Adapter<CookingCurveAdapter.CookingCurveViewHolder>() {

     class CookingCurveViewHolder(item: View) : RecyclerView.ViewHolder(item)

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int):CookingCurveViewHolder {
        var mRootView=LayoutInflater.from(mContext).inflate(R.layout.item_cooking_curve,p0,false)
        return CookingCurveViewHolder(mRootView)
    }

    override fun onBindViewHolder(p0:CookingCurveViewHolder, @SuppressLint("RecyclerView") p1: Int) {



        with(p0.itemView){
            item_cooking_curve_name.text=mList[p1]?.name
            var time=mList[p1]?.needTime
            var totalTime: String = if (time>3600){
                (time/3600).toString()+"h"+((time%3600)/60)+"min"+((time%3600)%60)+"s"
            }else if (time in 60..3599){
                if (time%60==0){
                    (time / 60).toString() + "min "
                }else {
                    (time / 60).toString() + "min " + (time % 60) + "s "
                }
            }else{
                (time).toString()+"s"
            }
            item_cooking_curve_time.text= mList[p1].gmtCreate?.let { stampToDate(it,"yyyy.MM.dd") }
            item_cooking_curve_device_working_time.text=totalTime
            item_cooking_curve_line_get_step.setOnClickListener {
                var mIntent=Intent(context,RecipeCureDetailActivity::class.java)
                mIntent.putExtra(RecipeCureDetailActivity.RECIPECUREDETAIL,mList[p1])
               mContext.startActivityForResult(mIntent,0x12345)
            }


            if (mList[p1].userId?.equals(Plat.accountService.currentUserId) == true){
                item_cooking_share_where.visibility=View.INVISIBLE
            }



            mList[p1].deviceParams?.let {

                item_cooking_curve_device_version.text =JsonUtils.getJson2Map(it)["platformName"].toString()
            }



            Glide.with(context).load( mList[p1]?.imageCover).error(R.mipmap.roki_user).into(item_cooking_curve_image_head)
            item_cooking_curve_line_get_staring.setOnClickListener {

                var mChooseDeviceDialog= ChooseDeviceDialog(mContext,mList[p1])
                mChooseDeviceDialog.create()
                mChooseDeviceDialog.show()

            }


            item_cooking_curve_line_share.setOnClickListener {
                    var mShareDialog=ShareDialog(mContext,object : MarkPointNameDialog.IResult{
                        override fun onName(name: String?) {
                            mList[p1]?.id?.let { it1 -> name?.let { it2 -> mIShare.share(it2, it1) } }
                        }
                    })

                    mShareDialog.create()
                    mShareDialog.show()
                }

        }
    }

    fun  getData() :ArrayList<Payload>{
        return mList
    }

    override fun getItemCount(): Int=mList.size
}