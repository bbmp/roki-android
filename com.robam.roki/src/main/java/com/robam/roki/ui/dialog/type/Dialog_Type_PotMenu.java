package com.robam.roki.ui.dialog.type;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.legent.plat.pojos.device.DeviceConfigurationFunctions;
import com.robam.roki.R;
import com.robam.roki.listener.OnItemSelectedListenerCenter;
import com.robam.roki.listener.OnItemSelectedListenerFrone;
import com.robam.roki.listener.OnItemSelectedListenerRear;
import com.robam.roki.ui.adapter.AbsSteamProfessionalAdapter;
import com.robam.roki.ui.adapter3.Rv610ModeAdapter;
import com.robam.roki.ui.adapter3.RvPotMenuChoiceAdapter;
import com.robam.roki.ui.dialog.BaseDialog;
import com.robam.roki.ui.dialog.CoreDialog;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */

public class Dialog_Type_PotMenu extends BaseDialog {
    private View rootView;
    private RecyclerView myGridView;
    public RvPotMenuChoiceAdapter rvPotMenuChoiceAdapter;
    List<String> mData = new ArrayList<>();

    public Dialog_Type_PotMenu(Context context) {
        super(context);
    }
    public void setmData( List<String> mData) {
        this.mData= mData;
        rvPotMenuChoiceAdapter.addData(mData);
    }
    @Override
    public void initDialog() {
        rootView = LayoutInflater.from(mContext).inflate(R.layout.common_dialog_layout_pot_mune, null);
        myGridView = rootView.findViewById(R.id.mode_list);
        myGridView.setLayoutManager(new LinearLayoutManager(mContext ,   RecyclerView.VERTICAL , false));
        rvPotMenuChoiceAdapter = new RvPotMenuChoiceAdapter();
        myGridView.setAdapter(rvPotMenuChoiceAdapter);

        createDialog(rootView);
    }

    @Override
    protected void createDialog(View rootView) {
        if (mDialog == null) {
            mDialog = new CoreDialog(mContext, R.style.Dialog_Microwave_professtion_bottom, rootView, true);
            mDialog.setPosition(Gravity.BOTTOM, 0, 210);
        }
    }

    @Override
    public void bindAllListeners() {
//        mCancelTv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onCancelClick(v);
//            }
//        });
//        mOkTv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onOkClick(v);
//            }
//        });


    }

    public void setGridClickListener (AdapterView.OnItemClickListener listener) {
//        myGridView.setOnItemClickListener(listener);
    }


    public interface OnItemSelectOkListener{
        void onOkListener(String temperature, String time ,String temp2);
    }


}
