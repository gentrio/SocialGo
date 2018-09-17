package com.gentriolee.sharego.core;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;

import com.android.dingtalk.share.ddsharemodule.IDDAPIEventHandler;
import com.android.dingtalk.share.ddsharemodule.message.BaseReq;
import com.android.dingtalk.share.ddsharemodule.message.BaseResp;
import com.android.dingtalk.share.ddsharemodule.message.DDImageMessage;
import com.android.dingtalk.share.ddsharemodule.message.DDMediaMessage;
import com.android.dingtalk.share.ddsharemodule.message.DDTextMessage;
import com.android.dingtalk.share.ddsharemodule.message.DDWebpageMessage;
import com.android.dingtalk.share.ddsharemodule.message.SendMessageToDD;
import com.gentriolee.sharego.core.callback.SocialShareCallback;
import com.gentriolee.sharego.core.entities.DDShareEntity;
import com.gentriolee.sharego.core.entities.ShareEntity;
import com.gentriolee.sharego.utils.ShareUtils;
import com.gentriolee.socialgo.core.DDSocial;

import java.io.File;

/**
 * Created by gentriolee
 */

public class DDShare extends DDSocial implements IShare, IDDAPIEventHandler {

    private SocialShareCallback shareCallback;
    private int target;

    DDShare(Activity activity, String appId) {
        super(activity, appId);
    }

    @Override
    public void share(SocialShareCallback callback, ShareEntity shareInfo) {
        this.shareCallback = callback;
        this.target = shareInfo.getType();
        if (!iddShareApi.isDDAppInstalled()) {
            if (shareCallback != null) {
                callback.shareFail(target, ErrCode.ERR_NOT_INSTALLED);
            }
            return;
        }
        if (!iddShareApi.isDDSupportAPI()) {
            if (shareCallback != null) {
                callback.shareFail(target, ErrCode.ERR_LOW_VERSION);
            }
            return;
        }

        SendMessageToDD.Req req = new SendMessageToDD.Req();
        req.mMediaMessage = createMessage(req, shareInfo.getParams());
        if (req.mMediaMessage == null) {
            return;
        }
        iddShareApi.sendReq(req);
    }

    private DDMediaMessage createMessage(SendMessageToDD.Req req, Bundle params) {
        DDMediaMessage msg = new DDMediaMessage();
        int type = params.getInt(DDShareEntity.KEY_DD_TYPE);
        boolean success = false;
        switch (type) {
            case DDShareEntity.TYPE_TEXT:
                success = addText(req, msg, params);
                break;
            case DDShareEntity.TYPE_IMG:
                success = addImage(req, msg, params);
                break;
            case DDShareEntity.TYPE_WEB:
                success = addWeb(req, msg, params);
                break;
        }
        if (!success) {
            return null;
        }
        return msg;
    }

    private boolean addText(SendMessageToDD.Req req, DDMediaMessage msg, Bundle params) {
        DDTextMessage textObj = new DDTextMessage();
        textObj.mText = params.getString(DDShareEntity.KEY_DD_TEXT);

        msg.mMediaObject = textObj;
        return true;
    }

    private boolean addImage(SendMessageToDD.Req req, DDMediaMessage msg, Bundle params) {
        if (params.containsKey(DDShareEntity.KEY_DD_IMG_BITMAP)) {
            Bitmap bitmap = params.getParcelable(DDShareEntity.KEY_DD_IMG_BITMAP);
            msg.mMediaObject = new DDImageMessage(bitmap);
            msg.mThumbData = ShareUtils.smallBmpToByteArray(bitmap);
            return true;
        }
        return false;
    }

    private boolean addWeb(SendMessageToDD.Req req, DDMediaMessage msg, Bundle params) {
        DDWebpageMessage webObj = new DDWebpageMessage();
        webObj.mUrl = params.getString(DDShareEntity.KEY_DD_WEB_URL);

        msg.mMediaObject = webObj;
        if (addTitleSummaryAndThumb(msg, params)) return false;

        return true;
    }

    private boolean addTitleSummaryAndThumb(DDMediaMessage msg, Bundle params) {
        if (params.containsKey(DDShareEntity.KEY_DD_TITLE)) {
            msg.mTitle = params.getString(DDShareEntity.KEY_DD_TITLE);
        }

        if (params.containsKey(DDShareEntity.KEY_DD_SUMMARY)) {
            msg.mContent = params.getString(DDShareEntity.KEY_DD_SUMMARY);
        }

        if (params.containsKey(DDShareEntity.KEY_DD_IMG_BITMAP)) {
            Bitmap bitmap = params.getParcelable(DDShareEntity.KEY_DD_IMG_BITMAP);
            msg.mThumbData = ShareUtils.smallBmpToByteArray(bitmap);
        }
        return false;
    }

    private boolean notFoundFile(String filePath) {
        if (!TextUtils.isEmpty(filePath)) {
            File file = new File(filePath);
            if (!file.exists()) {
                if (shareCallback != null) {
                    shareCallback.shareFail(target, ErrCode.ERR_NOT_FOUND_RESOURCE);
                }
                return true;
            }
        } else {
            if (shareCallback != null) {
                shareCallback.shareFail(target, ErrCode.ERR_NOT_FOUND_RESOURCE);
            }
            return true;
        }
        return false;
    }

    @Override
    public void onReq(BaseReq baseReq) {
        //nothing
    }

    @Override
    public void onResp(BaseResp baseResp) {
        //只支持分享 暂不支持授权登录
        if (baseResp.mErrCode == BaseResp.ErrCode.ERR_OK) {
            shareCallback.shareSuccess(target);
        } else {
            shareCallback.shareCancel(target);
        }
    }

}
