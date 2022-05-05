package com.robam.roki.ui.page;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.common.base.Objects;
import com.google.common.eventbus.Subscribe;
import com.legent.plat.Plat;
import com.legent.ui.UIService;
import com.legent.ui.ext.HeadPage;
import com.robam.common.events.OvenStatusChangedEvent;
import com.robam.common.pojos.device.Oven.AbsOven;
import com.robam.common.pojos.device.Oven.OvenStatus;
import com.robam.roki.R;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;
import com.robam.roki.ui.view.DeviceOven028ModelWheel;
import com.robam.roki.ui.view.DeviceOvenTemWheel;
import com.robam.roki.ui.view.DeviceOvenTimeWheel;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by yinwei on 2017/8/17.
 */

public class AbsDeviceOvenProfessionSetPage extends HeadPage {
    AbsOven oven;

    public View view;

    protected short time;
    protected short temp;
    protected short modeKind;

    protected Map<String, Short> modeKingMap = new HashMap<String, Short>();

    @InjectView(R.id.wheelView)
    LinearLayout wheelView;
    @InjectView(R.id.btnConfirm)
    TextView btnConfirm;
  /*  @InjectView(R.id.wv2)*/
    public DeviceOvenTemWheel temWheel;
    @InjectView(R.id.wv3)
    DeviceOvenTimeWheel timeWheel;
    @InjectView(R.id.txt1)
    TextView txtMode;
    @InjectView(R.id.txt2)
    TextView txtContext;

    public IRokiDialog rokiOrderTimeDialog = null;
    public final int HOUR_SELE = 0;//小时
    public final int MIN_SELE = 1;//分钟
    public DeviceOven028ModelWheel modeWheel;
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
                else if (modeWheel.getSelectedText().equals("底加热"))
                    txtContext.setText("适合加热餐点、燉干汤汁、制作果酱，也适合烘烤浅色糕点。");
                else if ("快速预热".equals(modeWheel.getSelectedText()))
                    txtContext.setText("适用于烘焙糕点前的预热，速度较快。");
                else if ("煎烤".equals(modeWheel.getSelectedText()))
                    txtContext.setText("用于烹饪需要从下方大量加热的披萨或菜式，会使底部焦脆，外表松软。");
                else if ("果蔬烘干".equals(modeWheel.getSelectedText()))
                    txtContext.setText("适合将切片果蔬中的水分烘干。");
                else if ("保温".equals(modeWheel.getSelectedText()))
                    txtContext.setText("可防止烹饪出的菜肴或糕点冷掉，建议保温不超过一个半小时以免影响口感。");
            }
        }
    };


    @Override
    protected View onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bd = getArguments();
        String guid = bd == null ? null : bd.getString(PageArgumentKey.Guid);
        oven = Plat.deviceService.lookupChild(guid);
        contentView = inflater.inflate(R.layout.page_device_oven028_professional_setting,
                container, false);
        modeWheel = contentView.findViewById(R.id.wv028);
        temWheel  = contentView.findViewById(R.id.wv2);
        ButterKnife.inject(this, contentView);
        initData();
      /*  modeWheel.setOnSelectListener(modeWheelLitener);
        modeWheel.setDefault(0);*/
        txtMode.setText(modeWheel.getSelectedText());
//        handler.sendEmptyMessage(1);
        return contentView;
    }

    public void initData() {

    }

    DeviceOven028ModelWheel.OnSelectListener modeWheelLitener = new DeviceOven028ModelWheel.OnSelectListener() {

        @Override
        public void endSelect(int index, Object item) {
            setDefaultValue(index,item);
        }

        @Override
        public void selecting(int index, Object item) {

        }
    };

    protected void setDefaultValue(int index,Object item){

    }



    @Subscribe
    public void onEvent(OvenStatusChangedEvent event) {
        if (oven == null || !Objects.equal(oven.getID(), event.pojo.getID()))
            return;
        if (oven.status == OvenStatus.Wait || oven.status == OvenStatus.Off || oven.status == OvenStatus.Working || oven.status == OvenStatus.Pause
                || oven.status == OvenStatus.Order || oven.status == OvenStatus.PreHeat) {
          /*  if (orderTimeDialog != null && orderTimeDialog.isShowing())
                orderTimeDialog.dismiss();
            orderTimeDialog = null;*/
            UIService.getInstance().popBack();
        }
    }
}
