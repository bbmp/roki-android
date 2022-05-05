package com.robam.roki.ui.page;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.base.Objects;
import com.google.common.eventbus.Subscribe;
import com.legent.Callback;
import com.legent.Callback2;
import com.legent.plat.Plat;
import com.legent.plat.events.DeviceConnectionChangedEvent;
import com.legent.ui.UIService;
import com.legent.ui.ext.BasePage;
import com.legent.utils.EventUtils;
import com.legent.utils.LogUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.events.WaterPurifierEvent;
import com.robam.common.events.WaterPurifiyStatusChangedEvent;
import com.robam.common.io.cloud.Reponses;
import com.robam.common.pojos.DataInfo;
import com.robam.common.pojos.WaterPurifierSetPeople;
import com.robam.common.pojos.device.WaterPurifier.AbsWaterPurifier;
import com.robam.common.pojos.device.WaterPurifier.WaterPurifierAlarm;
import com.robam.common.pojos.device.WaterPurifier.WaterPurifierStatus;
import com.robam.common.services.StoreService;
import com.robam.roki.MobApp;
import com.robam.roki.R;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.ui.Helper;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;
import com.robam.roki.ui.WaveHelper;
import com.robam.roki.ui.dialog.WaterPurifierShareDialog;
import com.robam.roki.ui.dialog.WaterPurifierShareMoreDialog;
import com.robam.roki.ui.view.ScrollviewMegListView;
import com.robam.roki.ui.view.WaterPurifierBubbleView;
import com.robam.roki.ui.view.WaterPurifierScrollView;
import com.robam.roki.ui.view.WaterPurifiyHistogramView;
import com.robam.roki.ui.view.WaveView;
import com.robam.roki.utils.DialogUtil;
import com.robam.roki.utils.PermissionsUtils;
import com.robam.roki.utils.TimeUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/5/9.
 */

public class AbsDeviceWaterPurifierPage<WaterPurifier extends AbsWaterPurifier> extends BasePage {
    @InjectView(R.id.waterpurifier)
    RelativeLayout waterpurifier;
    @InjectView(R.id.scrollView)
    WaterPurifierScrollView scrollView;
    @InjectView(R.id.waterpurifier_background) //设置背景颜色
            LinearLayout waterpurifier_background;
    @InjectView(R.id.water_title_background)
    RelativeLayout water_title_background;
    @InjectView(R.id.waterpurifier_belowtitle_background)
    RelativeLayout waterpurifier_belowtitle_background;
    @InjectView(R.id.img_retrun)//标题栏
            ImageView img_retrun;//返回图标
    @InjectView(R.id.water_more)//更多
            ImageView water_more;
    @InjectView(R.id.water_disconnectHintView)//断网提示
            View water_disconnectHintView;
    @InjectView(R.id.water_btn_people)//设置家庭人数
            TextView water_btn_people;
    @InjectView(R.id.water_set_time)
    LinearLayout water_set_time;
    @InjectView(R.id.water_none)
    View water_none;
    //中间水波view
    @InjectView(R.id.water_lvxin_use_time)
    View water_lvxin_use_time;
    @InjectView(R.id.content_wave)
    WaveView content_wave;
    @InjectView(R.id.content_circle)
    ImageView content_circle;//中间圆圈
    @InjectView(R.id.waterpurifier_pic_wash)
    ImageView waterpurifier_pic_wash;//冲洗的图片
    @InjectView(R.id.content_text_pre)
    TextView content_text_pre;//中间圆圈内文字
    @InjectView(R.id.content_text_level)
    TextView content_text_level;//中间文字下
    @InjectView(R.id.water_discont_text)
    TextView water_discont_text;
    @InjectView(R.id.water_quality_ml)
    TextView water_quality_ml;
    //当前水质
    @InjectView(R.id.water_before_pre)
    TextView water_before_pre;
    @InjectView(R.id.water_before)
    TextView water_before;
    //向上滑按钮
    @InjectView(R.id.bottom_mark)
    ImageView bottom_mark;
    //滤芯显示部分
    @InjectView(R.id.waterpurifiy_lvxin_use)//
            LinearLayout waterpurifiy_lvxin_use;
    @InjectView(R.id.filter_pp)
    LinearLayout filter_pp;//pp
    @InjectView(R.id.filter_pp_num)
    TextView filter_pp_num;//pp数字
    @InjectView(R.id.filter_cto)
    LinearLayout filter_cto;//cto
    @InjectView(R.id.filter_cto_num)
    TextView filter_cto_num;//cto数字
    @InjectView(R.id.filter_ro1)
    LinearLayout filter_ro1;//cto
    @InjectView(R.id.filter_ro1_num)
    TextView filter_ro1_num;//ro1数字
    @InjectView(R.id.filter_ro2)
    LinearLayout filter_ro2;//ro2
    @InjectView(R.id.filter_ro2_num)
    TextView filter_ro2_num;//ro2数字
    //编号
    @InjectView(R.id.filter_number_1)
    TextView filter_number_1;
    @InjectView(R.id.filter_number_2)
    TextView filter_number_2;
    @InjectView(R.id.filter_number_3)
    TextView filter_number_3;
    @InjectView(R.id.filter_number_4)
    TextView filter_number_4;

    //号
    @InjectView(R.id.water_text_hao1)
    TextView water_text_hao1;

    @InjectView(R.id.water_text_hao2)
    TextView water_text_hao2;
    @InjectView(R.id.water_text_hao3)
    TextView water_text_hao3;
    @InjectView(R.id.water_text_hao4)
    TextView water_text_hao4;
    //百分号
    @InjectView(R.id.filter_pp_sign)
    TextView filter_pp_sign;
    @InjectView(R.id.filter_cto_sign)
    TextView filter_cto_sign;
    @InjectView(R.id.filter_ro1_sign)
    TextView filter_ro1_sign;
    @InjectView(R.id.filter_ro2_sign)
    TextView filter_ro2_sign;
    //滤芯切换页面的图标
    @InjectView(R.id.filter_pp_pic)
    ImageView filter_pp_pic;
    @InjectView(R.id.filter_cto_pic)
    ImageView filter_cto_pic;
    @InjectView(R.id.filter_ro1_pic)
    ImageView filter_ro1_pic;
    @InjectView(R.id.filter_ro2_pic)
    ImageView filter_ro2_pic;


    @InjectView(R.id.water_jinghua_date)
    TextView water_jinghua_date;
    @InjectView(R.id.water_time_date)
    TextView water_time_date;
    @InjectView(R.id.water_lvxin_time)
    TextView water_lvxin_time;
    @InjectView(R.id.day_water)
    TextView day_water;
    @InjectView(R.id.tv_btn_detail_out)
    TextView tv_btn_detail_out;
    //今日饮水量
    @InjectView(R.id.water_intake)
    TextView water_intake;//今日饮水量
    @InjectView(R.id.date)
    TextView date;//日
    @InjectView(R.id.week)
    TextView week;//周
    @InjectView(R.id.month)
    TextView month;//月
    @InjectView(R.id.water_standard)
    TextView water_standard;
    @InjectView(R.id.water_drink_list)
    ScrollviewMegListView water_drink_list;//listview展示
    @InjectView(R.id.water_family_number)
    TextView watere_family_number;
    @InjectView(R.id.waterpurifiy_drink_check)
    Button waterpurifiy_drink_check;//查看所有的数据
    @InjectView(R.id.water_drink_standard)
    TextView water_drink_standard;
    @InjectView(R.id.water_cankao)
    TextView water_cankao;
    @InjectView(R.id.waterpurifiy_lvxin_text)
    TextView waterpurifiy_lvxin_text;
    @InjectView(R.id.water_power_saving)
    ImageView water_power_saving;
    @InjectView(R.id.water_bubble)
    WaterPurifierBubbleView water_bubble;
    @InjectView(R.id.water_title)
    TextView water_title;
    @InjectView(R.id.water_alarm_people)
    TextView water_alarm_people;

    AbsWaterPurifier purifier;
    boolean scrollViewEnable = true;
    private int preStatus = WaterPurifierStatus.None;
    Helper helper = new Helper();
    private String timeType = "day";
    private int memberCount = 3;
    String guid;
    Bundle bundle = new Bundle();
    int[] dateml = new int[7];
    String[] time = new String[7];
    ArrayList<DataInfo> list = new ArrayList<DataInfo>();
    ListAdapter adapter;
    public WaveHelper mWaveHelper;
    protected IRokiDialog mRokiDialog = null;
    WaterPurifiyHistogramView waterPurifiy_history_view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //获取净水器guid
        Bundle bd = getArguments();
        guid = bd == null ? null : bd.getString(PageArgumentKey.Guid);
        LogUtils.i("20180326", "guid::" + guid);
        purifier = Plat.deviceService.lookupChild(guid);
        //加载净水器页面
        View contentView = inflater.inflate(R.layout.page_device_waterpurifiy_normal,
                container, false);
        ButterKnife.inject(this, contentView);
        waterPurifiy_history_view = contentView.findViewById(R.id.waterPurifiy_history_view);
        water_drink_list = contentView.findViewById(R.id.water_drink_list);
        EventUtils.regist(this);
        adapter = new AbsDeviceWaterPurifierPage.ListAdapter();
        init();
        mRokiDialog = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_01);
        return contentView;
    }

    /**
     * 页面初始化 包括 滑动组件初始化 是否断网模式
     */
    private void init() {
        if ("RJ312".equals(guid.substring(0, 5))) {
            water_title.setText("净水机J312");
        } else if ("RJ320".equals(guid.substring(0, 5))) {
            water_title.setText("净水机J320");
        } else if ("RJ321".equals(guid.substring(0, 5))) {
            water_title.setText("净水机J321");
        }
        mWaveHelper = new WaveHelper(content_wave);
        mWaveHelper.start();
        getFamilyPeople();//获取家庭人数
        getTodayDrink();//获取今日饮水量
        getHistoryDrink();//获取历史饮水量
        water_btn_people.setText("三口之家");
        scrollView.setOnTouchListener(new View.OnTouchListener() {//禁止滑动
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                return !scrollViewEnable;
            }
        });
        scrollViewEnable = false;
        //设置断网提示GONE
        if (!purifier.isConnected()) {
            setOffOrDisConnectModel();
        } else {
            setOpenModel();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnClick(R.id.bottom_mark)
    public void onClickMark() {
        int offset = scrollView.getMeasuredHeight();
        scrollView.scrollTo(0, offset);
    }

    //更多
    @OnClick(R.id.water_more)
    public void onClickMore() {
        WaterPurifierShareMoreDialog.show(cx, guid);
    }

    @OnClick(R.id.date)
    public void onClickDate() {
        timeType = "day";
        Date d = new Date();
        startDate = TimeUtils.getDateBefore(d, 7);
        LogUtils.i("20180326", "startDate::" + startDate);
        endDate = TimeUtils.getNowTime(d);
        date.setBackgroundColor(Color.parseColor("#ff3e18"));
        week.setBackgroundColor(Color.parseColor("#64DBF7"));
        month.setBackgroundColor(Color.parseColor("#64DBF7"));
        StoreService.getInstance().getHistoryDrinking(guid, timeType, startDate, endDate, new Callback<Reponses.HistoryDrinkingResponse>() {
            @Override
            public void onSuccess(Reponses.HistoryDrinkingResponse rc) {
                if (rc != null) {
                    list.clear();
                    int i = 6;
                    for (DataInfo s : rc.item) {
                        LogUtils.i("s", "s:" + Integer.parseInt(s.volume));
                        if (s.time != null) {
                            dateml[i] = Integer.parseInt(s.volume);
                            time[i] = s.time.substring(8);
                            list.add(s);
                            i--;
                            if (i < 0) break;
                        }
                    }
                    if (memberCount == 0) {
                        memberCount = 3;
                    }
                    waterPurifiy_history_view.updateThisData(dateml, time);
                    waterPurifiy_history_view.updateLastData(time);
                    waterPurifiy_history_view.updateMax(memberCount, "day");
                    water_standard.setText(memberCount * 1500 + "ml");
                    adapter.notifyDataSetChanged();
                } else {
                    // Toast.makeText(cx, "数据请求失败", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(cx, "网络异常", Toast.LENGTH_LONG).show();
            }
        });

    }

    String[] timeDate1 = new String[7];

    @OnClick(R.id.week)
    public void onClickWeek() {
        final int[] datemlDate = new int[7];
        final String[] timeDate = new String[7];
        timeType = "week";
        Date d = new Date();
        startDate = TimeUtils.getDateBefore(d, 35);
        LogUtils.i("20180326", "startDate::" + startDate);
        endDate = TimeUtils.getNowTime(d);
        week.setBackgroundColor(Color.parseColor("#ff3e18"));
        date.setBackgroundColor(Color.parseColor("#64DBF7"));
        month.setBackgroundColor(Color.parseColor("#64DBF7"));
        StoreService.getInstance().getHistoryDrinking(guid, timeType, startDate, endDate, new Callback<Reponses.HistoryDrinkingResponse>() {
            @Override
            public void onSuccess(Reponses.HistoryDrinkingResponse rc) {
                if (rc != null) {
                    list.clear();
                    int i = 6;
                    for (DataInfo s : rc.item) {
                        if (s != null) {
                            datemlDate[i] = Integer.parseInt(s.volume);
                            timeDate[i] = s.time.substring(5);
                            list.add(s);
                            i--;
                            if (i < 0) break;
                        }
                    }
                    if (memberCount == 0) {
                        memberCount = 3;
                    }
                    waterPurifiy_history_view.updateThisData(datemlDate, timeDate);
                    waterPurifiy_history_view.updateLastData(timeDate);
                    waterPurifiy_history_view.updateMax(memberCount, "week");
                    water_standard.setText(memberCount * 1500 * 7 + "ml");


                    adapter.notifyDataSetChanged();
                } else {
                    // Toast.makeText(cx, "数据请求失败", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(cx, "网络异常", Toast.LENGTH_LONG).show();
            }
        });


    }

    @OnClick(R.id.month)
    public void onClickMonth() {
        Date d = new Date();
        startDate = TimeUtils.getDateBefore(d, 180);
        LogUtils.i("20180326", "startDate::" + startDate);
        endDate = TimeUtils.getNowTime(d);
        final int[] datemlMonth = new int[7];
        final String[] timeMonth = new String[7];
        timeType = "month";
        month.setBackgroundColor(Color.parseColor("#ff3e18"));
        date.setBackgroundColor(Color.parseColor("#64DBF7"));
        week.setBackgroundColor(Color.parseColor("#64DBF7"));
        StoreService.getInstance().getHistoryDrinking(guid, timeType, startDate, endDate, new Callback<Reponses.HistoryDrinkingResponse>() {
            @Override
            public void onSuccess(Reponses.HistoryDrinkingResponse rc) {
                if (rc != null) {
                    list.clear();
                    int i = 6;
                    for (DataInfo s : rc.item) {
                        if (s != null) {
                            datemlMonth[i] = Integer.parseInt(s.volume);
                            timeMonth[i] = s.time.substring(5);
                            list.add(s);
                            i--;
                            if (i < 0) break;
                        }

                    }
                    if (memberCount == 0) {
                        memberCount = 3;
                    }
                    waterPurifiy_history_view.updateThisData(datemlMonth, timeMonth);
                    waterPurifiy_history_view.updateLastData(timeMonth);
                    waterPurifiy_history_view.updateMax(memberCount, "month");
                    water_standard.setText(memberCount * 1500 * 30 + "ml");
                    adapter.notifyDataSetChanged();
                } else {
                    // Toast.makeText(cx, "数据请求失败", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(cx, "网络异常", Toast.LENGTH_LONG).show();
            }
        });

    }

    String nemberpeople;
    String[] nember = new String[]{"一", "二", "三", "四", "五", "六", "七", "八", "九", "十", "十一", "十二",
            "十三", "十四", "十五", "十六", "十七", "十八", "十九"};

    //设置家庭人数
    @OnClick(R.id.water_btn_people)
    public void onClickSet() {
        Helper.newWaterPurifierSetPeopleDialog(cx, purifier.getGuid().toString(), new Callback2<WaterPurifierSetPeople>() {
            @Override
            public void onCompleted(WaterPurifierSetPeople wpsp) {
                // Toast.makeText(cx,"fffffff",Toast.LENGTH_SHORT).show();
                WaterPurifierSetPeople msg = new WaterPurifierSetPeople(wpsp);
                flag1 = msg.getFlag();
                memberCount = Integer.parseInt(msg.getMemberCount());
                for (int i = 0; i < nember.length; i++) {
                    if (i == (memberCount - 1)) {
                        nemberpeople = nember[memberCount - 1];
                    }
                }
                LogUtils.i("aaa", "flag:" + wpsp.flag);
                if (flag1) {
                    // Toast.makeText(cx,"aa",Toast.LENGTH_LONG).show();
                    water_btn_people.setText(nemberpeople + "口之家");
                    watere_family_number.setText(nemberpeople + "口之家");
                    if (timeType.equals("day")) {
                        water_standard.setText(memberCount * 1500 + "ml");
                    } else if (timeType.equals("week")) {
                        water_standard.setText(memberCount * 1500 * 7 + "ml");
                    } else if (timeType.equals("month")) {
                        water_standard.setText(memberCount * 1500 * 30 + "ml");
                    }
                    waterPurifiy_history_view.updateMax(memberCount, timeType);
                }
            }
        });
    }

    //分享
    @OnClick(R.id.water_share)
    public void onClickShare() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int selfPermission = ContextCompat.checkSelfPermission(cx, Manifest.permission.READ_EXTERNAL_STORAGE);
            if (selfPermission == 0) {
                WaterPurifierShareDialog.show(cx, guid);
            } else {
                PermissionsUtils.checkPermission(cx, Manifest.permission.READ_EXTERNAL_STORAGE, PermissionsUtils.CODE_DEVICE_WATER_SHARE);
            }
        } else {
            WaterPurifierShareDialog.show(cx, guid);
        }

    }


    //查看所有数据
    @OnClick(R.id.waterpurifiy_drink_check)
    public void onClickCheck() {
        bundle.putString(PageArgumentKey.Guid, guid);
        bundle.putString(PageArgumentKey.timeType, timeType);
        UIService.getInstance().postPage(PageKey.DeviceWaterPurifierDetail, bundle);
    }

    @OnClick(R.id.img_retrun)
    public void onClickReturn() {
        UIService.getInstance().popBack();
    }


    //总的点击事件
    @OnClick(R.id.waterpurifiy_lvxin_use)
    public void onClickFilter() {


    }

    @OnClick(R.id.filter_pp)
    public void onClickPP() {
        onClickWaterpurifier(0);
    }

    @OnClick(R.id.filter_cto)
    public void onClickCto() {
        onClickWaterpurifier(1);
    }

    @OnClick(R.id.filter_ro1)
    public void onClickRo1() {
        onClickWaterpurifier(2);
    }

    @OnClick(R.id.filter_ro2)
    public void onClickRo2() {
        onClickWaterpurifier(3);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (PermissionsUtils.CODE_DEVICE_WATER_SHARE == requestCode) {
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    WaterPurifierShareDialog.show(cx, guid);
                }
            }
        }
    }


    private void onClickWaterpurifier(int id) {
        Bundle bundle = new Bundle();
        LogUtils.i("20180320", "isc::" + isCon);
        bundle.putBoolean(PageArgumentKey.isCon, isCon);
        if ("RJ312".equals(guid.substring(0, 5))) {
            bundle.putInt(PageArgumentKey.WaterFilterSurplus1, setFilterLife(purifier.filter_state_pp));
            bundle.putInt(PageArgumentKey.WaterFilterSurplus2, setFilterLife(purifier.filter_state_cto));
            bundle.putInt(PageArgumentKey.WaterFilterSurplus3, setFilterLife(purifier.filter_state_ro1));
            bundle.putInt(PageArgumentKey.WaterFilterSurplus4, setFilterLife(purifier.filter_state_ro2));
            bundle.putInt(PageArgumentKey.WaterFilterLeftTime_pp, setFilterLeft(purifier.filter_time_pp));
            bundle.putInt(PageArgumentKey.WaterFilterLeftTime_cto, setFilterLeft(purifier.filter_time_cto));
            bundle.putInt(PageArgumentKey.WaterFilterLeftTime_ro1, setFilterLeft(purifier.filter_time_ro1));
            bundle.putInt(PageArgumentKey.WaterFilterLeftTime_ro2, setFilterLeft(purifier.filter_time_ro2));
            bundle.putString(PageArgumentKey.Guid, guid.substring(0, 5));
            bundle.putInt("WaterFilterID", id);
            UIService.getInstance().postPage(PageKey.DeviceWaterPurifiyFilterShow, bundle);
        } else {
            bundle.putInt(PageArgumentKey.WaterFilterSurplus1, purifier.filter_state_pp);
            bundle.putInt(PageArgumentKey.WaterFilterSurplus2, purifier.filter_state_cto);
            bundle.putInt(PageArgumentKey.WaterFilterSurplus3, purifier.filter_state_ro1);
            bundle.putInt(PageArgumentKey.WaterFilterSurplus4, purifier.filter_state_ro2);
            bundle.putString(PageArgumentKey.Guid, guid.substring(0, 5));
            bundle.putInt("WaterFilterID", id);
            UIService.getInstance().postPage(PageKey.DeviceWaterPurifiyFilterShow, bundle);
        }
    }

    Boolean flag = true;

    @Subscribe
    public void onEvent(WaterPurifiyStatusChangedEvent event) {//轮训查询数据
        if (purifier == null || !Objects.equal(purifier.getID(), event.pojo.getID()))
            return;
        if (!checkConnection()) {
            setOffOrDisConnectModel();
            return;
        }

        LogUtils.i("aaaaa", "purifier.status:" + purifier.status);
        postEvent(new WaterPurifierEvent(purifier));

        switch (purifier.status) {

            case WaterPurifierStatus.Off:
                if ("RJ312".equals(guid.substring(0, 5))) {
                    setOpenModel();
                } else {
                    setOffModel();
                }
                break;
            case WaterPurifierStatus.On:
                setOpenModel();
                break;
            case WaterPurifierStatus.Wait:
                setOpenModel();
                break;
            case WaterPurifierStatus.Wash://冲洗
                //    LogUtils.i("20170703","Wash::"+WaterPurifierStatus.Wash);
                setWashModel();
                break;
            case WaterPurifierStatus.Purify://制水
                setCleanModel();
                break;
            case WaterPurifierStatus.AlarmStatus://报警
                setAlarmModel();
                break;
            default:
                break;
        }
        preStatus = purifier.status;
    }
//    static WaterPurifierAlarmDialog dlg;

    boolean isCon = false;

    @Subscribe
    public void onEvent(DeviceConnectionChangedEvent event) {//网络连接
        LogUtils.i("20180320", "isCon11::" + event.isConnected);
        LogUtils.i("20180320", "purifier::" + purifier.getID() + " ev:" + event.device.getID());
        if (purifier == null || !Objects.equal(purifier.getID(), event.device.getID()))
            return;
        if (!event.isConnected) {
            LogUtils.i("20180320", "isCon1::" + event.isConnected);
            isCon = event.isConnected;
            setOffOrDisConnectModel();
        } else {
            getFamilyPeople();//获取家庭人数
            getTodayDrink();//获取今日饮水量
            getHistoryDrink();//获取历史饮水量
            isCon = false;
        }
    }


    /**
     * 完成模式
     */
    private void setFinishModel(String text) {
        isCon = false;
        //开启滑动
        scrollViewEnable = true;
        if ("RJ312".equals(guid.substring(0, 5))) {
            if (setFilterLife(purifier.filter_state_pp) == 0 || setFilterLife(purifier.filter_state_cto) == 0
                    || setFilterLife(purifier.filter_state_ro1) == 0 || setFilterLife(purifier.filter_state_ro2) == 0) {
                filterTimeOut();
            } else {
                //关闭断网提示
                setBackground();
                finishUi();
                filterStatus();
                getTodayDrink();
            }
        } else {
            if (purifier.filter_state_pp == 255 || purifier.filter_state_cto == 255 || purifier.filter_state_ro1 == 255
                    || purifier.filter_state_ro2 == 255) {
                filterTimeOut();
            } else {
                //关闭断网提示
                setBackground();
                finishUi();
                filterStatus();
                getTodayDrink();
            }
        }
    }

    public void finishUi() {
        setFinishUi();
    }

    public void setFinishUi() {

    }


    /**
     * 制水模式
     */
    void setCleanModel() {
        //判断之前的状态是否是开机待机壶关机页面，若是将页面置顶
        if (preStatus == WaterPurifierStatus.Wait || preStatus == WaterPurifierStatus.Off
                || preStatus == WaterPurifierStatus.On || preStatus == WaterPurifierStatus.None)
            setScrollViewToTop();
        isCon = false;
        //开启滑动
        scrollViewEnable = true;
        if ("RJ312".equals(guid.substring(0, 5))) {
            if (setFilterLife(purifier.filter_state_pp) == 0 || setFilterLife(purifier.filter_state_cto) == 0
                    || setFilterLife(purifier.filter_state_ro1) == 0 || setFilterLife(purifier.filter_state_ro2) == 0) {
                filterTimeOut();
            } else {
                setBackground();
                cleanUi();
                filterStatus();
            }
        } else {
            if (purifier.filter_state_pp == 255 || purifier.filter_state_cto == 255 || purifier.filter_state_ro1 == 255
                    || purifier.filter_state_ro2 == 255) {
                filterTimeOut();
            } else {
                setBackground();
                cleanUi();
                filterStatus();
            }
        }


        //water_before.setText(setWaterQuality(purifier.input_tds));

    }

    public void cleanUi() {
        setCleanUI();
    }

    public void setCleanUI() {

    }


    /**
     * 冲洗模式
     */
    void setWashModel() {
        //判断之前的状态是否是开机待机壶关机页面，若是将页面置顶
        if (preStatus == WaterPurifierStatus.Wait || preStatus == WaterPurifierStatus.Off
                || preStatus == WaterPurifierStatus.On || preStatus == WaterPurifierStatus.None)
            setScrollViewToTop();
        water_disconnectHintView.setVisibility(View.GONE);//关闭断网提示;
        isCon = false;
        //开启滑动
        scrollViewEnable = true;
        if ("RJ312".equals(guid.substring(0, 5))) {
            if (setFilterLife(purifier.filter_state_pp) == 0 || setFilterLife(purifier.filter_state_cto) == 0
                    || setFilterLife(purifier.filter_state_ro1) == 0 || setFilterLife(purifier.filter_state_ro2) == 0) {
                filterTimeOut();
            } else {
                setBackground();
                washUI();
                filterStatus();
            }
        } else {
            if (purifier.filter_state_pp == 255 || purifier.filter_state_cto == 255 || purifier.filter_state_ro1 == 255
                    || purifier.filter_state_ro2 == 255) {
                filterTimeOut();
            } else {
                setBackground();
                washUI();
                filterStatus();
            }
        }

    }

    public void washUI() {
        setWashUI();
    }

    public void setWashUI() {

    }

    /**
     * 开机模式
     */
    void setOpenModel() {

        isCon = false;
        if (preStatus != WaterPurifierStatus.None && (purifier.preStatus == WaterPurifierStatus.Wash || purifier.preStatus == WaterPurifierStatus.Purify)) {
            if (preStatus == WaterPurifierStatus.Wash || purifier.status == WaterPurifierStatus.Wait) {
                setFinishModel("清洗");
            } else if (preStatus == WaterPurifierStatus.Purify || purifier.alarm == WaterPurifierAlarm.Water_None) {
                setFinishModel("制水");
            }
            return;
        }
        //开启滑动
        scrollViewEnable = true;
        if ("RJ312".equals(guid.substring(0, 5))) {
            if (setFilterLife(purifier.filter_state_pp) == 0 || setFilterLife(purifier.filter_state_cto) == 0
                    || setFilterLife(purifier.filter_state_ro1) == 0 || setFilterLife(purifier.filter_state_ro2) == 0) {
                filterTimeOut();
            } else {
                setBackground();
                //关闭断网提示
                openUI();
                filterStatus();
            }
        } else {
            if (purifier.filter_state_pp == 255 || purifier.filter_state_cto == 255 || purifier.filter_state_ro1 == 255
                    || purifier.filter_state_ro2 == 255) {
                filterTimeOut();
            } else {
                setBackground();
                //关闭断网提示
                openUI();
                filterStatus();
            }
        }

    }

    public void openUI() {
        setOpenUI();
    }

    public void setOpenUI() {

    }

    void setOffModel() {
        isCon = false;
        if (preStatus != WaterPurifierStatus.None && (purifier.preStatus == WaterPurifierStatus.Wash || purifier.preStatus == WaterPurifierStatus.Purify)) {
            if (preStatus == WaterPurifierStatus.Wash || purifier.status == WaterPurifierStatus.Wait) {
                setFinishModel("清洗");
            } else if (preStatus == WaterPurifierStatus.Purify || purifier.alarm == WaterPurifierAlarm.Water_None) {
                setFinishModel("制水");
            }
            return;
        }
        //开启滑动
        scrollViewEnable = true;
        if ("RJ312".equals(guid.substring(0, 5))) {
            if (setFilterLife(purifier.filter_state_pp) == 0 || setFilterLife(purifier.filter_state_cto) == 0
                    || setFilterLife(purifier.filter_state_ro1) == 0 || setFilterLife(purifier.filter_state_ro2) == 0) {
                filterTimeOut();
            } else {
                setBackground();
                //关闭断网提示
                offUI();
                filterStatus();
            }
        } else {
            if (purifier.filter_state_pp == 255 || purifier.filter_state_cto == 255 || purifier.filter_state_ro1 == 255
                    || purifier.filter_state_ro2 == 255) {
                filterTimeOut();
            } else {
                setBackground();
                //关闭断网提示
                offUI();
                filterStatus();
            }
        }

    }

    public void offUI() {
        setOffUi();
    }

    public void setOffUi() {

    }

    /**
     * 报警状态
     */
    private void setAlarmModel() {
        if (preStatus != WaterPurifierStatus.None && (purifier.preStatus == WaterPurifierStatus.Wash || purifier.preStatus == WaterPurifierStatus.Purify)) {
            if (preStatus == WaterPurifierStatus.Wash || purifier.status == WaterPurifierStatus.Wait) {
                setFinishModel("清洗");
            } else if (preStatus == WaterPurifierStatus.Purify || purifier.alarm == WaterPurifierAlarm.Water_None) {
                setFinishModel("制水");
            }
            return;
        }
        isCon = false;
        //开启滑动
        scrollViewEnable = true;
        if ("RJ312".equals(guid.substring(0, 5))) {
            if (setFilterLife(purifier.filter_state_pp) == 0 || setFilterLife(purifier.filter_state_cto) == 0
                    || setFilterLife(purifier.filter_state_ro1) == 0 || setFilterLife(purifier.filter_state_ro2) == 0) {
                filterTimeOut();
            } else {
                setBackground();
                //关闭断网提示
                AlarmUi();
                filterStatus();
            }
        } else {
            if (purifier.filter_state_pp == 255 || purifier.filter_state_cto == 255 || purifier.filter_state_ro1 == 255
                    || purifier.filter_state_ro2 == 255) {
                filterTimeOut();
            } else {
                setBackground();
                //关闭断网提示
                AlarmUi();
                filterStatus();
            }
        }
    }

    protected String alarmStr(short alarm) {
        String str = null;
        switch (alarm) {
            case WaterPurifierAlarm.Water_Leak:
                str = getString(R.string.water_conmon_loushui);
                break;
            case WaterPurifierAlarm.Water_Making:
                str = getString(R.string.water_common_all_water);
                break;
            case WaterPurifierAlarm.Water_None:
                str = getString(R.string.water_common_queshui);
                break;
            default:
                break;
        }
        return str;
    }

    public void AlarmUi() {
        setAlarmUi();
    }

    public void setAlarmUi() {

    }

    /**
     * 断网关机模式
     */
    void setOffOrDisConnectModel() {

    }


    /**
     * 判断净水器是否链接
     */
    boolean checkConnection() {
        if (!purifier.isConnected()) {
            ToastUtils.show("网络已断开", Toast.LENGTH_SHORT);
            return false;
        }
        return true;
    }


    /**
     * 根据传入的滤芯的状态，设置背景的颜色
     */
    public void setBackground() {

        /*if (setFilterLife(purifier.filter_state_pp) == 0 || setFilterLife(purifier.filter_state_cto) == 0 || setFilterLife(purifier.filter_state_ro1) == 0
                || setFilterLife(purifier.filter_state_ro2) == 0) {
            waterpurifier_background.setBackgroundColor(Color.parseColor("#D19990"));
            water_lvxin_use_time.setVisibility(View.VISIBLE);
            //filterDateStatus();

        } else {
            waterpurifier_background.setBackgroundColor(Color.parseColor("#5f97fa"));
            water_lvxin_use_time.setVisibility(View.GONE);
        }*/
    }

    /**
     * 滤芯过期的状态
     */

    public void filterTimeOut() {
        waterpurifier_background.setBackgroundColor(Color.parseColor("#787878"));
        water_disconnectHintView.setVisibility(View.GONE);
        waterpurifiy_lvxin_text.setVisibility(View.INVISIBLE);
        content_text_pre.setVisibility(View.GONE);
        water_drink_standard.setVisibility(View.INVISIBLE);
        content_text_level.setVisibility(View.GONE);
        water_cankao.setVisibility(View.GONE);
        water_lvxin_use_time.setVisibility(View.GONE);
        water_quality_ml.setVisibility(View.GONE);
        water_discont_text.setVisibility(View.VISIBLE);
        waterpurifier_pic_wash.setVisibility(View.GONE);
        if ("RJ312".equals(guid.substring(0, 5))) {
            water_discont_text.setText(R.string.water_discont_hint_new);
        } else {
            water_discont_text.setText(R.string.water_discont_hint);
        }
        water_before_pre.setVisibility(View.GONE);
        water_bubble.setVisibility(View.GONE);
        //water_before.setText("查看详情");
        water_before.setVisibility(View.GONE);
        tv_btn_detail_out.setVisibility(View.VISIBLE);
        water_jinghua_date.setVisibility(View.INVISIBLE);
        water_time_date.setVisibility(View.GONE);
        if (mWaveHelper.waveFlag) {
            mWaveHelper.cancel();
        }
        filterStatus();
    }

    @OnClick(R.id.tv_btn_detail_out)
    public void onClickDetail() {
        if ("RJ312".equals(guid.substring(0, 5))) {
            Bundle bundle = new Bundle();
            bundle.putInt(PageArgumentKey.WaterFilterSurplus1, setFilterLife(purifier.filter_state_pp));
            bundle.putInt(PageArgumentKey.WaterFilterSurplus2, setFilterLife(purifier.filter_state_cto));
            bundle.putInt(PageArgumentKey.WaterFilterSurplus3, setFilterLife(purifier.filter_state_ro1));
            bundle.putInt(PageArgumentKey.WaterFilterSurplus4, setFilterLife(purifier.filter_state_ro2));
            bundle.putString(PageArgumentKey.Guid, guid.substring(0, 5));
            UIService.getInstance().postPage(PageKey.DeviceWaterPurifiyFilterShow, bundle);
        } else {
            bundle.putInt(PageArgumentKey.WaterFilterSurplus1, purifier.filter_state_pp);
            bundle.putInt(PageArgumentKey.WaterFilterSurplus2, purifier.filter_state_cto);
            bundle.putInt(PageArgumentKey.WaterFilterSurplus3, purifier.filter_state_ro1);
            bundle.putInt(PageArgumentKey.WaterFilterSurplus4, purifier.filter_state_ro2);
            bundle.putString(PageArgumentKey.Guid, guid.substring(0, 5));
            UIService.getInstance().postPage(PageKey.DeviceWaterPurifiyFilterShow, bundle);
        }

    }

    /**
     * 获取今日饮水量
     */
    public void getTodayDrink() {
        LogUtils.i("20170113", purifier.getGuid().toString());
        // ToastUtils.show(guid, Toast.LENGTH_SHORT);
        StoreService.getInstance().getTodayDrinking(guid,
                "all", new Callback<Reponses.TodayDrinkingResponse>() {


                    @Override
                    public void onSuccess(Reponses.TodayDrinkingResponse rc) {
                        if (rc != null) {
                            LogUtils.i("2017gg", "rc " + rc.toString());
                        } else {
                            LogUtils.i("2017gg", "rc is null");
                        }
                        if (water_intake == null)
                            LogUtils.i("2017gg", " water_intake is null");
                        else
                            LogUtils.i("2017gg", " water_intake is not null");
                        try {
                            water_intake.setText(rc.item.get(0).volume);
                        } catch (Exception e) {
                            e.printStackTrace();
                            LogUtils.i("2017gg", e.getMessage() + "");
                        }

                    }

                    @Override
                    public void onFailure(Throwable t) {
                        Toast.makeText(cx, "网络异常", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    /**
     * 获取家庭人数
     */
    public void getFamilyPeople() {
        StoreService.getInstance().getFamilyMember(purifier.getGuid().toString(), new Callback<Reponses.GetFamilyResponse>() {
            @Override
            public void onSuccess(Reponses.GetFamilyResponse rc) {
                if (rc != null) {
                    try {

                        if (rc.memberCount == 0) {
                            memberCount = 3;
                        } else {
                            memberCount = rc.memberCount;
                        }

                        for (int i = 0; i < nember.length; i++) {
                            if (i == (memberCount - 1)) {
                                nemberpeople = nember[memberCount - 1];
                            }
                        }
                        water_btn_people.setText(nemberpeople + "口之家");
                        watere_family_number.setText(nemberpeople + "口之家");
                        watere_family_number.invalidate();
                        water_standard.setText((memberCount * 1500) + "ml");
                        waterPurifiy_history_view.updateMax(memberCount, timeType);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    String startDate;
    String endDate;

    /**
     * 获取历史饮水量
     */
    public void getHistoryDrink() {
        Date d = new Date();
        startDate = TimeUtils.getDateBefore(d, 7);
        LogUtils.i("20180326", "startDate::" + startDate);
        endDate = TimeUtils.getNowTime(d);
        LogUtils.i("20180326", "endDate::" + endDate);
        StoreService.getInstance().getHistoryDrinking(guid, timeType, startDate, endDate, new Callback<Reponses.HistoryDrinkingResponse>() {
            @Override
            public void onSuccess(Reponses.HistoryDrinkingResponse rc) {
                LogUtils.i("20180326", "rc:" + rc.toString());
                if (rc != null) {
                    list.clear();
                    //Toast.makeText(cx, "数据请求成功", Toast.LENGTH_LONG).show();
                    int i = 6;
                    for (DataInfo s : rc.item) {
                        LogUtils.i("20170719", "s:" + Integer.parseInt(s.volume));
                        if (s.time != null) {
                            dateml[i] = Integer.parseInt(s.volume);
                            //    LogUtils.i("20170719", "gfgfgg:" + s.time+" "+s.time.substring(8));
                            if ("day".equals(timeType)) {
                                time[i] = s.time.substring(8);
                            } else {
                                time[i] = s.time.substring(5);
                            }
                            list.add(s);
                            i--;
                            if (i < 0) break;
                        }
                    }
                    waterPurifiy_history_view.updateThisData(dateml, time);
                    waterPurifiy_history_view.updateLastData(time);
                    for (DataInfo s : list) {
                        LogUtils.i("qwe", "dateInfo" + s.time);
                    }
                    water_drink_list.setFocusable(false);
                    water_drink_list.setAdapter(adapter);
                    // setListViewHeightBasedOnChildren(water_drink_list);
                } else {
                    // ToastUtils.show("数据解析失败",Toast.LENGTH_SHORT);
                }

            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(cx, "网络异常", Toast.LENGTH_LONG).show();
            }
        });
    }


    /**
     * 设置滑动时的按钮的状态
     */
    public void setScrollViewStatus() {
        scrollView.setScrollViewListener(new WaterPurifierScrollView.ScrollViewListener() {//下滑按钮 淡入淡出
            @Override
            public void onScrollChanged(WaterPurifierScrollView scrollView, int x, int y, int oldx, int oldy) {
                Log.i("onScrollChanged", " x:" + x + " y:" + y + " oldx:" + oldx + " oldy" + oldy);
                if (y >= 0) {
                    if (y > 500) {
                        //bottom_mark.setVisibility(View.INVISIBLE);

                    } else if (y < 5) {
                        // bottom_mark.setVisibility(View.VISIBLE);
                    } else {
                        if (bottom_mark.getVisibility() == View.GONE)
                            bottom_mark.setVisibility(View.VISIBLE);
                        float h = (float) y / 500;
                        bottom_mark.setAlpha(1.0f - h);
                        Log.i("onScrollChanged", "h:" + h);
                    }

                }

            }
        });
    }


    public void filterDateStatus() {
        if (setFilterLife(purifier.filter_state_pp) == 0) {
            water_lvxin_time.setText(setFilterLeft(purifier.filter_time_pp) + "");
            water_lvxin_time.postInvalidate();
        } else if (setFilterLife(purifier.filter_state_cto) == 0) {
            water_lvxin_time.setText(setFilterLeft(purifier.filter_time_cto) + "");
            water_lvxin_time.postInvalidate();
        } else if (setFilterLife(purifier.filter_state_ro1) == 0) {
            water_lvxin_time.setText(setFilterLeft(purifier.filter_time_ro1) + "");
            water_lvxin_time.postInvalidate();
        } else if (setFilterLife(purifier.filter_state_ro2) == 0) {
            water_lvxin_time.setText(setFilterLeft(purifier.filter_time_ro2) + "");
       /* if (setFilterLeft(purifier.filter_time_ro2) == 0) {
            water_lvxin_time.setText("失效");
        }*/
            water_lvxin_time.postInvalidate();
        }
    }

    public void filterDate321Status() {
        if (setFilterLife(purifier.filter_state_pp) <= 10) {
            water_lvxin_time.setText(purifier.filter_state_pp + "%");
            water_lvxin_time.postInvalidate();
        } else if (setFilterLife(purifier.filter_state_cto) <= 10) {
            water_lvxin_time.setText(purifier.filter_state_cto + "%");
            water_lvxin_time.postInvalidate();
        } else if (setFilterLife(purifier.filter_state_ro1) <= 10) {
            water_lvxin_time.setText(purifier.filter_state_ro1 + "%");
            water_lvxin_time.postInvalidate();
        } else if (setFilterLife(purifier.filter_state_ro2) <= 10) {
            water_lvxin_time.setText(purifier.filter_state_ro2 + "%");
       /* if (setFilterLeft(purifier.filter_time_ro2) == 0) {
            water_lvxin_time.setText("失效");
        }*/
            water_lvxin_time.postInvalidate();
        }
    }


    /**
     * 滤芯的状态
     */
    Boolean flag1 = true;
    Boolean flag5 = true;
    Boolean flag2 = true;
    Boolean flag3 = true;
    Boolean flag4 = true;


    public void filterStatus() {

    }

    //断网时的滤芯状态
    protected void filterUnconnectStatus() {
        filter_pp_num.setText("--");
        filter_number_1.setBackgroundResource(R.drawable.shape_white_circular);
        filter_pp_num.setTextColor(Color.parseColor("#ffffff"));
        water_text_hao1.setTextColor(Color.parseColor("#ffffff"));
        filter_pp_pic.setImageResource(R.mipmap.img_waterpurifiy_filterright_white);
        filter_pp_sign.setVisibility(View.GONE);
        filter_cto_num.setText("--");
        filter_number_2.setBackgroundResource(R.drawable.shape_white_circular);
        filter_cto_num.setTextColor(Color.parseColor("#ffffff"));
        water_text_hao2.setTextColor(Color.parseColor("#ffffff"));
        filter_cto_pic.setImageResource(R.mipmap.img_waterpurifiy_filterright_white);
        filter_cto_sign.setVisibility(View.GONE);
        filter_ro1_num.setText("--");
        filter_number_3.setBackgroundResource(R.drawable.shape_white_circular);
        filter_ro1_num.setTextColor(Color.parseColor("#ffffff"));
        water_text_hao3.setTextColor(Color.parseColor("#ffffff"));
        filter_ro1_pic.setImageResource(R.mipmap.img_waterpurifiy_filterright_white);
        filter_ro1_sign.setVisibility(View.GONE);
        filter_ro2_num.setText("--");
        filter_number_4.setBackgroundResource(R.drawable.shape_white_circular);
        filter_ro2_num.setTextColor(Color.parseColor("#ffffff"));
        water_text_hao4.setTextColor(Color.parseColor("#ffffff"));
        filter_ro2_pic.setImageResource(R.mipmap.img_waterpurifiy_filterright_white);
        filter_ro2_sign.setVisibility(View.GONE);
    }

    protected String setWaterBeforeQuality(short tdsBefore, short tdsAfter) {
        short tdsTemp;
        if (tdsBefore != 0) {
            float precent = (float) (tdsBefore - tdsAfter) / tdsBefore;
            if (precent < 0.85) {
                tdsTemp = (short) (tdsAfter / (1 - precent));
                return waterQuality(tdsTemp);
            } else {
                return waterQuality(tdsBefore);
            }
        } else {
            return "优";
        }
    }

    protected String waterQuality(short tdswater) {
        short tds = tdswater;
        if (tds >= 0 && tds <= 80) {
            return "优";
        } else if (tds > 80 && tds <= 300) {
            return "良";
        } else if (tds > 300 && tds <= 600) {
            return "中";
        } else if (tds > 600 && tds <= 1000) {
            return "差";
        } else if (tds > 1000) {
            return "极差";
        } else {
            return null;
        }
    }


    protected String setTDS(short tds) {
        if (tds >= 0 && tds < 10) {
            return "00" + tds;
        } else if (tds < 100 && tds >= 10) {
            return "0" + tds;
        } else if (tds >= 100) {
            return tds + "";
        } else {
            return "-";
        }
    }

    /**
     * 根据参数TDS 算出水箱水质 优良中差
     *
     * @return
     */
    protected String setWaterQuality(short tds) {
        LogUtils.i("20170218", "tds:" + tds);
        if (true)
            return String.valueOf(tds);
        short num = tds;
        if (num > 300) {
            return "差";
        } else if (300 >= num && num >= 100) {
            return "中";
        } else if (100 > num && num >= 50) {
            return "良";
        } else if (num < 50) {
            return "优";
        } else {
            return "优";
        }
    }

    public String setCurrentWaterQuality(short tds) {
        short num = tds;
        if (num > 300) {
            return "差";
        } else if (300 >= num && num >= 100) {
            return "中";
        } else if (100 > num && num >= 50) {
            return "良";
        } else if (num < 50) {
            return "优";
        } else {
            return "优";
        }
    }


    public void getSystemTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日  HH:mm");
        Date curDate = new Date(System.currentTimeMillis());
        String curtime = formatter.format(curDate);
        water_time_date.setText(curtime);
    }

    /**
     * @param num 滤芯使用寿命含量（0—255）
     * @return
     */
    int setFilterLife(int num) {
        LogUtils.i("num", "num:" + num);
        if (num <= 0) {
            return 0;
        } else if (num >= 100) {
            return 100;
        }

        return num;
    }

    /**
     * 剩余天数
     *
     * @param time
     * @return
     */
    int setFilterLeft(int time) {
        if (time < 360) {
            return (time + 22) / 24;
        }
        return (time) / 24;
    }

    /**
     * 调整页面到最顶部
     */
    private void setScrollViewToTop() {
        if (scrollView.getScaleY() != 0)
            scrollView.scrollTo(0, 0);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public int px2dip(float pxValue) {
        final float scale = r.getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public int dip2px(float dpValue) {
        final float scale = r.getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    class ListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(cx, R.layout.item_water_drinking_list, null);
                holder = new ViewHolder();
                holder.date = convertView.findViewById(R.id.date);
                holder.water_drinking_number = convertView.findViewById(R.id.water_drinking_number);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.date.setText(list.get(position).time);
            holder.water_drinking_number.setText(list.get(position).volume);
            return convertView;
        }
    }

    static class ViewHolder {
        TextView date;
        TextView water_drinking_number;
    }

}
