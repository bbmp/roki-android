package com.robam.roki.utils;

import android.content.Context;
import android.widget.Toast;

import com.google.common.eventbus.Subscribe;
import com.legent.Callback;
import com.legent.plat.constant.IDeviceType;
import com.legent.plat.pojos.device.IDevice;
import com.legent.ui.UIService;
import com.legent.utils.LogUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.events.OvenStatusChangedEvent;
import com.robam.common.pojos.device.steameovenone.SteamOvenOnePowerStatus;
import com.robam.common.util.IsendCommand;
import com.robam.roki.R;
import com.robam.roki.ui.RecipeNewUtils;
import com.robam.roki.ui.view.RecipeParamShowView;

/**
 * Created by yinwei on 2017/12/11.
 */

public class SendCommandUtils implements IsendCommand {


    Context cx;
    int step;
    IDevice iDevice;
    RecipeParamShowView recipeParamShowView;
    private PollingState pollingStateLister;

    public SendCommandUtils(Context cx, int step, IDevice iDevice, RecipeParamShowView recipeParamShowView ) {
        this.iDevice = iDevice;
        this.step = step;
        this.recipeParamShowView = recipeParamShowView;
        this.cx = cx;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public void setiDevice(IDevice iDevice) {
        this.iDevice = iDevice;
    }

    public interface PollingState {
        void device();
    }

    public void setPollingStateLister(PollingState pollingStateLister) {
        this.pollingStateLister = pollingStateLister;
    }

    @Override
    public void onStart() {
        LogUtils.i("2020070102", "recipeParamShowView:::11::" + recipeParamShowView.toString());
        LogUtils.i("2020070102", "recipeParamShowView:::11::" + recipeParamShowView.paramMap);
        RecipeNewUtils.setDevicePreSetModel(cx, iDevice, recipeParamShowView, step, new Callback<Integer>() {
            @Override
            public void onSuccess(Integer n) {
                if (n == 0) {
                } else if (n == 1) {
                } else if (n == 2) {
                } else if (n == 3) {
                    ToastUtils.show("???????????????????????????", Toast.LENGTH_SHORT);
                } else if (n == 4) {
                    ToastUtils.show("??????????????????????????????????????????", Toast.LENGTH_SHORT);
                } else if (n == 5) {
                    ToastUtils.show("?????????????????????????????????????????????", Toast.LENGTH_SHORT);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                ToastUtils.show(R.string.device_Failure_text, Toast.LENGTH_SHORT);
            }
        });
    }

    short status;

    @Override
    public void onPause() {
        if (IDeviceType.RZKY.equals(iDevice.getDc())) {
            status = 1;
        } else {
            status = RecipeNewUtils.PAUSE;
        }
        LogUtils.i("20180207", "onpuse::" + status);
        RecipeNewUtils.setDeviceStatusModel(cx, status, iDevice, new Callback<Integer>() {
            @Override
            public void onSuccess(Integer n) {
                if (n == 0) {
                } else if (n == 1) {
                } else if (n == 2) {
                    ToastUtils.show("???????????????????????????????????????", Toast.LENGTH_SHORT);
                } else if (n == 3) {
                    ToastUtils.show("???????????????????????????", Toast.LENGTH_SHORT);
                } else if (n == 4) {
                    ToastUtils.show(R.string.device_Throwable_text, Toast.LENGTH_SHORT);
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    @Override
    public void onPreSend() {
        RecipeNewUtils.setDevicePreSetModel(cx, iDevice, recipeParamShowView, step, new Callback<Integer>() {
            @Override
            public void onSuccess(Integer n) {
            }

            @Override
            public void onFailure(Throwable t) {
                ToastUtils.show(R.string.device_Failure_text, Toast.LENGTH_SHORT);
            }
        });
    }

    @Override
    public void onFinish() {
        if (IDeviceType.RZKY.equals(iDevice.getDc())) {
            status = SteamOvenOnePowerStatus.RecipeOff;
        } else {
            status = RecipeNewUtils.OFF;
        }
        RecipeNewUtils.setDeviceStatusModel(cx, status, iDevice, new Callback<Integer>() {
            @Override
            public void onSuccess(Integer n) {
                if (n == 0) {
                } else if (n == 1) {
                } else if (n == 2) {
                } else if (n == 3) {
                    ToastUtils.show("???????????????????????????", Toast.LENGTH_SHORT);
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    @Override
    public void onRestart() {
        if (IDeviceType.RZKY.equals(iDevice.getDc())) {
            status = 3;
        } else {
            status = RecipeNewUtils.WORKING;
        }
        RecipeNewUtils.setDeviceStatusModel(cx, status, iDevice, new Callback<Integer>() {
            @Override
            public void onSuccess(Integer n) {
                if (n == 0) {
                } else if (n == 1) {
                } else if (n == 2) {
                } else if (n == 3) {
                    ToastUtils.show("???????????????????????????", Toast.LENGTH_SHORT);
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }


    /**
     * ????????????
     */
    @Subscribe
    public void onEvent(OvenStatusChangedEvent event) {
        if (pollingStateLister != null) {
            pollingStateLister.device();
        }
    }


    /**
     * ?????????
     */
   /* private Dialog promptDialog() {
        BlackPromptConfirmDialog dlg = Helper.newBlackPromptConfirmDialog(cx, new Callback2<Object>() {

            @Override
            public void onCompleted(Object o) {

            }
        }, R.layout.dialog_recipe_prompt);
        dlg.setButtonText(new String("?????????"));
        if (dlg != null && !dlg.isShowing()) {
            dlg.show();
        }
        return dlg;
    }*/
    private void promptDialog() {
        ToastUtils.show(iDevice.getName() + "?????????????????????????????????????????????", Toast.LENGTH_SHORT);
        UIService.getInstance().popBack();
    }
}
