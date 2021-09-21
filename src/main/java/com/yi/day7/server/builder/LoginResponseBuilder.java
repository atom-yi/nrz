package com.yi.day7.server.builder;

import com.yi.day7.ProtoInstant;
import com.yi.day7.proto.ProtoMessage;

public class LoginResponseBuilder {
    public ProtoMessage.Message build(ProtoInstant.ResultCodeEnum resultCode,
                                      long seqId, String sessionId) {
        ProtoMessage.Message.Builder mb = ProtoMessage.Message.newBuilder()
                .setType(ProtoMessage.HeadType.LOGIN_RESPONSE)
                .setSequence(seqId)
                .setSessionId(sessionId);
        ProtoMessage.LoginResponse.Builder lb = ProtoMessage.LoginResponse.newBuilder()
                .setCode(resultCode.getCode())
                .setInfo(resultCode.getDesc())
                .setExpose(1);
        return mb.setLoginResponse(lb.build()).build();
    }
}
