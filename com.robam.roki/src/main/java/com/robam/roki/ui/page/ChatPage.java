package com.robam.roki.ui.page;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.common.base.Function;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import com.google.common.eventbus.Subscribe;
import com.legent.Callback;
import com.legent.Helper;
import com.legent.VoidCallback;
import com.legent.plat.Plat;
import com.legent.plat.events.ChatReceivedMsgEvent;
import com.legent.plat.pojos.ChatMsg;
import com.legent.plat.pojos.User;
import com.legent.plat.services.ChatService;
import com.legent.ui.UIService;
import com.legent.ui.ext.BasePage;
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
import butterknife.OnClick;

/**
 * Created by sylar on 15/6/14.
 */
public class ChatPage extends BasePage {

    /* @InjectView(R.id.listview)
     ListView listview;*/
    @InjectView(R.id.edtSend)
    EditText edtSend;
    @InjectView(R.id.txtSend)
    TextView txtSend;

    Adapter adapter;
    ChatService cs = ChatService.getInstance();
    @InjectView(R.id.img_back)
    ImageView mImgBack;
    @InjectView(R.id.main)
    LinearLayout mMain;
    @InjectView(R.id.ll_bottom)
    LinearLayout mLlBottom;
    @InjectView(R.id.ll_title)
    LinearLayout mLlTitle;

    ListView listview;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        View view = inflater.inflate(R.layout.page_chat, container, false);
        ButterKnife.inject(this, view);
        listview = view.findViewById(R.id.listview);
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
//        addLayoutListener(mMain, mLlBottom, mLlTitle);
        return view;
    }

    private void addLayoutListener(final View main, final View llBottom, final View llTitle) {

        main.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect rectMain = new Rect();
                //1、获取main在窗体的可视区域
                main.getWindowVisibleDisplayFrame(rectMain);
                //2、获取main在窗体的不可视区域高度，在键盘没有弹起时，main.getRootView().getHeight()调节度应该和rect.bottom高度一样
                int mainInvisibleHeight = main.getRootView().getHeight() - rectMain.bottom;
                int screenHeight = main.getRootView().getHeight();

                //3、不可见区域大于屏幕本身高度的1/4：说明键盘弹起了
                if (mainInvisibleHeight > screenHeight / 4) {
                    int[] location = new int[2];
                    llBottom.getLocationInWindow(location);
                    // 4､获取llBottom的窗体坐标，算出main需要滚动的高度
                    int srollHeight = (location[1] + llBottom.getHeight()) - rectMain.bottom;
                    //5､让界面整体上移键盘的高度
                    main.scrollTo(0, srollHeight);

                } else {
                    //3、不可见区域小于屏幕高度1/4时,说明键盘隐藏了，把界面下移，移回到原有高度
                    main.scrollTo(0, 0);
                }

            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        cs.startReceiveMsg();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        cs.stopReceiveMsg();
    }

    @OnClick(R.id.txtSend)
    public void onClickSend() {
        try {
            sendMsg();
        } catch (Exception ex) {
            ToastUtils.showShort(ex.getMessage());
        }
    }

    @Subscribe
    public void onEvent(ChatReceivedMsgEvent event) {

        List<ChatMsg> subList = event.msgList;
        adapter.addMsgList(subList, null);
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

    @OnClick(R.id.img_back)
    public void onViewClicked() {

        UIService.getInstance().popBack();
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
            convertView = LayoutInflater.from(cx).inflate(isIncoming ? R.layout.view_chat_left
                    : R.layout.view_chat_right, parent, false);
            ViewHolder vh = new ViewHolder(convertView);
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
                    ImageUtils.displayImage(getActivity(), user.figureUrl, imgFigure,
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
