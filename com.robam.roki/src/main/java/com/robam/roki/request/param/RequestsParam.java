package com.robam.roki.request.param;

import com.robam.roki.net.base.BaseParam;

import java.util.List;

public interface RequestsParam {
    class CookingCurveMarkStepRequest  extends BaseParam {

        public String id;

        public List<CookingCurveMarkStepList> stepDtoList;

        public CookingCurveMarkStepRequest(String id, List<CookingCurveMarkStepList> stepDtoList) {
            this.id = id;
            this.stepDtoList = stepDtoList;
        }
    }
    class CookingCurveMarkStepList {

        public String curveCookbookId;

        public String markName;

        public String markTemp;

        public String markTime;

        public CookingCurveMarkStepList(String curveCookbookId, String markName, String markTemp, String markTime) {
            this.curveCookbookId = curveCookbookId;
            this.markName = markName;
            this.markTemp = markTemp;
            this.markTime = markTime;
        }
    }
}
