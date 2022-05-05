package com.robam.roki.ui.view.networkoptimization;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.legent.ui.ext.HeadPage;
import com.robam.roki.R;

import butterknife.ButterKnife;

/**
 * Created by sylar on 15/6/14.
 */
public class CantfindDevicePage extends HeadPage {

    @Override
    protected View onCreateContentView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View view = layoutInflater.inflate(R.layout.view_cantfind_model, viewGroup, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

}
