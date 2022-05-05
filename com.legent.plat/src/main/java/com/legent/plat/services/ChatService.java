package com.legent.plat.services;

import android.util.Log;

import com.google.common.eventbus.Subscribe;
import com.legent.Callback;
import com.legent.Helper;
import com.legent.VoidCallback;
import com.legent.plat.Plat;
import com.legent.plat.events.ChatNewMsgEvent;
import com.legent.plat.events.ChatReceivedMsgEvent;
import com.legent.plat.events.UserLoginEvent;
import com.legent.plat.io.cloud.CloudHelper;
import com.legent.plat.io.cloud.Reponses;
import com.legent.plat.pojos.ChatMsg;
import com.legent.services.AbsService;
import com.legent.services.TaskService;
import com.legent.utils.EventUtils;
import com.legent.utils.api.AppUtils;
import com.legent.utils.api.PreferenceUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ChatService extends AbsService {

    final static String TAG = "Chat";
    final static String LAST_CHAT_TIME = "LastChatTime";
    final static int ReceviePeriod = 1000 * 3;
    final static int COUNT_PER_QUERY = 10;
    final static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

    static private ChatService instance = new ChatService();

    synchronized static public ChatService getInstance() {
        return instance;
    }

    long userId;
    Date lastTime;
    boolean running;

    private ChatService() {
        this.userId = Plat.accountService.getCurrentUserId();
    }

    @Subscribe
    public void onEvent(UserLoginEvent event) {
        this.userId = event.pojo.id;
        if (AppUtils.isDebug(cx)) {
            saveLastTime(null);
        }

        lastTime = getLastTime();
        if (lastTime == null) {
            lastTime = new Date(System.currentTimeMillis());
        }
    }

    public void sendMsg(final String content, final VoidCallback callback) {

        receviceMsg(new Callback<Boolean>() {

            @Override
            public void onSuccess(Boolean result) {

                if (result) {
                    CloudHelper.sendChatMsg(userId, content, new Callback<Reponses.ChatSendReponse>() {

                        @Override
                        public void onSuccess(Reponses.ChatSendReponse res) {
                            saveLastTime(res.time);
                            callback.onSuccess();
                        }

                        @Override
                        public void onFailure(Throwable e) {
                            callback.onFailure(e);
                        }
                    });
                } else {
                    callback.onSuccess();
                }
            }

            @Override
            public void onFailure(Throwable e) {
                callback.onFailure(e);
            }
        });

    }

    public void queryBefore(Date date, final Callback<List<ChatMsg>> callback) {

        // if (!Utils.checkNetAndAuth(callback))
        // return;

        CloudHelper.getChatBefore(userId, date, COUNT_PER_QUERY,
                new Callback<List<ChatMsg>>() {

                    @Override
                    public void onSuccess(List<ChatMsg> result) {
                        callback.onSuccess(result);
                    }

                    @Override
                    public void onFailure(Throwable e) {
                        callback.onFailure(e);
                    }
                });
    }

    public void updateLastTime(Date lastTime) {
        saveLastTime(lastTime);
    }

    public void startReceiveMsg() {
        running = true;
        TaskService.getInstance().postUiTask(receiveTask, 0);
    }

    public void stopReceiveMsg() {
        running = false;
        TaskService.getInstance().removeUiTask(receiveTask);
    }

    private Runnable receiveTask = new Runnable() {

        @Override
        public void run() {

            if (!running)
                return;
            try {
                receviceMsg(null);
            } catch (Exception ex) {
            } finally {
                TaskService.getInstance()
                        .postUiTask(receiveTask, ReceviePeriod);
            }
        }
    };

    public void checkNewMsg(final Callback callback) {
        lastTime = getLastTime();
        if (userId <= 0)
            return;
        if (lastTime != null) {
            final Date date = this.lastTime;
            CloudHelper.isExistChatMsg(userId, date, callback);
        }

    }

    private void receviceMsg(final Callback<Boolean> callback) {

        Date date = lastTime;
        CloudHelper.getChatAfter(userId, date, 100, new Callback<List<ChatMsg>>() {

            @Override
            public void onSuccess(List<ChatMsg> result) {

                EventUtils.postEvent(new ChatNewMsgEvent(false));

                if (result != null && result.size() > 0) {
                    saveLastTime(result.get(result.size() - 1).getDate());
                    EventUtils.postEvent(new ChatReceivedMsgEvent(result));
                }

                Helper.onSuccess(callback, true);
            }

            @Override
            public void onFailure(Throwable e) {
                Log.w(TAG, e.getMessage());
                Helper.onSuccess(callback, false);
            }
        });
    }

    private Date getLastTime() {

        long time = PreferenceUtils.getLong(getKey(), 0);
        if (time == 0) {
            return null;
        } else {
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(time);
            return c.getTime();
        }

    }

    private void saveLastTime(Date time) {
        saveLastTime(time != null ? time.getTime() : 0);
    }

    private void saveLastTime(long time) {
        lastTime = new Date(time);
        PreferenceUtils.setLong(getKey(), time);
    }

    private String getKey() {
        return String.format("%S_%S", LAST_CHAT_TIME, userId);
    }

}
