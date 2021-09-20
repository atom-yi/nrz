package com.yi.day7.client.protoBuilder;

import com.yi.day7.client.ClientSession;
import com.yi.day7.pojo.User;
import com.yi.day7.proto.ProtoMessage;

public class LoginMsgBuilder extends BaseBuilder {
    private final User user;

    public LoginMsgBuilder(User user, ClientSession session) {
        super(ProtoMessage.HeadType.LOGIN_REQUEST, session);
        this.user = user;
    }

    public ProtoMessage.Message build() {
        ProtoMessage.Message msg = buildCommon(-1);
        ProtoMessage.LoginRequest.Builder lb =
                ProtoMessage.LoginRequest.newBuilder()
                .setDeviceId(user.getDevId())
                .setPlatform(user.getPlatform().getType())
                .setToken(user.getToken())
                .setUid(user.getUid());
        return msg.toBuilder().setLoginRequest(lb).build();
    }

    public static ProtoMessage.Message buildLoginMsg(User user, ClientSession session) {
        LoginMsgBuilder builder = new LoginMsgBuilder(user, session);
        return builder.build();
    }
}
