package com.yi.day6;

import lombok.Data;

@Data
public class JsonMsg {
    private int id;
    private String content;
    public String toJson() {
        return JSONUtil.toJson(this);
    }
    public static JsonMsg parse(String json) {
        return JSONUtil.parse(json, JsonMsg.class);
    }
}
