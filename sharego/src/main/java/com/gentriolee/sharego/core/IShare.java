package com.gentriolee.sharego.core;

import com.gentriolee.sharego.core.callback.SocialShareCallback;
import com.gentriolee.sharego.core.entities.ShareEntity;
import com.gentriolee.socialgo.core.ISocial;

/**
 * Created by gentriolee
 */

public interface IShare {

    void share(SocialShareCallback callback, ShareEntity shareInfo);

    interface ErrCode extends ISocial.ErrCode {

        //应用版本过低
        int ERR_LOW_VERSION = 1;
        //缺少小程序原始Id
        int ERR_EMPTY_APPID = 2;
    }
}
