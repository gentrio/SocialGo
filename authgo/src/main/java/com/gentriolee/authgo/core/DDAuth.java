package com.gentriolee.authgo.core;

import android.app.Activity;

import com.android.dingtalk.share.ddsharemodule.IDDAPIEventHandler;
import com.android.dingtalk.share.ddsharemodule.message.BaseReq;
import com.android.dingtalk.share.ddsharemodule.message.BaseResp;
import com.gentriolee.authgo.core.callback.SocialAuthCallback;
import com.gentriolee.socialgo.annotation.Unsupported;
import com.gentriolee.socialgo.core.DDSocial;

/**
 * Created by gentriolee
 */

@Unsupported
public class DDAuth extends DDSocial implements IAuthGo, IDDAPIEventHandler {

    DDAuth(Activity activity, String appId) {
        super(activity, appId);
    }


    @Override
    public void auth(SocialAuthCallback callback) {

    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp baseResp) {

    }
}

