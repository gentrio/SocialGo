package com.gentriolee.authgo.core.entities;

import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by gentriolee
 */

public class WBUser extends BaseUser{

    private String language;

    private String city;

    private String province;

    private String headimgurl_hd;

    public static WBUser parse(String json) throws JSONException {
        JSONObject jsonObject = new JSONObject(json);
        WBUser user = new WBUser();
        user.setNickName(jsonObject.getString("screen_name"));
        String gender = jsonObject.getString("gender");
        if (TextUtils.equals(gender, "m")) {
            user.setSex(1);
        } else if (TextUtils.equals(gender, "f")) {
            user.setSex(2);
        } else {
            user.setSex(0);
        }
        user.setHeadImageUrl(jsonObject.getString("profile_image_url"));
        user.setHeadImageUrlLarge(jsonObject.getString("avatar_large"));
        user.setHeadimgurl_hd(jsonObject.getString("avatar_hd"));
        user.setCity(jsonObject.getString("city"));
        user.setProvince(jsonObject.getString("province"));
        return user;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getHeadimgurl_hd() {
        return headimgurl_hd;
    }

    public void setHeadimgurl_hd(String headimgurl_hd) {
        this.headimgurl_hd = headimgurl_hd;
    }

    @Override
    public String toString() {
        return "WBUser{" +
                "language='" + language + '\'' +
                ", city='" + city + '\'' +
                ", province='" + province + '\'' +
                ", headimgurl_hd='" + headimgurl_hd + '\'' +
                ", authResult=" + authResult +
                ", nickName='" + nickName + '\'' +
                ", sex=" + sex +
                ", headImageUrl='" + headImageUrl + '\'' +
                ", headImageUrlLarge='" + headImageUrlLarge + '\'' +
                '}';
    }
}
