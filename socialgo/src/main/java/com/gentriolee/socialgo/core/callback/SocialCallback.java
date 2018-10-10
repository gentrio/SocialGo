package com.gentriolee.socialgo.core.callback;

public abstract class SocialCallback {

    private int target;

    /**
     * 设置第三方类型
     * @param target
     */
    public void setTarget(int target) {
        this.target = target;
    }

    /**
     * 第三方类型
     * @return
     */
    public int getTarget() {
        return target;
    }

    /**
     * 授权失败
     */
    public abstract void fail(int errorCode, String defaultMsg);

    /**
     * 授权取消
     */
    public abstract void cancel();
}
