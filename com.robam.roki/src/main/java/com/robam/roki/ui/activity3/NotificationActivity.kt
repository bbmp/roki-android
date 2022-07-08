package com.robam.roki.ui.activity3

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hjq.toast.ToastUtils
import com.jcodecraeer.xrecyclerview.XRecyclerView
import com.robam.roki.R
import com.robam.roki.net.OnRequestListener
import com.robam.roki.net.request.api.DEVICEMESSAGE
import com.robam.roki.net.request.api.MessageApi
import com.robam.roki.net.request.api.SYSTEMMESSAGE
import com.robam.roki.net.request.bean.MessageListBean
import com.robam.roki.ui.PageArgumentKey
import com.robam.roki.ui.activity3.recipedetail.RecipeDetailActivity
import com.robam.roki.ui.page.SelectThemeDetailPage
import com.robam.roki.ui.page.recipedetail.RecipeDetailPage
import kotlinx.android.synthetic.main.activity_notification.*
import kotlinx.android.synthetic.main.dialog_show_read_status.*
import kotlinx.android.synthetic.main.include_title_bar.*
import kotlinx.android.synthetic.main.item_notification_activity.view.*
import kotlinx.android.synthetic.main.item_notification_device.view.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

fun getYearStartTime(timeStamp: Long?, timeZone: String="GMT+8:00"): Long {
    val calendar = Calendar.getInstance() // 获取当前日期
    calendar.timeZone = TimeZone.getTimeZone(timeZone)
    calendar.timeInMillis = timeStamp!!
    calendar.add(Calendar.YEAR, 0)
    calendar.add(Calendar.DATE, 0)
    calendar.add(Calendar.MONTH, 0)
    calendar[Calendar.DAY_OF_YEAR] = 1
    calendar[Calendar.HOUR_OF_DAY] = 0
    calendar[Calendar.MINUTE] = 0
    calendar[Calendar.SECOND] = 0
    calendar[Calendar.MILLISECOND] = 0
    return calendar.timeInMillis
}
fun stampToDate(s:String,pattern:String="MM月dd日 HH:mm"):String{
    var res="";
    var simpleDateFormat =  SimpleDateFormat(pattern);

    var date =  Date(s.toLong());
    res = simpleDateFormat.format(date);
    return res;
}

class ShowReadStatusDialog(context: Context,var mIConfirmRead:IConfirmRead):Dialog(context){




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_show_read_status)
        window?.setBackgroundDrawableResource(android.R.color.transparent);
          var attr = window?.attributes;
            if (attr != null) {
                attr.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                attr.width = ViewGroup.LayoutParams.WRAP_CONTENT;
                attr.gravity = Gravity.CENTER;//设置dialog 在布局中的位置
            }
        setCanceledOnTouchOutside(false);
        dialog_show_read_status_cancel.setOnClickListener {

            dismiss()
        }

        dialog_show_read_status_confirm.setOnClickListener {
            dismiss()
            mIConfirmRead.onResult()
        }



    }
}
interface IConfirmRead{
   fun onResult();
}


/**
 * 判断string字符串是不是json格式
 * @param content
 * @return
 */
 fun isJson(content: String): Boolean {
    return try {
        if (content.contains("[") && content.contains("]")) {
            JSONArray(content)
            true
        } else {
            JSONObject(content)
            true
        }
    } catch (e: JSONException) {
        false
    }
}
class NotificationActivity : AppActivity(), OnRequestListener {




    lateinit var mMessageApi:MessageApi

    companion object{
        val DEVICE=1
        val ACTIVITY=2
        val MESSAGETYPE="messageType"
    }


    interface IClick{
        fun onItem(id:Int)
    }


    class NotificationViewHolder(view:View): RecyclerView.ViewHolder(view)
    class NotificationAdapter(var mDataList:ArrayList<MessageListBean.Datas>, var type:Int,var mIClick:IClick) :RecyclerView.Adapter<NotificationViewHolder>(){

        lateinit var context:Context
        override fun onCreateViewHolder(p0: ViewGroup, p1: Int): NotificationViewHolder {
            this.context=p0.context;
            if (type==DEVICE) {
                return NotificationViewHolder(
                    LayoutInflater.from(p0.context).inflate(R.layout.item_notification_device, null)
                )
            }
            return NotificationViewHolder(
                    LayoutInflater.from(p0.context).inflate(R.layout.item_notification_activity, null)
                )

        }

        override fun onBindViewHolder(p0: NotificationViewHolder, p1: Int) {
            if (type==DEVICE) {
                with(p0.itemView){
                    item_device_context.text=mDataList[p1].msgContent
                    item_device_name.text=mDataList[p1].msgTitle
                    if (mDataList[p1].createTime>= getYearStartTime( System.currentTimeMillis()).toString()){
                        item_notification_time_device.text = stampToDate(mDataList[p1].createTime)
                    }else {
                        item_notification_time_device.text = stampToDate(mDataList[p1].createTime,"yyyy年MM月dd日 HH:mm")
                    }
                    if (!mDataList[p1].readFlag){
                        item_notification_device_unread_logo.visibility=View.VISIBLE
                    }else{
                        item_notification_device_unread_logo.visibility=View.INVISIBLE
                    }
                }
            }else if (type==ACTIVITY) {
                with(p0.itemView) {

                    item_notification_time.text = stampToDate(mDataList[p1].createTime)
                    if (mDataList[p1].fileKey != null) {
                        Glide.with(context).load(mDataList[p1].fileKey)
                            .into(item_notification_activity_image)
                    }

                    if (!TextUtils.isEmpty(mDataList[p1].msgExtra)&&mDataList[p1].msgExtra.contains("no_action")) {
                        item_notification_activity_more.visibility = View.GONE
                    } else {
                        item_notification_activity_more.visibility = View.VISIBLE
//                        item_notification_activity_more.setOnClickListener {
//                            if (isJson(mDataList[p1].msgExtra)) {
//                                val msg: org.json.JSONObject =
//                                    org.json.JSONObject(mDataList[p1].msgExtra)
//                                if (msg?.get("type") != null) {
//                                    when {
//                                        msg.get("type").equals("theme") -> {
//                                            val intent =
//                                                Intent(
//                                                    context,
//                                                    SelectThemeDetailActivity::class.java
//                                                )
//                                            intent.putExtra(
//                                                PageArgumentKey.Id,
//                                                msg.get("id").toString().toLong()
//                                            )
//                                            intent.putExtra(
//                                                PageArgumentKey.ThemeType,
//                                                SelectThemeDetailPage.TYPE_THEME_BANNER
//                                            )
//                                            context.startActivity(intent)
//                                        }
//                                        msg.get("type").equals("game") -> {}
//                                        msg.get("type").equals("cook") -> {
//
//                                            val intent =
//                                                Intent(context, RecipeDetailActivity::class.java)
//                                            intent.putExtra(
//                                                PageArgumentKey.BookId,
//                                                msg.get("id").toString().toLong()
//                                            )
//                                            context.startActivity(intent)
//                                        }
//                                        msg.get("type").equals("h5") -> {
//                                            WebActivity.start(context, msg.get("url").toString())
//                                        }
//                                        else -> {
//                                        }
//                                    }
//                                }
//                            }
//                        }
                    }

                        item_activity_name.text = mDataList[p1].msgTitle
                        item_notification_activity_introduce.text = mDataList[p1].msgContent
                        item_notification_activity_rl.setOnClickListener {
                            mIClick.onItem(p1)
                            if (mDataList[p1].msgExtra!=null&&isJson(mDataList[p1].msgExtra)) {

                                if (mDataList[p1].msgExtra == "{}"){
                                    return@setOnClickListener
                                }
                                val msg=
                                   JSONObject(mDataList[p1].msgExtra)

                                if (msg?.get("type") != null) {
                                    when {
                                        msg.get("type").equals("theme") -> {
                                            val intent = Intent(context, SelectThemeDetailActivity::class.java)
                                            intent.putExtra(PageArgumentKey.Id, msg.get("id").toString().toLong())
                                            intent.putExtra(PageArgumentKey.ThemeType, SelectThemeDetailPage.TYPE_THEME_BANNER)
                                            context.startActivity(intent)
                                        }
                                        msg.get("type").equals("game") -> {

                                            context.startActivity(Intent(context,RandomRecipeActivity::class.java))


                                        }
                                        msg.get("type").equals("cook") -> {

                                            val intent =
                                                Intent(context, RecipeDetailActivity::class.java)
                                            intent.putExtra(
                                                PageArgumentKey.Id,
                                                msg.get("id").toString().toLong()
                                            )
                                            context.startActivity(intent)
                                        }
                                        msg.get("type").equals("h5") -> {
//                                            WebActivity.start(context, msg.get("url").toString())

//        WebActivity.start(this,event.url);
                                            Log.e("NOTIFICATIONACTION", msg.get("url").toString() + "\n" + msg.get("secondTitle").toString() + "\n" + mDataList[p1].fileKey + "\n" + mDataList[p1].msgTitle)
                                            NotificationWebActivity.start(context, msg.get("url").toString(),msg.get("secondTitle").toString(),mDataList[p1].fileKey,mDataList[p1].msgTitle)

                                        }
                                        else -> {
                                        }
                                    }
                                }
                            }
                        }

                        if (!mDataList[p1].readFlag) {
                            item_notification_activity_unread_logo.visibility = View.VISIBLE
                        } else {
                            item_notification_activity_unread_logo.visibility = View.INVISIBLE
                        }
                    }


                }

        }

        override fun getItemCount(): Int =mDataList.size

    }

    override fun getLayoutId(): Int=R.layout.activity_notification
    lateinit var mDataList:ArrayList<MessageListBean.Datas>
    var page=0;
    var mPosId=0
    var messagetype=1;
    override fun initView() {

//        RecipeDetailPage.setStatusBarColor(activity, R.color.black)
//        RecipeDetailPage.setLightStatusBar(
//            activity, false
//        )


        img_back.setOnClickListener {
            finish()
        }
        mMessageApi= MessageApi(this);


         messagetype=intent.getIntExtra(MESSAGETYPE,1)


        mDataList=ArrayList()

        mNotificationAdapter=NotificationAdapter(mDataList,intent.getIntExtra(MESSAGETYPE,1),object :IClick{
            override fun onItem(id:Int) {
                var ids=intArrayOf(mDataList[id].id)
                mPosId=id;
                if (messagetype== ACTIVITY) {
                    mMessageApi.messageStatusChange(
                        R.layout.activity_notification,
                        ids.size == mDataList.size,
                        ids,
                        SYSTEMMESSAGE
                    )
                }else{
                    mMessageApi.messageStatusChange(
                        R.layout.activity_notification,
                        ids.size == mDataList.size,
                        ids,
                        DEVICEMESSAGE
                    )
                }
            }

        })
        act_notification_recycler_view.layoutManager=LinearLayoutManager(this)

        act_notification_recycler_view.adapter=mNotificationAdapter

        act_notification_recycler_view.setPullRefreshEnabled(true);
        act_notification_recycler_view.setLoadingMoreEnabled(true);
//        RecipeDetailPage.setLightStatusBar(
//            activity, true
//        )
//        RecipeDetailPage.setStatusBarColor(activity, R.color.white)

        act_notification_recycler_view.setLoadingListener(object: XRecyclerView.LoadingListener{
            override fun onRefresh() {
                page=0
                when(messagetype){
                    DEVICE->{

                        mMessageApi.getUserMessageList(R.id.device_polling_task_id, DEVICEMESSAGE,page)
                    }

                    ACTIVITY->{
                        mMessageApi.getUserMessageList(R.id.act_rl_device_notification, SYSTEMMESSAGE,page)
                    }
                }
            }

            override fun onLoadMore() {
                page++
                when(messagetype){
                    DEVICE->{
                        mMessageApi.getUserMessageList(R.id.device_polling_task_id, DEVICEMESSAGE,page)
                    }
                    ACTIVITY->{
                        mMessageApi.getUserMessageList(R.id.act_rl_device_notification, SYSTEMMESSAGE,page)
                    }
                }
            }

        });

        when(messagetype){
            DEVICE->{

                tv_title.text=getString(R.string.device_notification)

//                tv_title.typeface = Typeface.defaultFromStyle(Typeface.BOLD);
                mMessageApi.getUserMessageList(R.id.device_polling_task_id, DEVICEMESSAGE)
            }
            ACTIVITY->{
                txt_edit.text="全部已读"
                tv_title.text=getString(R.string.activity_notification)

//                tv_title.typeface = Typeface.defaultFromStyle(Typeface.BOLD);
                txt_edit.setOnClickListener {

                    var isUnRead = false;
                    for (datas in mDataList) {
                        if (!datas.readFlag) {
                            isUnRead = true;
                        }
                    }

                    if (isUnRead) {
                        var mShowReadStatusDialog =
                            ShowReadStatusDialog(context, object : IConfirmRead {
                                override fun onResult() {
                                    var index = 0
                                    var ids = IntArray(mDataList.size)
                                    mDataList.forEach {
                                        ids[index++] = it.id
                                    }
                                    mMessageApi.messageStatusChange(
                                        R.layout.abs_cooker_helper,
                                        true,
                                        ids,
                                        SYSTEMMESSAGE
                                    )

                                }

                            })
                        mShowReadStatusDialog.create()
                        mShowReadStatusDialog.show()


                }else{
                   ToastUtils.show("没有未读消息")
                }
                }
                mMessageApi.getUserMessageList(R.id.act_rl_device_notification, SYSTEMMESSAGE)

            }
        }


    }
    lateinit var  mNotificationAdapter:NotificationAdapter
    override fun initData() {

    }

    override fun onFailure(requestId: Int, requestCode: Int, msg: String?, data: Any?) {


        act_notification_recycler_view.refreshComplete()

        msg?.let {
            ToastUtils.show(it)
        }
    }

    override fun onSaveCache(requestId: Int, requestCode: Int, paramObject: Any?) {


    }

    override fun onSuccess(requestId: Int, requestCode: Int, paramObject: Any?) {


        act_notification_recycler_view.refreshComplete()
        if (requestId==R.id.act_rl_device_notification){
            paramObject?.let {
                if (paramObject is MessageListBean) {
                    if (paramObject.datas == null||paramObject.datas.isEmpty()&&page==0) {
                        act_notification_recycler_view_empty.visibility = View.VISIBLE
                        act_notification_recycler_view.visibility = View.VISIBLE
                        return
                    }
                    if (page > 0) {
                        mDataList.addAll(paramObject.datas)
                    } else {
                        mDataList.clear()
                        mDataList.addAll(paramObject.datas)
                    }
                    act_notification_recycler_view_empty.visibility = View.GONE
                    act_notification_recycler_view.visibility = View.VISIBLE
                    mNotificationAdapter.notifyDataSetChanged()

                }
            }

            if (paramObject == null) {
                act_notification_recycler_view_empty.visibility = View.VISIBLE
                act_notification_recycler_view.visibility = View.VISIBLE
            }

        }else if (requestId==R.id.device_polling_task_id){
            paramObject?.let {
                if (paramObject is MessageListBean) {
                    if (paramObject.datas == null||paramObject.datas.isEmpty()&&page==0) {
                        act_notification_recycler_view_empty.visibility = View.VISIBLE
                        act_notification_recycler_view.visibility = View.VISIBLE
                        return
                    }
                    if (page > 0) {
                        mDataList.addAll(paramObject.datas)
                    } else {
                        mDataList.clear()
                        mDataList.addAll(paramObject.datas)
                    }
                    act_notification_recycler_view_empty.visibility = View.GONE
                    act_notification_recycler_view.visibility = View.VISIBLE
                    mNotificationAdapter.notifyDataSetChanged()

                    if (paramObject.datas!=null){
                        var index=0;
                        var ids= IntArray(mDataList.size)
                        mDataList.forEach {
                            ids[index++]=it.id

                        }
                        mMessageApi.messageStatusChange(R.layout.abs_cooker_helper,ids.size==mDataList.size,ids,DEVICEMESSAGE)
                    }
                }
            }
            if (paramObject == null) {
                act_notification_recycler_view_empty.visibility = View.VISIBLE
                act_notification_recycler_view.visibility = View.VISIBLE
            }
        }else if (R.layout.activity_notification==requestId){
//            mDataList[mPosId].readFlag=true
            //head 需要刷新

            mNotificationAdapter.mDataList[mPosId].readFlag=true;
            mNotificationAdapter.notifyItemChanged(mPosId+1)
        }else if (R.layout.abs_cooker_helper==requestId){
            mNotificationAdapter.mDataList.forEach {
                it.readFlag=true
            }
            mNotificationAdapter.notifyDataSetChanged()

        }
    }
}