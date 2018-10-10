package com.gentriolee.authgo.core.callback;

import com.gentriolee.authgo.core.entities.BaseUser;
import com.gentriolee.socialgo.core.callback.SocialCallback;

/**
 * Created by gentriolee
 */

public abstract class SocialLoginCallback extends SocialCallback{

    /**
     * 授权成功
     *
     * @param user
     */
    public abstract void success(BaseUser user);
}
