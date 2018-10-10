package com.gentriolee.authgo.core.callback;

import com.gentriolee.socialgo.core.callback.SocialCallback;

/**
 * Created by gentriolee
 */

public abstract class SocialAuthCallback extends SocialCallback {

    /**
     * 授权成功
     * @param token
     */
    public abstract void success(String token);

}
