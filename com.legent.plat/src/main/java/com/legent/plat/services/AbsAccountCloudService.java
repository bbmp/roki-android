package com.legent.plat.services;


import com.legent.Callback;
import com.legent.VoidCallback;
import com.legent.plat.io.cloud.CloudHelper;
import com.legent.plat.pojos.User;
import com.legent.services.AbsService;
import com.legent.plat.io.cloud.Reponses.UnregisterResponse;

/**
 * Created by sylar on 15/7/24.
 */
abstract public class AbsAccountCloudService extends AbsService {


    public void isExisted(String account, Callback<Boolean> callback) {
        CloudHelper.isExisted(account, callback);
    }

    public void getVerifyCode(String phone, Callback<String> callback) {
        CloudHelper.getVerifyCode(phone, callback);
    }

    public void getDynamicPwd(String phone, Callback<String> callback) {
        CloudHelper.getDynamicPwd(phone, callback);
    }

    public void registByPhone(String phone, String nickname, String password,
                              String figure, boolean gender, String verifyCode,
                              VoidCallback callback) {
        CloudHelper.registByPhone(phone, nickname, password, figure, gender, verifyCode, callback);
    }


    public void registByEmail(String email, String nickname, String password,
                              String figure, boolean gender, VoidCallback callback) {
        CloudHelper.registByEmail(email, nickname, password, figure, gender,
                callback);
    }

    public void login(final String account, String password,
                      final Callback<User> callback) {
        CloudHelper.login(account, password, callback);
    }

    public void unRegistAccount(long userId, String phone, String verifyCode, final Callback<UnregisterResponse> callback) {
        CloudHelper.unregistByPhone(userId, phone, verifyCode, callback);
    }

    public void expressLogin(final String phone, String verifyCode, final Callback<User> callback) {
        CloudHelper.expressLogin(phone, verifyCode, callback);
    }

    public void login3rd(final String appSource, final String openId, final String phone, final String verifyCode,
                         final Callback<User> callback) {
        CloudHelper.phoneBind(appSource, openId, phone, verifyCode, callback);
    }

    protected void loginFrom3rd(final int platId, final String userId3rd,
                             final String nickname, final String faceUrl, final String token,
                             final Callback<User> callback) {
        CloudHelper.loginFrom3rd(platId, userId3rd, nickname, faceUrl, token, callback);
    }

    public void logout(String tgt,VoidCallback callback) {
        CloudHelper.logout(tgt,callback);
    }

    public void getUser(final long userId, final Callback<User> callback) {
        CloudHelper.getUser(userId, callback);
    }

    public void updateUser(long id, final String name, final String phone, final String email,
                           final boolean gender, final VoidCallback callback) {
        CloudHelper.updateUser(id, name, phone, email, gender, callback);
    }

    public void updateFigure(long userId, String figure,
                             final Callback<String> callback) {
        CloudHelper.updateFigure(userId, figure, callback);
    }

    public void updatePassword(long userId, String oldPwd, final String newPwd,
                               final VoidCallback callback) {
        CloudHelper.updatePassword(userId, oldPwd, newPwd, callback);
    }

//    protected void bind3rd(long userId,  String code, String accessToken, String appleUserId, VoidCallback callback) {
//        CloudHelper.bind3rd(userId, code, accessToken, appleUserId, callback);
//    }

    protected void unbind3rd(long userId, VoidCallback callback) {
        CloudHelper.unbind3rd(userId, callback);
    }

    public void resetPasswordByPhone(String phone, String newPwd,
                                     String verifyCode, VoidCallback callback) {
        CloudHelper.resetPasswordByPhone(phone, newPwd, verifyCode, callback);
    }

    public void resetPasswordByEmail(String email, VoidCallback callback) {
        CloudHelper.resetPasswordByEmail(email, callback);
    }
}
