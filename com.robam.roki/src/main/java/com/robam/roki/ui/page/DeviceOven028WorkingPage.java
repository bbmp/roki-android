package com.robam.roki.ui.page;

import android.os.Bundle;
import android.view.View;

import com.google.common.base.Objects;
import com.google.common.eventbus.Subscribe;
import com.legent.ui.UIService;
import com.legent.utils.LogUtils;
import com.robam.common.events.OvenStatusChangedEvent;
import com.robam.common.pojos.device.Oven.OvenStatus;
import com.robam.roki.R;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;
import com.robam.roki.utils.DialogUtil;

/**
 * Created by yinwei on 2017/8/21.
 */

public class DeviceOven028WorkingPage extends AbsDeviceOvenWorkingPage {

    @Override
    public void init() {
        super.init();
        if (guid.startsWith("RR016")||guid.startsWith("RR026")){
            oven026_working_fra_rotate.setVisibility(View.INVISIBLE);
        }


        showMoreStage(oven026.currentStageValue,oven026.moreTotalValue);
        rokiDialog= RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_01);
        //发酵
    }

    private void showMoreStage(short stage,short moreTotal){
        if (moreTotal==2){
            oevn028_three.setVisibility(View.GONE);
        }else if (moreTotal==3){
            oevn028_three.setVisibility(View.VISIBLE);
        }
       // ToastUtils.show(moreTotal+"", Toast.LENGTH_SHORT);
        switch (stage){
            case 0:
                oven028_container.setVisibility(View.GONE);
                break;
            case 1:
                oven028_container.setVisibility(View.VISIBLE);
                oven028_one.setImageResource(R.mipmap.img_device_oven028_morecook_one_yellow);
                oven028_two.setImageResource(R.mipmap.img_device_oven028_morecook_two);
                oevn028_three.setImageResource(R.mipmap.img_device_oven028_morecook_three);
                break;
            case 2:
                oven028_container.setVisibility(View.VISIBLE);
                oven028_one.setImageResource(R.mipmap.img_device_oven028_morecook_one);
                oven028_two.setImageResource(R.mipmap.img_device_oven028_morecook_two_yellow);
                oevn028_three.setImageResource(R.mipmap.img_device_oven028_morecook_three);
                break;
            case 3:
                oven028_container.setVisibility(View.VISIBLE);
                oven028_one.setImageResource(R.mipmap.img_device_oven028_morecook_one);
                oven028_two.setImageResource(R.mipmap.img_device_oven028_morecook_two);
                oevn028_three.setImageResource(R.mipmap.img_device_oven028_morecook_three_yellow);
                break;
            default:
                break;
        }
    }

    @Override
    protected void back() {
        super.back();
        if (from == 1) {
            Bundle bd = new Bundle();
            bd.putString(PageArgumentKey.Guid, oven026.getID());
            UIService.getInstance().postPage(PageKey.DeviceOven028, bd);
        } else {
            UIService.getInstance().popBack();
        }
    }

    @Subscribe
    public void onEvent(OvenStatusChangedEvent event) {
        if (oven026 == null || !Objects.equal(oven026.getID(), event.pojo.getID()) || timer != null || training_lock)
            return;
        if (oven026.status == OvenStatus.PreHeat) {
            setPreHeatMode();
        } else if (oven026.status == OvenStatus.Working) {
            setWorkMode();
        } else if (oven026.status == OvenStatus.Pause) {
            setPauseMolde();
        } else if (oven026.status == OvenStatus.Order) {
            setOrderMolde();
        } else if (oven026.status == OvenStatus.Wait || oven026.status == OvenStatus.Off || oven026.status == OvenStatus.On) {
            if (rokiDialog!=null&&rokiDialog.isShow()){
                rokiDialog.dismiss();
            }
            if (isRunningForeground()){
                back();
            }
        } else {

        }
        if (oven026.status == OvenStatus.AlarmStatus) {
          back();
        }
    }



    public  void initExpModel() {
        if (oven026.runP==5||oven026.runP==13||oven026.runP==14||oven026.runP==10
                ||oven026.runP==15){
            oven026_working_img_pauseic1.setVisibility(View.INVISIBLE);
            oven026_working_img_pauseic3.setVisibility(View.INVISIBLE);
        }

        showMoreStage(oven026.currentStageValue,oven026.moreTotalValue);
        switch (oven026.runP) {
            case 1://快热
                oven026_working_img_circledown.setImageResource(R.mipmap.ic_028ovenwork_fastheat_white);
                oven026_working_tv_circleabove.setText("快热");
                break;
            case 2://风焙烤
                oven026_working_img_circledown.setImageResource(R.mipmap.ic_028ovenwork_fengbeikao_white);
                oven026_working_tv_circleabove.setText("风焙烤");
                break;
            case 3://焙烤
                oven026_working_img_circledown.setImageResource(R.mipmap.ic_028ovenwork_beikao_white);
                oven026_working_tv_circleabove.setText("焙烤");
                break;
            case 4://底加热
                oven026_working_img_circledown.setImageResource(R.mipmap.ic_028ovenwork_dijiare_white);
                oven026_working_tv_circleabove.setText("底加热");
                break;
            case 6://风扇烤
                oven026_working_img_circledown.setImageResource(R.mipmap.ic_028ovenwork_fengshankao_white);
                oven026_working_tv_circleabove.setText("风扇烤");
                break;
            case 7://烤烧
                oven026_working_img_circledown.setImageResource(R.mipmap.ic_028ovenwork_shaokao_white);
                oven026_working_tv_circleabove.setText("烤烧");
                break;
            case 8://强烤烧
                oven026_working_img_circledown.setImageResource(R.mipmap.ic_028ovenwork_qiangshaokao_white);
                oven026_working_tv_circleabove.setText("强烤烧");
                break;
            case 9://EXP
                oven026_working_img_circledown.setImageResource(R.mipmap.ic_028ovenwork_exp_white);
                oven026_working_tv_circleabove.setText("EXP模式");
                break;
            case 10://快速预热
                oven026_working_img_circledown.setImageResource(R.mipmap.ic_028oven_quickheat_white);
                oven026_working_tv_circleabove.setText("快速预热\n保温中");
                break;
            case 11://煎烤
                oven026_working_img_circledown.setImageResource(R.mipmap.ic_028ovenwork_jiankao_white);
                oven026_working_tv_circleabove.setText("煎烤");
                break;
            case 12://果蔬烘干
                oven026_working_img_circledown.setImageResource(R.mipmap.ic_028ovenwork_fvdrying_white);
                oven026_working_tv_circleabove.setText("果蔬烘干");
                break;
            case 5://解冻
                oven026_working_img_circledown.setImageResource(R.mipmap.ic_028ovenwork_unfreeze_white);
                oven026_working_tv_circleabove.setText("解冻");
               // oven026_working_fra_rotate.setVisibility(View.INVISIBLE);
                break;
            case 13://发酵
                oven026_working_img_circledown.setImageResource(R.mipmap.ic_028ovenwork_fa_white);
                oven026_working_tv_circleabove.setText("发酵");
              //  oven026_working_fra_rotate.setVisibility(View.INVISIBLE);
                break;
            case 14://杀菌
                oven026_working_img_circledown.setImageResource(R.mipmap.ic_028ovenwork_sterilize_white);
                oven026_working_tv_circleabove.setText("杀菌");
             //   oven026_working_fra_rotate.setVisibility(View.INVISIBLE);
                break;
            case 15://保温
                oven026_working_img_circledown.setImageResource(R.mipmap.device_work_keeptemp);
                oven026_working_tv_circleabove.setText("保温");
                //oven026_working_fra_rotate.setVisibility(View.INVISIBLE);
                break;
            default:break;
        }
    }

}
