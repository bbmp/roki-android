package com.robam.roki.ui.dialog;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.legent.ui.UIService;
import com.legent.ui.ext.dialogs.AbsDialog;
import com.legent.utils.api.ViewUtils;
import com.robam.common.pojos.device.Stove.StoveStatus;
import com.robam.common.pojos.device.fan.AbsFan;
import com.robam.common.pojos.dictionary.StoveAlarm;
import com.robam.roki.R;

import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class            DeviceNoticDialog extends AbsDialog {
    static final public int Notic_Type_1_Voltage = 1;
    static final public int Notic_Type_2_RebootHint = 2;
    static final public int Notic_Type_3_WithoutPan = 3;
    static final public int Notic_Type_4_InnerError = 4;
    static final public int Notic_Type_5_CleanNotic = 5;
    static final public int Notic_Type_6_TimeOver_9B39 = 6;    // E0  定时结束
    static final public int Notic_Type_7_TimeOver_9B37 = 7;    // E0  定时结束
    static final public int Notic_Type_8_FireFailure = 8;      // E1  点火失败
    static final public int Notic_Type_9_FireOver_Unexpect = 9;         // E2 突然熄火
    static final public int Notic_Type_10_InnerError = 10;              //E3/E4/E5/E8
    static final public int Notic_Type_11_InnerError_9B37 = 11;              //E3/E4/E5/E8

    static final public int Notic_Type_15_FanCleanLock = 12;            //5610烟机的清晰锁定提示
    static final public int Notic_Type_16_FanPlateRemove =13;           //5610烟机的面板移除提醒


    static List<NoticNode> nodes = Lists.newArrayList();
    static Map<Integer, DeviceNoticDialog> dialogMap = Maps.newHashMap();

    static {
        nodes.add(new NoticNode(R.mipmap.ic_notic_tip1, "电压不稳定，请稍后使用", null,
                "关闭烟灶", 0));

        nodes.add(new NoticNode(R.mipmap.ic_notic_tip2, "我出了点小状况，请重启", null,
                "重启", 0));

        nodes.add(new NoticNode(R.mipmap.ic_notic_tip3, "请正确使用锅具",
                "温馨提示：请把锅放置在灶具上；或者尝试调整锅的位置；请使用电磁灶匹配锅", "知道了", 0));

        nodes.add(new NoticNode(R.mipmap.ic_notic_tip4, "灶具内部故障，请申请售后", null,
                "95105855", R.mipmap.ic_notic_phone));

        nodes.add(new NoticNode(R.mipmap.ic_notic_tip5, "烟机油网可以清洗一下了~", null,
                "知道了", 0));

        nodes.add(new NoticNode(R.mipmap.ic_notic_tip4, "9B39 E0  定时OK啦\n" +
                "美味出炉，嘻嘻\n", null,
                "知道了", 0));

        nodes.add(new NoticNode(R.mipmap.ic_notic_tip4, "9B37 E0  定时OK啦\n" +
                "美味出炉，嘻嘻\n" +
                "记得复位按钮哦.", null,
                "知道了", 0));

        nodes.add(new NoticNode(R.mipmap.ic_notic_tip4, "E1  点火失败,请确认气源，重新点火", null,
                "知道了", 0));
        nodes.add(new NoticNode(R.mipmap.ic_notic_tip4, "E2 突然熄火，请申请售后", null,
                "95105855", R.mipmap.ic_notic_phone));
        nodes.add(new NoticNode(R.mipmap.ic_notic_tip4, "//E3/E4/E5/E8灶具内部故障，请申请售后", null,
                "95105855", R.mipmap.ic_notic_phone));
        nodes.add(new NoticNode(R.mipmap.ic_notic_tip4, "//E3/E4/E5/E8灶具内部故障9B37，请申请售后", null,
                "95105855", R.mipmap.ic_notic_phone));
//        nodes.add(new NoticNode(R.mipmap.ic_notic_tip5, "5610烟机清洗锁定提示~", null,
//                "知道了", 0));
        nodes.add(new NoticNode(R.mipmap.ic_notic_tip5, "5610烟机挡风板被移除~", null,
                "知道了", 0));

    }

    static public void show(Activity activity, AbsFan fan, int noticType) {
        show(activity, fan, noticType, null);
    }

    static public void show(Activity activity, AbsFan fan, int noticType, Object tag) {
        if (!UIService.getInstance().isMainForm()) return;

        if (dialogMap.containsKey(noticType)) {
            DeviceNoticDialog dlg = dialogMap.get(noticType);
            if (dlg != null && dlg.isShowing())
                return;
        }

        DeviceNoticDialog dlg = new DeviceNoticDialog(activity, fan, noticType);
        dlg.tag = tag;
        dialogMap.put(noticType, dlg);
        dlg.show();
    }


    @InjectView(R.id.imgTip)
    ImageView imgTip;

    @InjectView(R.id.imgBtnIcon)
    ImageView imgBtnIcon;

    @InjectView(R.id.txtNotice)
    TextView txtNotice;

    @InjectView(R.id.txtDesc)
    TextView txtDesc;

    @InjectView(R.id.txtBtnText)
    TextView txtBtnText;

//    RokiDevice device;
    AbsFan fan;
    int noticType;
    Object tag;

    private DeviceNoticDialog(Activity activity, AbsFan fan, int noticType) {
        super(activity, R.style.Theme_Dialog_FullScreen);
        ViewUtils.setFullScreen(activity, this, Gravity.BOTTOM);

        this.fan = fan;
        this.noticType = noticType;
        NoticNode node = nodes.get(noticType - 1);
        imgTip.setImageResource(node.tipResid);
        imgBtnIcon.setImageResource(node.btnIconResid);
        txtNotice.setText(node.notic);
        txtDesc.setText(node.desc);
        txtBtnText.setText(node.btnText);

        txtDesc.setVisibility(Strings.isNullOrEmpty(node.desc) ? View.GONE
                : View.VISIBLE);
        imgBtnIcon.setVisibility(node.btnIconResid == 0 ? View.GONE
                : View.VISIBLE);


    }

    @Override
    protected int getViewResId() {
        return R.layout.dialog_device_notice;
    }

    @Override
    protected void initView(View view) {
        ButterKnife.inject(this, view);
    }

    @OnClick(R.id.pnlButton)
    public void onClick() {
        switch (noticType) {
            case Notic_Type_1_Voltage:
                onCloseStoveHead();
                break;
            case Notic_Type_2_RebootHint:
                onReboot();
                break;
            case Notic_Type_3_WithoutPan:
                onWithoutPan();
                break;
            case Notic_Type_4_InnerError:
                onCallSaler();
                break;
            case Notic_Type_5_CleanNotic:
                onRestFanCleanTime();
                break;
        }

        this.dismiss();
    }

    void onCloseStoveHead() {
        if (tag != null) {
            if (tag instanceof StoveAlarm) {
                StoveAlarm sa = (StoveAlarm) tag;
                if (sa.src != null) {
                    sa.src.parent.setStoveStatus(false, sa.src.ihId, StoveStatus.Off, null);
                }
            }
        }
    }

    void onReboot() {
        //TODO
    }

    void onWithoutPan() {
        onCloseStoveHead();
    }

    void onCallSaler() {
        Uri uri = Uri.parse(String.format("tel:%s", txtBtnText.getText()
                .toString()));
        Intent it = new Intent(Intent.ACTION_DIAL, uri);
        getContext().startActivity(it);
    }

    void onRestFanCleanTime() {
        fan.restFanCleanTime(null);
        fan.clean = false;
    }

    static class NoticNode {
        public int tipResid;
        public String notic;
        public String desc;
        public String btnText;
        public int btnIconResid;

        public NoticNode(int tipResid, String notic, String desc,
                         String btnText, int btnIconResid) {
            this.notic = notic;
            this.desc = desc;
            this.btnText = btnText;
            this.tipResid = tipResid;
            this.btnIconResid = btnIconResid;
        }
    }

}
