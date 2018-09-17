package com.gentriolee.sharego.core.callback;

/**
 * Created by gentriolee
 */

public interface SocialLaunchCallback {

    /**
     * 唤起微信小程序成功回调
     *
     * @param extraData
     */
    void launchSuccess(String extraData);

    /**
     * 唤起失败
     */
    void launchFail(int errorCode);

    /**
     * 唤起取消
     */
    void launchCancel();
}
