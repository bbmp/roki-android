package com.robam.roki.ui.page;

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
public class AbsHeadPage extends HeadPage {

    @Override
    protected View onCreateContentView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View view = layoutInflater.inflate(R.layout.page_welcome, viewGroup, false);
        ButterKnife.inject(this, view);
        return view;
    }


}
