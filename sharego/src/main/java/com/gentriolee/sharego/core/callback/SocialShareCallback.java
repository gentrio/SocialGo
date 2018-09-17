package com.gentriolee.sharego.core.callback;

/**
 * Created by gentriolee
 */

public interface SocialShareCallback {

    /**
     * 分享成功
     */
    void shareSuccess(int target);

    /**
     * 分享失败
     * @param target
     * @param errorCode
     */
    void shareFail(int target, int errorCode);

    /**
     * 分享取消
     * @param target
     */
    void shareCancel(int target);
}
