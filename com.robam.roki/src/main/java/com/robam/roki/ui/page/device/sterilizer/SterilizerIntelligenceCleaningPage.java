package com.robam.roki.ui.page.device.sterilizer;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.legent.VoidCallback;
import com.legent.plat.Plat;
import com.legent.ui.UIService;
import com.legent.ui.ext.BasePage;
import com.legent.ui.ext.views.CheckBoxView;
import com.legent.utils.LogUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.pojos.device.Sterilizer.Steri826;
import com.robam.roki.R;
import com.robam.roki.ui.PageArgumentKey;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * XS855 智能保洁
 * Created by RuanWei on 2019/11/21.
 */

public class SterilizerIntelligenceCleaningPage extends BasePage {
    @InjectView(R.id.img_back)
    ImageView imgBack;

    @InjectView(R.id.tv_title)
    TextView tvTitle;

    @InjectView(R.id.tv_sterilizer_title)
    TextView tvSterilizerTitle;

    @InjectView(R.id.tv_sterilizer_content)
    TextView tvSterilizerContent;

    @InjectView(R.id.cb_sterilizer)
    CheckBoxView cbSterilizer;


    public String mGuid;
    public String functionParams;
    Steri826 steri826;
    private String title;
    private String desc;
    private String state;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.abs_sterilizer_intelligence, container, false);
        ButterKnife.inject(this, view);
        Bundle bd = getArguments();
        mGuid = bd == null ? null : bd.getString(PageArgumentKey.Guid);
        steri826 = Plat.deviceService.lookupChild(mGuid);
        functionParams = bd == null ? null : bd.getString(PageArgumentKey.functionParams);
        initData();
        return view;
    }

    private void initData() {
        try {

            JSONObject jsonObject = new JSONObject(functionParams);
            title = jsonObject.getJSONObject("title").getString("value");
            desc = jsonObject.getJSONObject("desc").getString("value");
            state = jsonObject.getString("state");
            tvTitle.setText(title);
            tvSterilizerTitle.setText(title);
            tvSterilizerContent.setText(desc);

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @OnClick({R.id.img_back, R.id.cb_sterilizer})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                UIService.getInstance().popBack();
                break;
            case R.id.cb_sterilizer:
                if (steri826.status == 0) {
                    steri826.setSteriPower((short) 1, (short) 0, (short) 0, new VoidCallback() {
                        @Override
                        public void onSuccess() {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    setClean();
                                }
                            }, 2000);

                        }

                        @Override
                        public void onFailure(Throwable t) {

                        }
                    });
                } else {
                    setClean();
                }


                break;
        }
    }

    private void setClean() {
        LogUtils.i("20191204567890", "state::" + state);
        steri826.setSteriPower(Short.decode(state), new VoidCallback() {
            @Override
            public void onSuccess() {
                UIService.getInstance().popBack();
            }

            @Override
            public void onFailure(Throwable t) {
                ToastUtils.show(t.getMessage(), Toast.LENGTH_SHORT);

            }
        });
    }


}
