package com.robam.roki.ui.page.curve

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.robam.base.BaseActivity
import com.legent.plat.Plat
import com.legent.ui.UIService
import com.legent.utils.api.ToastUtils
import com.robam.roki.R
import com.robam.roki.net.OnRequestListener
import com.robam.roki.request.api.CurveListApi
import com.robam.roki.request.bean.CurveListBean
import com.robam.roki.request.bean.Payload
import com.robam.roki.request.bean.ShareBean
import com.robam.roki.ui.adapter.CookingCurveAdapter
import com.robam.roki.ui.adapter.IShare
import com.robam.roki.ui.page.recipedetail.RecipeDetailPage
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItemClickListener
import kotlinx.android.synthetic.main.activity_cooking_curve_list.*
import kotlinx.android.synthetic.main.activity_recipte_list.*
import kotlinx.android.synthetic.main.include_title_bar.*

/**
 * 曲线列表
 */
class CookingCurveListActivity : BaseActivity(), OnRequestListener, IShare {


    private lateinit var mCurListApi: CurveListApi
    private lateinit var mList: ArrayList<Payload>

    /**
     * 记录删除的position
     */
    var adapterPosition = 0
    override fun getLayoutId(): Int {
        return R.layout.activity_cooking_curve_list
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==0x12345&&resultCode== RESULT_OK){
            finish()
        }
    }

    lateinit var mCookingCurveAdapter: CookingCurveAdapter
    override fun initView() {
        mCurListApi = CurveListApi(this)
        mList = ArrayList()

        RecipeDetailPage.setStatusBarColor(activity, R.color.white)
        RecipeDetailPage.setLightStatusBar(
                activity, true
        )
        val menuCreator = SwipeMenuCreator { _, rightMenu, _ ->
            val deleteItem = SwipeMenuItem(context)
            deleteItem
                    .setBackground(resources.getDrawable(R.drawable.layer_list_delete_drawable))
                    .setTextColor(Color.WHITE) // 文字颜色。
                    .setTextSize(15) // 文字大小。
                    .setWidth(140).height = ViewGroup.LayoutParams.MATCH_PARENT
            rightMenu.addMenuItem(deleteItem)
        }
        page_cooking_cure_list.setSwipeMenuCreator(menuCreator)
        val mMenuItemClickListener =
                SwipeMenuItemClickListener { swipeMenuBridge ->
                    // 任何操作必须先关闭菜单，否则可能出现Item菜单打开状态错乱。
                    swipeMenuBridge.closeMenu()
                    adapterPosition = swipeMenuBridge.adapterPosition // RecyclerView的Item的position。
                    val id1 = mCookingCurveAdapter.mList.get(adapterPosition).id
//                    when (swipeMenuBridge.position) {
                    mCurListApi.delCurve(mCurListApi.DEL_CURVE , id1 , Plat.accountService.currentUserId.toInt())
//                    }
                }

        page_cooking_cure_list.setSwipeMenuItemClickListener(mMenuItemClickListener)

        mCookingCurveAdapter = CookingCurveAdapter(this, mList, this)
        page_cooking_cure_list.layoutManager = LinearLayoutManager(getActivity())
        page_cooking_cure_list.adapter = mCookingCurveAdapter

        img_back.setOnClickListener {
            finish()
        }
        tv_title.text = getString(R.string.cooking_curve)
    }

    override fun initData() {
        mCurListApi.getList(R.id.page_cooking_cure_list)
    }


    private fun isEmpty(mDataList: ArrayList<Payload>) {
        if (mDataList.isEmpty()) {
            page_cooking_cure_list.visibility = View.GONE
            rl_cooking_curve_empty.visibility = View.VISIBLE
        } else {
            page_cooking_cure_list.visibility = View.VISIBLE
            rl_cooking_curve_empty.visibility = View.GONE
        }

    }

    override fun onFailure(requestId: Int, requestCode: Int, msg: String?, data: Any?) {

        ToastUtils.show(msg, Toast.LENGTH_LONG)
    }

    override fun onSaveCache(requestId: Int, requestCode: Int, paramObject: Any?) {

    }

    override fun onSuccess(requestId: Int, requestCode: Int, data: Any?) {


        if (requestId == R.id.page_cooking_cure_list) {

            if (data is CurveListBean) {
                data.getPayload()?.let {
                    mList.addAll(it)
                    isEmpty(mList)
                    mCookingCurveAdapter.notifyDataSetChanged()
                }
                act_cooking_curve_loading.visibility = View.GONE
                if (mList.isNotEmpty()) {
                    rl_cooking_curve_empty.visibility = View.GONE
                    page_cooking_cure_list.visibility = View.VISIBLE
                } else {
                    rl_cooking_curve_empty.visibility = View.VISIBLE
                    page_cooking_cure_list.visibility = View.GONE
                }
            }
        } else if (requestId == R.layout.dialog_cookbook_share) {

            if (data is ShareBean) {
                mCurListApi.shareCookBook(R.layout.item_delele, curveCookbookId, data.userId)
            }

        } else if (requestId == R.layout.item_delele) {
            ToastUtils.show("分享成功", Toast.LENGTH_SHORT)
        }else if(requestId == mCurListApi.DEL_CURVE){
            mCookingCurveAdapter.mList.removeAt(adapterPosition)
            mCookingCurveAdapter.notifyDataSetChanged();
            ToastUtils.show("删除成功", Toast.LENGTH_SHORT)
        }

    }

    var curveCookbookId: Int = -1
    override fun share(phone: String, curveCookbookId: Int) {
        this.curveCookbookId = curveCookbookId
        mCurListApi.searchPhone(R.layout.dialog_cookbook_share, phone)
    }
}