package com.robam.common.recipe.step;

/**
 * Created by as on 2017-07-17.
 */

public interface RecipeException {

    /**
     * 菜谱步骤启动异常类
     */
    class RecipeStepStartException extends Throwable {
        /**
         * 0 成功 1 失败(离线 断电...) 2 设备不存在了
         */
        private int error = 0;

        public int getError() {
            return error;
        }

        public RecipeStepStartException(int e) {
            this.error = e;
        }
    }

    /**
     * 菜谱步骤启动预设异常
     */
    class RecipeStepPreStartException extends Throwable {
        /**
         *  1 失败(离线 断电...) 2 设备不存在了
         */
        private int error = 0;

        public int getError() {
            return error;
        }

        public RecipeStepPreStartException(int e) {
            this.error = e;
        }
    }

    class RecipeStepPauseOrRestoreException extends Throwable {
        /**
         *  1 失败(离线 断电...)
         */
        private int error = 0;

        public int getError() {
            return error;
        }

        public RecipeStepPauseOrRestoreException(int e) {
            this.error = e;
        }
    }
}

