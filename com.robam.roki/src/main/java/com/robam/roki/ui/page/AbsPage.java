package com.robam.roki.ui.page;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.legent.ui.ext.BasePage;
import com.robam.roki.R;

import butterknife.ButterKnife;

/**
 * Created by sylar on 15/6/14.
 */
public class AbsPage extends BasePage {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.page_welcome, container, false);
        ButterKnife.inject(this, view);
        return view;
    }
}
