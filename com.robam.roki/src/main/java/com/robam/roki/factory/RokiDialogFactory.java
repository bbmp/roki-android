package com.robam.roki.factory;

import android.content.Context;

import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.legent.plat.pojos.device.DeviceConfigurationFunctions;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.listener.OnRecyclerViewItemClickListener;
import com.robam.roki.ui.dialog.type.DialogDiscoverNewVersion;
import com.robam.roki.ui.dialog.type.DialogDownNewVersion;
import com.robam.roki.ui.dialog.type.DialogTypeFanSuop_11;
import com.robam.roki.ui.dialog.type.DialogType_0;
import com.robam.roki.ui.dialog.type.DialogType_01;
import com.robam.roki.ui.dialog.type.DialogType_02;
import com.robam.roki.ui.dialog.type.DialogType_03;
import com.robam.roki.ui.dialog.type.DialogType_04;
import com.robam.roki.ui.dialog.type.DialogType_05;
import com.robam.roki.ui.dialog.type.DialogType_06;
import com.robam.roki.ui.dialog.type.DialogType_07;
import com.robam.roki.ui.dialog.type.DialogType_08;
import com.robam.roki.ui.dialog.type.DialogType_09;
import com.robam.roki.ui.dialog.type.DialogType_10;
import com.robam.roki.ui.dialog.type.DialogType_12;
import com.robam.roki.ui.dialog.type.DialogType_13;
import com.robam.roki.ui.dialog.type.DialogType_14;
import com.robam.roki.ui.dialog.type.DialogType_15;
import com.robam.roki.ui.dialog.type.DialogType_16;
import com.robam.roki.ui.dialog.type.DialogType_17;
import com.robam.roki.ui.dialog.type.DialogType_18;
import com.robam.roki.ui.dialog.type.DialogType_19;
import com.robam.roki.ui.dialog.type.DialogType_20;
import com.robam.roki.ui.dialog.type.DialogType_21;
import com.robam.roki.ui.dialog.type.DialogType_22;
import com.robam.roki.ui.dialog.type.DialogType_23;
import com.robam.roki.ui.dialog.type.DialogType_24;
import com.robam.roki.ui.dialog.type.DialogType_25;
import com.robam.roki.ui.dialog.type.DialogType_26;
import com.robam.roki.ui.dialog.type.DialogType_27;
import com.robam.roki.ui.dialog.type.DialogType_29;
import com.robam.roki.ui.dialog.type.DialogType_30;
import com.robam.roki.ui.dialog.type.DialogType_H_Finish_Work;
import com.robam.roki.ui.dialog.type.DialogType_Time;
import com.robam.roki.ui.dialog.type.DialogUpdateCompletion;
import com.robam.roki.ui.dialog.type.DialogUpdateFailed;
import com.robam.roki.ui.dialog.type.Dialog_Type_27;
import com.robam.roki.utils.DialogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 公共对话框创建工厂
 * 创建对象的时候就会初始化对话框布局了，
 * 请在需要弹对话框的地方才执行创建操作
 */

public class RokiDialogFactory {

    public static IRokiDialog createDialogByType(Context context, int dialogType) {
        IRokiDialog rokiDialog = null;

//        ArrayList<String>
        switch (dialogType) {
            case DialogUtil.DIALOG_TYPE_00:
                rokiDialog = new DialogType_0(context);
                break;
            case DialogUtil.DIALOG_TYPE_01:
                rokiDialog = new DialogType_01(context);
                break;
            case DialogUtil.DIALOG_TYPE_02:
                rokiDialog = new DialogType_02(context);
                break;
            case DialogUtil.DIALOG_TYPE_03:
                rokiDialog = new DialogType_03(context);
                break;
            case DialogUtil.DIALOG_TYPE_04:
                rokiDialog = new DialogType_04(context);
                break;
            case DialogUtil.DIALOG_TYPE_05:
                rokiDialog = new DialogType_05(context);
                break;
            case DialogUtil.DIALOG_TYPE_06:
                rokiDialog = new DialogType_06(context);
                break;
            case DialogUtil.DIALOG_TYPE_07:
                rokiDialog = new DialogType_07(context);
                break;
            case DialogUtil.DIALOG_TYPE_08:
                rokiDialog = new DialogType_08(context);
                break;
            case DialogUtil.DIALOG_TYPE_09:
                rokiDialog = new DialogType_09(context);
                break;
            case DialogUtil.DIALOG_TYPE_10:
                rokiDialog = new DialogType_10(context);
                break;
            case DialogUtil.DIALOG_TYPE_11:
                rokiDialog = new DialogTypeFanSuop_11(context);
                break;
            case DialogUtil.DIALOG_TYPE_12:
                rokiDialog = new DialogType_12(context);
                break;
            case DialogUtil.DIALOG_TYPE_13:
                rokiDialog = new DialogType_13(context);
                break;
            case DialogUtil.DIALOG_TYPE_14:
                rokiDialog = new DialogType_14(context);
                break;
            case DialogUtil.DIALOG_TYPE_15:
                rokiDialog = new DialogType_15(context);
                break;
            case DialogUtil.DIALOG_TYPE_16:
                rokiDialog = new DialogType_16(context);
                break;
            case DialogUtil.DIALOG_TYPE_17:
                rokiDialog = new DialogType_17(context);
                break;
            case DialogUtil.DIALOG_TYPE_18:
                rokiDialog = new DialogType_18(context);
                break;
            case DialogUtil.DIALOG_TYPE_19:
                rokiDialog = new DialogType_19(context);
                break;
            case DialogUtil.DIALOG_TYPE_20:
                rokiDialog = new DialogType_20(context);
                break;
            case DialogUtil.DIALOG_TYPE_21:
                rokiDialog = new DialogType_21(context);
                break;
            case DialogUtil.DIALOG_TYPE_22:
                rokiDialog = new DialogType_22(context);
                break;
            case DialogUtil.DIALOG_TYPE_23:
                rokiDialog = new DialogType_23(context);
                break;
            case DialogUtil.DIALOG_TYPE_24:
                rokiDialog = new DialogType_24(context);
                break;
            case DialogUtil.DIALOG_TYPE_25:
                rokiDialog = new DialogType_25(context);
                break;

            case DialogUtil.DIALOG_DISCOVER_NEW_VERSION:
                rokiDialog = new DialogDiscoverNewVersion(context);
                break;
            case DialogUtil.DIALOG_DOWN_NEW_VERSION:
                rokiDialog = new DialogDownNewVersion(context);
                break;
            case DialogUtil.DIALOG_UPDATE_FAILED:
                rokiDialog = new DialogUpdateFailed(context);
                break;
            case DialogUtil.DIALOG_UPDATE_COMPLETION:
                rokiDialog = new DialogUpdateCompletion(context);
                break;
            case DialogUtil.DIALOG_TYPE_26:
                rokiDialog = new DialogType_26(context);
                break;
            case DialogUtil.DIALOG_TYPE_27:
                rokiDialog = new DialogType_27(context);
                break;
            case DialogUtil.DIALOG_TYPE_TIME:
                rokiDialog = new DialogType_Time(context);
                break;
            case DialogUtil.DIALOG_TYPE_TIME_EXTEND:
                rokiDialog = new DialogType_29(context);
                break;
            case DialogUtil.DIALOG_CHOICE_STOVE:
                rokiDialog = new DialogType_30(context);
                break;
            case DialogUtil.DIALOG_H_FINISH_WORK:
                rokiDialog = new DialogType_H_Finish_Work(context);
                break;
            default:
                break;
        }
        return rokiDialog;
    }
    public static IRokiDialog createDialogByType27(Context context, List<DeviceConfigurationFunctions> deviceConfigurationFunctions) {
        IRokiDialog rokiDialog = null;

                rokiDialog = new Dialog_Type_27(context , deviceConfigurationFunctions);

        return rokiDialog;
    }
    public static Dialog_Type_27 createDialogByType27(Context context, List<DeviceConfigurationFunctions> deviceConfigurationFunctions, OnItemClickListener onItemClickListener) {
        Dialog_Type_27 rokiDialog = null;

        rokiDialog = new Dialog_Type_27(context , deviceConfigurationFunctions ,onItemClickListener);

        return rokiDialog;
    }
    public static DialogType_30 createDialogType_30(Context context) {
        DialogType_30 rokiDialog = null;

        rokiDialog = new DialogType_30(context );

        return rokiDialog;
    }
}
