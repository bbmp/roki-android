package com.legent.plat.io.cloud;

import com.legent.plat.pojos.RCReponse;

public interface RetrofitCallback<T extends RCReponse> {

    void onSuccess(T t);

    void onFaild(String err);
}
