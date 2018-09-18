package com.gentriolee.authgo.core.callback;

/**
 * Created by gentriolee
 */

public abstract class SocialAuthCallback {

    private int shareType;

    /**
     * 设置授权类型
     * @param shareType
     */
    public void setShareType(int shareType) {
        this.shareType = shareType;
    }

    /**
     * 授权类型
     * @return
     */
    public int getShareType() {
        return shareType;
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
