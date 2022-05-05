package com.robam.roki.wxapi;

import android.os.Bundle;

import com.legent.ui.AbsActivity;
import com.legent.utils.LogUtils;
import com.robam.common.events.WxCode2Event;
import com.robam.common.events.WxCodeShareEvent;
import com.robam.roki.MobApp;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;

import static com.legent.utils.EventUtils.postEvent;

public class WXEntryActivity extends AbsActivity implements IWXAPIEventHandler {


    @Override
    protected String createFormKey() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      /*  api = WXAPIFactory.createWXAPI(this, WX_APP_ID);
        api.handleIntent(getIntent(), this);*/
        MobApp.mWxApi.handleIntent(getIntent(), this);
    }


    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp resp) {
        int errorCode = resp.errCode;
        LogUtils.i("20171023", "code:" + errorCode);
        switch (errorCode) {
            case BaseResp.ErrCode.ERR_OK:
                //用户同意
                LogUtils.i("20171023", "code:11" + resp.getType());
                switch (resp.getType()) {

                    case ConstantsAPI.COMMAND_SENDAUTH:
                        SendAuth.Resp resp1 = (SendAuth.Resp) resp;
                        String code = ((SendAuth.Resp) resp).code;
                        String state = ((SendAuth.Resp) resp).state;
                        LogUtils.i("20171023", "code:" + code);
                        postEvent(new WxCode2Event(code ,state));
                        finish();
                        break;
                    case ConstantsAPI.COMMAND_SENDMESSAGE_TO_WX:
                        LogUtils.i("20171023", "code33:" + resp.getType());
                        postEvent(new WxCodeShareEvent());
                        finish();
                        break;
                    default:
                        break;
                }

                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                //用户拒绝
                finish();
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                //用户取消
                finish();
                break;
            default:
                finish();
                break;
        }

    }

}

