package com.gentriolee.sharego.core;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;

import com.gentriolee.sharego.R;
import com.gentriolee.sharego.core.callback.SocialLaunchCallback;
import com.gentriolee.sharego.core.callback.SocialShareCallback;
import com.gentriolee.sharego.core.entities.ShareEntity;
import com.gentriolee.sharego.core.entities.WXLaunchEntity;
import com.gentriolee.sharego.core.entities.WXShareEntity;
import com.gentriolee.sharego.utils.ShareUtils;
import com.gentriolee.socialgo.core.WXSocial;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXMiniProgramObject;
import com.tencent.mm.opensdk.modelmsg.WXTextObject;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;

/**
 * Created by gentriolee
 */

public class WXShare extends WXSocial implements IShare, IWXAPIEventHandler {

    private SocialShareCallback shareCallback;
    private SocialLaunchCallback launchCallback;

    WXShare(Activity activity, String appId) {
        super(activity, appId);
    }

    @Override
    public void share(SocialShareCallback callback, ShareEntity shareInfo) {
        callback.setShareType(shareInfo.getTarget());
        this.shareCallback = callback;
        if (!iwxapi.isWXAppInstalled()) {
            if (shareCallback != null) {
                shareCallback.shareFail(ErrCode.ERR_NOT_INSTALLED, getString(R.string.social_uninstall_wx));
            }
            return;
        }
        //是否分享到朋友圈，微信4.2以下不支持朋友圈
        boolean isTimeLine = shareInfo.getTarget() == ShareGo.TARGET_WX_TIMELINE;
        if (isTimeLine && iwxapi.getWXAppSupportAPI() < 0x21020001) {
            if (shareCallback != null) {
                shareCallback.shareFail(ErrCode.ERR_LOW_VERSION, getString(R.string.share_wx_version_low_error));
            }
            return;
        }

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.message = createMessage(req, shareInfo.getParams());
        if (req.message == null) {
            return;
        }
        req.scene = isTimeLine ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
        iwxapi.sendReq(req);
    }


    public void launch(SocialLaunchCallback callback, WXLaunchEntity launchInfo) {
        this.launchCallback = callback;
        if (!iwxapi.isWXAppInstalled()) {
            if (launchCallback != null) {
                launchCallback.launchFail(ErrCode.ERR_NOT_INSTALLED, getString(R.string.social_uninstall_wx));
            }
            return;
        }

        if (TextUtils.isEmpty(launchInfo.getUserName())) {
            if (launchCallback != null) {
                launchCallback.launchFail(ErrCode.ERR_EMPTY_APPID, getString(R.string.share_empty_origin_id));
            }
            return;
        }
        WXLaunchMiniProgram.Req req = new WXLaunchMiniProgram.Req();
        req.userName = launchInfo.getUserName();
        req.path = launchInfo.getPath();
        req.miniprogramType = launchInfo.isRelease() ? WXLaunchMiniProgram.Req.MINIPTOGRAM_TYPE_RELEASE : WXLaunchMiniProgram.Req.MINIPROGRAM_TYPE_PREVIEW;
        iwxapi.sendReq(req);
    }

    private WXMediaMessage createMessage(SendMessageToWX.Req req, Bundle params) {
        WXMediaMessage msg = new WXMediaMessage();
        int type = params.getInt(WXShareEntity.SHARE_TYPE);
        boolean success = false;
        switch (type) {
            case WXShareEntity.SHARE_TYPE_TEXT:
                success = addText(req, msg, params);
                break;
            case WXShareEntity.SHARE_TYPE_IMG:
                success = addImage(req, msg, params);
                break;
            case WXShareEntity.SHARE_TYPE_WEB:
                success = addWeb(req, msg, params);
                break;
            case WXShareEntity.SHARE_TYPE_MINI_APP:
                success = addMiniApp(req, msg, params);
        }
        if (!success) {
            return null;
        }
        return msg;
    }

    private boolean addText(SendMessageToWX.Req req, WXMediaMessage msg, Bundle params) {
        WXTextObject textObj = new WXTextObject();
        textObj.text = params.getString(WXShareEntity.SHARE_TITLE);

        msg.mediaObject = textObj;
        msg.description = textObj.text;

        req.transaction = ShareUtils.buildTransaction("text");
        return true;
    }

    private boolean addImage(SendMessageToWX.Req req, WXMediaMessage msg, Bundle params) {
        if (params.containsKey(WXShareEntity.SHARE_IMAGE_BITMAP)) {//分为本地文件和应用内资源图片
            Bitmap bitmap = params.getParcelable(WXShareEntity.SHARE_IMAGE_BITMAP);
            msg.mediaObject = new WXImageObject(bitmap);
            msg.thumbData = ShareUtils.smallBmpToByteArray(bitmap);
            req.transaction = ShareUtils.buildTransaction("img");
            return true;
        }
        return false;
    }

    private boolean addWeb(SendMessageToWX.Req req, WXMediaMessage msg, Bundle params) {
        WXWebpageObject musicObject = new WXWebpageObject();
        musicObject.webpageUrl = params.getString(WXShareEntity.SHARE_LINK);

        msg.mediaObject = musicObject;
        if (addTitleSummaryAndThumb(msg, params)) return false;

        req.transaction = ShareUtils.buildTransaction("webpage");
        return true;
    }

    private boolean addMiniApp(SendMessageToWX.Req req, WXMediaMessage msg, Bundle params) {
        WXMiniProgramObject miniProgramObject = new WXMiniProgramObject();
        miniProgramObject.webpageUrl = params.getString(WXShareEntity.SHARE_LINK);
        miniProgramObject.userName = params.getString(WXShareEntity.SHARE_MINI_APP_ID);
        miniProgramObject.path = params.getString(WXShareEntity.SHARE_MINI_APP_PATH);
        miniProgramObject.miniprogramType = params.getBoolean(WXShareEntity.SHARE_MINI_APP_TYPE) ? WXMiniProgramObject.MINIPTOGRAM_TYPE_RELEASE : WXMiniProgramObject.MINIPROGRAM_TYPE_PREVIEW;

        msg.mediaObject = miniProgramObject;
        if (addTitleSummaryAndThumb(msg, params)) return false;

        req.transaction = ShareUtils.buildTransaction("webpage");
        return true;
    }

    private boolean addTitleSummaryAndThumb(WXMediaMessage msg, Bundle params) {
        if (params.containsKey(WXShareEntity.SHARE_TITLE)) {
            msg.title = params.getString(WXShareEntity.SHARE_TITLE);
        }

        if (params.containsKey(WXShareEntity.SHARE_DESC)) {
            msg.description = params.getString(WXShareEntity.SHARE_DESC);
        }

        boolean isNeedBigThumb = !TextUtils.isEmpty(params.getString(WXShareEntity.SHARE_MINI_APP_PATH));
        if (params.containsKey(WXShareEntity.SHARE_IMAGE_BITMAP)) {
            Bitmap bitmap = params.getParcelable(WXShareEntity.SHARE_IMAGE_BITMAP);
            //thumbData 普通分享图片大小限制32k 小程序封面属于大图128k
            if (isNeedBigThumb) {
                msg.thumbData = ShareUtils.bigBmpToByteArray(bitmap);
            } else {
                msg.thumbData = ShareUtils.smallBmpToByteArray(bitmap);
            }
        }
        return false;
    }

    @Override
    public void onReq(BaseReq baseReq) {
        //nothing
    }

    @Override
    public void onResp(BaseResp baseResp) {
        if (baseResp.getType() == ConstantsAPI.COMMAND_SENDMESSAGE_TO_WX) {
            //分享
            if (baseResp.errCode == BaseResp.ErrCode.ERR_OK) {
                if (shareCallback != null) {
                    shareCallback.shareSuccess();
                }
            } else {
                if (shareCallback != null) {
                    shareCallback.shareCancel();
                }
            }
        } else if (baseResp.getType() == ConstantsAPI.COMMAND_LAUNCH_WX_MINIPROGRAM) {
            //跳转微信小程序
            if (baseResp.errCode == BaseResp.ErrCode.ERR_OK) {
                WXLaunchMiniProgram.Resp launchMiniProResp = (WXLaunchMiniProgram.Resp) baseResp;
                if (launchCallback != null) {
                    launchCallback.launchSuccess(launchMiniProResp.extMsg);
                }
            } else {
                if (launchCallback != null) {
                    launchCallback.launchCancel();
                }
            }
        }
    }
}

