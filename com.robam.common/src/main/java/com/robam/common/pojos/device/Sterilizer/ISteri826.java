package com.robam.common.pojos.device.Sterilizer;

import com.legent.VoidCallback;

/**
 * Created by zhaiyuanyi on 15/11/19.
 */
public interface ISteri826 extends ISterilizer {


     void setSteriPower(short status, VoidCallback callback);

     void setSteriPower(short status, short powerTime, short argumentNumber, VoidCallback callback);


     void setSteriLock(short isChildLock, VoidCallback voidCallback);

     /**
      * 设置消毒柜烘干
      * DRYING_TIME[1Byte] {0取消烘干，>1 为烘干时间}
      */
     void setSteriDrying(short SteriDrying,VoidCallback voidCallback);
     /**
      * 设置消毒柜保洁
      * CLEAN_TIME[1Byte] {0取消保洁，60，保洁时间}
      */

     void setSteriClean(short SteriCleanTime, VoidCallback voidCallback);

     /**
      * 设置消毒柜消毒
      * DISINFECT_TIME[1Byte] {0取消消毒,150消毒时间}
      */
     void setSteriDisinfect(short SteriDisinfectTime, VoidCallback voidCallback);
}
