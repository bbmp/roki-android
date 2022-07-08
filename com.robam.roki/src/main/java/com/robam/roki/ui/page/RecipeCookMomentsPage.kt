package com.robam.roki.ui.page

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.view.View
import android.widget.ImageView
import android.widget.Toast

import com.robam.roki.ui.page.login.MyBasePage
import com.robam.roki.ui.form.MainActivity
import butterknife.InjectView
import com.robam.roki.ui.view.EmojiEmptyView
import com.robam.roki.ui.view.MomentGridView
import com.robam.roki.net.request.api.PublishApi
import butterknife.ButterKnife
import com.robam.roki.MobApp
import com.robam.common.events.CookMomentsRefreshEvent
import com.legent.ui.ext.dialogs.ProgressDialogHelper
import com.robam.common.services.CookbookManager
import com.legent.VoidCallback
import com.robam.common.events.HomeRecipeViewEvent
import butterknife.OnClick
import com.google.common.eventbus.Subscribe
import com.legent.ui.UIService
import com.legent.ui.ext.dialogs.DialogHelper
import com.legent.ui.ext.views.TitleBar
import com.legent.utils.EventUtils
import com.legent.utils.api.ToastUtils
import com.robam.common.events.ParaiseEvent
import com.robam.common.pojos.CookAlbum
import com.robam.roki.R
import com.robam.roki.factory.RokiDialogFactory
import com.robam.roki.net.OnRequestListener
import com.robam.roki.net.request.bean.AlbumList
import com.robam.roki.net.request.bean.AlumListBean
import com.robam.roki.ui.activity3.recipedetail.ProductDetailActivity
import com.robam.roki.utils.DialogUtil
import kotlinx.android.synthetic.main.include_title_bar.*
import kotlinx.android.synthetic.main.page_recipe_cook_moments.*
import kotlinx.android.synthetic.main.page_recipe_cook_moments.img_back

/**
 * Created by sylar on 15/6/14.
 * 晒过的厨艺
 */
class RecipeCookMomentsPage : MyBasePage<MainActivity?>(), OnRequestListener {
//    @JvmField
//    @InjectView(R.id.emptyView)
//    var emptyView: EmojiEmptyView? = null
//
//    @JvmField
//    @InjectView(R.id.gridView)
//    var gridView: MomentGridView? = null
//
//    @JvmField
//    @InjectView(R.id.img_back)
//    var imgBack: ImageView? = null
    private var publishApi: PublishApi? = null
    override fun getLayoutId(): Int {
        return R.layout.page_recipe_cook_moments
    }

    override fun initView() {
        regsitRightView()

        img_back.setOnClickListener {
            UIService.getInstance().popBack()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        ButterKnife.reset(this)
    }

    override fun onResume() {
        super.onResume()

        getActivity()?.let {
            MobApp.getmFirebaseAnalytics().setCurrentScreen(it, "晒过的厨艺页", null)
        }

    }

    @Subscribe
    fun onEvent(event: ParaiseEvent) {
        publishApi?.getUserList(PublishApi.Companion.getUserList)
    }

    override fun initData() {

//        tv_title.typeface = Typeface.defaultFromStyle(Typeface.BOLD);
        ProgressDialogHelper.setRunning(cx, true)
        publishApi = PublishApi(this)

        //        CookbookManager.getInstance().getMyCookAlbums(new Callback<List<CookAlbum>>() {
//            @Override
//            public void onSuccess(List<CookAlbum> cookAlbums) {
//                ProgressDialogHelper.setRunning(cx, false);
//                switchView(cookAlbums == null || cookAlbums.size() == 0);
//                gridView.loadData(cookAlbums);
//            }
//
//            @Override
//            public void onFailure(Throwable t) {
//                ProgressDialogHelper.setRunning(cx, false);
//                ToastUtils.showThrowable(t);
//            }
//        });
        publishApi?.getUserList(PublishApi.Companion.getUserList)
    }

    fun switchView(isEmpty: Boolean) {
        emptyView?.visibility = if (isEmpty) View.VISIBLE else View.GONE
        gridView?.visibility = if (!isEmpty) View.VISIBLE else View.GONE
        tv_clear?.visibility = if (isEmpty) View.GONE else View.VISIBLE
    }

    @Subscribe
    fun event(mHomeRecipeViewEvent:HomeRecipeViewEvent){
//        HomeRecipeViewEvent
        publishApi?.getUserList(PublishApi.Companion.getUserList)

    }

    fun regsitRightView() {
        tv_clear.setOnClickListener {
            val deleteDialog = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_26)
            deleteDialog.setTitleText("确认清空所有厨艺照片？")
            deleteDialog.setOkBtn(R.string.ok_btn) {
                ProgressDialogHelper.setRunning(cx, true)
                CookbookManager.getInstance().clearMyCookAlbums(object : VoidCallback {
                    override fun onSuccess() {
                        deleteDialog.dismiss()
                        ProgressDialogHelper.setRunning(cx, false)
                        ToastUtils.showShort("清空成功")
                        EventUtils
                                .postEvent(HomeRecipeViewEvent(0))
                    }

                    override fun onFailure(t: Throwable) {
                        ProgressDialogHelper.setRunning(cx, false)
                        ToastUtils.showThrowable(t)
                    }
                })
            }
            deleteDialog.setCancelBtn(R.string.can_btn) { deleteDialog.dismiss() }
            deleteDialog.setCancelable(false)
            deleteDialog.show();

        }
    }


    override fun onFailure(requestId: Int, requestCode: Int, msg: String?, data: Any?) {
        if (requestId == PublishApi.Companion.getUserList) {
          ProgressDialogHelper.setRunning(cx, false);

            msg?.let {
                ToastUtils.show(it, Toast.LENGTH_LONG);
            }

        }


    }
    override fun onSaveCache(requestId: Int, requestCode: Int, paramObject: Any?) {}
    override fun onSuccess(requestId: Int, requestCode: Int, paramObject: Any?) {
        if (requestId == PublishApi.Companion.getUserList) {
            if (paramObject == null) {
                return
            }
            if (paramObject is AlumListBean) {
                var  cookAlbums= paramObject.albumList?.map {
                    it: AlbumList ->
                    it.ex()
                }

                ProgressDialogHelper.setRunning(cx, false);
               switchView(cookAlbums == null || cookAlbums.isEmpty());

                cookAlbums?.let {
                    gridView?.loadData(it);
                }



            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }
}