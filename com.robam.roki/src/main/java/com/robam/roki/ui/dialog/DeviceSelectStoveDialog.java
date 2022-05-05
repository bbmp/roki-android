package com.robam.roki.ui.dialog;

import android.content.Context;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.legent.plat.pojos.device.IDevice;
import com.legent.ui.ext.dialogs.AbsDialog;
import com.legent.utils.LogUtils;
import com.robam.common.pojos.device.Stove.Stove;
import com.robam.common.pojos.device.fan.AbsFan;
import com.robam.roki.R;
import com.robam.roki.ui.holder.MyViewHolder;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Dell on 2018/1/2.
 */

public class DeviceSelectStoveDialog extends AbsDialog {
    @InjectView(R.id.desc_txt)
    TextView stoveDescTxt;
    @InjectView(R.id.device_select_show)
    RecyclerView deviceSelectStove;

    public DeviceSelectStoveDialog(Context context,List<Stove> stoveList) {
        super(context, R.style.Dialog_Microwave_professtion_bottom);
        this.cx = context;
        this.stoveList = stoveList;
        init();
    }

    private DeviceSelectAdapter deviceSelectAdapter;

    private void init() {
        LogUtils.i("20170105","init:");
        LinearLayoutManager layoutManager = new LinearLayoutManager(cx);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        deviceSelectStove.setLayoutManager(layoutManager);
        deviceSelectAdapter = new DeviceSelectAdapter(cx,stoveList);
        LogUtils.i("20170105"," deviceSelectAdapter");
        deviceSelectStove.setAdapter(deviceSelectAdapter);
        deviceSelectAdapter.setOnItemClickListener(new MyViewHolder.MyItemClickListener() {
            @Override
            public void onItemClick(View view, int postion) {
                LogUtils.i("20171206","position::"+postion);
                if (lister!=null){
                    lister.onConfirm(stoveList.get(postion));
                    if (dlg!=null && dlg.isShowing()){
                        dlg.dismiss();
                    }
                }
            }
        });
    }

    public interface PickDeviceSelectLister{
        void onConfirm(IDevice dev);
    }

    private PickDeviceSelectLister lister;

    public void setPickDeviceSelectLister(PickDeviceSelectLister lister){
        this.lister = lister;
    }

    public static DeviceSelectStoveDialog dlg;

    Context cx;


    List<Stove> stoveList;

    @Override
    protected int getViewResId() {
        return R.layout.dialog_device_select_show;
    }

    @Override
    protected void initView(View view) {
        ButterKnife.inject(this, view);
    }

    @OnClick(R.id.cannel)
    public void onClickCannel(){
        dlg.dismiss();
    }

    static public DeviceSelectStoveDialog show(Context cx, List<Stove> iDevice) {
        LogUtils.i("20171206","show"+iDevice.size());
        dlg = new DeviceSelectStoveDialog(cx,iDevice);
        Window win = dlg.getWindow();
        WindowManager wm = (WindowManager) cx.getSystemService(Context.WINDOW_SERVICE);
        int screenHeight = wm.getDefaultDisplay().getHeight();
        win.setGravity(Gravity.BOTTOM);  //此处可以设置dialog显示的位置
        WindowManager.LayoutParams wl = win.getAttributes();
        wl.width = WindowManager.LayoutParams.MATCH_PARENT;
        wl.height = screenHeight/2;
        win.setAttributes(wl);
        dlg.show();
        return dlg;
    }

    class DeviceSelectAdapter extends RecyclerView.Adapter<MyViewHolder>{
        private List<Stove> iDevList = null;
        Context cx;
        private MyViewHolder.MyItemClickListener myItemClickListener;

        public DeviceSelectAdapter(Context cx,List<Stove> iDevList){
            this.cx = cx;
            this.iDevList = iDevList;
            LogUtils.i("20170105","iDe::"+iDevList.size());
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view=null;
            view = LayoutInflater.from(cx).inflate(R.layout.device_stove_select_item, parent, false);
            MyViewHolder holder = new MyViewHolder(view,myItemClickListener);
            return holder;
        }

        public void setOnItemClickListener(MyViewHolder.MyItemClickListener listener){
            this.myItemClickListener = listener;
        }


        @Override
        public void onBindViewHolder(final MyViewHolder holder, int position) {
            LogUtils.i("20170105","onBindViewHolder");
            holder.deviceName.setText("烟机"+iDevList.get(position).getParent().getDispalyType());
            holder.deviceDesc.setText("灶具"+iDevList.get(position).getDispalyType());
        }

        @Override
        public int getItemCount() {
            return iDevList==null ? 0 : iDevList.size();
        }
    }
}
