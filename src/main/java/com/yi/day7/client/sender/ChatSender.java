package com.yi.day7.client.sender;

import com.yi.day7.client.protoBuilder.ChatMsgBuilder;
import com.yi.day7.pojo.ChatMsg;
import com.yi.day7.proto.ProtoMessage;

public class ChatSender extends BaseSender {
    public void sendChatMsg(String toUid, String content) {
        System.out.println("发送消息");
        ChatMsg msg = new ChatMsg(getUser());
        msg.setContent(content);
        msg.setMsgType(ChatMsg.MSG_TYPE.TEXT);
        msg.setTo(toUid);
        ProtoMessage.Message message =
                ChatMsgBuilder.buildChatMsg(msg, getUser(), getSession());
        sendMsg(message);
    }

    @Override
    public void sendSuccess(ProtoMessage.Message msg) {
        System.out.println("发送成功：" + msg.getMessageRequest().getContent());
    }
}
