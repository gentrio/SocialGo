package com.gentriolee.sharego.core.callback;

/**
 * Created by gentriolee
 */

public abstract class SocialLaunchCallback {

    private int shareType;

    /**
     * 设置分享类型
     * @param shareType
     */
    public void setShareType(int shareType) {
        this.shareType = shareType;
    }

    /**
     * 分享类型
     * @return
     */
    public int getShareType() {
        return shareType;
    }

    /**
     * 唤起微信小程序成功回调
     *
     * @param extraData
     */
    public abstract void launchSuccess(String extraData);

    /**
     * 唤起失败
     * @param errorCode
     * @param defaultMsg
     */
    public abstract void launchFail(int errorCode, String defaultMsg);

    /**
     * 唤起取消
     */
    public abstract void launchCancel();
}
