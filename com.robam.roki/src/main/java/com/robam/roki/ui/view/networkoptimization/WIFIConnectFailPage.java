package com.robam.roki.ui.view.networkoptimization;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
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

/**
 * Created by Administrator on 2017/2/22.
 */

public class WIFIConnectFailPage extends BasePage {
    View contentView;
    @InjectView(R.id.try_again)
    TextView tryAgain;
    @InjectView(R.id.service_online)
    TextView serviceOnline;
    private final String KEY_AFTER_SALES_TEXT = "95105855";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        contentView = inflater.inflate(R.layout.wificonnectfail, container, false);
        ButterKnife.inject(this, contentView);
        return contentView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnClick({R.id.try_again, R.id.service_online})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.try_again:

                UIService.getInstance().popBack();
                break;
            case R.id.service_online:
                Uri uri = Uri.parse(String.format("tel:%s", KEY_AFTER_SALES_TEXT));
                Intent it = new Intent(Intent.ACTION_DIAL, uri);
                startActivity(it);
                break;
        }
    }


    @OnClick(R.id.img_back)
    public void onViewClicked() {
        UIService.getInstance().popBack();
    }
}
