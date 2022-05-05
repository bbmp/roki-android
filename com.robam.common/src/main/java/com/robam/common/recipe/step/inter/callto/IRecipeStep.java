package com.robam.common.recipe.step.inter.callto;

/**
 * 菜谱步骤逻辑 对外声明可调用接口
 * Created by as on 2017-07-13.
 */

public interface IRecipeStep {


    /**
     * 开启运行
     */
    void run(String id);

    /**
     * 菜谱步骤关闭并释放资源
     */
    void close(boolean isOff);


    /**
     * 菜谱步骤重新初始化
     */
    void refreshInit();

    /**
     * 暂停
     */
    void pause();

    /**
     * 恢复
     */
    void restore();


}
