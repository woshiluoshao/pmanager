package jp.utils;

import com.alibaba.fastjson.JSONObject;

public class ChatUtils {

    public static String chatJson(String isSystem, String fromName, String toName, String message) {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("isSystem", isSystem);
        jsonObject.put("fromName", fromName);
        jsonObject.put("toName", toName);
        jsonObject.put("message", message);

        return jsonObject.toJSONString();
    }
}
