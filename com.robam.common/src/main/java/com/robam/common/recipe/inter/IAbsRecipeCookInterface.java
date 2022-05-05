package com.robam.common.recipe.inter;

/**
 * Created by as on 2017-07-12.
 */

public interface IAbsRecipeCookInterface {
    int getmCurrentIndex();

    /**
     * 返回值： 0 成功 1 菜谱数据结构异常
     */
    int start();


    void pause();

    void restore();

    /**
     * @param index 调用stop结束当前步骤逻辑并切换第几步骤
     *              若当前步骤号码和index相同，相当于重新开始本步骤
     */
    void changeStep(int index);

    /**
     * 停止当前做菜逻辑
     */
    void stop();

    /**
     * 调用stop结束当前步骤逻辑并关闭close整个菜谱逻辑
     */
    void close();


}
