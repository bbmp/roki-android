package com.robam.roki.ui.page.device.sterilizer;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.legent.VoidCallback;
import com.legent.plat.Plat;
import com.legent.ui.UIService;
import com.legent.ui.ext.BasePage;
import com.legent.utils.LogUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.pojos.device.Sterilizer.Steri826;
import com.robam.roki.R;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.dialog.AbsSterilizerDialog;
import com.robam.roki.utils.DialogUtil;
import com.robam.roki.utils.TestDatas;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * XS855 消毒、烘干 、暖碟、母婴功能的预约页面
 * Created by RuanWei on 2019/11/19.
 */

public class SterilizerOrderPage extends BasePage {
    public String mGuid;
    public String functionParams;

    @InjectView(R.id.tv_title)
    TextView tvTitle;

    @InjectView(R.id.tv_title_desc)
    TextView tvTitleDesc;


    @InjectView(R.id.rb_switch_one)
    RadioButton rbSwitchOne;

    @InjectView(R.id.rb_switch_two)
    RadioButton rbSwitchTwo;

    @InjectView(R.id.tv_desc)
    TextView tvDesc;

    @InjectView(R.id.tv_order_time)
    TextView tvOrderTime;


    @InjectView(R.id.tv_begin_work)
    TextView tvBeginWork;
    private List<Integer> hourList;
    private List<Integer> minuteList;
    private int def1;
    private int def2;
    Steri826 steri826;
    short setTime;
    private Short mode;
    private String desc;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.abs_sterilizer_order, container, false);
        ButterKnife.inject(this, view);
        Bundle bd = getArguments();
        mGuid = bd == null ? null : bd.getString(PageArgumentKey.Guid);
        String titleName = bd == null ? null : bd.getString(PageArgumentKey.tag);
        steri826 = Plat.deviceService.lookupChild(mGuid);
        functionParams = bd == null ? null : bd.getString(PageArgumentKey.functionParams);
        tvTitle.setText(titleName);
        initData();
        return view;
    }

    private void initData() {
        try {
            JSONObject object = new JSONObject(functionParams);
            String state = object.getString("state");
            mode = Short.decode(state);
            String setTimeDef = object.getJSONObject("setTimeDef").getString("value");
            if (!"".equals(setTimeDef)) {
                setTime = Short.decode(setTimeDef);
            }
            JSONArray setTimeArray = object.getJSONObject("setTime").getJSONArray("value");
            String orderTitle = object.getJSONObject("orderTitle").getString("value");
            desc = object.getJSONObject("desc").getString("value");
            JSONArray hourArray = object.getJSONObject("appointment").getJSONObject("hour").getJSONArray("value");
            JSONArray minuteArray = object.getJSONObject("appointment").getJSONObject("minute").getJSONArray("value");
            String defaultHour = object.getJSONObject("appointment").getJSONObject("defaultHour").getString("value");
            String defaultMinute = object.getJSONObject("appointment").getJSONObject("defaultMinute").getString("value");


            List<Integer> hours = new ArrayList<>();
            for (int i = 0; i < hourArray.length(); i++) {
                Integer hour = (Integer) hourArray.get(i);
                hours.add(hour);
            }

            hourList = TestDatas.createModeDataTime(hours);

            List<Integer> minutes = new ArrayList<>();
            for (int i = 0; i < minuteArray.length(); i++) {
                Integer time = (Integer) minuteArray.get(i);
                minutes.add(time);
            }
            minuteList = TestDatas.createModeDataTime(minutes);
            def1 = Integer.parseInt(defaultHour) - hours.get(0);
            def2 = Integer.parseInt(defaultMinute) - minutes.get(0);

            final List<String> setTimeList = new ArrayList<>();
            for (int i = 0; i < setTimeArray.length(); i++) {
                setTimeList.add((String) setTimeArray.get(i));
            }
            if (setTimeList.size() >= 2) {
                rbSwitchOne.setText(String.format("%s分钟", setTimeList.get(0)));
                rbSwitchTwo.setText(String.format("%s分钟", setTimeList.get(1)));
                if (setTimeDef.equals(setTimeList.get(0))) {
                    rbSwitchOne.setChecked(true);
                } else {
                    rbSwitchTwo.setChecked(true);
                }
            }

            tvTitleDesc.setText(orderTitle);
            tvDesc.setText(desc);


            rbSwitchOne.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (setTimeList.size() >= 2) {
                        if (isChecked) {
                            setTime = Short.decode(setTimeList.get(0));
                        } else {
                            setTime = Short.decode(setTimeList.get(1));
                        }
                    }
                }
            });


        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @OnClick({R.id.tv_order_time, R.id.tv_begin_work, R.id.img_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_order_time:
                setDialogParam(hourList, minuteList, "小时", "分钟", def1, def2);
                break;
            case R.id.tv_begin_work:
                send();
                break;
            case R.id.img_back:
                UIService.getInstance().popBack();
                break;
            default:
                break;
        }
    }


    private void setDialogParam(List<Integer> hours, List<Integer> minutes, String str1,
                                String str2, int defNum1, int defNum2) {
        LogUtils.i("201911211", "defNum2:::" + defNum2);
        List<String> listButton = TestDatas.createExpDialogText(str1, str2, "取消", "开始工作");
        AbsSterilizerDialog absSterilizerDialog = new AbsSterilizerDialog(cx, hours, minutes, listButton, defNum1, defNum2,desc);
        absSterilizerDialog.show(absSterilizerDialog);
        absSterilizerDialog.setListener(new AbsSterilizerDialog.PickListener() {
            @Override
            public void onCancel() {

            }

            @Override
            public void onConfirm(int index1, int index2) {
                LogUtils.i("201911206", "index1:::" + index1 + "   index2:::" + index2);
                short orderTime = (short) (index1 * 60 + index2);
                LogUtils.i("201911206", "orderTime:::" +orderTime);
                sendCommand(mode, setTime, orderTime);
            }
        });

    }


    private void sendCommand(final short mode, final short minute, final short orderTime) {
        //如果是关机状态先开机
        if (steri826.status == 0) {
            steri826.setSteriPower((short) 1, (short) 0, (short) 0, new VoidCallback() {
                @Override
                public void onSuccess() {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            setSterilizerMode(mode, minute, orderTime);
                        }
                    }, 500);
                }
                @Override
                public void onFailure(Throwable t) {
                    t.printStackTrace();

                }
            });
        } else {

            setSterilizerMode(mode, minute, orderTime);
        }


    }

    private void setSterilizerMode(final short mode, final short minute, final short orderTime) {


        if (steri826.status==0) {
            steri826.setSteriPower((short) 1, (short) 0, (short) 0, new VoidCallback() {
                @Override
                public void onSuccess() {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            setMode(mode, minute, orderTime);

                        }
                    }, 500);

                }

                @Override
                public void onFailure(Throwable t) {

                }
            });

        }else{
            setMode(mode, minute, orderTime);
        }

    }

    private void setMode(short mode, short minute, short orderTime) {
        steri826.setSteriPower2(mode, minute, (short) 1, (short) 0, orderTime, new VoidCallback() {
            @Override
            public void onSuccess() {
                //UIService.getInstance().popBack();
            }

            @Override
            public void onFailure(Throwable t) {
                ToastUtils.show(t.getMessage(), Toast.LENGTH_SHORT);
            }
        });
    }


    private void send() {

        if (steri826.status==0) {
            steri826.setSteriPower((short) 1, (short) 0, (short) 0, new VoidCallback() {
                @Override
                public void onSuccess() {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            setSteriPower2();
                        }
                    }, 500);

                }

                @Override
                public void onFailure(Throwable t) {

                }
            });
        }else {
            setSteriPower2();
        }



    }

    private void setSteriPower2() {
        steri826.setSteriPower2(mode, setTime, new VoidCallback() {
            @Override
            public void onSuccess() {


                //UIService.getInstance().popBack();
            }

            @Override
            public void onFailure(Throwable t) {
                ToastUtils.show("指令下发失败", Toast.LENGTH_SHORT);
            }
        });
    }



}
