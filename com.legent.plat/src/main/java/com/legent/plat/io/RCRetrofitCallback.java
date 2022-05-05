package com.legent.plat.io;

import com.google.gson.Gson;
import com.legent.Callback;
import com.legent.Helper;
import com.legent.events.ChangeLoginErrorEvent;
import com.legent.plat.exceptions.ExceptionHelper;
import com.legent.plat.pojos.RCReponse;
import com.legent.plat.services.ResultCodeManager;
import com.legent.services.RestfulService;
import com.legent.utils.EventUtils;
import com.legent.utils.LogUtils;

import java.io.IOException;
import java.net.ConnectException;

import retrofit.RetrofitError;
import retrofit.client.Response;

public class RCRetrofitCallback<T extends RCReponse> implements
        retrofit.Callback<T> {

    Callback<?> callback;

    public RCRetrofitCallback(Callback<?> callback) {
        this.callback = callback;
    }

    @Override
    public void success(T result, Response response) {
        if (result == null) {
            Helper.onFailure(callback, ExceptionHelper.newRestfulNullException());
            return;
        }

        boolean isSuccess = result.isSuccess();
        if (!isSuccess) {

            Helper.onFailure(callback, ExceptionHelper.newRestfulException(result.rc ,result.tgt));

        } else {
            afterSuccess(result);
        }
    }

    @Override
    public void failure(RetrofitError e) {
        Response response = e.getResponse();
        LogUtils.i("20180912", "response:" + response);
        if (response != null) {
            int status = response.getStatus();
            EventUtils.postEvent(new ChangeLoginErrorEvent(status));
        }
        RestfulService.printError(e);
        if (e.getBody() != null && e.getBody().toString() != null) {
            RCReponse rcReponse = new Gson().fromJson(e.getBody().toString(), RCReponse.class);
            if (rcReponse.error_description != null) {
                Helper.onFailure(callback,
                        ExceptionHelper.newRestfulException(ResultCodeManager.EC_RC_Failure, rcReponse.error_description));
                return;
            }
        }
        if (e.getCause() instanceof ConnectException) {
            Helper.onFailure(callback, ExceptionHelper.newConnectException());
        } else if (e.getCause() instanceof IOException) {
            Helper.onFailure(callback,
                    ExceptionHelper.newDeviceIOException(e.getMessage()));
        } else {
            Helper.onFailure(callback,
                    ExceptionHelper.newRestfulException(e.getMessage()));
        }
    }

    /**
     * RC码正确时的处理
     */
    protected void afterSuccess(T result) {
    }
}
