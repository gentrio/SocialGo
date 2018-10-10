package com.gentriolee.sharego.core.callback;

import com.gentriolee.socialgo.core.callback.SocialCallback;

/**
 * Created by gentriolee
 */

public abstract class SocialLaunchCallback extends SocialCallback {

    /**
     * 唤起微信小程序成功回调
     *
     * @param extraData
     */
    public abstract void success(String extraData);
}
