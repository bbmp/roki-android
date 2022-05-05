package com.robam.roki.ui.page;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.legent.ui.UIService;
import com.legent.ui.ext.BasePage;
import com.robam.roki.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class TellRokiSucessPage extends BasePage {


    @InjectView(R.id.tv_back_home)
    TextView tvBackHome;

    public TellRokiSucessPage() {
        // Required empty public constructor
    }


    public static TellRokiSucessPage newInstance(String param1, String param2) {
        TellRokiSucessPage fragment = new TellRokiSucessPage();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tell_roki_sucess, container, false);
        // Inflate the layout for this fragment
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnClick(R.id.tv_back_home)
    public void onClickToHomePage() {
        UIService.getInstance().returnHome();
    }

}