package com.gentriolee.authgo.core;

import android.app.Activity;

import com.gentriolee.authgo.core.callback.SocialAuthCallback;
import com.gentriolee.socialgo.core.WXSocial;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;

/**
 * Created by gentriolee
 */

public class WXAuth extends WXSocial implements IAuthGo, IWXAPIEventHandler {

    private SocialAuthCallback callback;

    WXAuth(Activity activity, String appId) {
        super(activity, appId);
    }

    @Override
    public void auth(SocialAuthCallback callback) {
        this.callback = callback;
        if (!iwxapi.isWXAppInstalled()) {
            if (callback != null) {
//                callback.authFail(activity.getString(R.string.social_wx_uninstall));
            }
            return;
        }

        SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "doWXLogin";
        iwxapi.sendReq(req);
    }

    public void warrant(SocialAuthCallback callback) {
        this.callback = callback;
        if (!iwxapi.isWXAppInstalled()) {
            if (callback != null) {
//                callback.authFail(activity.getString(R.string.social_wx_uninstall));
            }
            return;
        }

        SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "doWeChatWarrant";
        iwxapi.sendReq(req);
    }

    @Override
    public void onReq(BaseReq baseReq) {
        //nothing
    }

    @Override
    public void onResp(BaseResp baseResp) {
        if (baseResp.getType() == ConstantsAPI.COMMAND_SENDAUTH) {
            //授权
            if (baseResp.errCode == BaseResp.ErrCode.ERR_OK) {
                //授权码
                String code = ((SendAuth.Resp) baseResp).code;
                callback.authSuccess(code);
            } else {
//                callback.authCancel(activity.getString(R.string.social_social_cancel));
            }
        }
    }
}
