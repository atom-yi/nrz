package com.yi.day6;

import com.alibaba.fastjson.JSON;
import com.google.gson.GsonBuilder;

public class JSONUtil {

    static GsonBuilder gb = new GsonBuilder();
    static {
        gb.disableHtmlEscaping();
    }

    public static String toJson(Object obj) {
        return gb.create().toJson(obj);
    }

    public static <T> T parse(String json, Class<T> clz) {
        return JSON.parseObject(json, clz);
    }

}
