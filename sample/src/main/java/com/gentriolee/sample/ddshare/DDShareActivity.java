package com.gentriolee.sample.ddshare;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.android.dingtalk.share.ddsharemodule.DDShareApiFactory;
import com.android.dingtalk.share.ddsharemodule.IDDAPIEventHandler;
import com.android.dingtalk.share.ddsharemodule.IDDShareApi;
import com.android.dingtalk.share.ddsharemodule.message.BaseReq;
import com.android.dingtalk.share.ddsharemodule.message.BaseResp;
import com.gentriolee.socialgo.config.SocialConfig;
import com.gentriolee.socialgo.core.SocialGo;

/**
 * Created by libai on 2018/6/7.
 */

public class DDShareActivity extends Activity implements IDDAPIEventHandler {

    private IDDShareApi mApi;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mApi = DDShareApiFactory.createDDShareApi(this, SocialConfig.getInstance().getBuilder().getDdAppId(), true);
        mApi.registerApp(SocialConfig.getInstance().getBuilder().getDdAppId());

        mApi.handleIntent(getIntent(), this);
    }

    @Override
    public void onReq(BaseReq baseReq) {
        SocialGo.onDDAPIHandlerReq(baseReq);
    }

    @Override
    public void onResp(BaseResp baseResp) {
        SocialGo.onDDAPIHandlerResp(baseResp);

        finish();
    }
}
