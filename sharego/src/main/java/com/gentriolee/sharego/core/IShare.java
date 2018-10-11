package com.gentriolee.sharego.core;

import com.gentriolee.sharego.core.entities.ShareEntity;
import com.gentriolee.socialgo.core.ISocial;

/**
 * Created by gentriolee
 */

public interface IShare {

    void share(ShareEntity shareInfo);

    interface ErrCode extends ISocial.ErrCode {

        //分享的ErrCode 从100开始

        //应用版本过低
        int ERR_LOW_VERSION = 100;
        //缺少小程序原始Id
        int ERR_EMPTY_APPID = 101;
    }
}
