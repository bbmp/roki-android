package com.robam.roki.ui.helper3.http;

public interface IHttpClient {
    /**
     * 持有IRequest的依赖 ，是否使用缓存(可配置)
     * @param request
     * @param froceCache
     */
    IResponse get(IRequest request, boolean froceCache);

    IResponse post(IRequest request, boolean forceCache);
}
