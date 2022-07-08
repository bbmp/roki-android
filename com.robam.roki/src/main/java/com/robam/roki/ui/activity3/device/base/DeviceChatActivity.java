package com.robam.roki.ui.activity3.device.base;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.common.base.Function;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import com.legent.Callback;
import com.legent.Helper;
import com.legent.VoidCallback;
import com.legent.plat.Plat;
import com.legent.plat.constant.IDeviceType;
import com.legent.plat.pojos.ChatMsg;
import com.legent.plat.pojos.User;
import com.legent.plat.pojos.device.AbsDevice;
import com.legent.plat.pojos.device.AbsDeviceHub;
import com.legent.plat.pojos.device.IDevice;
import com.legent.plat.services.ChatService;
import com.legent.ui.ext.adapters.ExtBaseAdapter;
import com.legent.utils.LogUtils;
import com.legent.utils.api.ToastUtils;
import com.legent.utils.graphic.ImageUtils;
import com.robam.roki.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * author : huxw
 * time   : 2022/06/23
 * desc   : 设备客服界面
 */
public  class DeviceChatActivity extends DeviceBaseFuntionActivity {


    private ListView listview;
    private LinearLayout llBottom;
    private EditText edtSend;
    private TextView txtSend;

    Adapter adapter;
    ChatService cs = ChatService.getInstance();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_device_chat;
    }

    @Override
    protected void initView() {
        listview = findViewById(R.id.listview);
        llBottom = findViewById(R.id.ll_bottom);
        edtSend = findViewById(R.id.edtSend);
        txtSend = findViewById(R.id.txtSend);
        txtSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMsg();
            }
        });
    }


    @Override
    protected void dealData() {
        setTitle("留言咨询");
        edtSend.clearFocus();
        adapter = new Adapter();
        listview.setAdapter(adapter);
        final Date now = Calendar.getInstance().getTime();
        queryHistory(now, new VoidCallback() {

            @Override
            public void onSuccess() {
                cs.updateLastTime(now);
            }

            @Override
            public void onFailure(Throwable t) {
                ToastUtils.showThrowable(t);
            }
        });
        cs.startReceiveMsg();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cs.stopReceiveMsg();
    }

    void queryHistory(Date date, final VoidCallback callback) {

        cs.queryBefore(date, new Callback<List<ChatMsg>>() {

            @Override
            public void onSuccess(List<ChatMsg> result) {
                if (result != null) {
                    LogUtils.i("20170626", "result::" + result.toString());
                    adapter.addMsgList(result,"history");
                }
                Helper.onSuccess(callback);
            }

            @Override
            public void onFailure(Throwable t) {
                Helper.onFailure(callback, t);
            }
        });
    }


    void sendMsg() {

        final String content = edtSend.getText().toString();
        if (Strings.isNullOrEmpty(content))
            return;

        cs.sendMsg(content, new VoidCallback() {

            @Override
            public void onSuccess() {
                ChatMsg msg = ChatMsg.newSendMsg(content);
                edtSend.setText(null);
                adapter.appendMsg(msg);
                LogUtils.i("20170623", "msg:" + msg);
            }

            @Override
            public void onFailure(Throwable t) {
                ToastUtils.showThrowable(t);
            }
        });

    }

    class Adapter extends ExtBaseAdapter<ChatMsg> {

        SimpleDateFormat SDF = new SimpleDateFormat("MM月dd日 HH:mm");
        long lastTimeInMillis = 0;
        long earlyTimeInMillis;
        Ordering<ChatMsg> ordering = Ordering.natural().nullsFirst()
                .onResultOf(new Function<ChatMsg, Long>() {
                    public Long apply(ChatMsg foo) {
                        return foo.postTime;
                    }
                });

        public void addMsgList(List<ChatMsg> newList, String history) {
            if (newList == null || newList.size() == 0)
                return;
            for (ChatMsg msg : newList) {

                if (!list.contains(msg)) {
                    LogUtils.i("20190218", "msg:" + msg.toString());
                    list.add(msg);
                }
            }
//            list = ordering.sortedCopy(list);
            if (!TextUtils.isEmpty(history)){
                Collections.reverse(list);
            }
            notifyDataSetChanged();
            if (list.size() > 0) {
                long last = list.get(list.size() - 1).getPostTime();
                long early = list.get(0).getPostTime();

                if (last > lastTimeInMillis) {
                    listview.setSelection(getCount() - 1);
                } else if (early < earlyTimeInMillis) {
                    listview.setSelection(0);
                }

                earlyTimeInMillis = early;
                lastTimeInMillis = last;
            }

        }


        public void appendMsg(ChatMsg msg) {
            List<ChatMsg> list = Lists.newArrayList();
            LogUtils.i("20170627", "msg:" + msg.toString());
            list.add(msg);
            addMsgList(list, null);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ChatMsg msg = list.get(position);
            LogUtils.i("20190216", "msg:" + msg + "  position:" + position);
            boolean isIncoming = msg.isIncoming();
            convertView = LayoutInflater.from(DeviceChatActivity.this).inflate(isIncoming ? R.layout.view_chat_left
                    : R.layout.view_chat_right, parent, false);
            Adapter.ViewHolder vh = new Adapter.ViewHolder(convertView);
            vh.showData(msg, position);
            return convertView;
        }

        class ViewHolder {
            final static int ChatTimeSpan = 1000 * 60 * 3;
            @InjectView(R.id.txtSendTime)
            TextView txtSendTime;
            @InjectView(R.id.imgFigure)
            ImageView imgFigure;
            @InjectView(R.id.txtUserName)
            TextView txtUserName;
            @InjectView(R.id.txtMsg)
            TextView txtMsg;

            ViewHolder(View view) {
                ButterKnife.inject(this, view);
                view.setTag(this);
            }

            void showData(ChatMsg msg, int position) {
                boolean isJust = isJustNow(position);
                txtSendTime.setText(SDF.format(new Date(msg.postTime)));
                txtSendTime.setVisibility(isJust ? View.GONE : View.VISIBLE);
                txtMsg.setText(msg.msg);
                if (!msg.isIncoming()) {
                    User user = Plat.accountService.getCurrentUser();
                    txtUserName.setText(user.name);
                    ImageUtils.displayImage(user.figureUrl, imgFigure,
                            com.robam.roki.ui.Helper.DisplayImageOptions_UserFace);
                }

            }

            boolean isJustNow(int position) {
                if (position <= 0 || position >= list.size())
                    return false;
                ChatMsg curMsg = list.get(position);
                ChatMsg prefMsg = list.get(position - 1);

                long span = curMsg.getPostTime() - prefMsg.getPostTime();
                return span < ChatTimeSpan;
            }

        }
    }

}