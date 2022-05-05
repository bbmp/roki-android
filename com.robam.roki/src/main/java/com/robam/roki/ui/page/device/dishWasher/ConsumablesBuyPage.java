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
import android.widget.Toast;

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
import com.legent.utils.api.ToastUtils;
import com.robam.common.pojos.device.dishWasher.AbsDishWasher;
import com.robam.roki.MobApp;
import com.robam.roki.R;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.adapter.ConsumablesBuyAdapter;
import com.robam.roki.ui.adapter.SpacesItemDecoration;
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
 * 耗材购买页
 */
public class ConsumablesBuyPage extends BasePage  {
    @InjectView(R.id.iv_back)
    ImageView ivBack;
    @InjectView(R.id.tv_title)
    TextView tvTitle;
    @InjectView(R.id.rv_consumables)
    RecyclerView rvConsumables;
    ConsumablesBuyAdapter adapter;
    List<ConsumablesList> consumablesLists = new ArrayList<>();
    private String guid;
    private String title;
    long userId = Plat.accountService.getCurrentUserId();
    private String wxappletParams;
    private String userName;
    private String type;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.consumables_buy_page, container, false);
        ButterKnife.inject(this, view);
        initData();

        return view;
    }

    @OnClick(R.id.iv_back)
    public void OnClickView(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                UIService.getInstance().popBack();
                break;
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Bundle bd = getArguments();
        guid = bd == null ? null : (String) bd.getSerializable(PageArgumentKey.Guid);
        title = bd == null ? null : (String) bd.getSerializable(PageArgumentKey.title);
        wxappletParams = bd == null ? null : (String) bd.getSerializable(PageArgumentKey.wxparams);
    }

    private void initData() {
        tvTitle.setText(title);
        AbsDishWasher washer = Plat.deviceService.lookupChild(guid);
        if (washer != null) {
            String dt = washer.getDt();
            String dc = washer.getDc();
            getConsumablesList(dt, dc);
        }

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
        adapter = new ConsumablesBuyAdapter(cx, consumablesLists);
        rvConsumables.addItemDecoration(new SpacesItemDecoration(30));
        rvConsumables.setLayoutManager(manager);
        rvConsumables.setAdapter(adapter);

        adapter.setOnItemClickListener(new ConsumablesBuyAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                long accessoryId = consumablesLists.get(position).accessoryId;
                getConsumablesDetails(String.valueOf(userId), accessoryId);
            }
        });
    }

    private void getConsumablesList(String dt, String dc) {
        CloudHelper.getConsumablesList(dt, dc,"", new Callback<Reponses.ConsumablesResponse>() {

            @Override
            public void onSuccess(Reponses.ConsumablesResponse consumablesResponse) {
                if (consumablesResponse == null) {
                    return;
                }
                if (consumablesLists != null) {
                    consumablesLists.clear();
                }
                List<ConsumablesList> list = consumablesResponse.list;
                if (list != null) {
                    consumablesLists.addAll(list);
                    initAdapter();
                }

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
                req.miniprogramType =Integer.parseInt(type);
                api.sendReq(req);

            }

            @Override
            public void onFailure(Throwable t) {
                t.printStackTrace();
                LogUtils.i("20200418", t.getMessage());

            }
        });
    }

//    @Override
//    public void onItemClick(View view, int position) {
//        long accessoryId = consumablesLists.get(position).accessoryId;
//        ToastUtils.show("accessoryId::"+accessoryId,Toast.LENGTH_SHORT);
//    }
}
