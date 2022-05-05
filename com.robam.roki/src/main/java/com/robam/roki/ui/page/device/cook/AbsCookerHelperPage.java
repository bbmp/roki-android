package com.robam.roki.ui.page.device.cook;

import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.legent.Callback;
import com.legent.ui.UIService;
import com.legent.ui.ext.BasePage;
import com.legent.utils.LogUtils;
import com.robam.common.io.cloud.Reponses;
import com.robam.common.pojos.PayLoad;
import com.robam.common.services.StoreService;
import com.robam.roki.R;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;
import com.robam.roki.ui.adapter.CookerHelperAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Dell on 2018/6/5.
 */

public class AbsCookerHelperPage extends BasePage {

    @InjectView(R.id.iv_back)
    ImageView ivBack;
    @InjectView(R.id.device_help_title)
    TextView deviceHelpTitle;
    @InjectView(R.id.item_help)
    RecyclerView itemHelp;

    CookerHelperAdapter cookerHelperAdapter;
    List<PayLoad> helperParam = new ArrayList<>();
    StoreService ss = StoreService.getInstance();
    //  public static final String imgUrl="http://roki.oss-cn-hangzhou.aliyuncs.com/cookbook/large/8c0e1ae4-57de-47be-9c3b-3d9d7cc00f6c.jpg";
    String guid;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bd = getArguments();
        guid = bd == null ? null : bd.getString(PageArgumentKey.Guid);
        View view = inflater.inflate(R.layout.abs_cooker_helper, container, false);
        ButterKnife.inject(this, view);
        initDate();
        return view;
    }


    private void initDate() {
        ss.getKuFRecipeInter(new Callback<Reponses.GetKufaRecipeResponse>() {
            @Override
            public void onSuccess(Reponses.GetKufaRecipeResponse getKufaRecipeResponse) {
                if (getKufaRecipeResponse != null) {
                    LogUtils.i("20181029", "str:" + getKufaRecipeResponse.toString());
                    // helperParam = getKufaRecipeResponse.payLoads;
                    for (int i = 0; i < getKufaRecipeResponse.payLoads.size(); i++) {
                        helperParam.add(getKufaRecipeResponse.payLoads.get(i));
                    }
                    initView();
                    LogUtils.i("20180612", "helperParam:" + helperParam.toString());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                LogUtils.i("20181029", "t:" + t.getMessage());
            }
        });

    }

    private void initView() {

        cookerHelperAdapter = new CookerHelperAdapter(cx, helperParam);
        itemHelp.setAdapter(cookerHelperAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager
                (cx, LinearLayoutManager.VERTICAL, false);
        itemHelp.setLayoutManager(linearLayoutManager);
        cookerHelperAdapter.setOnItemClickListener(new CookerHelperAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(View view, int postion) {
                dealItemClick(view, postion);
            }
        });

    }

    //事件处理方法
    private void dealItemClick(View view, int postion) {
        //ToastUtils.show(""+postion, Toast.LENGTH_SHORT);
        Bundle bd = new Bundle();
        bd.putString(PageArgumentKey.Guid, guid);
        bd.putString(PageArgumentKey.RecipeId, helperParam.get(postion).id + "");
        UIService.getInstance().postPage(PageKey.DeviceDetailCooker, bd);
    }

    @OnClick(R.id.iv_back)
    public void onClickT() {
        UIService.getInstance().popBack();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
