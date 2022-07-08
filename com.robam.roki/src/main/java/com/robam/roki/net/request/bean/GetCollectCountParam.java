package com.robam.roki.net.request.bean;

import com.robam.roki.net.base.BaseParam;

import java.io.Serializable;

/**
 * 获取用户收藏点赞数
 */
public class GetCollectCountParam extends BaseParam {
    public long userId ;

    public GetCollectCountParam(long userId) {
        this.userId = userId;
    }
}
