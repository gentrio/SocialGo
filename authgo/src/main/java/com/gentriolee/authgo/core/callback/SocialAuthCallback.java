package com.gentriolee.authgo.core.callback;

/**
 * Created by gentriolee
 */

public interface SocialAuthCallback {

    /**
     * 授权成功
     *
     * @param code
     */
    void authSuccess(String code);

    /**
     * 授权失败
     * @param msg
     */
    void authFail(String msg);

    /**
     * 授权取消
     * @param msg
     */
    void authCancel(String msg);
}
