package com.gentriolee.authgo.core;

import com.gentriolee.authgo.core.callback.SocialAuthCallback;

/**
 * Created by gentriolee
 */

public interface IAuth {

    void auth(SocialAuthCallback callback);

    interface ErrCode {

        //SDK内部调用错误
        int ERR_SDK_INTERNAL = -1;

        //未安装应用
        int ERR_NOT_INSTALLED = 100;
    }
}
