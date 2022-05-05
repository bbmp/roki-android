package com.robam.roki.ui.helper3;

import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.view.View;

import com.legent.plat.Plat;
import com.legent.ui.UIService;
import com.legent.utils.api.PreferenceUtils;
import com.robam.common.ui.UiHelper;
import com.robam.roki.R;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.utils.DialogUtil;
import com.robam.roki.utils.UiUtils;

public class DialogHelper {
    private static int tag = 0 ;
    private static IRokiDialog dialog;
    public static void notNetDialog(Context context) {
        if (tag != 0){
            return;
        }
        if (dialog == null){
            dialog = RokiDialogFactory.createDialogByType(context, DialogUtil.DIALOG_TYPE_26);
            dialog.setTitle("检查网络设置");
            dialog.setTitleText(R.string.net_not);
            dialog.show();
            dialog.setCancelBtn(R.string.can_btn, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tag = 1;
                }
            });
            dialog.setOkBtn(R.string.my_setting, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tag = 1 ;
                    dialog.dismiss();
                    Intent intent = new Intent(Settings.ACTION_SETTINGS);
                    context.startActivity(intent);
                }
            });
        }else {
            if (!dialog.isShow()){
                dialog.show();
            }
        }

    }
}
