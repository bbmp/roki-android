package com.robam.roki.ui.activity3

import android.app.AppOpsManager
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.os.Build
import android.provider.Settings
import android.view.View
import androidx.core.app.NotificationManagerCompat
import com.legent.ui.UIService
import com.robam.base.BaseActivity
import com.robam.roki.R
import com.robam.roki.net.OnRequestListener
import com.robam.roki.net.request.api.MessageApi
import com.robam.roki.net.request.api.WORDSWRITTE
import com.robam.roki.net.request.bean.MessageUserUnreadBean
import com.robam.roki.ui.PageKey
import com.robam.roki.ui.page.recipedetail.RecipeDetailPage
import kotlinx.android.synthetic.main.activity_message.*
import kotlinx.android.synthetic.main.include_title_bar.*
import java.lang.reflect.Field
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method


private fun goToSet(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.BASE) {
        // 进入设置系统应用权限界面
        val intent: Intent = Intent(Settings.ACTION_SETTINGS)
        context.startActivity(intent)
        return
    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { // 运行系统在5.x环境使用
        // 进入设置系统应用权限界面
        val intent: Intent = Intent(Settings.ACTION_SETTINGS)
        context.startActivity(intent)
        return
    }
}

/**
 * 消息通知
 */
class MessageActivity : AppActivity(), OnRequestListener {




    lateinit var mMessageApi: MessageApi
    override fun getLayoutId(): Int =R.layout.activity_message

    override fun initView() {
//        RecipeDetailPage.setStatusBarColor(activity, R.color.black)
//        RecipeDetailPage.setLightStatusBar(
//            activity, false
//        )
        act_rl_device_notification.setOnClickListener {

            val intent=Intent(this,NotificationActivity::class.java)
            intent.putExtra(NotificationActivity.MESSAGETYPE,NotificationActivity.DEVICE)
            startActivity(intent)
        }

        img_back.setOnClickListener { finish() }
        tv_title.text="消息"

        act_rl_activity_notification.setOnClickListener {
            val intent=Intent(this,NotificationActivity::class.java)
            intent.putExtra(NotificationActivity.MESSAGETYPE,NotificationActivity.ACTIVITY)
            startActivity(intent)
        }
//        RecipeDetailPage.setStatusBarColor(activity, R.color.white)
//        RecipeDetailPage.setLightStatusBar(
//            activity, true
//        )

        act_rl_customer_service_notification.setOnClickListener {


            var ids= IntArray(1);
            mMessageApi.messageStatusChange(R.layout.activity_message,true,ids,WORDSWRITTE)
            startActivity(Intent(this,ChatActivity::class.java))
        }

        open_notification_permission.setOnClickListener {
            goToSet(this);
        }


        mMessageApi=MessageApi(this)


        open_notification_permission_close.setOnClickListener {
            act_rl_notification.visibility=View.GONE
        }
    }

    override fun initData() {

    }


    override fun onResume() {
        super.onResume()
        val areNotificationsEnabled = NotificationManagerCompat.from(this).areNotificationsEnabled()
        if (!areNotificationsEnabled){
            act_rl_notification.visibility=View.VISIBLE
        }else{
            act_rl_notification.visibility=View.GONE
        }
        mMessageApi.getUserUnreadMessage(R.id.device_polling_task_id)
    }
    override fun onFailure(requestId: Int, requestCode: Int, msg: String?, data: Any?) {

    }

    override fun onSaveCache(requestId: Int, requestCode: Int, paramObject: Any?) {

    }

    override fun onSuccess(requestId: Int, requestCode: Int,  paramObject: Any?) {

        if (requestId==R.id.device_polling_task_id){
            paramObject?.let {
                if (paramObject is MessageUserUnreadBean){
                    var mMessageUserUnreadBean= paramObject

                    if (mMessageUserUnreadBean.payload.chatUnReadCount==0){
                        act_img_customer_service_notification_unread_num.visibility=View.INVISIBLE
                    }else {
                        act_img_customer_service_notification_unread_num.text =
                            mMessageUserUnreadBean.payload.chatUnReadCount.toString()
                    }
                    var unReadCount: Int = mMessageUserUnreadBean.payload.deviceUnReadCount;
                    if (unReadCount == 0){
                        act_img_device_notification_number.visibility=View.INVISIBLE
                    }else {
                        if (unReadCount<=99)
                            act_img_device_notification_number.text = unReadCount.toString()
                        else
                            act_img_device_notification_number.text = "99+"
                    }

                    if (mMessageUserUnreadBean.payload.sysUnReadCount==0){
                        act_img_activity_notification_number.visibility=View.INVISIBLE
                    }else {
                        act_img_activity_notification_number.text =
                            mMessageUserUnreadBean.payload.sysUnReadCount.toString()
                    }
                }

            }

        }
    }
}