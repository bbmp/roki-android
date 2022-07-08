package com.robam.roki.utils;

import androidx.fragment.app.FragmentActivity;

import com.legent.Callback;
import com.legent.plat.Plat;
import com.legent.plat.pojos.User;
import com.legent.ui.UIService;
import com.legent.utils.LogUtils;
import com.robam.common.io.cloud.Reponses;
import com.robam.common.pojos.Token;
import com.robam.common.services.CookbookManager;
import com.youzan.sdk.YouzanSDK;
import com.youzan.sdk.YouzanToken;

/**
 * Created by Administrator on 2017/06/24.
 * 有赞用户认证
 */
public class YouzanUserAttestationUtils {
    private static String type;

    /**
     * 有赞认证接口,请求自己的后台
     */
    public static void initYouzanData() {
//        final long currentUserId = Plat.accountService.getCurrentUserId();
//        User user = Plat.accountService.getCurrentUser();
//        String phone = null;
//        if (currentUserId == 0) {type = "0";} else {type = "1";
//            phone = user.phone;
//        }
//        final String finalPhone = phone;
//        CookbookManager.getInstance().getYouzanDetailContent(currentUserId, type, finalPhone,
//                new Callback<Reponses.TokenResponses>() {
//                    @Override
//                    public void onSuccess(Reponses.TokenResponses tokenResponses) {
//                        processingData(tokenResponses);
//                        LogUtils.i("20181212","有赞 成功");
//                    }
//                    @Override
//                    public void onFailure(Throwable t) {
//                        LogUtils.i("20181212","有赞 失败");
//                    }
//                });
    }

    /**
     * 处理json数据
     * @param tokenResponses 存储用户认证的对象
     */
    private static void processingData(Reponses.TokenResponses tokenResponses) {
        Token resultJson = tokenResponses.token;
        String accessToken = resultJson.accessToken;
        String cookieKey = resultJson.cookieKey;
        String cookieValue = resultJson.cookieValue;
        YouzanToken token = new YouzanToken();
        token.setAccessToken(accessToken);
        token.setCookieKey(cookieKey);
        token.setCookieValue(cookieValue);
        FragmentActivity activity = UIService.getInstance().getMain().getActivity();
        YouzanSDK.sync(activity, token);
    }
}
