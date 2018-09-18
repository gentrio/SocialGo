package com.gentriolee.sharego.core.callback;

/**
 * Created by gentriolee
 */

public abstract class SocialShareCallback {

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
     * 分享成功
     */
    public abstract void shareSuccess();

    /**
     * 分享失败
     *
     * @param errorCode
     * @param defaultMsg
     */
    public abstract void shareFail(int errorCode, String defaultMsg);

    /**
     * 分享取消
     */
    public abstract void shareCancel();
}
