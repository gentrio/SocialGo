package com.gentriolee.sharego.core;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;

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
import com.tencent.mm.opensdk.modelmsg.WXMusicObject;
import com.tencent.mm.opensdk.modelmsg.WXTextObject;
import com.tencent.mm.opensdk.modelmsg.WXVideoObject;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;

/**
 * Created by gentriolee
 */

public class WXShare extends WXSocial implements IShare, IWXAPIEventHandler {

    private SocialShareCallback shareCallback;
    private SocialLaunchCallback launchCallback;
    private int target;

    WXShare(Activity activity, String appId) {
        super(activity, appId);
    }

    @Override
    public void share(SocialShareCallback callback, ShareEntity shareInfo) {
        this.shareCallback = callback;
        this.target = shareInfo.getType();
        if (!iwxapi.isWXAppInstalled()) {
            if (callback != null) {
                callback.shareFail(target, ErrCode.ERR_NOT_INSTALLED);
            }
            return;
        }
        //是否分享到朋友圈，微信4.2以下不支持朋友圈
        boolean isTimeLine = shareInfo.getType() == ShareEntity.TYPE_WX_TIMELINE;
        if (isTimeLine && iwxapi.getWXAppSupportAPI() < 0x21020001) {
            if (shareCallback != null) {
                shareCallback.shareFail(target, ErrCode.ERR_LOW_VERSION);
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
            if (callback != null) {
                callback.launchFail(ErrCode.ERR_NOT_INSTALLED);
            }
            return;
        }

        if (TextUtils.isEmpty(launchInfo.getUserName())) {
            if (callback != null) {
                callback.launchFail(ErrCode.ERR_UNKNOW);
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
        int type = params.getInt(WXShareEntity.KEY_WX_TYPE);
        boolean success = false;
        switch (type) {
            case WXShareEntity.TYPE_TEXT:
                success = addText(req, msg, params);
                break;
            case WXShareEntity.TYPE_IMG:
                success = addImage(req, msg, params);
                break;
            case WXShareEntity.TYPE_MUSIC:
                success = addMusic(req, msg, params);
                break;
            case WXShareEntity.TYPE_VIDEO:
                success = addVideo(req, msg, params);
                break;
            case WXShareEntity.TYPE_WEB:
                success = addWeb(req, msg, params);
                break;
            case WXShareEntity.TYPE_MINI_APP:
                success = addMiniApp(req, msg, params);
        }
        if (!success) {
            return null;
        }
        return msg;
    }

    private boolean addText(SendMessageToWX.Req req, WXMediaMessage msg, Bundle params) {
        WXTextObject textObj = new WXTextObject();
        textObj.text = params.getString(WXShareEntity.KEY_WX_TEXT);

        msg.mediaObject = textObj;
        msg.description = textObj.text;

        req.transaction = ShareUtils.buildTransaction("text");
        return true;
    }

    private boolean addImage(SendMessageToWX.Req req, WXMediaMessage msg, Bundle params) {
        if (params.containsKey(WXShareEntity.KEY_WX_IMG_BITMAP)) {//分为本地文件和应用内资源图片
            Bitmap bitmap = params.getParcelable(WXShareEntity.KEY_WX_IMG_BITMAP);
            msg.mediaObject = new WXImageObject(bitmap);
            msg.thumbData = ShareUtils.smallBmpToByteArray(bitmap);
            req.transaction = ShareUtils.buildTransaction("img");
            return true;
        }
        return false;
    }

    private boolean addMusic(SendMessageToWX.Req req, WXMediaMessage msg, Bundle params) {
        WXMusicObject musicObject = new WXMusicObject();
        musicObject.musicUrl = params.getString(WXShareEntity.KEY_WX_MUSIC_URL);

        msg.mediaObject = musicObject;
        if (addTitleSummaryAndThumb(msg, params)) return false;

        req.transaction = ShareUtils.buildTransaction("music");
        return true;
    }

    private boolean addVideo(SendMessageToWX.Req req, WXMediaMessage msg, Bundle params) {
        WXVideoObject musicObject = new WXVideoObject();
        musicObject.videoUrl = params.getString(WXShareEntity.KEY_WX_VIDEO_URL);

        msg.mediaObject = musicObject;
        if (addTitleSummaryAndThumb(msg, params)) return false;

        req.transaction = ShareUtils.buildTransaction("video");
        return true;
    }

    private boolean addWeb(SendMessageToWX.Req req, WXMediaMessage msg, Bundle params) {
        WXWebpageObject musicObject = new WXWebpageObject();
        musicObject.webpageUrl = params.getString(WXShareEntity.KEY_WX_WEB_URL);

        msg.mediaObject = musicObject;
        if (addTitleSummaryAndThumb(msg, params)) return false;

        req.transaction = ShareUtils.buildTransaction("webpage");
        return true;
    }

    private boolean addMiniApp(SendMessageToWX.Req req, WXMediaMessage msg, Bundle params) {
        WXMiniProgramObject miniProgramObject = new WXMiniProgramObject();
        miniProgramObject.webpageUrl = params.getString(WXShareEntity.KEY_WX_WEB_URL);
        miniProgramObject.userName = params.getString(WXShareEntity.KEY_WX_MINI_PROGRAM_ID);
        miniProgramObject.path = params.getString(WXShareEntity.KEY_WX_MINI_PROGRAM_URL);
        miniProgramObject.miniprogramType = params.getBoolean(WXShareEntity.KEY_WX_MINI_PROGRAM_TYPE) ? WXMiniProgramObject.MINIPTOGRAM_TYPE_RELEASE : WXMiniProgramObject.MINIPROGRAM_TYPE_PREVIEW;

        msg.mediaObject = miniProgramObject;
        if (addTitleSummaryAndThumb(msg, params)) return false;

        req.transaction = ShareUtils.buildTransaction("webpage");
        return true;
    }

    private boolean addTitleSummaryAndThumb(WXMediaMessage msg, Bundle params) {
        if (params.containsKey(WXShareEntity.KEY_WX_TITLE)) {
            msg.title = params.getString(WXShareEntity.KEY_WX_TITLE);
        }

        if (params.containsKey(WXShareEntity.KEY_WX_SUMMARY)) {
            msg.description = params.getString(WXShareEntity.KEY_WX_SUMMARY);
        }

        boolean isNeedBigThumb = !TextUtils.isEmpty(params.getString(WXShareEntity.KEY_WX_MINI_PROGRAM_URL));
        if (params.containsKey(WXShareEntity.KEY_WX_IMG_BITMAP)) {
            Bitmap bitmap = params.getParcelable(WXShareEntity.KEY_WX_IMG_BITMAP);
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
                shareCallback.shareSuccess(target);
            } else {
                shareCallback.shareCancel(target);
            }
        } else if (baseResp.getType() == ConstantsAPI.COMMAND_LAUNCH_WX_MINIPROGRAM) {
            //跳转微信小程序
            if (baseResp.errCode == BaseResp.ErrCode.ERR_OK) {
                WXLaunchMiniProgram.Resp launchMiniProResp = (WXLaunchMiniProgram.Resp) baseResp;
                launchCallback.launchSuccess(launchMiniProResp.extMsg);
            } else {
                launchCallback.launchCancel();
            }
        }
    }
}

