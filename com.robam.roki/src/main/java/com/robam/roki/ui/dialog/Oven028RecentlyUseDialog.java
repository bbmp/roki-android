package com.robam.roki.ui.dialog;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.eventbus.Subscribe;
import com.legent.dao.DaoHelper;
import com.legent.plat.Plat;
import com.legent.ui.ext.dialogs.AbsDialog;
import com.legent.utils.LogUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.events.OvenStatusChangedEvent;
import com.robam.common.pojos.OvenUserAction;
import com.robam.common.pojos.device.IRokiFamily;
import com.robam.common.pojos.device.Oven.AbsOven;
import com.robam.common.pojos.device.Oven.Oven028;
import com.robam.common.pojos.device.Oven.OvenMode;
import com.robam.roki.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by yinwei on 2017/8/19.
 */

public class Oven028RecentlyUseDialog extends AbsDialog {
    @InjectView(R.id.title)
    TextView title;
    @InjectView(R.id.listview)
    ListView listView;
    @InjectView(R.id.start_cook)
    TextView start_cook;

    public static Oven028RecentlyUseDialog dlg;
    Context cx;
    private View customView;
    AbsOven oven;
    int positionlocal = -1;
    List<OvenUserAction> list = new ArrayList<>();
    List<OvenUserAction> listNew = new ArrayList<>();
    long userid = Plat.accountService.getCurrentUserId();
    private int last_item;
    private View oldView = null;

    public Oven028RecentlyUseDialog(Context context, AbsOven oven) {
        super(context, R.style.Dialog_Micro_FullScreen);
        this.cx = context;
        if (IRokiFamily.RR075.equals(oven.getDt())) {
            this.oven = oven;
        } else {
            this.oven = oven;
        }

        try {
            this.listNew = DaoHelper.getWhereEq(OvenUserAction.class, "userid", userid);
            LogUtils.i("20170824", "userId::" + userid + "listNew::" + this.listNew.size());
            if (listNew.size() > 3) {
                for (int i = 0; i < listNew.size() - 3; i++) {
                    try {
                        DaoHelper.deleteWhereEq(OvenUserAction.class, "timeDate", listNew.get(i).getTimeDate());
                    } catch (Exception e) {
                        Log.e("Oven028recently", "error:" + e.getMessage());
                    }
                }
            }
            this.list = DaoHelper.getWhereEq(OvenUserAction.class, "userid", userid);
            Collections.reverse(this.list);
            init();
        } catch (Exception e) {
            Log.e("Oven028recently", "error:" + e.getMessage());
        }
    }

    @Override
    protected int getViewResId() {
        return R.layout.dialog_micro526_recetly_use;
    }

    @Override
    protected void initView(View view) {
        this.customView = view;
    }

    private void init() {
        ButterKnife.inject(this, customView);
        title.setText("最近使用");
        start_cook.setText("开始");
        listView.setAdapter(new OvenAdapter(cx, list));
        //点击事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                positionlocal = position;
                View itemView = view;
                itemView.setBackgroundColor(Color.parseColor("#252525"));
                LogUtils.i("20170828", "last_item::" + last_item);
                if (last_item != -1 && last_item != position) {
                    if (oldView == null) {
                        //为空不进行任何操作
                    } else {
                        LogUtils.i("20170828", "position::" + position);
                        oldView.setBackgroundColor(Color.parseColor("#00000000"));
                    }
                }
                oldView = itemView;
                last_item = position;
            }
        });
    }

    @Subscribe
    public void onEvent(OvenStatusChangedEvent event) {
        LogUtils.i("20171717", "event::" + event.pojo.getID());
    }

    //返回
    @OnClick(R.id.imgreturn)
    public void onClickBack() {
        if (dlg != null && dlg.isShowing())
            dlg.dismiss();
    }

    //开始烹饪
    @OnClick(R.id.start_cook)
    public void onClickStartCook() {
        if (list.size() == 0) {
            ToastUtils.show("无最近使用的参数", Toast.LENGTH_SHORT);
            return;
        } else {

            if (listener != null) {
                if (positionlocal == -1) {
                    ToastUtils.show("请选择下发的参数", Toast.LENGTH_SHORT);
                    return;
                } else {
                    listener.onConfirm(list.get(positionlocal));
                }
            }
        }

        if (dlg != null && dlg.isShowing())
            dlg.dismiss();
    }

    static public Oven028RecentlyUseDialog show(Context context, AbsOven oven) {
        dlg = new Oven028RecentlyUseDialog(context, oven);
        Window win = dlg.getWindow();
        WindowManager.LayoutParams wl = win.getAttributes();
        wl.width = WindowManager.LayoutParams.MATCH_PARENT;
        wl.height = WindowManager.LayoutParams.MATCH_PARENT;
        win.setAttributes(wl);
        dlg.show();
        return dlg;
    }

    public interface PickListener {

        void onCancel();

        void onConfirm(OvenUserAction ovenuserAction);
    }

    private Oven028RecentlyUseDialog.PickListener listener;

    public void setPickListener(Oven028RecentlyUseDialog.PickListener listener) {
        this.listener = listener;
    }

    private class OvenAdapter extends BaseAdapter {
        private Context mContext;
        private LayoutInflater inflater;
        private List<OvenUserAction> list = new ArrayList<OvenUserAction>();

        public OvenAdapter(Context mContext, List<OvenUserAction> list) {
            this.mContext = mContext;
            inflater = LayoutInflater.from(mContext);
            this.list = list;
        }

        @Override
        public int getCount() {
            return list == null ? 0 : list.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = inflater.inflate(R.layout.dialog_oven028_recently_use_item, null);
            TextView time = convertView.findViewById(R.id.txtTime);//时间
            ImageView imgMode = convertView.findViewById(R.id.imgMode);//图标
            TextView tempUp = convertView.findViewById(R.id.oven_recently_tempUp);//模式名称
            TextView temp = convertView.findViewById(R.id.oven_recently_temp);//火力和重量
            TextView tempDown = convertView.findViewById(R.id.oven_recently_tempDown);
            TextView timecook = convertView.findViewById(R.id.oven_recently_time);//单位
            String[] strTime = list.get(position).getTimeDate().split(",");
            time.setText(strTime[0] + "\n" + strTime[1]);
            imgMode.setImageResource(getModelText(position).res);
            if ("自动模式".equals(list.get(position).getName())) {
                tempUp.setVisibility(View.GONE);
                tempDown.setVisibility(View.GONE);
                temp.setVisibility(View.VISIBLE);
                temp.setText("默认");
            } else {
                if (!"EXP".equals(list.get(position).getName())) {
                    tempUp.setVisibility(View.GONE);
                    tempDown.setVisibility(View.GONE);
                    temp.setVisibility(View.VISIBLE);
                    temp.setText(list.get(position).getTemperature() + "℃");
                } else {
                    temp.setVisibility(View.GONE);
                    tempUp.setVisibility(View.VISIBLE);
                    tempDown.setVisibility(View.VISIBLE);
                    tempUp.setText(list.get(position).getTemperUp() + "℃");
                    tempDown.setText(list.get(position).getTemperDown() + "℃");
                }
            }

            timecook.setText(list.get(position).getTimeCook() + "");
            return convertView;
        }
    }


    /*public ModelInfo getModel075Text(int position){
        ModelInfo info = new ModelInfo();
        int res = 0;
        switch (list.get(position).getMode()){
            case OvenMode.KUAIRE://快热
                info.res = R.mipmap.ic_075_kuaire;
                break;
            case OvenMode.FENGBEIKAO://风焙烤
                info.res = R.mipmap.ic_075_fengbeikao;
                break;
            case OvenMode.BEIKAO://焙烤
                info.res = R.mipmap.ic_075_beikao;
                break;
            case OvenMode.DIJIARE://底加热
                info.res = R.mipmap.ic_075_dijiare;
                break;
            case OvenMode.FENGSHANKAO://风扇烤
                info.res = R.mipmap.ic_075_fengshankao;
                break;
            case OvenMode.KAOSHAO://烤烧
                info.res = R.mipmap.ic_075_kaosao;
                break;
            case OvenMode.QIANGKAOSHAO://强烤烧
                info.res = R.mipmap.ic_075_qiangkaosao;
                break;
            case OvenMode.EXP://EXP
                info.res = R.mipmap.ic_075_exp;
                break;
            default:
                break;
        }
        return info;
    }*/

    public ModelInfo getModelText(int position) {
        ModelInfo info = new ModelInfo();
        int res = 0;
        if ("自动模式".equals(list.get(position).getName())) {
            switch (list.get(position).getMode()) {
                case OvenMode.BEEF://牛排
                    info.res = R.mipmap.ic_026ovenmain_beef_white;
                    break;
                case OvenMode.BREAD://面包
                    info.res = R.mipmap.ic_026ovenmain_bread_white;
                    break;
                case OvenMode.BISCUITS://饼干
                    info.res = R.mipmap.ic_026ovenmain_biscuits_white;
                    break;
                case OvenMode.CHICKENWINGS://鸡翅
                    info.res = R.mipmap.ic_026ovenmain_chicken_white;
                    break;
                case OvenMode.CAKE://蛋糕
                    info.res = R.mipmap.ic_026ovenmain_cake_white;
                    break;
                case OvenMode.PIZZA://披萨
                    info.res = R.mipmap.ic_026ovenmain_pizza_white;
                    break;
                case OvenMode.GRILLEDSHRIMP://虾
                    info.res = R.mipmap.ic_026ovenmain_shrimp_white;
                    break;
                case OvenMode.ROASTFISH://烤鱼
                    info.res = R.mipmap.ic_026ovenmain_fish_white;
                    break;
                case OvenMode.SWEETPOTATO://红薯
                    info.res = R.mipmap.ic_026ovenmain_sweetpotato_white;
                    break;
                case OvenMode.CORN://玉米
                    info.res = R.mipmap.ic_026ovenmain_corn_white;
                    break;
                case OvenMode.STREAKYPORK://五花肉
                    info.res = R.mipmap.ic_026ovenmain_wuhuarou_white;
                    break;
                case OvenMode.VEGETABLES://蔬菜
                    info.res = R.mipmap.ic_026ovenmain_vegetables_white;
                    break;
                default:
                    break;
            }
        } else {
            switch (list.get(position).getMode()) {
                case OvenMode.KUAIRE://快热
                    info.res = R.mipmap.ic_oven028_recently_fasetheat_new;
                    break;
                case OvenMode.FENGBEIKAO://风焙烤
                    info.res = R.mipmap.ic_oven028_recently_fengbeikao_new;
                    break;
                case OvenMode.BEIKAO://焙烤
                    info.res = R.mipmap.ic_oven028_recently_beikao;
                    break;
                case OvenMode.DIJIARE://底加热
                    info.res = R.mipmap.ic_oven028_recently_bottomheat;
                    break;
                case OvenMode.JIEDONG://解冻
                    info.res = R.mipmap.ic_oven028_recently_unfreeze;
                    break;
                case OvenMode.FENGSHANKAO://风扇烤
                    info.res = R.mipmap.ic_oven028_recently_fengshankao;
                    break;
                case OvenMode.KAOSHAO://烤烧
                    info.res = R.mipmap.ic_oven028_recently_kaosao;
                    break;
                case OvenMode.QIANGKAOSHAO://强烤烧
                    info.res = R.mipmap.ic_oven028_recently_qingkaosao;
                    break;
                case OvenMode.EXP://EXP
                    info.res = R.mipmap.ic_oven028_recently_exp;
                    break;
                case OvenMode.KUAISUYURE://快速预热
                    info.res = R.mipmap.ic_oven028_recently_kuaisuyure;
                    break;
                case OvenMode.JIANKAO://煎烤
                    info.res = R.mipmap.ic_oven028_recently_jiankao;
                    break;
                case OvenMode.GUOSHUHONGGAN://果蔬烘干
                    info.res = R.mipmap.ic_oven028_recently_guoshufry;
                    break;
                case OvenMode.FAJIAO://发酵
                    info.res = R.mipmap.ic_oven028_recently_fa;
                    break;
                case OvenMode.SHAJUN://杀菌
                    info.res = R.mipmap.ic_oven028_recently_shajun;
                    break;
                case OvenMode.BAOWEN:
                    info.res = R.mipmap.img_oven028_baowen_mode;
                    break;
                default:
                    break;
            }
        }

        return info;
    }

    class ModelInfo {
        int res;
    }

}
