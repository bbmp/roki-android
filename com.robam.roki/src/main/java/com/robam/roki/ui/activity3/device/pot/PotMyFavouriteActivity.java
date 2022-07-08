package com.robam.roki.ui.activity3.device.pot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.legent.Callback;
import com.legent.plat.Plat;
import com.legent.plat.constant.IDeviceType;
import com.legent.plat.io.cloud.Reponses;
import com.legent.ui.UIService;
import com.legent.utils.api.ToastUtils;
import com.robam.common.pojos.device.Pot.Pot;
import com.robam.common.pojos.device.Stove.Stove;
import com.robam.common.pojos.device.fan.AbsFan;
import com.robam.roki.R;
import com.robam.roki.listener.OnRecyclerViewItemClickListener;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;
import com.robam.roki.ui.activity3.device.base.DeviceBaseActivity;
import com.robam.roki.ui.activity3.device.base.DeviceBaseFuntionActivity;
import com.robam.roki.ui.adapter3.PotMyMenuAdapter;
import com.robam.roki.ui.dialog.type.DialogType_PotKP100;

import java.util.ArrayList;
import java.util.List;

public class PotMyFavouriteActivity extends DeviceBaseFuntionActivity {

    /**
     * 功能区adapter
     */
    private RecyclerView rvFavourite;

    TextView tv_top_tips;

    PotMyMenuAdapter potMyMenuAdapter;
    Pot pot;
    AbsFan fan ;
    Stove childStove ;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_pot_my_favourite;
    }

    @Override
    protected void initView() {
        rvFavourite = findViewById(R.id.rv_favourite);
        findViewById(R.id.iv_device_switch).setVisibility(View.INVISIBLE);
        findViewById(R.id.iv_device_more).setVisibility(View.INVISIBLE);
        tv_top_tips = findViewById(R.id.tv_top_tips);

        rvFavourite.setLayoutManager(new StaggeredGridLayoutManager(2, RecyclerView.VERTICAL));
    }
    @Override
    protected void dealData() {
        setTitle("我的最爱");
        if (mDevice instanceof Pot) {
            pot = (Pot) mDevice;
        }
        fan = Plat.deviceService.lookupChild(pot.getParent().getID());
        childStove = fan.getChildStove();
        if(childStove==null){
            tv_top_tips.setVisibility(View.VISIBLE);
            tv_top_tips.setText("请先连接智能灶，再启动菜谱。");
        }else if(!childStove.isConnected()){
            tv_top_tips.setVisibility(View.VISIBLE);
            tv_top_tips.setText("暂时无法运行，请确认智能灶是否在线。");
        }else {
            tv_top_tips.setVisibility(View.GONE);
        }
        setMenuData();
    }

    public void setMenuData() {
        List<String> list = new ArrayList<>();
        list.add("1");
        list.add("1");
        list.add("1");
        list.add("1");
        list.add("1");
        list.add("1");
        potMyMenuAdapter = new PotMyMenuAdapter(this, list, new OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view) {
//                ToastUtils.show(view.getTag().toString(), Toast.LENGTH_SHORT);
                if (Integer.parseInt(view.getTag().toString()) == list.size() - 1) {
                    bundle.putSerializable(PageArgumentKey.Bean, pot);
                    Intent intent = new Intent(getActivity() , PotMoreMenuActivity.class);
                    intent.putExtra(DeviceBaseFuntionActivity.BUNDLE, bundle);
                    startActivity(intent);
                } else {
//                    choiceHead();
                    startCook(Integer.parseInt(view.getTag().toString()),1);
                }

            }
        });
        rvFavourite.setAdapter(potMyMenuAdapter);

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

//        if (dialogType_potKP100 == null) {
//            dialogType_potKP100 = DialogType_PotKP100.createDialogType_PotKP100(cx);
//        }
//        dialogType_potKP100.choiceHeadStartCook(childStove);
//        dialogType_potKP100.show();
//        dialogType_potKP100.setOnLeftHeadClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ToastUtils.show("左", Toast.LENGTH_SHORT);
////                startCook(0);
//            }
//        });
//        dialogType_potKP100.setOnRightHeadClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ToastUtils.show("右", Toast.LENGTH_SHORT);
////                startCook(1);
//            }
//        });
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



}