package com.robam.roki.ui.page.device.cook;

import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.google.common.base.Objects;
import com.google.common.eventbus.Subscribe;
import com.legent.Callback;
import com.legent.plat.events.DeviceConnectionChangedEvent;
import com.legent.ui.UIService;
import com.legent.utils.LogUtils;
import com.legent.utils.speech.SpeechManager;
import com.robam.common.events.CookerStatusChangeEvent;
import com.robam.common.io.cloud.Reponses;
import com.robam.common.pojos.PayLoadKuF;
import com.robam.common.pojos.device.cook.AbsCooker;
import com.robam.common.pojos.device.cook.CookerStatus;
import com.robam.roki.ui.PageKey;

/**
 * Created by Dell on 2018/6/7.
 */

public class DeviceSubCookerPage extends AbsCookerDevicePage {

    private Handler myHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    try{
                        ovenFirstView.setVisibility(View.INVISIBLE);
                        ovenFirstView.setVisibility(View.INVISIBLE);
                        payLoadKuF = (PayLoadKuF) msg.obj;
                        if (addView.getChildAt(2)!=null){
                            deviceCookerRecipeView.setVisibility(View.VISIBLE);
                            deviceCookerRecipeView.upDateStep(payLoadKuF);
                            return;
                        }
                        deviceCookerRecipeView = new DeviceCookerRecipeView(cx, absCooker, payLoadKuF.kuCookingStepses,payLoadKuF.name);
                        addView.addView(deviceCookerRecipeView);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                default:
                    break;
            }
        }
    };


    @Subscribe
    public void onEvent(DeviceConnectionChangedEvent event){
        if (!event.isConnected){
            coolerOffLine.setVisibility(View.VISIBLE);
            ivDeviceSwitch.setVisibility(View.INVISIBLE);
            disCon();
        }
    }

    @Subscribe
    public void onEvent(CookerStatusChangeEvent event) {
        if (absCooker == null || !Objects.equal(absCooker.getID(), event.pojo.getID()))
            return;
        LogUtils.i("20181227","here is run::"+PageKey.DeviceSubCooker.equals(UIService.getInstance().getTop().getCurrentPageKey()));
        if (!PageKey.DeviceSubCooker.equals(UIService.getInstance().getTop().getCurrentPageKey())) {
            return;
        }
        LogUtils.i("20181227","here is run::"+PageKey.DeviceSubCooker.equals(UIService.getInstance().getTop().getCurrentPageKey()));
        this.absCooker = event.pojo;
        coolerOffLine.setVisibility(View.GONE);
        if (event.pojo.recipeStatus==4){
            return;
        }
        if (event.pojo.recipeStatus !=3 && event.pojo.recipeStatus != 0){
            ivDeviceSwitch.setVisibility(View.VISIBLE);
            recipeRun(event.pojo.recipeId+"");
        }else{
            cookStatus(event);
        }
    }


    private void cookStatus(CookerStatusChangeEvent event){
    //    flag = true;
        switch (event.pojo.powerStatus){
            case CookerStatus.off:
            case CookerStatus.wait:
                ivDeviceSwitch.setVisibility(View.INVISIBLE);
                LogUtils.i("20180808","from:"+from);
                if (event.pojo.recipeStatus==0||event.pojo.recipeStatus==3){
                    if (addView.getChildAt(2)!=null){
                        addView.getChildAt(2).setVisibility(View.INVISIBLE);
                    }
                    cookerWorkingView.closeDialog();
                    addView.getChildAt(1).setVisibility(View.INVISIBLE);
                    addView.getChildAt(0).setVisibility(View.VISIBLE);
                }
                break;
            case CookerStatus.run:
                if (event.pojo.recipeStatus==3){
                    return;
                }
                ivDeviceSwitch.setVisibility(View.VISIBLE);
                if (event.pojo.currentAction ==0) {
                    upDateUi(event.pojo.mode, 0,event.pojo);
                } else {
                    upDateUi(event.pojo.currentAction, 1,event.pojo);
                }
                addView.getChildAt(1).setVisibility(View.VISIBLE);
                addView.getChildAt(0).setVisibility(View.INVISIBLE);
                break;
            case CookerStatus.alarm:
                // ToastUtils.show("报警故障状态", Toast.LENGTH_SHORT);
                if (addView.getChildAt(2)!=null){
                    addView.getChildAt(2).setVisibility(View.INVISIBLE);
                }
                addView.getChildAt(0).setVisibility(View.VISIBLE);
                addView.getChildAt(1).setVisibility(View.INVISIBLE);
                break;
            default:
                break;
        }
    }



    private void disCon(){
        //flag=true;
        addView.getChildAt(1).setVisibility(View.INVISIBLE);
        if (addView.getChildAt(2)!=null){
            addView.getChildAt(2).setVisibility(View.INVISIBLE);
        }
        addView.getChildAt(0).setVisibility(View.VISIBLE);

    }




    String preId="";
    private void recipeRun(String id) {
        addView.getChildAt(0).setVisibility(View.INVISIBLE);
        addView.getChildAt(1).setVisibility(View.INVISIBLE);
        if (!preId.equals(id)){
            if (addView.getChildAt(2)!=null){
                addView.removeViewAt(2);
            }
            getRecipe(id);
        }
        preId=id;
        if (addView.getChildAt(2)!=null){
            addView.getChildAt(2).setVisibility(View.VISIBLE);
            deviceCookerRecipeView.upDate(absCooker);
        }
    }

    public void getRecipe(String id){
        ss.getKuFRecipeDetailInte(id, new Callback<Reponses.GetKuFRecipeDetailResonse>() {
            @Override
            public void onSuccess(Reponses.GetKuFRecipeDetailResonse getKuFRecipeDetailResonse) {
                if (getKuFRecipeDetailResonse != null) {
                    Message msg = Message.obtain();
                    msg.obj = getKuFRecipeDetailResonse.payLoadKuFs;
                    msg.what=1;
                    myHandler.sendMessage(msg);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                LogUtils.i("20180612", "t::" + t);
            }
        });
    }



    //mode代表两种模式0代表action为0,1代表action不为0区action
    //code是电磁炉的模式
    private void upDateUi(int code, int mode,AbsCooker absCooker) {
        if (absCooker.recipeStatus!=0&&absCooker.recipeStatus!=3){
            addView.getChildAt(0).setVisibility(View.INVISIBLE);
            addView.getChildAt(1).setVisibility(View.INVISIBLE);
            if (addView.getChildAt(2)!=null){
                addView.getChildAt(2).setVisibility(View.VISIBLE);
            }
        }else{
            if (addView.getChildAt(2)!=null){
                addView.getChildAt(2).setVisibility(View.INVISIBLE);
            }
            addView.getChildAt(0).setVisibility(View.INVISIBLE);
            addView.getChildAt(1).setVisibility(View.VISIBLE);
            cookerWorkingView.upDateUi(code, mode,absCooker);
        }
    }




    @Override
    public void onDestroyView() {
        SpeechManager.getInstance().dispose();
        super.onDestroyView();
    }

}
