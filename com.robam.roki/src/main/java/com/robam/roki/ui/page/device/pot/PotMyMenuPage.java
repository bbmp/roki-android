package com.robam.roki.ui.page.device.pot;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.bumptech.glide.Glide;
import com.google.common.base.Objects;
import com.google.common.eventbus.Subscribe;
import com.legent.Callback;
import com.legent.VoidCallback;
import com.legent.plat.Plat;
import com.legent.plat.constant.IDeviceType;
import com.legent.plat.events.DeviceConnectionChangedEvent;
import com.legent.plat.pojos.device.IDevice;
import com.legent.plat.services.DeviceTypeManager;
import com.legent.ui.UIService;
import com.legent.ui.ext.BasePage;
import com.legent.utils.api.ToastUtils;
import com.robam.common.pojos.device.Pot.Pot;
import com.robam.common.pojos.device.Stove.Stove;
import com.robam.common.pojos.device.fan.AbsFan;
import com.robam.roki.MobApp;
import com.robam.roki.R;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.listener.OnRecyclerViewItemClickListener;
import com.robam.roki.model.bean.DeviceMoreBean;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;
import com.robam.roki.ui.adapter.AbsMoreAdapter;
import com.robam.roki.ui.adapter.DividerItemDecoration;
import com.robam.roki.ui.adapter3.PotMyMenuAdapter;
import com.robam.roki.ui.adapter3.RvRecipeThemeAdapter;
import com.robam.roki.ui.dialog.type.DialogType_PotKP100;
import com.robam.roki.utils.DialogUtil;
import com.youth.banner.Banner;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 无人锅 我的菜谱
 */

public class PotMyMenuPage extends BasePage {

    @InjectView(R.id.iv_back)
    ImageView mIvBack;
    @InjectView(R.id.tv_failed_tips)
    TextView tv_failed_tips;
    @InjectView(R.id.recyclerView_more)
    RecyclerView mRecyclerViewMore;

    String mGuid;
    Pot pot;
    DialogType_PotKP100 dialogType_potKP100;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Bundle bd = getArguments();
        mGuid = bd == null ? null : bd.getString(PageArgumentKey.Guid);
        if (bd != null) {
            pot = (Pot) bd.getSerializable(PageArgumentKey.Bean);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.page_pot_my_menu, container, false);
        ButterKnife.inject(this, view);
        initView();
        initData();
        return view;
    }

    @Subscribe
    public void onEvent(DeviceConnectionChangedEvent event) {
        if (pot == null || !Objects.equal(pot.getID(), event.device.getID())) {
            return;
        }
        if (!event.isConnected) {

        }
    }


    PotMyMenuAdapter potMyMenuAdapter;

    private void initView() {
        mRecyclerViewMore.setLayoutManager(new StaggeredGridLayoutManager(2, RecyclerView.VERTICAL));
        dialogType_potKP100 = DialogType_PotKP100.createDialogType_PotKP100(cx);
    }

    private void initData() {
        List<String> list = new ArrayList<>();
        list.add("1");
        list.add("1");
        list.add("1");
        list.add("1");
        list.add("1");
        list.add("1");
        potMyMenuAdapter = new PotMyMenuAdapter(cx, list, new OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view) {
//                ToastUtils.show(view.getTag().toString(), Toast.LENGTH_SHORT);
                if (Integer.parseInt(view.getTag().toString()) == list.size() - 1) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(PageArgumentKey.Bean, pot);
                    bundle.putString(PageArgumentKey.Guid, pot.getID());
                    UIService.getInstance().postPage(PageKey.PotMyMenuMore, bundle);
                } else {
//                    choiceHead();
                    startCook(Integer.parseInt(view.getTag().toString()),1);
                }

            }
        });
        mRecyclerViewMore.setAdapter(potMyMenuAdapter);

//        Bundle bundleDry = new Bundle();
//                bundleDry.putSerializable(PageArgumentKey.Bean, pot);
//                UIService.getInstance().postPage(PageKey.PotMyMenuMore, bundleDry);
    }

    private void choiceHead() {
        AbsFan fan = Plat.deviceService.lookupChild(pot.getParent().getID());
        Stove childStove = fan.getChildStove();
        if (childStove instanceof Stove) {
            String dc = childStove.getDc();
            //电磁灶
            if (IDeviceType.RDCZ.equals(dc)) {

                //燃气灶
            } else if (IDeviceType.RRQZ.equals(dc)) {

            }
        }

        if (dialogType_potKP100 == null) {
            dialogType_potKP100 = DialogType_PotKP100.createDialogType_PotKP100(cx);
        }
        dialogType_potKP100.choiceHeadStartCook(childStove);
        dialogType_potKP100.show();
        dialogType_potKP100.setOnLeftHeadClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.show("左", Toast.LENGTH_SHORT);
//                startCook(0);
            }
        });
        dialogType_potKP100.setOnRightHeadClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.show("右", Toast.LENGTH_SHORT);
//                startCook(1);
            }
        });
    }

    private void startCook(int position,int headId) {
        /**
         * ※P档菜谱启停{Key:1,Len:3,Val}
         * 炉头号[1B]:0左 1:右
         * P档序号[1B]: 1~5
         * 控制值:[1B] 0:停止 1:启动
         */
        Pot.Interaction interaction = new Pot.Interaction();
        List<Pot.Interaction> data = new ArrayList<>();
        interaction.key = 1;
        interaction.length = 3;
        interaction.value = new int[]{headId,position+1,1};

        data.add(interaction);

        pot.setInteraction(data, new Callback() {
            @Override
            public void onSuccess(Object o) {

            }

            @Override
            public void onFailure(Throwable t) {

            }
    });}

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {

        UIService.getInstance().popBack();
    }
}
