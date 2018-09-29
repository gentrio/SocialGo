package com.gentriolee.socialgo.core;

import android.content.Intent;

import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;

/**
 * Created by gentriolee
 */

public interface ISocial {

    /**
     * 微信 = 1 << 2
     * 朋友圈 = 1 << 3
     * QQ = 1 << 4
     * QQ空间 = 1<< 5
     * 微博 = 1<< 6
     * 钉钉 = 1 << 7
     */

    int TARGET_WX = 1 << 2;
    int TARGET_WX_TIMELINE = 1 << 3;
    int TARGET_QQ = 1 << 4;
    int TARGET_QQ_ZONE = 1 << 5;
    int TARGET_WB = 1 << 6;
    int TARGET_DD = 1 << 7;

    void onWXAPIHandlerReq(BaseReq baseReq);

    void onWXAPIHandlerResp(BaseResp baseResp);

    void onDDAPIHandlerReq(com.android.dingtalk.share.ddsharemodule.message.BaseReq baseReq);

    void onDDAPIHandlerResp(com.android.dingtalk.share.ddsharemodule.message.BaseResp baseResp);

    void onActivityResult(int requestCode, int resultCode, Intent data);

    void onNewIntent(Intent intent);
}
