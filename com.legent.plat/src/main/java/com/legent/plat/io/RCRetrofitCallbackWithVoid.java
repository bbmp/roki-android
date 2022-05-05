package com.legent.plat.io;

import com.legent.Helper;
import com.legent.VoidCallback;
import com.legent.events.ChangeLoginErrorEvent;
import com.legent.plat.exceptions.ExceptionHelper;
import com.legent.plat.pojos.RCReponse;
import com.legent.services.RestfulService;
import com.legent.utils.EventUtils;

import java.net.ConnectException;

import retrofit.RetrofitError;
import retrofit.client.Response;

public class RCRetrofitCallbackWithVoid<T extends RCReponse> implements
		retrofit.Callback<T> {

	VoidCallback callback;

	public RCRetrofitCallbackWithVoid(VoidCallback callback) {
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
			Helper.onFailure(callback, ExceptionHelper.newRCException(result.rc));
		} else {
			afterSuccess(result);
			Helper.onSuccess(callback);
		}
	}

	@Override
	public void failure(RetrofitError e) {
        Response response = e.getResponse();
        if (response !=null){
            int status = response.getStatus();
            EventUtils.postEvent(new ChangeLoginErrorEvent(status));
        }
		RestfulService.printError(e);

		if (e.getCause() instanceof ConnectException) {
			Helper.onFailure(callback, ExceptionHelper.newConnectException());
		} else {
			Helper.onFailure(callback,
					ExceptionHelper.newRestfulException(e.getMessage()));
		}
	}

	/**
	 * RC码正确时的处理
	 * 
	 * @param result
	 */
	protected void afterSuccess(T result) {
	}

}
