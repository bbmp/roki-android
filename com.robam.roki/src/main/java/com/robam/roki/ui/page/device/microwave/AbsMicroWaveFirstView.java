package com.robam.roki.ui.page.device.microwave;

import android.content.Context;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.TextView;

import com.legent.plat.pojos.device.DeviceConfigurationFunctions;
import com.legent.utils.LogUtils;
import com.robam.roki.R;
import com.robam.roki.ui.adapter.MicroWaveCommonAdapter;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Dell on 2018/7/12.
 */

public class AbsMicroWaveFirstView extends FrameLayout{
    Context cx;
    @InjectView(R.id.microwave_offline_txt)
    TextView microWaveOfflineTxt;
    @InjectView(R.id.microwave_func)
    GridView microWaveFuncFunc;
    @InjectView(R.id.micro_wave_func_show)
    RecyclerView microWaveFuncShow;

    List<DeviceConfigurationFunctions> mainList;
    List<DeviceConfigurationFunctions> otherList;

    public AbsMicroWaveFirstView(Context context, List<DeviceConfigurationFunctions> mainList,
                                 List<DeviceConfigurationFunctions> otherList) {
        super(context);
        this.cx = context;
        this.mainList = mainList;
        this.otherList = otherList;
        initView();
    }

    public AbsMicroWaveFirstView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.cx = context;
        initView();
    }

    MicroWaveCommonAdapter microWaveCommonAdapter;

    private void initView() {

        View view = LayoutInflater.from(cx).inflate(R.layout.microwave_first_view, this, true);
        if (!view.isInEditMode()) {
            ButterKnife.inject(this, view);
        }
        microWaveCommonAdapter = new MicroWaveCommonAdapter(cx,mainList,otherList);
        microWaveFuncShow.setAdapter(microWaveCommonAdapter);
        GridLayoutManager layoutManager = new GridLayoutManager(cx, 1);
        microWaveFuncShow.setLayoutManager(layoutManager);
        microWaveCommonAdapter.setGridViewOnclickLister(new MicroWaveCommonAdapter.GridViewOnclick() {
            @Override
            public void onGridClick(String pos) {
//                ToastUtils.show(pos, Toast.LENGTH_SHORT);
                if (onclickMainLister!=null){
                    onclickMainLister.onclickMain(pos);
                }
            }
        });

        microWaveCommonAdapter.setItemViewOnclickLister(new MicroWaveCommonAdapter.ItemViewOnclick() {

            @Override
            public void onItemClick(String pos) {
//                ToastUtils.show(pos,Toast.LENGTH_SHORT);
                if (onclickMainLister!=null){
                    onclickMainLister.onclickOther(pos);
                }
            }
        });
    }

    protected void setUpData(List<DeviceConfigurationFunctions> moreList){
        LogUtils.i("20180831","first moreList:" + moreList.size());
        microWaveCommonAdapter.upMoreView(moreList);
    }

    protected void removeMoreView(){
        microWaveCommonAdapter.removeMoreView();
    }

    protected void disConnect(boolean isCon){
        if (isCon){
            microWaveOfflineTxt.setVisibility(View.VISIBLE);
        }else{
            microWaveOfflineTxt.setVisibility(View.INVISIBLE);
        }
    }

    public interface OnClickMian{

        void  onclickMain(String str);

        void  onclickOther(String str);
    }

    public OnClickMian onclickMainLister;

    public void setOnclickMainLister(OnClickMian onclickMainLister) {
        this.onclickMainLister = onclickMainLister;
    }
}
