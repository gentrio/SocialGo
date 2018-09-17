package com.gentriolee.socialgo.core;

import android.content.Intent;

import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;

/**
 * Created by gentriolee
 */

public interface ISocialGo {

    void onWXAPIHandlerReq(BaseReq baseReq);

    void onWXAPIHandlerResp(BaseResp baseResp);

    void onDDAPIHandlerReq(com.android.dingtalk.share.ddsharemodule.message.BaseReq baseReq);

    void onDDAPIHandlerResp(com.android.dingtalk.share.ddsharemodule.message.BaseResp baseResp);

    void onActivityResult(int requestCode, int resultCode, Intent data);

    void onNewIntent(Intent intent);
}
