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

import com.legent.dao.DaoHelper;
import com.legent.plat.Plat;
import com.legent.ui.ext.dialogs.AbsDialog;
import com.legent.utils.api.ToastUtils;
import com.robam.common.pojos.UserAction;
import com.robam.common.pojos.device.microwave.AbsMicroWave;
import com.robam.common.pojos.device.microwave.MicroWaveM526;
import com.robam.common.pojos.device.microwave.MicroWaveModel;
import com.robam.roki.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/4/21.
 */

public class Microwave526RecentlyUseDialog extends AbsDialog {
    @InjectView(R.id.title)
    TextView title;
    @InjectView(R.id.listview)
    ListView listView;
    public static Microwave526RecentlyUseDialog dlg;

    Context cx;
    private View customView;
   // int  positionlocal=1;
    int  positionlocal=-1;
    List<UserAction> list=new ArrayList<>();
    List<UserAction> listNew=new ArrayList<>();
    long userid= Plat.accountService.getCurrentUserId();
    MicroWaveM526 microWave;
    public Microwave526RecentlyUseDialog(Context context,AbsMicroWave microWave) {
        super(context, R.style.Dialog_Micro_FullScreen);
        this.cx=context;
        this.microWave= (MicroWaveM526) microWave;
        try{
            this.listNew = DaoHelper.getWhereEq(UserAction.class,"userId",userid);
            if (listNew.size()>3){
                for (int i=0;i<listNew.size()-3;i++){
                    DaoHelper.deleteWhereEq(UserAction.class,"timeDate",listNew.get(i).getTimeDate());
                }
            }
            this.list= DaoHelper.getWhereEq(UserAction.class,"userId",userid);
            Collections.reverse(this.list);
            init();
        }catch (Exception e){
            Log.e("526Recently","error:"+e.getMessage());
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
    private int last_item;
    private View oldView=null;
    private void init() {
        ButterKnife.inject(this, customView);
        title.setText("最近使用");
        listView.setAdapter(new MicroAdapter(cx, list));
        //默认选中第二条数据
       /* listView.post(new Runnable() {
            @Override
            public void run() {
                if (listView.getChildCount()>=2){
                    listView.getChildAt(1).setBackgroundColor(Color.parseColor("#000000"));
                }
            }
        });*/
        //点击事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                positionlocal=position;
               /* if(position==1){
                    //当点击默认的时候不改变默认的背景颜色否侧改变默认的颜色
                }else{
                    if(parent.getChildAt(1)!=null){
                        parent.getChildAt(1).setBackgroundColor(Color.parseColor("#00000000"));
                    }
                }*/
                View itemView=view;
                itemView.setBackgroundColor(Color.parseColor("#000000"));
                if (last_item!=-1&&last_item!=position){
                    if (oldView==null){
                        //为空不进行任何操作
                    }else{
                        oldView.setBackgroundColor(Color.parseColor("#00000000"));
                    }
                }
                oldView=itemView;
                last_item=position;
            }
        });
    }

    //返回
    @OnClick(R.id.imgreturn)
    public void onClickBack(){
        if (dlg != null && dlg.isShowing())
            dlg.dismiss();
    }

    //开始烹饪
    @OnClick(R.id.start_cook)
    public void onClickStartCook(){
        if (list.size()==0){
            ToastUtils.show("无最近使用的参数",Toast.LENGTH_SHORT);
            return;
        }else{
           /* if (listener != null){
                if (list.size()>0){
                    if (list.size()==1){
                        if (positionlocal==0){
                            listener.onConfirm(list.get(positionlocal));
                        }else{
                            ToastUtils.show("请选择下发指令", Toast.LENGTH_SHORT);
                            return;
                        }
                    }else{
                        listener.onConfirm(list.get(positionlocal));
                    }
                }
            } */
            if (listener!=null){
                if (positionlocal==-1){
                    ToastUtils.show("请选择下发的参数",Toast.LENGTH_SHORT);
                    return;
                }else{
                    listener.onConfirm(list.get(positionlocal));
                }
            }
        }

        if (dlg != null && dlg.isShowing())
            dlg.dismiss();
    }

    static public Microwave526RecentlyUseDialog show(Context context, AbsMicroWave microWave) {
        dlg = new Microwave526RecentlyUseDialog(context,microWave);
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

        void onConfirm(UserAction userAction);
    }

    private Microwave526RecentlyUseDialog.PickListener listener;

    public void setPickListener(Microwave526RecentlyUseDialog.PickListener listener) {
        this.listener = listener;
    }

    //适配器
    private class MicroAdapter extends BaseAdapter{
        private Context mContext;
        private LayoutInflater inflater;
        private List<UserAction> list=new ArrayList<UserAction>();
        public  MicroAdapter(Context mContext, List<UserAction> list){
            this.mContext=mContext;
            inflater=LayoutInflater.from(mContext);
            this.list=list;
        }

        @Override
        public int getCount() {
            return list==null ? 0 : list.size();
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
            convertView = inflater.inflate(R.layout.dialog_micro526_recently_use_item, null);
            TextView time = convertView.findViewById(R.id.txtTime);
            ImageView imgMode = convertView.findViewById(R.id.imgMode);
            TextView modeName = convertView.findViewById(R.id.modeName);
            TextView fireAndweight = convertView.findViewById(R.id.fireAndWeight);
            TextView unit= convertView.findViewById(R.id.fireAndke);
            String[] strTime=list.get(position).getTimeDate().split(",");
            time.setText(strTime[0]+"\n"+strTime[1]);

            imgMode.setImageResource(getModelText(position).res);
            if (list.get(position).getMode() == 50 || list.get(position).getMode() == 51 || list.get(position).getMode() == 52) {
                if (list.get(position).getMode() == MicroWaveModel.Barbecue) {
                    switch (list.get(position).fire) {
                        case 7:
                            modeName.setText(6+"火力");
                            break;
                        case 8:
                            modeName.setText(4+"火力");
                            break;
                        case 9:
                            modeName.setText(2+"火力");
                            break;
                        default:
                            break;
                    }
                }else if (list.get(position).getMode() == MicroWaveModel.ComibineHeating){
                    switch (list.get(position).fire){
                        case 10:
                            modeName.setText(6+"火力");break;
                        case 11:
                            modeName.setText(4+"火力");break;
                        case 12:
                            modeName.setText(2+"火力");break;
                        default:break;
                    }
                }else{
                    modeName.setText(list.get(position).getFire()+"火力");
                }

                if (list.get(position).getTimeCook()/60<10){
                    if (list.get(position).getTimeCook()%60<10){
                        fireAndweight.setText("0"+list.get(position).getTimeCook()/60+":0"+list.get(position).getTimeCook()%60);
                    }else{
                        fireAndweight.setText("0"+list.get(position).getTimeCook()/60+":"+list.get(position).getTimeCook()%60);
                    }
                }else{
                    if (list.get(position).getTimeCook()%60<10){
                        fireAndweight.setText(list.get(position).getTimeCook()/60+":0"+list.get(position).getTimeCook()%60);
                    }else{
                        fireAndweight.setText(list.get(position).getTimeCook()/60+":"+list.get(position).getTimeCook()%60);
                    }
                }

                unit.setText("");
            }else if(list.get(position).getMode()==6||list.get(position).getMode()==7){
                modeName.setText(getModelText(position).string);
                fireAndweight.setText(list.get(position).getWeight()+"");
                unit.setText("份");
            } else {
                modeName.setText(getModelText(position).string);
                fireAndweight.setText(list.get(position).getWeight()+"");
                unit.setText("克");
            }
            return convertView;
        }
    }

    public ModelInfo getModelText(int position){
        ModelInfo info = new ModelInfo();
        int res = 0;
        switch (list.get(position).getMode()){
            case MicroWaveModel.Meat:
                info.string = "黑椒牛排";
                info.res = R.mipmap.ic_micro526_recently_beef;
                break;
            case MicroWaveModel.Checken:
                info.string = "香烤全鸡";
                info.res = R.mipmap.ic_micro526_recently_chicken;
                break;
            case MicroWaveModel.Kebab:
                info.string = "风味肉串";
                info.res = R.mipmap.ic_micro526_recently_rou;
                break;
            case MicroWaveModel.Rice:
                info.string = "煮饭";
                info.res = R.mipmap.ic_micro526_rcently_porridge;
                break;
            case MicroWaveModel.Porridge:
                info.string = "煮粥";
                info.res = R.mipmap.ic_micro526_recently_rice;
                break;
            case MicroWaveModel.Milk:
                info.string = "煲汤";
                info.res = R.mipmap.ic_micro526_recently_baotang;
                break;
            case MicroWaveModel.Bread:
                info.string = "早餐面包";
                info.res = R.mipmap.ic_micro526_recently_bread;
                break;
            case MicroWaveModel.Vegetables:
                info.string = "炒时蔬";
                info.res = R.mipmap.ic_micro526_recently_vegetable;
                break;
            case MicroWaveModel.Fish:
                info.string = "蒸鱼";
                info.res = R.mipmap.ic_micro526_recently_fish;
                break;
            case MicroWaveModel.Barbecue:
                info.string = "烧烤";
                info.res = R.mipmap.ic_micro526_recently_babico;
                break;
            case MicroWaveModel.MicroWave:
                if ("去味".equals(list.get(position).getName())){
                    info.string = "去味";
                    info.res =  R.mipmap.ic_micro526_recently_clean;
                } else{
                    info.string = "微波";
                    info.res = R.mipmap.ic_micro526_recently_wave;
                }
                break;
            case MicroWaveModel.ComibineHeating:
                info.string = "组合加热";
                info.res = R.mipmap.ic_micro526_recently_combineheat;
                break;
            case MicroWaveModel.HeatingAgain:
                info.string = "再加热";
                info.res = R.mipmap.ic_micro526_recently_heatagain;
                break;
            case MicroWaveModel.Unfreeze:
                info.string = "解冻";
                info.res = R.mipmap.ic_micro526_recently_jiedong;
                break;
            default:
                break;
        }
        return info;
    }


    class ModelInfo {
        String string;
        int res;
    }

}
