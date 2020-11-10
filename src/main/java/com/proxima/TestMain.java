package com.proxima;

import org.json.simple.JSONObject;

public class TestMain {

    public static void main(String[] args) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("token", "");
        System.out.println(jsonObject.toJSONString());
    }
}
