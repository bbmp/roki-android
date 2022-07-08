package com.robam.roki.request.param;

import androidx.annotation.NonNull;

import com.robam.roki.net.base.BaseParam;


public class TestParam extends BaseParam {
//    {"userId":2085018415,"cookbookId":13675,"entranceCode":"1","needStepsInfo":"1"}
    public  String userId;
    public String cookbookId;
    public String entranceCode;
    public  String needStepsInfo;

    public TestParam( String functionId, String userId, String cookbookId, String entranceCode, String needStepsInfo) {

        this.userId = userId;
        this.cookbookId = cookbookId;
        this.entranceCode = entranceCode;
        this.needStepsInfo = needStepsInfo;
    }

    public TestParam(@NonNull String functionId) {

    }
}
