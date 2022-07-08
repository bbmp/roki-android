package com.robam.roki.ui.helper3;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.legent.Callback;
import com.legent.Helper;
import com.robam.common.io.cloud.Reponses;
import com.robam.common.io.cloud.RokiRestHelper;
import com.robam.common.pojos.Recipe;
import com.robam.common.util.RecipeUtils;
import com.robam.roki.ui.bean3.ImageDetail;
import com.robam.roki.ui.helper3.http.BaseRequest;
import com.robam.roki.ui.helper3.http.IRequest;
import com.robam.roki.ui.helper3.http.IResponse;
import com.robam.roki.ui.helper3.http.OkhttpClientImpl;

public class RecipeHelper {
    //新增加的菜谱接口，新增加参数needStepsInfo.
    public static void getCookbookById(long bookId, String entranceCode, String needStepsInfo, final Callback<Recipe> callback) {
        RokiRestHelper.getCookbookById(bookId, entranceCode, needStepsInfo, new Callback<Recipe>() {
            @Override
            public void onSuccess(Recipe result) {
                if (result != null) {
                    result.hasDetail = true;
                    String recipeImgUrl = RecipeUtils.getRecipeImgUrl(result);
                    if (!TextUtils.isEmpty(recipeImgUrl)
                    ) {
                        try {
                            TaskHelper.newFixedThreadPool(1).execute(new Runnable() {
                                @Override
                                public void run() {
                                    IRequest request = new BaseRequest(recipeImgUrl + "?x-oss-process=image/info");
                                    OkhttpClientImpl httpClient = new OkhttpClientImpl();
                                    IResponse response = httpClient.get(request, false);
                                    if (response != null && response.getCode() == 200) {
                                        ImageDetail imageDetail = new Gson().fromJson(response.getData(), ImageDetail.class);
                                        float imageHeight = Integer.parseInt(imageDetail.imageHeight.value);
                                        float imageWidth = Integer.parseInt(imageDetail.imageWidth.value);
                                        result.length_width = imageHeight / imageWidth;
                                        Helper.onSuccess(callback, result);
                                    }

                                }
                            });
                        }catch (Exception e){
                            e.getMessage();
                            result.length_width = 0;
                            Helper.onSuccess(callback, result);
                        }



                    } else {
                        result.length_width = 0;
                        Helper.onSuccess(callback, result);
                    }
//                    result.tra2Save();

                }else {
                    Helper.onSuccess(callback, null);
                }

            }

            @Override
            public void onFailure(Throwable t) {
                Helper.onFailure(callback, t);
            }
        });
    }



}
