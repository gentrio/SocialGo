package com.gentriolee.sharego.core;

import com.gentriolee.sharego.core.callback.SocialShareCallback;
import com.gentriolee.sharego.core.entities.ShareEntity;

/**
 * Created by gentriolee
 */

public interface IShare {

    void share(SocialShareCallback callback, ShareEntity shareInfo);

    interface ErrCode {
        //未知错误
        int ERR_UNKNOW = -1;
        //不支持的分享类型
        int ERR_NOT_SUPPORT = 1;
        //无相册权限
        int ERR_NO_ALBUM_PERMISSION = 2;
        //分享图片资源未找到
        int ERR_NOT_FOUND_RESOURCE = 3;
        //应用版本过低
        int ERR_LOW_VERSION = 4;

        //未安装应用
        int ERR_NOT_INSTALLED = 100;
        //分享失败
        int ERR_SHARE_FAILD = 200;
    }
}
