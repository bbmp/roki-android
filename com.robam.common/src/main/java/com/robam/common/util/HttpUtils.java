package com.robam.common.util;

import com.legent.plat.pojos.dictionary.ServerOpt;

import retrofit.RestAdapter;
import retrofit.http.GET;

/**
 * Created by as on 2016/8/2.
 */

public class HttpUtils {
    static public ServerOpt serverOpt = new ServerOpt();
    public static RequestUrl service;

    static {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(serverOpt.getRestfulBaseUrl())
                .build();
        service = restAdapter.create(RequestUrl.class);
    }

    public interface RequestUrl {
        //获取主题菜谱列表
        @GET("/rest/api/theme-manage/list-all")
        String getThemeRecipes();
    }
}
