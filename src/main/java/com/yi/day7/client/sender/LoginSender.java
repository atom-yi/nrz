package com.yi.day7.client.sender;

import com.yi.day7.client.protoBuilder.LoginMsgBuilder;
import com.yi.day7.proto.ProtoMessage;

public class LoginSender extends BaseSender {
    public void sendLoginMsg() {
        if (!isConnected()) {
            System.out.println("连接还未建立");
            return;
        }

        ProtoMessage.Message loginMsg = LoginMsgBuilder.buildLoginMsg(getUser(), getSession());
        System.out.println("发送登录信息");
        sendMsg(loginMsg);
    }
}
