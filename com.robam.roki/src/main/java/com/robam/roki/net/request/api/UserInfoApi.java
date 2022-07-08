package com.robam.roki.net.request.api;

import com.robam.roki.net.OnRequestListener;
import com.robam.roki.net.base.BaseApi;
import com.robam.roki.net.request.bean.GetCollectCountBean;
import com.robam.roki.net.request.bean.GetCollectCountParam;

/**
 * <pre>
 *     author : huxw
 *     e-mail : xwhu93@163.com
 *     time   : 2022/05/07
 *     desc   : 用户信息相关接口
 *     version: 1.0
 * </pre>
 */
public class UserInfoApi extends BaseApi {
    public static int getCollectCountCode = 1001 ;
    /**
     * 获取用户收藏数量和作品数量
     */
    String getCollectAlbumCount= "/rest/cks/api/cooking/collect-album-count";

    public UserInfoApi(OnRequestListener mOnRequestListener) {
        super(mOnRequestListener);
    }

    public void getCollectAlbumCount(long userId){
        doPost(getCollectCountCode , getCollectAlbumCount ,new GetCollectCountParam(userId) , GetCollectCountBean.class);
    }
}
