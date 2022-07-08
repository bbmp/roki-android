package com.robam.roki.ui.activity3.device.fan.adapter;

import static com.blankj.utilcode.util.ColorUtils.getColor;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.google.gson.Gson;
import com.legent.plat.Plat;
import com.legent.plat.pojos.device.DeviceConfigurationFunctions;
import com.legent.plat.pojos.device.IDevice;
import com.robam.common.pojos.device.Pot.Pot;
import com.robam.common.pojos.device.SmartParams;
import com.robam.common.pojos.device.Stove.Stove;
import com.robam.common.pojos.device.fan.AbsFan;
import com.robam.roki.R;
import com.robam.roki.net.request.bean.LinkageConfig;
import com.robam.roki.net.request.param.SetLinkageConfigParam;
import com.robam.roki.ui.activity3.device.fan.bean.LinkageBean;
import com.robam.roki.utils.StringUtil;
import com.robam.widget.view.SwitchButton;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import skin.support.content.res.SkinCompatResources;


/**
 * @author r210190
 * des 联动功能adapter
 */
public class RvLinkageAdapter extends BaseQuickAdapter<DeviceConfigurationFunctions, BaseViewHolder> {
    /**
     * 烟机设备
     */
    private AbsFan fan ;

    /**
     * 烟机智能设定状态
     */
    private SmartParams smartParams ;

    /**
     * 无人锅状态
     */
    private short potParam = 1  ;

    /**
     * 蒸烤一体机联动状态
     */
    private SetLinkageConfigParam linkageConfig ;

    /**
     * 主功能联动
     */
    public static int MAIN_LINK = 0 ;
    /**
     * 次级别联动
     */
    public static int SEC_LINK = 1 ;
    /**
     * checkBox监听
     */
    OnLinkageChangeLinstener onLinkageChangeLinstener ;
    /**
     * 添加checkBox监听
     */
    public void addOnLinkageChangeLinstener(OnLinkageChangeLinstener onLinkageChangeLinstener) {
        this.onLinkageChangeLinstener = onLinkageChangeLinstener;
    }
    /**
     * checkBox监听接口
     */
    public interface OnLinkageChangeLinstener{
        void linkChange( boolean checked , DeviceConfigurationFunctions func , int linkState);
    }

    /**
     * 设置adapter数据
     * @param list
     * @param fan
     */
    public void setNewInstance(@Nullable List<DeviceConfigurationFunctions> list , AbsFan fan) {
        this.fan = fan ;
        super.setNewInstance(list);

    }

    /**
     * 获取烟机智能设定状态
     * @param smartParams
     */
    @SuppressLint("NotifyDataSetChanged")
    public void setSmart(SmartParams smartParams){
        this.smartParams = smartParams ;
        notifyDataSetChanged();
    }

    /**
     * 设置无人锅状态
     * @param potParam
     */
    public void setPotParam(short potParam) {
        this.potParam = potParam;
        notifyDataSetChanged();
    }

    /**
     * 设置一体机状态状态
     * @param linkageConfig
     */
    public void setSteamingRoast(SetLinkageConfigParam linkageConfig) {
        this.linkageConfig = linkageConfig;
        notifyDataSetChanged();
    }

    public RvLinkageAdapter() {
        super(R.layout.item_fan_linkage);
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void convert(@NotNull BaseViewHolder holder, DeviceConfigurationFunctions item) {

        holder.setText(R.id.tv_linkage_name , item.functionName) ;
        String functionParams = item.functionParams ;
        LinkageBean linkageBean = new Gson().fromJson(functionParams, LinkageBean.class);
        holder.setText(R.id.tv_linkage_funtion , linkageBean.function.get(0).des) ;

        SwitchButton sbLinkage = (SwitchButton)holder.getView(R.id.sb_linkage);
        SwitchButton sbLinkageFuntion = (SwitchButton)holder.getView(R.id.sb_linkage_funtion);
        View ivRight = holder.getView(R.id.iv_right);


        sbLinkage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onLinkageChangeLinstener != null){
                    onLinkageChangeLinstener.linkChange(sbLinkage.isChecked() , item , MAIN_LINK);
                }
            }
        });

        sbLinkageFuntion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onLinkageChangeLinstener != null){
                    onLinkageChangeLinstener.linkChange(sbLinkageFuntion.isChecked() , item , SEC_LINK);
                }
            }
        });
        //显示关联的子设备
        if (fan != null){
            if (item.functionCode.equals("stovelinkage")){
                //烟灶智能设定判断
                if (smartParams != null){
                    sbLinkage.setChecked(smartParams.IsPowerLinkage);
                    sbLinkageFuntion.setChecked(smartParams.fanStoveAuto == 1 );
                }else {
                    sbLinkage.setChecked(false);
                    sbLinkageFuntion.setChecked(false);
                }

                //烟灶联动功能
                IDevice childStove = fan.getChildStove();
                if (childStove != null && childStove instanceof Stove){
                    holder.setText(R.id.tv_child_device , childStove.getDispalyType());
                }else {
                    holder.setText(R.id.tv_child_device , "暂无关联产品");
                    sbLinkageFuntion.setChecked(false);
                    sbLinkageFuntion.setEnabled(false);
                    holder.setTextColor(R.id.tv_relation , getColorSkin(R.color.text_color_gray));
                    holder.setTextColor(R.id.tv_child_device , getColorSkin(R.color.text_color_gray));
                    holder.setTextColor(R.id.tv_linkage_funtion , getColorSkin(R.color.text_color_gray));
                }
                ivRight.setVisibility(View.GONE);
            }else if (item.functionCode.equals("potLinkage")){
                sbLinkage.setChecked((potParam & 0x02) != 0);
                sbLinkageFuntion.setChecked((potParam & 0x04) != 0);

                IDevice childPot = fan.getChildPot();
                if (childPot != null && childPot instanceof Pot){
                    holder.setText(R.id.tv_child_device , childPot.getDispalyType());
                }else {
                    holder.setText(R.id.tv_child_device , "暂无关联产品");
                    sbLinkage.setChecked(false);
                    sbLinkage.setEnabled(false);
                    sbLinkageFuntion.setEnabled(false);
                    holder.setTextColor(R.id.tv_relation , getColorSkin(R.color.text_color_gray));
                    holder.setTextColor(R.id.tv_child_device , getColorSkin(R.color.text_color_gray));
                    holder.setTextColor(R.id.tv_linkage_funtion , getColorSkin(R.color.text_color_gray));
                }
                ivRight.setVisibility(View.GONE);
            }else if (item.functionCode.equals("steamingRoastLinkage")){
                ivRight.setVisibility(View.VISIBLE);

                if (linkageConfig  != null ){
                    holder.setTextColor(R.id.tv_relation , getColorSkin(R.color.text_color_device_category));
                    holder.setTextColor(R.id.tv_child_device , getColorSkin(R.color.text_color_device_category));
                    holder.setTextColor(R.id.tv_linkage_funtion , getColorSkin(R.color.text_color_device_category));

                    sbLinkage.setChecked(linkageConfig.enabled);
                    sbLinkageFuntion.setChecked(linkageConfig.doorOpenEnabled);
                    sbLinkageFuntion.setEnabled(!StringUtil.isBlank(linkageConfig.targetGuid));
                    holder.setText(R.id.tv_child_device , StringUtil.isBlank(linkageConfig.targetGuid) ? "暂无关联产品"
                            : (StringUtil.isBlank(linkageConfig.targetDeviceName) ? linkageConfig.targetGuid : linkageConfig.targetDeviceName));
                    holder.setTextColor(R.id.tv_child_device , StringUtil.isBlank(linkageConfig.targetGuid) ? getColorSkin(R.color.text_color_kitchen) :
                            getColor(R.color.common_dialog_btn_normal_bg_blue) );
                }
            }
            View view = holder.getView(R.id.ll_relation);
            view.setVisibility(sbLinkage.isChecked() ? View.VISIBLE :View.GONE);
        }
    }


    private int getColorSkin(int resId){
        return SkinCompatResources.getColor(getContext() ,resId) ;
    }
}
