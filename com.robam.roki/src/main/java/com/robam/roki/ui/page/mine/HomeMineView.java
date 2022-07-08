package com.robam.roki.ui.page.mine;

import android.content.Context;
import android.content.Intent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.FragmentActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.common.base.Strings;
import com.google.common.eventbus.Subscribe;
import com.hjq.toast.ToastUtils;
import com.legent.Callback;
import com.legent.plat.Plat;
import com.legent.plat.events.ChatNewMsgEvent;
import com.legent.plat.events.MessageEventNumber;
import com.legent.plat.events.UserLoginEvent;
import com.legent.plat.events.UserLogoutEvent;
import com.legent.plat.events.UserUpdatedEvent;
import com.legent.plat.io.cloud.CloudHelper;
import com.legent.plat.pojos.User;
import com.legent.ui.UIService;
import com.legent.ui.ext.dialogs.ProgressDialogHelper;
import com.robam.common.events.CmccBackEvent;
import com.robam.common.events.CookMomentsRefreshEvent;
import com.robam.common.events.FavorityBookRefreshEvent;
import com.robam.common.events.HomeRecipeViewEvent;
import com.robam.common.events.WxCode2Event;
import com.robam.roki.R;
import com.robam.roki.net.OnRequestListener;
import com.robam.roki.net.request.api.UserInfoApi;
import com.robam.roki.net.request.bean.GetCollectCountBean;
import com.robam.roki.ui.PageKey;
import com.robam.roki.ui.activity3.MessageActivity;
import com.robam.roki.ui.activity3.main.HomeActivity;
import com.robam.roki.ui.page.curve.CookingCurveListActivity;
import com.robam.roki.ui.page.login.helper.CmccLoginHelper;
import com.robam.roki.ui.page.login.helper.LoginHelper;
import com.robam.roki.ui.widget.layout.SettingBar;
import com.robam.roki.ui.widget.layout.SettingBar2;
import com.robam.roki.utils.GlideCircleTransform;
import com.robam.roki.utils.OnMultiClickListener;
import com.robam.roki.utils.StringUtil;
import com.robam.roki.utils.ToolUtils;
import com.robam.roki.utils.suspendedball.SystemUtils;
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.w3c.dom.Text;

import skin.support.SkinCompatManager;
import skin.support.utils.SkinPreference;

/**
 * 我的界面
 *
 * @author hxw
 */
public class HomeMineView extends MyBaseView implements OnRequestListener {
    private static final String TAG = "HomeMineView";
    /**
     * 头像
     */
    private ImageView ivPhoto;
    /**
     * 用户名
     */
    private TextView tvUserName;
    /**
     * 手机号
     */
    private TextView tvUserPhone;
    /**
     * 编辑资料
     */
//    private AppCompatButton btnEditInfo;
    /**
     * 我的收藏
     */
    private LinearLayout llCollection;
    /**
     * 我的作品
     */
    private LinearLayout llWork;
    /**
     * 烹饪曲线
     */
    private LinearLayout stbCookLine;

    /**
     * 厨电管理
     */
    private TextView stbDevice;
    /**
     * 售后服务
     */
    private TextView stbSaleService;
    /**
     * 关于
     */
    private TextView stbAbout;
    /**
     * 设置
     */
    private TextView stbSetting;
    private CmccLoginHelper instance;
    //产品手册
    private TextView stbCpsc;
    //服务预约
    private TextView stbFwyy;
    //服务商城
    private TextView stbFwsc;
    private UserInfoApi userInfoApi;
    private TextView tvCollectionNum;
    private TextView tvWorkNum;

    public HomeMineView(Context context, FragmentActivity activity) {
        super(context, activity);
    }


    @Subscribe
    public void onEvent(UserLogoutEvent event) {
        onRefresh();
    }

    @Subscribe
    public void onEvent(UserLoginEvent event) {
        onRefresh();
    }


    @Subscribe
    public void onEvent(UserUpdatedEvent event) {
        onRefresh();
    }

    @Subscribe
    public void onEvent(CookMomentsRefreshEvent event) {
        onRefresh();
    }

    @Subscribe
    public void onEvent(FavorityBookRefreshEvent event) {
        onRefresh();
    }

    @Subscribe
    public void onEvent(HomeRecipeViewEvent event) {
        onRefresh();
    }

    @Subscribe
    public void onEvent(ChatNewMsgEvent event) {
        boolean hasNew = event.hasNew;
    }

    @Subscribe
    public void onEvent(CmccBackEvent event) {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_home_mine;
    }

    @Override
    protected void initView() {
        setStateBarFixer(cx);
        ivPhoto = (ImageView) findViewBy(R.id.iv_photo);
        tvUserName = (TextView) findViewBy(R.id.tv_user_name);
        tvUserPhone = (TextView) findViewBy(R.id.tv_user_phone);
//        btnEditInfo = (AppCompatButton) findViewBy(R.id.btn_edit_info);
        llCollection = findViewBy(R.id.ll_collection);
        llWork = findViewBy(R.id.ll_work);
        stbCookLine = findViewBy(R.id.ll_curve);
        stbDevice = findViewBy(R.id.tv_device_manage);
        stbSaleService = findViewBy(R.id.tv_sale_service);
        stbAbout =  findViewBy(R.id.tv_about);
        stbSetting =  findViewBy(R.id.tv_set);
        stbCpsc = findViewById(R.id.tv_product);
        stbFwyy = findViewById(R.id.tv_service);
        stbFwsc = findViewById(R.id.tv_sercie_shop);
        tvCollectionNum = findViewById(R.id.tv_collection_num);
        tvWorkNum = findViewById(R.id.tv_work_num);
        setOnClickListener(
                llCollection, llWork, stbCookLine, stbDevice, stbSaleService, stbAbout, stbSetting
                , ivPhoto, tvUserName, tvUserPhone, stbCpsc, stbFwsc, stbFwyy);



        findViewBy(R.id.tv_curve_list).setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                getContext().startActivity(new Intent(getContext(),CookingCurveListActivity.class));

            }
        });
        findViewBy(R.id.rl_mine_message).setOnClickListener(view -> {
            if (Plat.accountService.isLogon()) {
                getContext().startActivity(new Intent(getContext(), MessageActivity.class));
            } else {
                CmccLoginHelper.getInstance().toLogin();
            }
        });


    }

    @Subscribe
    public void event(MessageEventNumber event) {

        if (!Plat.accountService.isLogon() || event.getNumber() == 0) {
            findViewById(R.id.textview_home_device_number).setVisibility(View.GONE);
        } else {
            if (event.getNumber() <= 99) {
//                ((TextView) findViewById(R.id.textview_home_device_number)).setTextSize(getResources().getDimension(R.dimen.sp_12));
                ((TextView) findViewById(R.id.textview_home_device_number)).setText(event.getNumber() + "");
            } else {
//                ((TextView) findViewById(R.id.textview_home_device_number)).setTextSize(getResources().getDimension(R.dimen.sp_8));
                ((TextView) findViewById(R.id.textview_home_device_number)).setText("99+");
            }

            findViewById(R.id.textview_home_device_number).setVisibility(View.VISIBLE);
        }
        if (event.getNumber() == 0) {
            findViewById(R.id.textview_home_device_number).setVisibility(View.GONE);
        }
    }




    @Override
    protected void initData() {
        userInfoApi = new UserInfoApi(this);
        instance = CmccLoginHelper.getInstance();
        instance.initSdk(activity);
        instance.getPhnoeInfo();
        onRefresh();
    }

    @Override
    public void onClick(View view) {
        boolean isLogin = Plat.accountService.isLogon();
//        if (view.equals(ivPhoto) || view.equals(tvUserName) || view.equals(tvUserPhone)) {
//            onLogin();
//        } else
        if (view.equals(ivPhoto) || view.equals(tvUserPhone) || view.equals(tvUserName)) {
            if (isLogin) {
                UIService.getInstance().postPage(PageKey.MineEditInfoPage);
            } else {
                startLogin();
            }
        } else if (view.equals(llCollection)) {
            if (isLogin) {
                UIService.getInstance().postPage(PageKey.AbsThemeRecipeListGrid);
            } else {
                onLogin();
            }
        } else if (view.equals(llWork)) {
            if (isLogin) {
                UIService.getInstance().postPage(PageKey.RecipeCookMoments);
            } else {
                onLogin();
            }
        } else if (view.equals(stbDevice)) {
            if (isLogin) {
                UIService.getInstance().postPage(PageKey.DeviceManager);
            } else {
                onLogin();
            }
        } else if (view.equals(stbSaleService)) {
            UIService.getInstance().postPage(PageKey.SaleService);
        } else if (view.equals(stbAbout)) {
            UIService.getInstance().postPage(PageKey.MineAboutPage);
        } else if (view.equals(stbSetting)) {
            UIService.getInstance().postPage(PageKey.MineSettingPage);
            String name = SkinPreference.getInstance().getSkinName();
            if ("night".equals(name))
                SkinCompatManager.getInstance().restoreDefaultTheme();
            else
                SkinCompatManager.getInstance().loadSkin("night", SkinCompatManager.SKIN_LOADER_STRATEGY_BUILD_IN);
//            HomeActivity.start(getContext());
        } else if (view.equals(stbCookLine)) {
            if (isLogin) {
                UIService.getInstance().postPage(PageKey.CurveCookbooks);
            } else {
                onLogin();
            }
        } else if (view.equals(stbCpsc)) {
            toWx("gh_6f90c5303966");
        } else if (view.equals(stbFwyy)) {
            toWx("gh_271333adff20");
        } else if (view.equals(stbFwsc)) {
            toWx("gh_06d2df2c161a");
        }
    }

    /**
     * 跳转微信小程序
     * @param userName
     */
    public void toWx(String userName){
        if (Plat.accountService.isLogon()) {
            IWXAPI api = WXAPIFactory.createWXAPI(cx, "wx77973859a88a8921");
            WXLaunchMiniProgram.Req req = new WXLaunchMiniProgram.Req();
            req.userName = userName ;
            req.miniprogramType = WXLaunchMiniProgram.Req.MINIPTOGRAM_TYPE_RELEASE;// 可选打开 开发版，体验版和正式版
            api.sendReq(req);
        } else {
            onLogin();
        }

    }

    /**
     * 登录？用户信息
     */
    public void onLogin() {
        boolean isLogin = Plat.accountService.isLogon();
        if (isLogin) {
//            UIService.getInstance().postPage(PageKey.MineEditInfoPage);
        } else {
            startLogin();
        }
    }

    /**
     * 指向登录界面
     */
    private void startLogin() {
        if (instance.isGetPhone) {
            instance.loginAuth();
        } else {
            instance.login();
        }
    }

    @Subscribe
    public void onEvent(WxCode2Event event) {
        if (!StringUtil.isEmpty(event.code) && "user_wx_login".equals(event.state)) {
            LoginHelper.loginWx(activity, event.code, true);
            instance.quitAuthActivity();
        }

    }

    /**
     * 获取用户信息3.7
     */
    public void getUser() {
        if (Plat.accountService.isLogon()) {
            ProgressDialogHelper.setRunning(cx, true);
            CloudHelper.getUser2(Plat.accountService.getCurrentUserId(), new Callback<User>() {

                @Override
                public void onSuccess(User user) {
                    ProgressDialogHelper.setRunning(cx, false);
                    Plat.accountService.onLogin(user);
                }

                @Override
                public void onFailure(Throwable t) {
                    ProgressDialogHelper.setRunning(cx, false);
                    ToastUtils.show(t.getMessage());
                }
            });

        }
    }

    /**
     * 设置用户信息
     */
    @Override
    public void onRefresh() {
        boolean isLogin = Plat.accountService.isLogon();
        if (isLogin) {
            User user = Plat.accountService.getCurrentUser();
            tvUserName.setText(Strings.isNullOrEmpty(user.name) ? user.phone : user.name);
//            tvUserPhone.setText(user.getPhone());
            tvUserPhone.setText("点击编辑个人资料");
            if (!Strings.isNullOrEmpty(user.figureUrl)) {
                Glide.with(cx).load(user.figureUrl).placeholder(R.mipmap.ic_user_default_figure)
                        .transform(new GlideCircleTransform(cx, 1, cx.getResources().getColor(R.color.White)))
                        .diskCacheStrategy(DiskCacheStrategy.ALL).into(ivPhoto);
            }
            userInfoApi.getCollectAlbumCount(Plat.accountService.getCurrentUserId());
        } else {
            tvUserName.setText(getString(R.string.not_login));
            tvUserPhone.setText(getString(R.string.login_or_register));
            ivPhoto.setBackgroundResource(R.drawable.headportrait_wdl);
            Glide.with(cx).load(R.drawable.headportrait_wdl)
                    .transform(new GlideCircleTransform(cx, 1, cx.getResources().getColor(R.color.White)))
                    .diskCacheStrategy(DiskCacheStrategy.ALL).into(ivPhoto);
            tvCollectionNum.setText("0");
            tvWorkNum.setText("0");
        }

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }


    @Override
    public void onFailure(int requestId, int requestCode, @Nullable String msg, @Nullable Object data) {
        if (requestId == UserInfoApi.getCollectCountCode) {
            tvCollectionNum.setText("0");
            tvWorkNum.setText("0");
        }
    }

    @Override
    public void onSaveCache(int requestId, int requestCode, @Nullable Object paramObject) {

    }

    @Override
    public void onSuccess(int requestId, int requestCode, @Nullable Object paramObject) {
        if (requestId == UserInfoApi.getCollectCountCode) {
            if (paramObject instanceof GetCollectCountBean) {
                GetCollectCountBean getCollectCountBean = (GetCollectCountBean) paramObject;
                tvCollectionNum.setText(StringUtil.processNull(getCollectCountBean.payload.collect));
                tvWorkNum.setText(StringUtil.processNull(getCollectCountBean.payload.album));

            }
        }
    }
    /**
     * 设置状态栏占位
     */
    private void setStateBarFixer(Context context){
        View mStateBarFixer = findViewBy(R.id.status_bar_fix);
        if (mStateBarFixer != null){
            ViewGroup.LayoutParams layoutParams = mStateBarFixer.getLayoutParams();
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.height = SystemUtils.getStatusBarHeight(context);
            mStateBarFixer.setLayoutParams(layoutParams);
        }
    }
}
