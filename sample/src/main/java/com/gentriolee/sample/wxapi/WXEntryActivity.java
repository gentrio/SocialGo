
package com.gentriolee.sample.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.gentriolee.socialgo.config.SocialConfig;
import com.gentriolee.socialgo.core.SocialGo;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

    private IWXAPI mApi;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mApi = WXAPIFactory.createWXAPI(this, SocialConfig.getInstance().getBuilder().getWxAppId(), true);
        mApi.registerApp(SocialConfig.getInstance().getBuilder().getWxAppId());

        mApi.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        mApi.handleIntent(getIntent(), this);
    }

    @Override
    public void onReq(BaseReq baseReq) {
        SocialGo.onWXAPIHandlerReq(baseReq);
    }

    @Override
    public void onResp(BaseResp baseResp) {
        if (null == baseResp) {
            return;
        }
        SocialGo.onWXAPIHandlerResp(baseResp);
        finish();
    }
}
