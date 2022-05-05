package com.robam.roki.ui.page.device.dishWasher;

import android.content.Context;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.legent.Callback;
import com.legent.plat.Plat;
import com.legent.plat.io.cloud.CloudHelper;
import com.legent.plat.io.cloud.Reponses;
import com.legent.plat.pojos.PayloadDishWasher;
import com.legent.plat.pojos.device.ConsumablesList;
import com.legent.plat.pojos.device.Payload;
import com.legent.ui.UIService;
import com.legent.ui.ext.BasePage;
import com.legent.utils.LogUtils;
import com.robam.common.pojos.device.dishWasher.AbsDishWasher;
import com.robam.roki.MobApp;
import com.robam.roki.R;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.adapter.ConsumablesBuyAdapter;
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 特殊状态查看页面
 */
public class SpecialStatePage extends BasePage  {
    @InjectView(R.id.iv_back)
    ImageView ivBack;
    @InjectView(R.id.tv_title)
    TextView tvTitle;
    @InjectView(R.id.iv_tips)
    ImageView ivTips;
    @InjectView(R.id.tv_tips)
    TextView tvTips;
    @InjectView(R.id.tv_tips_title)
    TextView tvTipsTitle;
    @InjectView(R.id.tv_tips_content)
    TextView tvTipsContent;
    @InjectView(R.id.rv_consumables)
    RecyclerView rvConsumables;
    private ConsumablesBuyAdapter consumablesBuyAdapter;
    long userId = Plat.accountService.getCurrentUserId();

    AbsDishWasher washer;
    String url;
    String params;
    String titleText;
    String dt, dc;
    List<ConsumablesList> consumablesLists = new ArrayList<>();
    private String tag;
    private String wxappletParams;
    private String userName;
    private String type;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.special_state_page, container, false);
        ButterKnife.inject(this, view);
        initData();
        initView();
        return view;
    }

    private void initData() {

        try {
            JSONObject jsonObject=new JSONObject(wxappletParams);
            userName = jsonObject.getJSONObject("userName").getString("value");
            type = jsonObject.getJSONObject("type").getString("value");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initAdapter() {
        RecyclerView.LayoutManager manager = new LinearLayoutManager(cx, RecyclerView.VERTICAL, false);
        consumablesBuyAdapter = new ConsumablesBuyAdapter(cx, consumablesLists);
        rvConsumables.setLayoutManager(manager);
        rvConsumables.setAdapter(consumablesBuyAdapter);
        consumablesBuyAdapter.setOnItemClickListener(new ConsumablesBuyAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                long accessoryId = consumablesLists.get(position).accessoryId;
                getConsumablesDetails(String.valueOf(userId),accessoryId);
            }
        });
    }

    private void initView() {
        tvTitle.setText(titleText);
        dt = washer.getDt();
        dc = washer.getDc();
        try {
            JSONObject jsonObject = new JSONObject(params);
            JSONObject obj = jsonObject.getJSONObject(tag);
            String status = obj.getJSONObject("status").getString("value");
            String effect = obj.getJSONObject("effect").getString("value");
            tvTips.setText(titleText);
            tvTipsTitle.setText(status);
            tvTipsContent.setText(effect);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if ("2".equals(tag)) {
            getConsumablesList(dt, dc,"盐");
        }else if ("3".equals(tag)){
            getConsumablesList(dt, dc,"漂洗剂");
        }

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Bundle bd = getArguments();
        washer = bd == null ? null : (AbsDishWasher) bd.getSerializable(PageArgumentKey.Bean);
        url = bd == null ? null : (String) bd.getSerializable(PageArgumentKey.Url);
        titleText = bd == null ? null : (String) bd.getSerializable(PageArgumentKey.title);
        params = bd == null ? null : (String) bd.getSerializable(PageArgumentKey.text);
        tag = bd == null ? null : (String) bd.getSerializable(PageArgumentKey.tag);
        wxappletParams = bd == null ? null : (String) bd.getSerializable(PageArgumentKey.wxparams);
    }

    @OnClick({R.id.iv_back})
    public void onClickView(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                UIService.getInstance().popBack();
                break;
        }
    }


    private void getConsumablesList(String dt, String dc,String name) {
        CloudHelper.getConsumablesList(dt, dc, name,new Callback<Reponses.ConsumablesResponse>() {

            @Override
            public void onSuccess(Reponses.ConsumablesResponse consumablesResponse) {
                if (consumablesResponse == null) {
                    return;
                }
                if (consumablesLists!=null) {
                    consumablesLists.clear();
                }
                // TODO: 2020/6/17  
                //List<ConsumablesList> list = consumablesResponse.list;
                //if (list!=null&&list.size()>0) {
                //    consumablesLists.add(list.get(0));
                //    initAdapter();
                //}

            }

            @Override
            public void onFailure(Throwable t) {
                t.printStackTrace();
                LogUtils.i("20200417", t.getMessage());
            }
        });


    }



    private void getConsumablesDetails(String userId, long accessoryId) {
        CloudHelper.getPurchaseUrl(userId, accessoryId, new Callback<Reponses.PurchaseUrlResponse>() {
            @Override
            public void onSuccess(Reponses.PurchaseUrlResponse purchaseUrlResponse) {
                PayloadDishWasher payload = purchaseUrlResponse.payload;
                String path = payload.path;
                IWXAPI api = WXAPIFactory.createWXAPI(cx, MobApp.WX_APP_ID);
                WXLaunchMiniProgram.Req req = new WXLaunchMiniProgram.Req();
                req.userName =userName;// 填小程序原始id
                req.path = path;//拉起小程序页面的可带参路径，不填默认拉起小程序首页，对于小游戏，可以只传入 query 部分，来实现传参效果，如：传入 "?foo=bar"。
                req.miniprogramType = Integer.parseInt(type);// 可选打开 开发版，体验版和正式版
                api.sendReq(req);



            }

            @Override
            public void onFailure(Throwable t) {
                t.printStackTrace();
                LogUtils.i("20200418", t.getMessage());

            }
        });
    }


}
