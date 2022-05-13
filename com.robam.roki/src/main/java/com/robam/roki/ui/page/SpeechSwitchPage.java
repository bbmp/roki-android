//package com.robam.roki.ui.page;
//
//import android.app.Activity;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.CompoundButton;
//
//import com.aispeech.dui.dds.DDS;
//import com.legent.ui.UIService;
//import com.legent.ui.ext.BasePage;
//import com.legent.ui.ext.views.CheckBoxView;
//import com.legent.utils.EventUtils;
//import com.legent.utils.LogUtils;
//import com.legent.utils.api.PreferenceUtils;
//import com.robam.common.events.ClearTextEvent;
//import com.robam.roki.R;
//import com.robam.roki.ui.PageKey;
//import com.robam.roki.utils.suspendedball.FloatingView;
//
//import butterknife.ButterKnife;
//import butterknife.InjectView;
//import butterknife.OnClick;
//
///**
// * 思必驰语音开关
// */
//
//public class SpeechSwitchPage extends BasePage {
//
//    @InjectView(R.id.chkIsInternalDays)
//    CheckBoxView mChkIsInternalDays;
//
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.page_speech_switch, container, false);
//        ButterKnife.inject(this, view);
//        if (PreferenceUtils.getBool("speech_switch", false)) {
//            mChkIsInternalDays.setChecked(true);
//            LogUtils.i("201910121","isCheck:::"+"true");
//        } else {
//            mChkIsInternalDays.setChecked(false);
//            LogUtils.i("201910121","isCheck:::"+"false");
//        }
//
//        mChkIsInternalDays.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//                    PreferenceUtils.setBool("speech_switch", true);
//                    FloatingView.get().getView().setVisibility(View.VISIBLE);
//                    try {
//                        EventUtils.postEvent(new ClearTextEvent(true));
//                        DDS.getInstance().doAuth();
//                        DDS.getInstance().getAgent().getWakeupEngine().enableWakeup();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                    FloatingView.get().attach((Activity) cx);
//                    //打开 悬浮球
//                    LogUtils.i("201910121",":::"+PreferenceUtils.getBool("speech_switch",true)+"");
//                } else {
//                    //关闭 悬浮球
//                    PreferenceUtils.setBool("speech_switch", false);
//                    FloatingView.get().getView().setVisibility(View.GONE);
//                    try {
//                        DDS.getInstance().getAgent().stopDialog();
//                        DDS.getInstance().getAgent().getWakeupEngine().disableWakeup();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//
//
//                }
//            }
//        });
//        return view;
//    }
//
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        ButterKnife.reset(this);
//    }
//
//    @OnClick({R.id.img_back, R.id.ll_information_use})
//    public void onViewClicked(View view) {
//        switch (view.getId()) {
//            case R.id.img_back:
//                UIService.getInstance().popBack();
//                break;
//            case R.id.ll_information_use:
//                UIService.getInstance().postPage(PageKey.Instructions);
//                break;
//
//        }
//
//    }
//
//
//}
