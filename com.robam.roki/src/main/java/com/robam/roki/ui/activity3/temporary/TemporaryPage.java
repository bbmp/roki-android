package com.robam.roki.ui.activity3.temporary;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.legent.Callback;
import com.legent.plat.pojos.AppVersionInfo;
import com.legent.ui.UIService;
import com.legent.ui.ext.BasePage;
import com.legent.ui.ext.dialogs.ProgressDialogHelper;
import com.legent.utils.LogUtils;
import com.legent.utils.api.PackageUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.io.cloud.IRokiRestService;
import com.robam.common.services.AppUpdateService;
import com.robam.roki.MobApp;
import com.robam.roki.R;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;
import com.robam.roki.ui.page.login.MyBasePage;

import java.util.Calendar;
import java.util.concurrent.ScheduledFuture;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by sylar on 15/6/14.
 * 关于 关于Roki页
 */
public class TemporaryPage extends MyBasePage<TemporaryActivity> {


    @Override
    protected int getLayoutId() {
        return R.layout.page_about;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }
}
