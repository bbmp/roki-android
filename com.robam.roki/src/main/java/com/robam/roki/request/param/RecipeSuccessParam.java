package com.robam.roki.request.param;

import androidx.annotation.NonNull;

import com.robam.roki.net.base.BaseParam;

public class RecipeSuccessParam extends BaseParam {
    public Long id;
    public String userId;
    public String cookbookId;
    public String entranceCode;
    public String needStepsInfo;
    //{"userId":2085018415,"cookbookId":13675,"entranceCode":"1","needStepsInfo":"1"}
    public RecipeSuccessParam(Long id) {

        this.id=id;
    }

    public RecipeSuccessParam( Long id, String userId, String cookbookId, String entranceCode, String needStepsInfo) {

        this.id = id;
        this.userId = userId;
        this.cookbookId = cookbookId;
        this.entranceCode = entranceCode;
        this.needStepsInfo = needStepsInfo;
    }
}
