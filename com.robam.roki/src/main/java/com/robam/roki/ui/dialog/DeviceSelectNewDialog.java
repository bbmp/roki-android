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
import com.robam.roki.R;
import com.robam.roki.ui.holder.MyViewHolder;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by yinwei on 2017/12/6.
 */

public class DeviceSelectNewDialog extends AbsDialog {
    @InjectView(R.id.desc_txt)
    TextView descTxt;
    @InjectView(R.id.device_select_show)
    RecyclerView deviceSelectShow;



    public interface PickDeviceSelectLister{
        void onConfirm(IDevice dev);
    }

    private PickDeviceSelectLister lister;

    public void setPickDeviceSelectLister(PickDeviceSelectLister lister){
        this.lister = lister;
    }

    public static DeviceSelectNewDialog dlg;
    Context cx;
    List<IDevice> iDevice;
    public DeviceSelectNewDialog(Context context,List<IDevice> iDevice) {
        super(context, R.style.Dialog_Microwave_professtion_bottom);
        LogUtils.i("20171206","deviceSelectNewDialog:"+iDevice.size());
        this.cx = context;
        this.iDevice = iDevice;
        initDeviceSelect();
    }

    @Override
    protected int getViewResId() {
        return R.layout.dialog_device_select_show;
    }

    protected void initDeviceSelect() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(cx);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        deviceSelectShow.setLayoutManager(layoutManager);
        deviceSelectAdapter = new DeviceSelectAdapter(cx,iDevice);
        deviceSelectShow.setAdapter(deviceSelectAdapter);
        deviceSelectAdapter.setOnItemClickListener(new MyViewHolder.MyItemClickListener() {
            @Override
            public void onItemClick(View view, int postion) {
                LogUtils.i("20171206","position::"+postion);
                if (lister!=null){
                    lister.onConfirm(iDevice.get(postion));
                    if (dlg!=null && dlg.isShowing()){
                        dlg.dismiss();
                    }
                }
            }
        });
    }

    private DeviceSelectAdapter deviceSelectAdapter;

    @Override
    protected void initView(View view) {
        ButterKnife.inject(this, view);
    }

    @OnClick(R.id.cannel)
    public void onClickCannel(){
        if (dlg!=null&&dlg.isShowing())
            dlg.dismiss();
    }

    static public DeviceSelectNewDialog show(Context cx, List<IDevice> iDevice) {
        LogUtils.i("20171206","show"+iDevice.size());
        dlg = new DeviceSelectNewDialog(cx,iDevice);
        Window win = dlg.getWindow();
        WindowManager wm = (WindowManager) cx.getSystemService(Context.WINDOW_SERVICE);
        int screenHeight = wm.getDefaultDisplay().getHeight();
        win.setGravity(Gravity.BOTTOM);  //??????????????????dialog???????????????
        WindowManager.LayoutParams wl = win.getAttributes();
        wl.width = WindowManager.LayoutParams.MATCH_PARENT;
        wl.height = screenHeight/2;
        win.setAttributes(wl);
        dlg.show();
        return dlg;
    }

    class DeviceSelectAdapter extends RecyclerView.Adapter<MyViewHolder>{
        private List<IDevice> iDevList = null;
        Context cx;
        private MyViewHolder.MyItemClickListener myItemClickListener;
        public DeviceSelectAdapter(Context cx,List<IDevice> iDevList){
            this.cx = cx;
            this.iDevList = iDevList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view=null;
            view = LayoutInflater.from(cx).inflate(R.layout.device_select_show_item, parent, false);
            MyViewHolder holder = new MyViewHolder(view,myItemClickListener);
            return holder;
        }

        public void setOnItemClickListener(MyViewHolder.MyItemClickListener listener){
            this.myItemClickListener = listener;
        }


        @Override
        public void onBindViewHolder(final MyViewHolder holder, int position) {
            LogUtils.i("20180307","device::"+iDevList.get(position).getName()+" "+iDevList.get(position).getDispalyType()
            +"  "+iDevList.get(position).getDeviceType());
            holder.deviceName.setText(iDevList.get(position).getDeviceType()+"");
            holder.deviceDesc.setText(iDevList.get(position).getDispalyType());

        }

        @Override
        public int getItemCount() {
            return iDevList==null ? 0 : iDevList.size();
        }
    }


}
