package com.robam.roki.ui.page.device.oven;

import android.content.Context;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.legent.plat.pojos.device.DeviceConfigurationFunctions;
import com.legent.utils.LogUtils;
import com.robam.common.pojos.device.Oven.AbsOven;
import com.robam.roki.R;
import com.robam.roki.ui.adapter.OvenCommonAdapter;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Dell on 2018/7/12.
 */

public class AbsOvenFirstView extends FrameLayout{
    Context cx;
    @InjectView(R.id.oven_offline_txt)
    TextView ovenOfflineTxt;
    @InjectView(R.id.oven_func)
    MyGridView ovenFunc;
    @InjectView(R.id.oven_func_show)
    RecyclerView ovenFuncShow;

    List<DeviceConfigurationFunctions> mainList;
    List<DeviceConfigurationFunctions> otherList;
    AbsOven oven;

    public AbsOvenFirstView(Context context,List<DeviceConfigurationFunctions> mainList,List<DeviceConfigurationFunctions> otherList) {
        super(context);
        this.cx = context;
        this.mainList = mainList;
        this.otherList = otherList;
        for (int i = 0; i <otherList.size() ; i++) {
            LogUtils.i("20190314","listL:::"+otherList.get(i).toString());
        }
        initView();
    }

    public AbsOvenFirstView(Context context,List<DeviceConfigurationFunctions> mainList,List<DeviceConfigurationFunctions> otherList,AbsOven oven) {
        super(context);
        this.cx = context;
        this.mainList = mainList;
        this.otherList = otherList;
        this.oven = oven;
        for (int i = 0; i <otherList.size() ; i++) {
            LogUtils.i("20190314","listL:::"+otherList.get(i).toString());
        }
        initView();
    }
    public AbsOvenFirstView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.cx = context;
        initView();
    }

    OvenCommonAdapter ovenCommonAdapter;
    private void initView() {
        LogUtils.i("2020031304","AbsOvenFirstView:::111");
        View view = LayoutInflater.from(cx).inflate(R.layout.oven_first_view, this, true);
        if (!view.isInEditMode()) {
            ButterKnife.inject(this, view);
        }
        LogUtils.i("20201009333","mainList:::"+mainList.size());
        ovenCommonAdapter = new OvenCommonAdapter(cx,mainList,otherList,oven);
        ovenFuncShow.setAdapter(ovenCommonAdapter);
        GridLayoutManager layoutManager = new GridLayoutManager(cx, 1);
        ovenFuncShow.setLayoutManager(layoutManager);

        LogUtils.i("2020031304","AbsOvenFirstView:::333");
        ovenCommonAdapter.setGridViewOnclickLister(new OvenCommonAdapter.GridViewOnclick() {
            @Override
            public void onGridClick(String pos) {
                if (onclickMainLister!=null){
                    onclickMainLister.onclickMain(pos);
                }
            }
        });

        ovenCommonAdapter.setItemViewOnclickLister(new OvenCommonAdapter.ItemViewOnclick() {

            @Override
            public void onItemClick(String pos) {
                if (onclickMainLister!=null){
                    onclickMainLister.onclickOther(pos);
                }
            }
        });
    }

    public void setUpData(List<DeviceConfigurationFunctions> moreList){
        ovenCommonAdapter.upMoreView(moreList);
    }

    public void removeMoreView(){
        ovenCommonAdapter.removeMoreView();
    }

    public void disConnect(boolean isCon){
        if (isCon){
            ovenOfflineTxt.setVisibility(View.VISIBLE);
            ovenCommonAdapter.setDisCon(true);
        }else{
            ovenOfflineTxt.setVisibility(View.INVISIBLE);
            ovenCommonAdapter.setDisCon(false);
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
