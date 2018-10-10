package com.gentriolee.authgo.core.entities;

import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by gentriolee
 */

public class QQUser extends BaseUser{

    private String qZoneHeadImage;

    private String qZoneHeadImageLarge;

    public static QQUser parse(String json) throws JSONException {
        JSONObject jsonObject = new JSONObject(json);
        QQUser user = new QQUser();
        user.setNickName(jsonObject.getString("nickname"));
        user.setSex(TextUtils.equals("男", jsonObject.getString("gender")) ? 1 : 2);
        user.setHeadImageUrl(jsonObject.getString("figureurl_qq_1"));
        user.setHeadImageUrlLarge(jsonObject.getString("figureurl_qq_2"));
        user.setqZoneHeadImage(jsonObject.getString("figureurl_1"));
        user.setqZoneHeadImageLarge(jsonObject.getString("figureurl_2"));
        return user;
    }

    public String getqZoneHeadImage() {
        return qZoneHeadImage;
    }

    public void setqZoneHeadImage(String qZoneHeadImage) {
        this.qZoneHeadImage = qZoneHeadImage;
    }

    public String getqZoneHeadImageLarge() {
        return qZoneHeadImageLarge;
    }

    public void setqZoneHeadImageLarge(String qZoneHeadImageLarge) {
        this.qZoneHeadImageLarge = qZoneHeadImageLarge;
    }

    @Override
    public String toString() {
        return "QQUser{" +
                "qZoneHeadImage='" + qZoneHeadImage + '\'' +
                ", qZoneHeadImageLarge='" + qZoneHeadImageLarge + '\'' +
                ", baseToken=" + baseToken +
                ", nickName='" + nickName + '\'' +
                ", sex=" + sex +
                ", headImageUrl='" + headImageUrl + '\'' +
                ", headImageUrlLarge='" + headImageUrlLarge + '\'' +
                '}';
    }
}
