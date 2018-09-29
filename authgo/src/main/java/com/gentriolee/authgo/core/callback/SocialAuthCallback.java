package com.gentriolee.authgo.core.callback;

/**
 * Created by gentriolee
 */

public abstract class SocialAuthCallback {

    private int target;

    /**
     * 设置授权类型
     * @param target
     */
    public void setTarget(int target) {
        this.target = target;
    }

    /**
     * 授权类型
     * @return
     */
    public int getTarget() {
        return target;
    }

    /**
     * 授权成功
     * @param code
     */
    public abstract void authSuccess(String code);

    /**
     * 授权失败
     */
    public abstract void authFail(int errorCode, String defaultMsg);

    /**
     * 授权取消
     */
    public abstract void authCancel();
}
