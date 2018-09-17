package com.gentriolee.authgo.core;

import com.gentriolee.authgo.core.callback.SocialAuthCallback;

/**
 * Created by gentriolee
 */

public interface IAuthGo {

    void auth(SocialAuthCallback callback);
}
