package com.robam.roki.ui.page.device.cook;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.legent.Callback;
import com.legent.plat.pojos.device.IDevice;
import com.legent.ui.UIService;
import com.legent.ui.ext.BasePage;
import com.legent.ui.ext.dialogs.ProgressDialogHelper;
import com.robam.common.io.cloud.Reponses;
import com.robam.common.io.cloud.RokiRestHelper;
import com.robam.roki.R;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;
import com.robam.roki.ui.adapter3.CookLineListAdapter;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 烹饪曲线列表
 */

public class CurveCookbooksPage extends BasePage {

    public String mGuid;
    @InjectView(R.id.iv_back)
    ImageView ivBack;
    @InjectView(R.id.tv_device_model_name)
    TextView tvDeviceModelName;
    @InjectView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    private IDevice iDevice;

    private CookLineListAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        Bundle bd = getArguments();
//        mGuid = bd == null ? null : bd.getString(PageArgumentKey.Guid);
//        iDevice = Plat.deviceService.lookupChild(mGuid);
//        LogUtils.i("20181128", "mGuid::" + mGuid);
        View view = inflater.inflate(R.layout.curve_cook_books_page, container, false);
        ButterKnife.inject(this, view);
        initView();
        initListData();
        return view;
    }



    private void initView() {
        adapter=new CookLineListAdapter();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(cx ,LinearLayoutManager.VERTICAL , false));
        mRecyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener((baseQuickAdapter, view, i) -> {
            Bundle bd = new Bundle();
            bd.putSerializable("Item", adapter.getData().get(i));
            UIService.getInstance().postPage(PageKey.CurveCookbooksUser, bd);
        });
    }

    @OnClick(R.id.iv_back)
    public void reBack() {
        UIService.getInstance().popBack();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    /**
     * 获取列表数据
     */
    protected void initListData() {
        ProgressDialogHelper.setRunning(cx, true);
        RokiRestHelper.getCurveCookbooks("2619535727", new Callback<Reponses.GetCurveCookbooksResonse>() {

            @Override
            public void onSuccess(Reponses.GetCurveCookbooksResonse getCurveCookbooksResonse) {
                if (adapter !=null){
                    adapter.addData(getCurveCookbooksResonse.payload);
                }
                ProgressDialogHelper.setRunning(cx, false);
            }

            @Override
            public void onFailure(Throwable t) {
                ProgressDialogHelper.setRunning(cx, false);
            }
        });
    }
}
