package com.gentriolee.sharego.core;

import com.gentriolee.sharego.core.callback.SocialShareCallback;
import com.gentriolee.sharego.core.entities.ShareEntity;

/**
 * Created by gentriolee
 */

public interface IShare {

    void share(SocialShareCallback callback, ShareEntity shareInfo);

    interface ErrCode {

        //SDK内部调用错误
        int ERR_SDK_INTERNAL = -1;
        //应用版本过低
        int ERR_LOW_VERSION = 1;
        //缺少小程序原始Id
        int ERR_EMPTY_APPID = 2;

        //未安装应用
        int ERR_NOT_INSTALLED = 100;
    }
}
