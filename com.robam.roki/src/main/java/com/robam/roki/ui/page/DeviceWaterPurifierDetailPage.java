package com.robam.roki.ui.page;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.legent.Callback;
import com.legent.ui.UIService;
import com.legent.ui.ext.BasePage;
import com.legent.utils.LogUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.io.cloud.Reponses;
import com.robam.common.pojos.DataInfo;
import com.robam.common.pojos.SeriesInfo;
import com.robam.common.services.StoreService;
import com.robam.roki.R;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.UIListeners;
import com.robam.roki.utils.TimeUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by yinwei on 2016/12/29.
 */

public class DeviceWaterPurifierDetailPage extends BasePage implements UIListeners.IRefresh {

    //listview控件的对象
    @InjectView(R.id.water_drinking_list)
    PullToRefreshListView water_drinking_list;
    @InjectView(R.id.waterpurifiy_lvxin_return)
    ImageView waterpurifiy_lvxin_return;//返回
    @InjectView(R.id.tv_day)
    TextView mTvDay;
    @InjectView(R.id.tv_month)
    TextView mTvMonth;
    private View contentView;

    private String guid;
    private String timeType = "day";
    private ArrayList<DataInfo> list = new ArrayList<DataInfo>();
    int i = 20;
    ListAdapter mAdapter;
    String startDate;
    String endDate;
    Date d;
    Calendar calendar = Calendar.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bd = getArguments();



        guid = bd.getString(PageArgumentKey.Guid);
        contentView = inflater.inflate(R.layout.page_waterpurifier_drinking_detail,
                container, false);
        ButterKnife.inject(this, contentView);
        mTvMonth.setTextColor(getResources().getColor(R.color.c41));
        d = new Date();
        ListView tem = water_drinking_list.getRefreshableView();
        registerForContextMenu(tem);
        water_drinking_list.setMode(PullToRefreshBase.Mode.BOTH);
        mAdapter = new ListAdapter();
        tem.setAdapter(mAdapter);
        water_drinking_list.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
                getDate();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
                getDate();
            }
        });
        initData();
        return contentView;
    }

    public void initData() {
        if ("month".equals(timeType)) {
            i = 600;
        } else if ("day".equals(timeType)) {
            i = 20;
        }
        getDate();
    }

    @OnClick(R.id.waterpurifiy_lvxin_return)
    public void onClickReturn() {
        UIService.getInstance().popBack();
    }

    @Override
    public void onRefresh() {

    }

    String dateMo = "2017-01-01";
    boolean falg;

    public void getDate() {

        startDate = TimeUtils.getDateBefore(d, i);
        endDate = TimeUtils.getNowTime(d);
        falg = TimeUtils.getTimeCompare(startDate, dateMo);
        if (falg) {
            startDate = dateMo;
        }
        StoreService.getInstance().getHistoryDrinking(guid, timeType, startDate, endDate, new Callback<Reponses.HistoryDrinkingResponse>() {
            @Override
            public void onSuccess(Reponses.HistoryDrinkingResponse rc) {
                if (list != null && list.size() > 0) {
                    Iterator<DataInfo> iterator = list.iterator();
                    while (iterator.hasNext()) {
                        iterator.next();
                        iterator.remove();
                    }
                }
                if ("month".equals(timeType)) {
                    i += 600;
                } else if ("day".equals(timeType)) {
                    i += 20;
                }
                endDate = startDate;
                if (rc != null && rc.item != null) {
                    for (int j = 0; j < rc.item.size(); j++) {
                        String time = rc.item.get(j).time;
                        if (timeType.equals("day")){
                            String calendar = getIntradayCalendar();
                            if (!calendar.equals(time)){
                                list.add(rc.item.get(j));
                            }
                        }else {
                            String month = time.substring(5, 7);
                            String intradayMonth = getIntradayMonth();
                            if (!intradayMonth.equals(month)){
                                list.add(rc.item.get(j));
                            }
                        }
                    }
                    mAdapter.notifyDataSetChanged();
                    water_drinking_list.onRefreshComplete();
                    if (falg) {
                        ToastUtils.show("无更多数据了", Toast.LENGTH_SHORT);
                    } else {
                        Toast.makeText(cx, "刷新成功", Toast.LENGTH_LONG).show();
                    }
                } else {
                    water_drinking_list.onRefreshComplete();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(cx, "刷新失败", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnClick(R.id.tv_day)
    public void onMTvDayClicked() {
        if (list != null && list.size() > 0) {
            Iterator<DataInfo> iterator = list.iterator();
            while (iterator.hasNext()) {
                iterator.next();
                iterator.remove();
            }
        }
        timeType = "day";
        mTvMonth.setTextColor(getResources().getColor(R.color.c41));
        mTvMonth.setBackground(getResources().getDrawable(R.drawable.shape_water_more_data_month_white_bg));
        mTvDay.setBackground(getResources().getDrawable(R.drawable.shape_water_more_data_day_bule_bg));
        mTvDay.setTextColor(getResources().getColor(R.color.white));
        initData();
    }

    @OnClick(R.id.tv_month)
    public void onMTvMonthClicked() {
        if (list != null && list.size() > 0) {
            Iterator<DataInfo> iterator = list.iterator();
            while (iterator.hasNext()) {
                iterator.next();
                iterator.remove();
            }
        }
        timeType = "month";
        mTvMonth.setTextColor(getResources().getColor(R.color.white));
        mTvMonth.setBackground(getResources().getDrawable(R.drawable.shape_water_more_data_month_bg));
        mTvDay.setBackground(getResources().getDrawable(R.drawable.shape_water_more_data_day_white_bg));
        mTvDay.setTextColor(getResources().getColor(R.color.c41));
        initData();
    }

    public String getIntradayCalendar() {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        LogUtils.i("20190308", "year:" + year + " month:" + month + " day:" + day);
        String yearStr = null;
        String monthStr = null;
        String dayStr = null;
        yearStr = ""+year;
        if (month <= 9) {
            monthStr = "0" + month;
        } else {
            monthStr = "" + month;
        }
        if (day <= 9) {
            dayStr = "0" + day;
        }else {
            dayStr = "" + day;
        }
        return yearStr + "-" + monthStr + "-" + dayStr;
    }

    public String getIntradayMonth() {
        int month = calendar.get(Calendar.MONTH) + 1;
        String monthStr = null;
        if (month <= 9) {
            monthStr = "0" + month;
        } else {
            monthStr = "" + month;
        }

        return monthStr;
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
            holder.water_drinking_number.setText(list.get(position).volume + "ml");
            return convertView;
        }
    }

    static class ViewHolder {
        TextView date;
        TextView water_drinking_number;
    }
}
