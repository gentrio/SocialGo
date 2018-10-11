package com.gentriolee.authgo.core.entities;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by gentriolee
 */

public class WXUser extends BaseUser {

    private String city;

    private String country;

    private String province;

    private String unionid;

    public static WXUser parse(String json) throws JSONException {
        JSONObject jsonObject = new JSONObject(json);
        WXUser user = new WXUser();
        user.setNickName(jsonObject.getString("nickname"));
        user.setSex(jsonObject.getInt("sex"));
        user.setHeadImageUrl(jsonObject.getString("headimgurl"));
        user.setHeadImageUrlLarge(jsonObject.getString("headimgurl"));
        user.setProvince(jsonObject.getString("province"));
        user.setCity(jsonObject.getString("city"));
        user.setCountry(jsonObject.getString("country"));
        user.setUnionid(jsonObject.getString("unionid"));
        return user;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }

    @Override
    public String toString() {
        return "WXUser{" +
                "city='" + city + '\'' +
                ", country='" + country + '\'' +
                ", province='" + province + '\'' +
                ", unionid='" + unionid + '\'' +
                ", authResult=" + authResult +
                ", nickName='" + nickName + '\'' +
                ", sex=" + sex +
                ", headImageUrl='" + headImageUrl + '\'' +
                ", headImageUrlLarge='" + headImageUrlLarge + '\'' +
                '}';
    }
}
