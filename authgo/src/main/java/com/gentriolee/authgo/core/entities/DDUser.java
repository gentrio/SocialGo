package com.gentriolee.authgo.core.entities;

import org.json.JSONException;
import org.json.JSONObject;

public class DDUser extends BaseUser {

    public static DDUser parse(String json) throws JSONException {
        JSONObject jsonObject = new JSONObject(json);
        DDUser user = new DDUser();

        JSONObject userInfoJson = jsonObject.getJSONObject("user_info");
        user.setNickName(userInfoJson.getString("nick"));
        return user;
    }

    @Override
    public String toString() {
        return "DDUser{" +
                "authResult=" + authResult +
                ", nickName='" + nickName + '\'' +
                ", sex=" + sex +
                ", headImageUrl='" + headImageUrl + '\'' +
                ", headImageUrlLarge='" + headImageUrlLarge + '\'' +
                '}';
    }
}
