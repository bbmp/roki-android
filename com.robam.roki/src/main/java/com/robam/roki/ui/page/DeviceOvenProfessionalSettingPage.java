package com.robam.roki.ui.page;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.common.collect.Lists;
import com.legent.plat.Plat;
import com.legent.ui.UIService;
import com.legent.ui.ext.HeadPage;
import com.robam.common.pojos.device.Oven.Oven039;
import com.robam.common.ui.UiHelper;
import com.robam.roki.R;
import com.robam.roki.model.NormalModeItemMsg;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;
import com.robam.roki.ui.view.DeviceOvenModeWheel;
import com.robam.roki.ui.view.DeviceOvenTemWheel;
import com.robam.roki.ui.view.DeviceOvenTimeWheel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by linxiaobin on 2015/12/22.
 */
public class DeviceOvenProfessionalSettingPage extends HeadPage {


    Oven039 oven;

    public View view;

    private short time;
    private short temp;
    private short modeKind;
    private Map<String, Short> modeKingMap = new HashMap<String, Short>();

    @InjectView(R.id.wheelView)
    LinearLayout wheelView;
    @InjectView(R.id.btnConfirm)
    TextView btnConfirm;
    @InjectView(R.id.wv1)
    DeviceOvenModeWheel modeWheel;
    @InjectView(R.id.wv2)
    DeviceOvenTemWheel temWheel;
    @InjectView(R.id.wv3)
    DeviceOvenTimeWheel timeWheel;
    @InjectView(R.id.txt1)
    TextView txtMode;
    @InjectView(R.id.txt2)
    TextView txtContext;


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0x03) {
                txtMode.setText(modeWheel.getSelectedText());
                if (modeWheel.getSelectedText().equals("快热"))
                    txtContext.setText("快热模式适合烤蛋挞、玉米、葱油饼。");
                else if (modeWheel.getSelectedText().equals("风焙烤"))
                    txtContext.setText("风焙烤模式适合烤一些成品食物，如：肉串、薯条。");
                else if (modeWheel.getSelectedText().equals("焙烤"))
                    txtContext.setText("焙烤模式特别适合烘焙，主要烤蛋糕、饼干、奶黄包。");
                else if (modeWheel.getSelectedText().equals("风扇烤"))
                    txtContext.setText("风扇烤适合烤鸭，烤鸡，也适合烤牛排、猪排、五花肉、培根、翅中、鸡腿。");
                else if (modeWheel.getSelectedText().equals("烤烧"))
                    txtContext.setText("烤烧模式适合烤猪排、肉串、香肠、翅根。");
                else if (modeWheel.getSelectedText().equals("强烤烧"))
                    txtContext.setText("强烤烧功能强大，适合烤：牛排、香肠、培根、鸡肉、翅中、翅根、鸡腿、肉串、烤鱼。");
                else if (modeWheel.getSelectedText().equals("解冻"))
                    txtContext.setText("尤其适合禽类和肉类的解冻。");
                else if (modeWheel.getSelectedText().equals("底加热"))
                    txtContext.setText("适合加热餐点、燉干汤汁、制作果酱，也适合烘烤浅色糕点。");

//            if (msg.what == 1) {
//                Log.e("String", modeWheel.getSelectedText());
//                Log.e("String", temWheel.getSelectedText());
//                Log.e("String", timeWheel.getSelectedText());
//                handler.sendEmptyMessageDelayed(1, 1000);
//            }
            }
        }
    };


    @Override
    protected View onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bd = getArguments();
        String guid = bd == null ? null : bd.getString(PageArgumentKey.Guid);
        oven = Plat.deviceService.lookupChild(guid);
        contentView = inflater.inflate(R.layout.page_device_oven_professional_setting,
                container, false);
        ButterKnife.inject(this, contentView);
        modeWheel.setOnSelectListener(modeWheelLitener);
        modeWheel.setDefault(0);
        initData();
        txtMode.setText(modeWheel.getSelectedText());
//        Log.e("mode", mode);

//        handler.sendEmptyMessage(1);
        return contentView;
    }

    private void initData() {
        modeKingMap.put("快热", (short) 0);
        modeKingMap.put("风焙烤", (short) 1);
        modeKingMap.put("焙烤", (short) 2);
        modeKingMap.put("底加热", (short) 3);
        modeKingMap.put("解冻", (short) 4);
        modeKingMap.put("风扇烤", (short) 5);
        modeKingMap.put("强烤烧", (short) 7);
        modeKingMap.put("烤烧", (short) 6);
    }


    private String tempPar = null;

    private void hanToEnglish(DeviceOvenModeWheel modeWheel) {
        if ("快热".equals(modeWheel.getSelectedText())) {
            tempPar = "QuickHeat";
        } else if ("风焙烤".equals(modeWheel.getSelectedText())) {
            tempPar = "WindBaking";
        } else if ("焙烤".equals(modeWheel.getSelectedText())) {
            tempPar = "Baking";
        } else if ("底加热".equals(modeWheel.getSelectedText())) {
            tempPar = "BottomHeat";
        } else if ("解冻".equals(modeWheel.getSelectedText())) {
            tempPar = "Unfreeze";
        } else if ("风扇烤".equals(modeWheel.getSelectedText())) {
            tempPar = "FanBaking";
        } else if ("烧烤".equals(modeWheel.getSelectedText())) {
            tempPar = "Barbecue";
        } else if ("强烧烤".equals(modeWheel.getSelectedText())) {
            tempPar = "StrongBarbecue";
        }
    }

    DeviceOvenModeWheel.OnSelectListener modeWheelLitener = new DeviceOvenModeWheel.OnSelectListener() {
        @Override
        public void endSelect(int index, Object item) {
            Log.i("index+Item:endSelect", index + "+" + item);
            List<?> list1 = getList2(item);
            List<?> list2 = getList3(item);
            temWheel.setData(list1);
            timeWheel.setData(list2);
            int def1 = 0, def2 = 0;
            if (index == 0) {
                def1 = 150;
                def2 = 45;
            } else if (index == 1) {
                def1 = 150;
                def2 = 55;
            } else if (index == 2) {
                def1 = 110;
                def2 = 55;
            } else if (index == 3) {//底加热
                def1 = 110;
                def2 = 45;
            } else if (index == 4) {//解冻
                def1 = 45;
                def2 = 15;
            } else if (index == 5) {
                def1 = 170;
                def2 = 55;
            } else if (index == 6) {
                def1 = 130;
                def2 = 45;
            } else if (index == 7) {
                def1 = 130;
                def2 = 35;
            }
            handler.sendEmptyMessage(0x03);
            temWheel.setDefault(def1);
            timeWheel.setDefault(def2);
        }

        @Override
        public void selecting(int index, Object item) {

        }
    };

    /*设置各种模式温度范围*/
    protected List<?> getList2(Object item) {
        List<Integer> list = Lists.newArrayList();
        String s = (String) item;

        if (s.equals("解冻")) {
            for (int i = 15; i <= 80; i++) {
                list.add(i);
            }
        } else if (s.equals("底加热")) {
            for (int i = 50; i <= 180; i++) {
                list.add(i);
            }
        } else if (s.equals("快热") || s.equals("风焙烤") || s.equals("焙烤")
                || s.equals("风扇烤") || s.equals("烤烧") || s.equals("强烤烧")) {
            for (int i = 50; i <= 230; i++) {
                list.add(i);
            }
        }
        return list;
    }

    /*设置各种模式时间*/
    protected List<?> getList3(Object item) {
        List<Integer> list = Lists.newArrayList();
        String s = (String) item;
        if (s.equals("快热") || s.equals("风焙烤") || s.equals("解冻") || s.equals("风扇烤")
                || s.equals("烤烧") || s.equals("强烤烧") || s.equals("焙烤") || s.equals("底加热")) {
            for (int i = 5; i <= 90; i++) {
                list.add(i);
            }
        }
        return list;
    }

    @OnClick(R.id.btnConfirm)
    public void onClickConfirm() {
        hanToEnglish(modeWheel);
        if (UiHelper.checkAuthWithDialog(getContext(), PageKey.UserLogin)) {
            time = Short.valueOf(timeWheel.getSelectedText());
            temp = Short.valueOf(temWheel.getSelectedText());
            modeKind = modeKingMap.get(modeWheel.getSelectedText());

            Bundle bundle = new Bundle();
            NormalModeItemMsg msg = new NormalModeItemMsg();
            msg.setType(modeWheel.getSelectedText());
            msg.setTemperature(temWheel.getSelectedText());
            msg.setTime(timeWheel.getSelectedText());
            bundle.putSerializable("msg", msg);
            bundle.putString(PageArgumentKey.Guid, oven.getID());
            UIService.getInstance().postPage(PageKey.DeviceOvenWorking039, bundle);
        }
    }
}
