package com.yi.day6;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

@Slf4j
public class JsonMsgTest {
    private JsonMsg generate() {
        JsonMsg msg = new JsonMsg();
        msg.setId(1);
        msg.setContent("dudududu....");
        return msg;
    }

    @Test
    public void testConvert() {
        JsonMsg msg = generate();
        // 序列化
        String json = msg.toJson();
        log.info("serialize => \n{}", json);
        // 反序列化
        log.info("deserialize => \n{}", JsonMsg.parse(json));
    }

}
