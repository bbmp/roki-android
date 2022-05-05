package com.robam.roki.ui.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.legent.ui.ext.dialogs.DialogHelper;
import com.legent.utils.api.ViewUtils;
import com.robam.roki.R;

public class SteriTimeDialog {

    public static void show(Context cx, String title, String unit, final int min,
                            final int max, int value, final NumberSeletedCallback callback) {
        View inflate = LayoutInflater.from(cx).inflate(R.layout.steri_order, null);
        TextView unitView = inflate.findViewById(R.id.tv_time_unit);
        unitView.setText(unit);
        final NumberPicker numberPicker = inflate.findViewById(R.id.hour_picker);
        numberPicker.setFocusable(false);
        numberPicker.setMinValue(min);
        numberPicker.setMaxValue(max);
        numberPicker.setValue(value);
        numberPicker
                .setDescendantFocusability(TimePicker.FOCUS_BLOCK_DESCENDANTS);

        AlertDialog dialog = DialogHelper.newDialog_OkCancel(cx, title, null,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // numberPicker.clearFocus();
                        ViewUtils.setDialogShowField(dialog, true);
                        if (which == DialogInterface.BUTTON_POSITIVE) {
                            int v = numberPicker.getValue();
                            if (v < min || v > max) {
                                ViewUtils.setDialogShowField(dialog, false);
                            } else {
                                if (callback != null) {
                                    callback.onNumberSeleted(v);
                                }
                            }

                        }
                    }
                });

        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);
        dialog.setView(inflate);
        dialog.show();
    }

    public interface NumberSeletedCallback {
        void onNumberSeleted(int value);
    }
}
