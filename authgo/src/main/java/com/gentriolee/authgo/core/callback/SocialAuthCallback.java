package com.gentriolee.authgo.core.callback;

import com.gentriolee.authgo.core.entities.AuthResult;
import com.gentriolee.socialgo.core.callback.SocialCallback;

/**
 * Created by gentriolee
 */

public abstract class SocialAuthCallback extends SocialCallback {

    /**
     * 授权成功
     *
     * @param authResult
     */
    public abstract void success(AuthResult authResult);

}
