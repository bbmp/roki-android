package com.robam.roki.ui.page.device.water;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.legent.Callback;
import com.legent.VoidCallback;
import com.legent.ui.UIService;
import com.legent.ui.ext.BasePage;
import com.legent.utils.LogUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.io.cloud.Reponses;
import com.robam.common.pojos.DataInfo;
import com.robam.common.services.StoreService;
import com.robam.roki.R;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.listener.OnItemSelectedListenerCenter;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;
import com.robam.roki.ui.view.WaterPurifiyHistogramModelView;
import com.robam.roki.utils.DialogUtil;
import com.robam.roki.utils.RemoveManOrsymbolUtil;
import com.robam.roki.utils.StringConstantsUtil;
import com.robam.roki.utils.TimeUtils;
import com.robam.roki.utils.WaterPurifierUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by 14807 on 2018/10/30.
 * 净水机，家庭饮水统计页面
 */
public class HouseholdDrinkingWaterStatisticsPage extends BasePage {

    @InjectView(R.id.iv_back)
    ImageView mIvBack;
    @InjectView(R.id.tv_water_num_home)
    TextView mTvWaterNumHome;
    @InjectView(R.id.tv_water_ml)
    TextView mTvWaterMl;
    @InjectView(R.id.tv_water_dec)
    TextView mTvWaterDec;
    @InjectView(R.id.waterPurifiy_history_view)
    WaterPurifiyHistogramModelView mWaterPurifiyHistoryView;
    @InjectView(R.id.tv_water_standard)
    TextView mWaterStandard;
    @InjectView(R.id.tv_water_intake)
    TextView mTvWaterIntake;
    @InjectView(R.id.iv_more_water_data)
    ImageView mIvMoreWaterData;
    private String mGuid;
    private final int NUMBER_PEOPLE = 1;
    private IRokiDialog mNumberPeopleDialog;
    private String timeType = "day";
    int[] dateml = new int[7];
    String[] time = new String[7];
    ArrayList<DataInfo> list = new ArrayList<DataInfo>();

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case NUMBER_PEOPLE:
                    setNumberPeople((String) msg.obj);
                    break;
            }
        }
    };


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Bundle bd = getArguments();
        mGuid = bd == null ? null : bd.getString(PageArgumentKey.Guid);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.page_household_drinking_water_statistcs, container, false);
        ButterKnife.inject(this, view);
        readFamilyMember();
        getTodayDrink();//获取今日饮水量
        getHistoryDrink();//获取历史饮水量
        return view;
    }


    private void setNumberPeople(String data) {
        if (TextUtils.isEmpty(data)) return;
        String removeResult = null;
        if (data.contains(StringConstantsUtil.STRING_PERSON)) {
            removeResult = RemoveManOrsymbolUtil.getRemoveString(data);
        }

        final String finalRemoveResult = removeResult;
        mNumberPeopleDialog.setOkBtn(R.string.ok_btn, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mNumberPeopleDialog != null && mNumberPeopleDialog.isShow()) {
                    mNumberPeopleDialog.dismiss();
                }
                StoreService.getInstance().setFamilyMember(finalRemoveResult, mGuid, new VoidCallback() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(cx, "设置成功", Toast.LENGTH_SHORT).show();
                        readFamilyMember();
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        ToastUtils.showThrowable(t);
                    }
                });


            }
        });

        mNumberPeopleDialog.setCancelBtn(R.string.can_btn, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mNumberPeopleDialog != null && mNumberPeopleDialog.isShow()) {
                    mNumberPeopleDialog.dismiss();
                }
            }
        });
    }

    private void readFamilyMember() {
        StoreService.getInstance().getFamilyMember(mGuid, new Callback<Reponses.GetFamilyResponse>() {
            @Override
            public void onSuccess(Reponses.GetFamilyResponse getFamilyResponse) {
                if (getFamilyResponse==null) {
                    return;
                }
                if (getFamilyResponse.memberCount!=0) {
                    int memberCount = getFamilyResponse.memberCount;
                    String resultCount = WaterPurifierUtils.getMemberCount(memberCount);
                    mTvWaterNumHome.setText(resultCount);
                    mWaterStandard.setText((memberCount * 1500) + "ml");
                    mTvWaterMl.setText((memberCount * 1500) + "ml");
                    mWaterPurifiyHistoryView.updateMax(memberCount, timeType);
                }

            }

            @Override
            public void onFailure(Throwable t) {
                ToastUtils.showShort(R.string.device_without_setting_failed);
            }
        });
    }

    /**
     * 获取今日饮水量
     */
    public void getTodayDrink() {
        StoreService.getInstance().getTodayDrinking(mGuid,
                "all", new Callback<Reponses.TodayDrinkingResponse>() {
                    @Override
                    public void onSuccess(Reponses.TodayDrinkingResponse rc) {
                        if (rc != null) {
                            LogUtils.i("2017gg", "rc " + rc.toString());
                        } else {
                            LogUtils.i("2017gg", "rc is null");
                        }
                        try {
                            mTvWaterIntake.setText(rc.item.get(0).volume);
                        } catch (Exception e) {
                            e.printStackTrace();
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
     * 获取历史饮水量
     */
    public void getHistoryDrink() {
        Date d = new Date();
        String startDate = TimeUtils.getDateBefore(d, 8);
        String endDate = TimeUtils.getNowTime(d);
        StoreService.getInstance().getHistoryDrinking(mGuid, timeType, startDate, endDate, new Callback<Reponses.HistoryDrinkingResponse>() {
            @Override
            public void onSuccess(Reponses.HistoryDrinkingResponse rc) {
                if (rc != null) {
                    list.clear();
                    int i = 6;
                    ArrayList<DataInfo> item = rc.item;
                    if (item == null || item.size() == 0) return;
                    List<DataInfo> dataInfos = item.subList(1, item.size());//当天的数据不做展示
                    for (DataInfo s : dataInfos) {
                        if (s.time != null) {
                            Integer volume = Integer.parseInt(s.volume);
                            LogUtils.i("20191024","volume:" + volume);
                            dateml[i] = volume;
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
                    mWaterPurifiyHistoryView.updateThisData(dateml, time);
                    mWaterPurifiyHistoryView.updateLastData(time);
                }
            }

            @Override
            public void onFailure(Throwable t) {
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnClick(R.id.iv_back)
    public void onMIvBackClicked() {
        UIService.getInstance().popBack();
    }

    @OnClick(R.id.tv_water_num_home)
    public void onMTvWaterNumHomeClicked() {

        mNumberPeopleDialog = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_03);
        mNumberPeopleDialog.setWheelViewData(null, getListNumberPeople(), null, false, 0, 3, 0, null, new OnItemSelectedListenerCenter() {
            @Override
            public void onItemSelectedCenter(String contentCenter) {
                Message message = mHandler.obtainMessage();
                message.what = NUMBER_PEOPLE;
                message.obj = contentCenter;
                mHandler.sendMessage(message);
            }
        }, null);
        mNumberPeopleDialog.show();

    }

    private List<String> getListNumberPeople() {

        List<String> list = new ArrayList<>();
        for (int i = 1; i <= 7; i++) {
            list.add(i + StringConstantsUtil.STRING_PERSON);
        }
        return list;
    }

    @OnClick(R.id.iv_more_water_data)
    public void onMIvMoreWaterDataClicked() {
        Bundle moreBundle = new Bundle();
        moreBundle.putString(PageArgumentKey.Guid, mGuid);
        UIService.getInstance().postPage(PageKey.DeviceWaterPurifierDetail, moreBundle);
    }

}
