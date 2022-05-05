package com.robam.roki.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.robam.roki.R;

/**
 * Created by Administrator on 2016/4/14.
 */
public  class HoodStopWorkTipDialog extends Dialog {
    public  HoodStopWorkTipDialog(Context context) {
        super(context);
    }

    public  HoodStopWorkTipDialog(Context context, int theme) {
        super(context, theme);
    }

    public static class Builder {
        private Context context;
        public Builder(Context context) {
            this.context = context;
        }


        public HoodStopWorkTipDialog build() {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // instantiate the dialog with the custom Theme
            final HoodStopWorkTipDialog dialog = new HoodStopWorkTipDialog(context, R.style.Dialog);
            View layout = inflater.inflate(R.layout.dialog_hood_stopworking, null);
            dialog.addContentView(layout, new ViewGroup.LayoutParams(
                    LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            LinearLayout logoclick= layout.findViewById(R.id.dialog_hood_stop_working);
            logoclick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                 dialog.dismiss();
                }
            });
            dialog.setContentView(layout);
            dialog.setCancelable(true);
             return dialog;
        }
    }



}
