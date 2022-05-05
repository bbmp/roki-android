package com.robam.roki.utils;

import com.robam.roki.model.bean.StoveBackgroundFunParams;

/**
 * Created by 14807 on 2018/7/2.
 */

public class StoveLevelUtils {

    public static String getStoveLevel(short level, StoveBackgroundFunParams paramBean) {
        StoveBackgroundFunParams.ParamBean param = paramBean.getParam();

        switch (level) {
            case 0:
                return param.get_$0().getValue();
            case 1:

                return param.get_$1().getValue();
            case 2:

                return param.get_$2().getValue();
            case 3:

                return param.get_$3().getValue();
            case 4:

                return param.get_$4().getValue();
            case 5:

                return param.get_$5().getValue();
            case 6:

                return param.get_$6().getValue();
            case 7:

                return param.get_$7().getValue();
            case 8:

                return param.get_$8().getValue();

            case 9:

                return param.get_$9().getValue();

            case 10:

                return param.get_$10().getValue();

        }

        return "";
    }
}
