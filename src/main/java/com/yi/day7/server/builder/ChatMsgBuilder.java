package com.yi.day7.server.builder;

import com.yi.day7.ProtoInstant;
import com.yi.day7.proto.ProtoMessage;

public class ChatMsgBuilder {
    public ProtoMessage.Message build(long seqId, ProtoInstant.ResultCodeEnum resultCode) {
        ProtoMessage.Message.Builder mb = ProtoMessage.Message.newBuilder()
                .setType(ProtoMessage.HeadType.MESSAGE_RESPONSE)
                .setSequence(seqId);
        ProtoMessage.MessageResponse.Builder msgBuilder = ProtoMessage.MessageResponse.newBuilder()
                .setCode(resultCode.getCode())
                .setInfo(resultCode.getDesc())
                .setExpose(1);
        return mb.setMessageResponse(msgBuilder.build()).build();
    }
}
