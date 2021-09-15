package com.yi.day6;

import com.yi.day6.proto.MsgProto;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Slf4j
public class ProtoBufTest {
    private MsgProto.Msg buildMsg() {
        MsgProto.Msg.Builder builder = MsgProto.Msg.newBuilder();
        builder.setId(1000);
        builder.setContent("😊dududu!😭");
        return builder.build();
    }

    @Test
    public void testSerialization() throws IOException {
        MsgProto.Msg msg = buildMsg();
        byte[] data = msg.toByteArray();

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        output.write(data);
        data = output.toByteArray();

        MsgProto.Msg message = MsgProto.Msg.parseFrom(data);
        log.info("id=>{}, content=>{}", message.getId(), message.getContent());
    }

}
