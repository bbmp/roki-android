package com.robam.roki.net.request.bean;

import com.robam.roki.net.base.BaseBean;

/**
 * <pre>
 *     author : huxw
 *     e-mail : xwhu93@163.com
 *     time   : 2022/05/07
 *     desc   : 获取用户收藏作品数量实体类
 *     version: 1.0
 * </pre>
 */
public class GetCollectCountBean extends BaseBean {

    public PayloadDTO payload;

    public static class PayloadDTO {
        public int album;
        public int collect;
    }
}
