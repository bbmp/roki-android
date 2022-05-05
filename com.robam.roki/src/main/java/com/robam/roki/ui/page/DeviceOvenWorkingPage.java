package com.robam.roki.ui.page;

import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.base.Objects;
import com.google.common.eventbus.Subscribe;
import com.legent.Callback2;
import com.legent.VoidCallback;
import com.legent.plat.Plat;
import com.legent.plat.events.DeviceConnectionChangedEvent;
import com.legent.ui.UIService;
import com.legent.ui.ext.BasePage;
import com.legent.ui.ext.popoups.PopoupHelper;
import com.legent.ui.ext.views.TitleBar;
import com.legent.utils.LogUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.events.OvenSpitRotateResetEvent;
import com.robam.common.events.OvenStatusChangedEvent;
import com.robam.common.pojos.device.Oven.Oven039;
import com.robam.common.pojos.device.Oven.OvenStatus;
import com.robam.roki.R;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.model.NormalModeItemMsg;
import com.robam.roki.ui.Helper;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.dialog.OvenBrokenDialog;
import com.robam.roki.utils.DialogUtil;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by linxiaobin on 2015/12/25.
 */
public class DeviceOvenWorkingPage extends BasePage {

    Oven039 oven;
    private int remainTime;
    private short preTime;
    private short preTemp;
    private String type;
    private short rotate;
    private short light;

    private short testStatus = 0;

    static final int Start = 4;
    static final int Pause = 0;
    static final int Working = 1;
    static final int Done = 2;
    static final int CountDown = 3;
    static final int Reset = 5;
    static final int ReturnHome = 6;
    private int lastTime;
    private boolean canCountDown = false;
    int preStatus;
    int currentStatus;
    static final int Light = 7;
    static final int Rotate = 8;

    static final int PollStatus = 9;
    static final int RefreshTime = 10;

    static boolean fromSetting = false;
    static boolean hasStart = false;

    private OvenBrokenDialog dlg = null;//报警

    private Animation circleRotate = null;//动画设置
    private Animation spitRotate = null;//动画设置
    short runType;
    boolean isFirst = true;

    View contentView;

    TextView txtCurrentTem;
    TextView txtCurrentTime;
    ImageView imgSpinCircle;
    ImageView imgContent;

    ImageView imgPause;

    ImageView imgDone;

    TextView workType1;

    TextView workType2;

    ImageView imgLight;

    RelativeLayout rlLight;

    ImageView imgRotate;

    ImageView imgReturn;

    TextView txtRecipe;

    ImageView imgTempReset;

    ImageView imgTimeReset;


    TextView txtTemSet;

    TextView txtTimeSet;
    String guid;
    PopupWindow pop;//弹出框（温度 时间）设置
    boolean timeLock=false;
    boolean handlerLock=false;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Start:
                    Log.i("orderyiguodetail", "Start" + oven.status);
                    if (type.equals(cx.getString(R.string.device_steamOvenOne_name_kuaire))
                            || type.equals(cx.getString(R.string.device_oven_model_dangao))
                            || type.equals(cx.getString(R.string.device_steam_model_shucai))) {
                        oven.setOvenQuickHeating(preTime, preTemp, (short) 0, new VoidCallback() {
                            @Override
                            public void onSuccess() {
                                oven.setOvenStatusControl(OvenStatus.Working, new VoidCallback() {
                                    @Override
                                    public void onSuccess() {
                                        initStart(cx.getString(R.string.device_mode_kuaire),R.mipmap.img_oven_quick_heating_working);
                                    }

                                    @Override
                                    public void onFailure(Throwable t) {
                                        LogUtils.i("20190805",t.getMessage());
                                    }
                                });
                            }

                            @Override
                            public void onFailure(Throwable t) {
                                ToastUtils.showThrowable(t);
                            }
                        });
                    } else if (type.equals(cx.getString(R.string.device_steamOvenOne_name_fengpeikao))) {
                        oven.setOvenAirBaking(preTime, preTemp, (short) 0, new VoidCallback() {
                            @Override
                            public void onSuccess() {
                                oven.setOvenStatusControl(OvenStatus.Working, new VoidCallback() {
                                    @Override
                                    public void onSuccess() {
                                        initStart(cx.getString(R.string.device_mode_fengpeikao),R.mipmap.img_oven_air_barking_working);
                                    }


                                    @Override
                                    public void onFailure(Throwable t) {
                                    }
                                });
                            }

                            @Override
                            public void onFailure(Throwable t) {
                                ToastUtils.showThrowable(t);
                            }
                        });
                    } else if (type.equals(cx.getString(R.string.device_steamOvenOne_name_peikao))
                            || type.equals(cx.getString(R.string.device_oven_model_niupai))
                            || type.equals(cx.getString(R.string.device_oven_model_pisa))) {
                        oven.setOvenToast(preTime, preTemp, (short) 0, new VoidCallback() {
                            @Override
                            public void onSuccess() {
                                oven.setOvenStatusControl(OvenStatus.Working, new VoidCallback() {
                                    @Override
                                    public void onSuccess() {
                                        initStart(cx.getString(R.string.device_mode_peikao),R.mipmap.img_oven_toast_working);
                                    }

                                    @Override
                                    public void onFailure(Throwable t) {
                                    }
                                });
                            }

                            @Override
                            public void onFailure(Throwable t) {
                                ToastUtils.showThrowable(t);
                            }
                        });
                    } else if (type.equals(cx.getString(R.string.device_steamOvenOne_name_dijiare))) {
                        oven.setOvenBottomHeating(preTime, preTemp, (short) 0, new VoidCallback() {
                            @Override
                            public void onSuccess() {
                                oven.setOvenStatusControl(OvenStatus.Working, new VoidCallback() {
                                    @Override
                                    public void onSuccess() {
                                        initStart(cx.getString(R.string.device_mode_dijiare),R.mipmap.img_oven_bottom_heating_working);
                                    }

                                    @Override
                                    public void onFailure(Throwable t) {
                                    }
                                });
                            }

                            @Override
                            public void onFailure(Throwable t) {
                                ToastUtils.showThrowable(t);
                            }
                        });
                    } else if (type.equals(cx.getString(R.string.device_steamOvenOne_name_jiedong))) {
                        oven.setOvenUnfreeze(preTime, preTemp, (short) 0, new VoidCallback() {
                            @Override
                            public void onSuccess() {
                                oven.setOvenStatusControl(OvenStatus.Working, new VoidCallback() {
                                    @Override
                                    public void onSuccess() {
                                        initStart(cx.getString(R.string.device_mode_jiedong),R.mipmap.img_oven_unfreezing_working);
                                    }

                                    @Override
                                    public void onFailure(Throwable t) {
                                    }
                                });
                            }

                            @Override
                            public void onFailure(Throwable t) {
                                ToastUtils.showThrowable(t);
                            }
                        });
                    } else if (type.equals(cx.getString(R.string.device_steamOvenOne_name_fengshankao))
                            || type.equals(cx.getString(R.string.device_oven_model_binggan))
                            || type.equals(cx.getString(R.string.device_oven_model_mianbao))
                            || type.equals(cx.getString(R.string.device_oven_model_wuhuarou))) {
                        oven.setOvenAirBarbecue(preTime, preTemp, (short) 0, new VoidCallback() {
                            @Override
                            public void onSuccess() {
                                oven.setOvenStatusControl(OvenStatus.Working, new VoidCallback() {
                                    @Override
                                    public void onSuccess() {
                                        initStart(cx.getString(R.string.device_mode_fengshankao),R.mipmap.img_oven_air_barbecue_working);
                                    }

                                    @Override
                                    public void onFailure(Throwable t) {
                                    }
                                });

                            }

                            @Override
                            public void onFailure(Throwable t) {
                                ToastUtils.showThrowable(t);
                            }
                        });
                    } else if (type.equals(cx.getString(R.string.device_steamOvenOne_name_kaoshao))) {
                        oven.setOvenBarbecue(preTime, preTemp, (short) 0, new VoidCallback() {
                            @Override
                            public void onSuccess() {
                                oven.setOvenStatusControl(OvenStatus.Working, new VoidCallback() {
                                    @Override
                                    public void onSuccess() {
                                        initStart(cx.getString(R.string.device_steamOvenOne_name_kaoshao),R.mipmap.img_oven_barbecue_working);
                                    }

                                    @Override
                                    public void onFailure(Throwable t) {
                                    }
                                });

                            }

                            @Override
                            public void onFailure(Throwable t) {
                                ToastUtils.showThrowable(t);
                            }
                        });
                    } else if (type.equals(cx.getString(R.string.device_steamOvenOne_name_qiangshaokao)) ||
                            type.equals(cx.getString(R.string.device_oven_model_haixian))
                            || type.equals(cx.getString(R.string.device_oven_model_jichi))) {
                        oven.setOvenStrongBarbecue(preTime, preTemp, (short) 0, new VoidCallback() {
                            @Override
                            public void onSuccess() {
                                oven.setOvenStatusControl(OvenStatus.Working, new VoidCallback() {
                                    @Override
                                    public void onSuccess() {
                                        initStart(cx.getString(R.string.device_steamOvenOne_name_qiangshaokao),R.mipmap.img_oven_strong_barbecue_working);
                                    }

                                    @Override
                                    public void onFailure(Throwable t) {
                                    }
                                });

                            }

                            @Override
                            public void onFailure(Throwable t) {
                                ToastUtils.showThrowable(t);
                            }
                        });
                    } else if (type.equals(cx.getString(R.string.device_steam_self_cleaning))) {
                        oven.setOvenAirBarbecue(preTime, preTemp, (short) 0, new VoidCallback() {
                            @Override
                            public void onSuccess() {
                                txtTemSet.setText(String.valueOf(preTemp));
                                txtTimeSet.setText(String.valueOf(preTime));
                                txtCurrentTem.setClickable(false);
                                txtCurrentTime.setClickable(false);
                                imgContent.setClickable(false);
                                imgRotate.setVisibility(View.GONE);
                                rlLight.setClickable(false);
                                if (circleRotate == null) {
                                    circleRotate = AnimationUtils.loadAnimation(getContext(), R.anim.device_oven_circle_rotate);
                                    LinearInterpolator lin = new LinearInterpolator();
                                    circleRotate.setInterpolator(lin);
                                }
                                imgContent.setImageDrawable(resource.getDrawable(R.mipmap.ic_device_oven_self_clean));
                                workType1.setText(cx.getString(R.string.device_steam_self_cleaning));
                                Log.i("imgSpinCircle", "333");
                                imgSpinCircle.startAnimation(circleRotate);

                            }

                            @Override
                            public void onFailure(Throwable t) {
                                ToastUtils.showThrowable(t);
                            }
                        });
                    }
                    hasStart = true;
                    break;
                case Pause:
                    Log.i("orderyiguodetail", "Pause" + oven.status);
                    oven.setOvenStatusControl(OvenStatus.Pause, new VoidCallback() {
                        @Override
                        public void onSuccess() {
                            imgPause.setVisibility(View.VISIBLE);
                            //txtCurrentTem.setClickable(true);
                            //txtCurrentTime.setClickable(true);
                            //imgTempReset.setVisibility(View.VISIBLE);
                            //imgTimeReset.setVisibility(View.VISIBLE);
                            Log.i("imgSpinCircle", "355");
                            imgSpinCircle.clearAnimation();
                            imgPause.bringToFront();
                        }

                        @Override
                        public void onFailure(Throwable t) {
                            ToastUtils.showThrowable(t);
                        }
                    });
                    break;
                case Working:
                    Log.i("orderyiguodetail", "Working" + oven.status);
                    oven.setOvenStatusControl(OvenStatus.Working, new VoidCallback() {
                        @Override
                        public void onSuccess() {
                            imgPause.setVisibility(View.GONE);
                            imgTempReset.setVisibility(View.GONE);
                            imgTimeReset.setVisibility(View.GONE);
                            txtCurrentTem.setClickable(false);
                            if (circleRotate == null) {
                                circleRotate = AnimationUtils.loadAnimation(getContext(), R.anim.device_oven_circle_rotate);
                                LinearInterpolator lin = new LinearInterpolator();
                                circleRotate.setInterpolator(lin);
                            }
                            Log.i("imgSpinCircle", "379");
                            imgSpinCircle.startAnimation(circleRotate);
                            handler.sendEmptyMessage(CountDown);
                            canCountDown = true;
                        }

                        @Override
                        public void onFailure(Throwable t) {

                        }
                    });
                    break;
                case Done:
                    try {
                        Log.i("imgSpinCircle", "394");
                        circleRotate.cancel();circleRotate=null;
                        imgSpinCircle.clearAnimation();
                        imgSpinCircle.setVisibility(View.GONE);
                        imgDone.setVisibility(View.VISIBLE);
                        imgContent.setVisibility(View.GONE);
                        workType1.setVisibility(View.GONE);
                        workType2.setVisibility(View.VISIBLE);
                        txtCurrentTime.setText("0");timeLock=true;
                        // 10min后自动关机
                        handler.sendEmptyMessageDelayed(ReturnHome, 3 * 1000);
                        break;
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        break;
                    }
                case CountDown://用于温度和时间的更新
                    try {
                        if (oven.status == OvenStatus.Working) {
                            Log.i("test01", "oven.status:" + oven.status);
                            int min = (int) (oven.time / 60.0);
                            Log.i("test01", "remainTime:" + remainTime);
                            int leftMin = oven.time - min * 60;
                            Log.i("test01", "min:" + min);
                            if (leftMin > 0)
                                min++;
                            Log.i("test02", "min947" + min);
                            if(!timeLock)
                                txtCurrentTime.setText(String.valueOf(min));
                            remainTime--;
                            txtCurrentTem.setText(String.valueOf(oven.temp));
                            handler.sendEmptyMessageDelayed(CountDown, 1000);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        break;
                    }
                case Light:
                    Log.e("light", String.valueOf(oven.light));
                    Log.e("revolve", String.valueOf(oven.revolve));
                    if (oven.light == 0) {
                        oven.setOvenSpitRotateLightControl(oven.revolve, (short) 1, (short) 0, new VoidCallback() {
                            @Override
                            public void onSuccess() {
                                imgLight.setImageDrawable(resource.getDrawable(R.mipmap.ic_device_fan_light_selected));
                                rlLight.setBackground(resource.getDrawable(R.mipmap.img_oven_working_circle_yellow));
                            }

                            @Override
                            public void onFailure(Throwable t) {
                                ToastUtils.showThrowable(t);
                            }
                        });
                    } else if (oven.light == 1) {
                        oven.setOvenSpitRotateLightControl(oven.revolve, (short) 0, (short) 0, new VoidCallback() {
                            @Override
                            public void onSuccess() {
                                imgLight.setImageDrawable(resource.getDrawable(R.mipmap.ic_device_fan_light_normal));
                                rlLight.setBackground(resource.getDrawable(R.mipmap.ic_device_oven_gray_circle));
                            }

                            @Override
                            public void onFailure(Throwable t) {
                                ToastUtils.showThrowable(t);
                            }
                        });
                    }
                    break;
                case Rotate:
                    if (oven.revolve == 0) {
                        oven.setOvenSpitRotateLightControl((short) 1, oven.light, (short) 0, new VoidCallback() {
                            @Override
                            public void onSuccess() {
                                if (spitRotate == null) {
                                    spitRotate = AnimationUtils.loadAnimation(getContext(), R.anim.device_oven_spit_rotate);
                                    LinearInterpolator lin = new LinearInterpolator();
                                    spitRotate.setInterpolator(lin);
                                }
                                imgRotate.setImageDrawable(resource.getDrawable(R.mipmap.img_device_oven_rotate_open));
//                                imgRotate.startAnimation(spitRotate);
                            }

                            @Override
                            public void onFailure(Throwable t) {
                                ToastUtils.showThrowable(t);
                            }
                        });
                    } else if (oven.revolve == 1) {
                        oven.setOvenSpitRotateLightControl((short) 0, oven.light, (short) 0, new VoidCallback() {
                            @Override
                            public void onSuccess() {
                                imgRotate.clearAnimation();
                                imgRotate.setImageDrawable(resource.getDrawable(R.mipmap.img_device_oven_rotate_close));
                            }

                            @Override
                            public void onFailure(Throwable t) {
                                ToastUtils.showThrowable(t);
                            }
                        });
//                        imgRotate.clearAnimation();
                    }
                    break;
                case Reset:
                    Log.i("orderyiguodetail", "Reset" + oven.status);
                    final NormalModeItemMsg message = (NormalModeItemMsg) msg.obj;
                    imgTempReset.setVisibility(View.GONE);
                    imgTimeReset.setVisibility(View.GONE);


                    if (oven.runP == 1)
                        oven.setOvenQuickHeating(Short.valueOf(message.getTime()), Short.valueOf(message.getTemperature()), (short) 0, new VoidCallback() {
                            @Override
                            public void onSuccess() {
                                txtTemSet.setText(message.getTemperature());
                                txtTimeSet.setText(message.getTime());
                                remainTime = Short.valueOf(message.getTime()) * 60;

                                if (circleRotate == null) {
                                    circleRotate = AnimationUtils.loadAnimation(getContext(), R.anim.device_oven_circle_rotate);
                                    LinearInterpolator lin = new LinearInterpolator();
                                    circleRotate.setInterpolator(lin);
                                    Log.i("imgSpinCircle", "504");
                                    imgSpinCircle.startAnimation(circleRotate);
                                }
                            }

                            @Override
                            public void onFailure(Throwable t) {
                                ToastUtils.showThrowable(t);
                            }
                        });
                    if (oven.runP == 2)
                        oven.setOvenAirBaking(Short.valueOf(message.getTime()), Short.valueOf(message.getTemperature()), (short) 0, new VoidCallback() {
                            @Override
                            public void onSuccess() {
                                txtTemSet.setText(message.getTemperature());
                                txtTimeSet.setText(message.getTime());
                                remainTime = Short.valueOf(message.getTime()) * 60;
                            }

                            @Override
                            public void onFailure(Throwable t) {
                                ToastUtils.showThrowable(t);
                            }
                        });
                    if (oven.runP == 3)
                        oven.setOvenToast(Short.valueOf(message.getTime()), Short.valueOf(message.getTemperature()), (short) 0, new VoidCallback() {
                            @Override
                            public void onSuccess() {
                                txtTemSet.setText(message.getTemperature());
                                txtTimeSet.setText(message.getTime());
                                remainTime = Short.valueOf(message.getTime()) * 60;
                                if (circleRotate == null) {
                                    circleRotate = AnimationUtils.loadAnimation(getContext(), R.anim.device_oven_circle_rotate);
                                    LinearInterpolator lin = new LinearInterpolator();
                                    circleRotate.setInterpolator(lin);
                                    Log.i("imgSpinCircle", "539");
                                    imgSpinCircle.startAnimation(circleRotate);
                                }
                            }

                            @Override
                            public void onFailure(Throwable t) {
                                ToastUtils.showThrowable(t);
                            }
                        });
                    if (oven.runP == 4)
                        oven.setOvenBottomHeating(Short.valueOf(message.getTime()), Short.valueOf(message.getTemperature()), (short) 0, new VoidCallback() {
                            @Override
                            public void onSuccess() {
                                txtTemSet.setText(message.getTemperature());
                                txtTimeSet.setText(message.getTime());
                                remainTime = Short.valueOf(message.getTime()) * 60;
                                if (circleRotate == null) {
                                    circleRotate = AnimationUtils.loadAnimation(getContext(), R.anim.device_oven_circle_rotate);
                                    LinearInterpolator lin = new LinearInterpolator();
                                    circleRotate.setInterpolator(lin);
                                    Log.i("imgSpinCircle", "561");
                                    imgSpinCircle.startAnimation(circleRotate);
                                }
                            }

                            @Override
                            public void onFailure(Throwable t) {
                                ToastUtils.showThrowable(t);
                            }
                        });
                    if (oven.runP == 5)
                        oven.setOvenUnfreeze(Short.valueOf(message.getTime()), Short.valueOf(message.getTemperature()), (short) 0, new VoidCallback() {
                            @Override
                            public void onSuccess() {
                                txtTemSet.setText(message.getTemperature());
                                txtTimeSet.setText(message.getTime());
                                remainTime = Short.valueOf(message.getTime()) * 60;
                                if (circleRotate == null) {
                                    circleRotate = AnimationUtils.loadAnimation(getContext(), R.anim.device_oven_circle_rotate);
                                    LinearInterpolator lin = new LinearInterpolator();
                                    circleRotate.setInterpolator(lin);
                                    Log.i("imgSpinCircle", "582");
                                    imgSpinCircle.startAnimation(circleRotate);
                                }
                            }

                            @Override
                            public void onFailure(Throwable t) {
                                ToastUtils.showThrowable(t);
                            }
                        });
                    if (oven.runP == 6)
                        oven.setOvenAirBarbecue(Short.valueOf(message.getTime()), Short.valueOf(message.getTemperature()), (short) 0, new VoidCallback() {
                            @Override
                            public void onSuccess() {
                                txtTemSet.setText(message.getTemperature());
                                txtTimeSet.setText(message.getTime());
                                remainTime = Short.valueOf(message.getTime()) * 60;
                                if (circleRotate == null) {
                                    circleRotate = AnimationUtils.loadAnimation(getContext(), R.anim.device_oven_circle_rotate);
                                    LinearInterpolator lin = new LinearInterpolator();
                                    circleRotate.setInterpolator(lin);
                                    Log.i("imgSpinCircle", "602");
                                    imgSpinCircle.startAnimation(circleRotate);
                                }
                            }

                            @Override
                            public void onFailure(Throwable t) {
                                ToastUtils.showThrowable(t);
                            }
                        });
                    if (oven.runP == 7)
                        oven.setOvenBarbecue(Short.valueOf(message.getTime()), Short.valueOf(message.getTemperature()), (short) 0, new VoidCallback() {
                            @Override
                            public void onSuccess() {
                                txtTemSet.setText(message.getTemperature());
                                txtTimeSet.setText(message.getTime());
                                remainTime = Short.valueOf(message.getTime()) * 60;
                                if (circleRotate == null) {
                                    circleRotate = AnimationUtils.loadAnimation(getContext(), R.anim.device_oven_circle_rotate);
                                    LinearInterpolator lin = new LinearInterpolator();
                                    circleRotate.setInterpolator(lin);
                                    Log.i("imgSpinCircle", "624");
                                    imgSpinCircle.startAnimation(circleRotate);
                                }
                            }

                            @Override
                            public void onFailure(Throwable t) {
                                ToastUtils.showThrowable(t);
                            }
                        });
                    if (oven.runP == 8)
                        oven.setOvenStrongBarbecue(Short.valueOf(message.getTime()), Short.valueOf(message.getTemperature()), (short) 0, new VoidCallback() {
                            @Override
                            public void onSuccess() {
                                txtTemSet.setText(message.getTemperature());
                                txtTimeSet.setText(message.getTime());
                                remainTime = Short.valueOf(message.getTime()) * 60;
                            }

                            @Override
                            public void onFailure(Throwable t) {
                                ToastUtils.showThrowable(t);
                            }
                        });
                    circleRotate = AnimationUtils.loadAnimation(getContext(), R.anim.device_oven_circle_rotate);
                    LinearInterpolator lin = new LinearInterpolator();
                    circleRotate.setInterpolator(lin);
                    Log.i("imgSpinCircle", "654");
                    imgSpinCircle.startAnimation(circleRotate);
                    handler.sendEmptyMessage(PollStatus);
                    break;

                case PollStatus:
                    try {
                        Log.i("testrent", "PollStatus" + oven.status);
                        Log.e("temp", String.valueOf(oven.setTemp));
                        if (oven.status == OvenStatus.On) {
                            UIService.getInstance().popBack();
                        }
                        if (oven.status == OvenStatus.Off) {
                            UIService.getInstance().popBack();
                        }
                        if (oven.status == OvenStatus.Working || oven.status == OvenStatus.Pause) {
                            if (runType != oven.runP || oven.setTime != preTime ||
                                    oven.setTemp != preTemp) {
                                if (isFirst) {
                                    isFirst = false;
                                } else {
                                    initViewType();
                                    runType = oven.runP;
                                    preTemp = oven.setTemp;
                                    preTime = oven.setTime;
                                }
                            }

                        }
                        //判断完成后关机
                        int min = oven.time / 60;
                        Log.i("testrent", "oven.time:" + oven.time);
                        int leftMin = oven.time - min * 60;
                        Log.i("testrent", "leftMin:" + leftMin);
                        if (leftMin > 0)
                            min++;
                        if(Build.VERSION.SDK_INT>=23){
                            if (1<=oven.time&&oven.time <=10) {//关机
                                if(!handlerLock){
                                    handlerLock=true;
                                    handler.sendEmptyMessageDelayed(Done,oven.time*1000);
                                }
                            }
                        }else{
                            if (oven.time == 1||oven.time == 2||oven.time == 3||oven.time == 4) {//关机
                                if(!handlerLock){
                                    handlerLock=true;
                                    handler.sendEmptyMessageDelayed(Done,oven.time*1000);
                                }
                            }
                        }

                        preStatus = currentStatus;
                        currentStatus = oven.status;
                        if(!timeLock)
                            txtCurrentTime.setText(String.valueOf(min));
                        checkRotate();
                        if (preStatus == OvenStatus.Working && currentStatus != preStatus && currentStatus != OvenStatus.Off
                                && currentStatus != OvenStatus.Pause && currentStatus != OvenStatus.Working && currentStatus != 18 && preStatus != 18)
                            if (oven.status == OvenStatus.Off) {
                                UIService.getInstance().popBack();
                            }
                        if (oven.status == OvenStatus.Pause) {
                            imgPause.setVisibility(View.VISIBLE);
                            txtCurrentTem.setClickable(true);
                            txtCurrentTime.setClickable(true);
                            Log.i("imgSpinCircle", "710");
                            imgSpinCircle.clearAnimation();
                            imgPause.bringToFront();
                        } else if (oven.status == OvenStatus.Working) {
                            imgPause.setVisibility(View.GONE);
                            imgTempReset.setVisibility(View.GONE);
                            imgTimeReset.setVisibility(View.GONE);
                            txtCurrentTem.setClickable(false);
                            txtCurrentTime.setClickable(false);
                            if (circleRotate == null) {
                                circleRotate = AnimationUtils.loadAnimation(getContext(), R.anim.device_oven_circle_rotate);
                                LinearInterpolator lin1 = new LinearInterpolator();
                                circleRotate.setInterpolator(lin1);
                                Log.i("imgSpinCircle", "726");
                                imgSpinCircle.clearAnimation();
                                imgSpinCircle.startAnimation(circleRotate);
                            } else {
                                if (preStatus != OvenStatus.Working) {
                                    Log.i("imgSpinCircle", "731");
                                    imgSpinCircle.clearAnimation();
                                    imgSpinCircle.startAnimation(circleRotate);
                                }
                            }
//                        if (oven.time < lastTime) {
//                        handler.sendEmptyMessage(15);
                            Log.i("orderyiguodetail", "oven.time:" + oven.time + "lastTime:" + lastTime);
                            Log.i("orderyiguodetail", "canCountDown" + canCountDown);
                            if (oven.time < lastTime) {
                                Log.i("orderyiguodetail", "oven.time < lastTime");
                                if (!canCountDown) {
                                    handler.sendEmptyMessage(CountDown);
                                    canCountDown = true;
                                }
                            } else {
                                Log.i("orderyiguodetail", "oven.time>lastTime");
                                lastTime = oven.time;
                                canCountDown = false;
                            }
                        } else if (oven.status == OvenStatus.Wait) {
                            /*if (dlg != null && dlg.isShowing()) {
                                dlg.dismiss();dlg=null;
                            }*/
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        break;
                    }
                case ReturnHome:
                    try {
                        Log.i("orderyiguodetail", "ReturnHome");
                        Log.i("imgSpinCircle", "759");
                        imgSpinCircle.clearAnimation();
                        imgRotate.clearAnimation();
                        UIService.getInstance().returnHome();
                    } catch (Exception e) {
                        e.printStackTrace();
                        break;
                    } finally {
                        break;
                    }
                case RefreshTime:
                    try {
                        Log.i("orderyiguodetail", "RefreshTime");
                        int minute = oven.time / 60;
                        Log.i("orderyiguodetail", "oven.time" + oven.time);
                        int minuteLeft = oven.time - minute * 60;
                        Log.i("orderyiguodetail", "minuteLeft" + minuteLeft);
                        if (minuteLeft > 0)
                            minute++;
                        Log.i("test02", "minute" + minute);
                        if(!timeLock)
                            txtCurrentTime.setText(String.valueOf(minute));
//                    txtCurrentTime.setText(TimeUtils.sec2clock(50));
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        break;
                    }
                    case 11:
                        UIService.getInstance().returnHome();
                        break;
                default:
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        Bundle bd = getArguments();
        guid = bd == null ? null : bd.getString(PageArgumentKey.Guid);
        NormalModeItemMsg msg = bd == null ? null : (NormalModeItemMsg) bd.getSerializable("msg");
        if (msg != null) {
            preTime = Short.valueOf(msg.getTime());
            preTemp = Short.valueOf(msg.getTemperature());
            type = msg.getType();
        }
        resource = getResources();
        oven = Plat.deviceService.lookupChild(guid);
//        oven.setTime = preTime;
//        oven.setTemp = preTemp;
        contentView = inflater.inflate(R.layout.page_device_oven_working,
                container, false);
        ButterKnife.inject(this, contentView);

       /*

        @InjectView(R.id.imgRotate)
        ImageView imgRotate;
        @InjectView(R.id.imgReturn)
        ImageView imgReturn;
        @InjectView(R.id.txtRecipe)
        TextView txtRecipe;
        @InjectView(R.id.imgTempReset)
        ImageView imgTempReset;
        @InjectView(R.id.imgTimeReset)
        ImageView imgTimeReset;

        @InjectView(R.id.txtTemSet)
        TextView txtTemSet;
        @InjectView(R.id.txtTimeSet)
        TextView txtTimeSet;*/

        txtCurrentTem = contentView.findViewById(R.id.txtCurrentTem);
        txtCurrentTime = contentView.findViewById(R.id.txtCurrentTime);
        imgSpinCircle = contentView.findViewById(R.id.imgSpinCircle);
        imgContent = contentView.findViewById(R.id.imgContent);
        imgPause = contentView.findViewById(R.id.imgPause);
        imgDone = contentView.findViewById(R.id.imgDone);
        workType1 = contentView.findViewById(R.id.workType1);
        workType2 = contentView.findViewById(R.id.workType2);
        imgLight = contentView.findViewById(R.id.imgLight);
        rlLight = contentView.findViewById(R.id.rlLight);
        imgRotate = contentView.findViewById(R.id.imgRotate);
        imgReturn = contentView.findViewById(R.id.imgReturn);
        txtRecipe = contentView.findViewById(R.id.txtRecipe);
        imgTempReset = contentView.findViewById(R.id.imgTempReset);
        imgTimeReset = contentView.findViewById(R.id.imgTimeReset);
        txtTemSet = contentView.findViewById(R.id.txtTemSet);
        txtTimeSet= contentView.findViewById(R.id.txtTimeSet);




//        Log.e("temp", String.valueOf(oven.setTemp));
        if (oven.alarm == Oven039.Event_Oven_Alarm_Senor_Open || oven.alarm == Oven039.Event_Oven_Alarm_ok
                || oven.alarm == Oven039.Event_Oven_Alarm_Senor_Short) {
            //checkBroken();
        }
        if (oven.status == OvenStatus.Pause || oven.status == OvenStatus.Working) {//烤箱处于暂停或者工作状态
            Log.i("orderyiguodetail", "烤箱处于暂停或者工作状态" + oven.status);
            initView();
        }
        if (oven.status == OvenStatus.On) {//烤箱开机但是未开始工作
            fromSetting = true;
            Log.i("orderyiguodetail", "onCreateView()--》oven.status" + oven.status);
            restoreView();
        }
        initState();
        //startTimer();
        return contentView;
    }

    private void initState() {
        // --------设置titleBar--------

        TextView cookbook = TitleBar.newTitleTextView(getContext(), cx.getString(R.string.home_search_title_recipe), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO 跳转菜谱
            }
        });

//        txtTemSet.setText(oven.temp);
//        txtTimeSet.setText(oven.time);
        readyToWork();
    }

    private void initViewType() {
        Log.e("temp", String.valueOf(oven.setTemp));
        txtCurrentTem.setText(String.valueOf(oven.temp));
        int min = oven.time / 60;
        int leftMin = oven.time - min * 60;
        if (leftMin > 0)
            min++;
        Log.e("min", String.valueOf(min));
        txtTemSet.setText(String.valueOf(oven.setTemp));
        txtTimeSet.setText(String.valueOf(oven.setTime));
        Log.i("test02", "min891" + min);
        if(!timeLock)
            txtCurrentTime.setText(String.valueOf(min));
        remainTime = oven.time;
        //更新图标以及模式
        if (oven.runP == 1) {
            workType1.setText(cx.getString(R.string.device_mode_kuaire));
            imgContent.setImageResource(R.mipmap.img_oven_quick_heating_working);
        } else if (oven.runP == 2) {
            workType1.setText(cx.getString(R.string.device_mode_fengpeikao));
            imgContent.setImageResource(R.mipmap.img_oven_air_barking_working);
        } else if (oven.runP == 3) {
            workType1.setText(cx.getString(R.string.device_steamOvenOne_name_peikao));
            imgContent.setImageResource(R.mipmap.img_oven_toast_working);
        } else if (oven.runP == 4) {
            workType1.setText(cx.getString(R.string.device_steamOvenOne_name_dijiare));
            imgContent.setImageResource(R.mipmap.img_oven_bottom_heating_working);
        } else if (oven.runP == 5) {
            workType1.setText(cx.getString(R.string.device_steamOvenOne_name_jiedong));
            imgContent.setImageResource(R.mipmap.img_oven_unfreezing_working);
        } else if (oven.runP == 6) {
            workType1.setText(cx.getString(R.string.device_steamOvenOne_name_fengshankao));
            imgContent.setImageResource(R.mipmap.img_oven_air_barbecue_working);
        } else if (oven.runP == 7) {
            workType1.setText(cx.getString(R.string.device_steamOvenOne_name_kaoshao));
            imgContent.setImageResource(R.mipmap.img_oven_barbecue_working);
        } else if (oven.runP == 8) {
            workType1.setText(cx.getString(R.string.device_steamOvenOne_name_qiangshaokao));
            imgContent.setImageResource(R.mipmap.img_oven_strong_barbecue_working);
        }
    }

    private void initView() {
        initViewType();
        if (oven.status == OvenStatus.Pause) {//如果是暂停状态
            imgPause.setVisibility(View.VISIBLE);
            //imgTempReset.setVisibility(View.VISIBLE);
            //imgTimeReset.setVisibility(View.VISIBLE);
            txtCurrentTem.setClickable(true);
            txtCurrentTime.setClickable(true);
            Log.i("imgSpinCircle", "885");
            imgSpinCircle.clearAnimation();
            imgPause.bringToFront();
        } else {
            imgPause.setVisibility(View.GONE);//工作状态
            imgTempReset.setVisibility(View.GONE);
            imgTimeReset.setVisibility(View.GONE);
            txtCurrentTem.setClickable(false);
            txtCurrentTime.setClickable(false);
            if (circleRotate == null) {
                circleRotate = AnimationUtils.loadAnimation(getContext(), R.anim.device_oven_circle_rotate);
                LinearInterpolator lin = new LinearInterpolator();
                circleRotate.setInterpolator(lin);
            }
            Log.i("imgSpinCircle", "898");
            imgSpinCircle.startAnimation(circleRotate);
            if (canCountDown) {
                handler.sendEmptyMessage(CountDown);
                canCountDown = true;
            }
        }
    }

    private void restoreView() {

        txtTimeSet.setText(String.valueOf(preTime));
        txtTemSet.setText(String.valueOf(preTemp));
        remainTime = preTime;
        handler.sendEmptyMessageDelayed(Start, 3000);

    }


    private Resources resource;

    private void checkRotate() {
        if (oven.revolve == 1)
            imgRotate.setImageDrawable(resource.getDrawable(R.mipmap.img_device_oven_rotate_open));
        else
            imgRotate.setImageDrawable(resource.getDrawable(R.mipmap.img_device_oven_rotate_close));
    }

    private void readyToWork() {
        lastTime = 0;
        if (oven.status == OvenStatus.On || oven.status == OvenStatus.Wait) {
            IRokiDialog dialogByType = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_07);
            dialogByType.show();
        }
    }


    @Subscribe
    public void onEvent(OvenStatusChangedEvent event) {
        if (oven == null || !Objects.equal(oven.getID(), event.pojo.getID())) {
            return;
        }
        handler.sendEmptyMessage(PollStatus);
    }

    @Subscribe
    public void onEvent(OvenSpitRotateResetEvent event) {
        handler.sendEmptyMessage(Rotate);

    }
    @Subscribe
    public void onEvent(DeviceConnectionChangedEvent event) {
        if (oven == null || !Objects.equal(oven.getID(), event.device.getID()))
            return;
        if (!event.isConnected) {
            UIService.getInstance().popBack();
        }
    }

    @OnClick({R.id.txtCurrentTem, R.id.txtCurrentTime})
    public void onClickCurTem() {
        if(true)return;
        NormalModeItemMsg msg = new NormalModeItemMsg();
        String s = cx.getString(R.string.device_preheat);
        short type = oven.runP;
        if (type == 1) {
            s = cx.getString(R.string.device_steamOvenOne_name_kuaire);
        } else if (type == 2) {
            s = cx.getString(R.string.device_steamOvenOne_name_fengpeikao);
        } else if (type == 3) {
            s = cx.getString(R.string.device_steamOvenOne_name_peikao);
        } else if (type == 4) {
            s = cx.getString(R.string.device_steamOvenOne_name_dijiare);
        } else if (type == 5) {
            s = cx.getString(R.string.device_steamOvenOne_name_jiedong);
        } else if (type == 6) {
            s = cx.getString(R.string.device_steamOvenOne_name_fengshankao);
        } else if (type == 7) {
            s = cx.getString(R.string.device_steamOvenOne_name_kaoshao);
        } else if (type == 8) {
            s = cx.getString(R.string.device_steamOvenOne_name_qiangshaokao);
        }
        msg.setType(s);
        pop = Helper.newOvenResetTwoSettingPicker(cx, new Callback2<NormalModeItemMsg>() {
            @Override
            public void onCompleted(final NormalModeItemMsg message) {
                Message msg = Message.obtain();
                msg.what = Reset;
                msg.obj = message;
                handler.sendMessage(msg);
            }
        }, msg);

        PopoupHelper.show(contentView, pop, Gravity.BOTTOM);
    }

    @OnClick(R.id.linSwitch)
    public void onClickSwitch() {
        final IRokiDialog closedialog = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_10);
        closedialog.setTitleText(R.string.close_work);
        closedialog.setContentText(R.string.is_close_work);
        closedialog.show();
        closedialog.setOkBtn(R.string.ok_btn, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closedialog.dismiss();
                oven.setOvenStatus(OvenStatus.On, new VoidCallback() {
                    @Override
                    public void onSuccess() {
                        Message message = handler.obtainMessage();
                        message.what = 11;
                        handler.sendMessage(message);
                    }
                    @Override
                    public void onFailure(Throwable t) {
                        ToastUtils.showThrowable(t);
                    }
                });
            }
        });

        closedialog.setCancelBtn(R.string.can_btn, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (closedialog.isShow()){
                    closedialog.dismiss();

                }
            }
        });

    }

    /*初始化设置页面时间温度图标动画等等*/
    private void initStart(String str,int res) {
        txtCurrentTem.setClickable(false);
        txtCurrentTime.setClickable(false);
        if (circleRotate == null) {
            circleRotate = AnimationUtils.loadAnimation(cx, R.anim.device_oven_circle_rotate);
            LinearInterpolator lin = new LinearInterpolator();
            circleRotate.setInterpolator(lin);
        }
        imgContent.setImageDrawable(resource.getDrawable(res));
        workType1.setText(str);
        Log.i("imgSpinCircle", "1082");
        imgSpinCircle.startAnimation(circleRotate);
        runType = oven.runP;
    }

    @OnClick(R.id.imgReturn)
    public void onClickReturn() {
        if (oven.status == OvenStatus.Working || oven.status == OvenStatus.Pause)
            UIService.getInstance().returnHome();
        else
            getFragmentManager().popBackStack();
    }

    @OnClick(R.id.txtRecipe)
    public void onClickRecipe() {
        // TODO 跳转菜谱
        //HomeRecipeView.recipeCategoryClick(DeviceType.RDKX);
        ToastUtils.show(R.string.please_look_forward_opening, Toast.LENGTH_SHORT);
    }


    @OnClick(R.id.imgContent)
    public void onClickContent() {
        if (oven.status == OvenStatus.Working) {
            handler.sendEmptyMessage(Pause);
        } else if (oven.status == OvenStatus.Pause) {
            handler.sendEmptyMessage(Working);
        }
    }

    @OnClick(R.id.rlLight)
    public void onClickLight() {
        light = oven.light;
        if (oven.status == OvenStatus.Working) {
            handler.sendEmptyMessage(Light);
        }
    }

    @OnClick(R.id.imgRotate)
    public void onClickRotate() {
        rotate = oven.revolve;
        if (oven.status == OvenStatus.Working) {
            handler.sendEmptyMessage(Rotate);
        }
    }

    private String getCurTem() {
        // 从硬件获取当前温度
        return String.valueOf(oven.temp);
    }

    private short getCurTime() {
        // 从硬件获取当前剩余时间
        return oven.time;
    }


// ---------------------------------------- 计时器代码段 -----------------------------------------

    /**
     * 倒计时单位时间
     */
    private Timer timer;

    private MyTimerTask timerTask;

    private void startTimer() {
        timer = new Timer();
        timerTask = new MyTimerTask();
        timer.scheduleAtFixedRate(timerTask, 1000 * 4, 30 * 1000);
    }

    private class MyTimerTask extends TimerTask {

        @Override
        public void run() {
//            txtCurrentTime.setText(TimeUtils.sec2clock(oven.time * 60));
//            txtCurrentTime.setText(TimeUtils.sec2clock(50 * 60));
            remainTime = oven.time;
            Log.i("MyTimerTask-->run()", oven.time + "");
            handler.sendEmptyMessage(RefreshTime);

        }

    }


    // ------------------------------------------  监控传感器代码段-----------------------------------

    // TODO 创建广播，接收传感器信息，包括蒸汽炉未关、缺水以及传感器错误


    // SteamOvenWarningDialog.show(getContext, string, type);

    //----------------------------------------- 手动退出当前页面 ------------------------------------


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            UIService.getInstance().returnHome();
        }
        return true;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (pop != null)
            pop.dismiss();
        ButterKnife.reset(this);
    }

}

