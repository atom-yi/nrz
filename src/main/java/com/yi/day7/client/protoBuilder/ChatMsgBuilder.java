package com.yi.day7.client.protoBuilder;

import com.yi.day7.client.ClientSession;
import com.yi.day7.pojo.ChatMsg;
import com.yi.day7.pojo.User;
import com.yi.day7.proto.ProtoMessage;

public class ChatMsgBuilder extends BaseBuilder {
    private ChatMsg chatMsg;
    private User user;

    public ChatMsgBuilder(ChatMsg chatMsg, User user, ClientSession session) {
        super(ProtoMessage.HeadType.MESSAGE_REQUEST, session);
        this.chatMsg = chatMsg;
        this.user = user;

    }


    public ProtoMessage.Message build() {
        ProtoMessage.Message message = buildCommon(-1);
        ProtoMessage.MessageRequest.Builder cb
                = ProtoMessage.MessageRequest.newBuilder();

        chatMsg.fillMsg(cb);
        return message
                .toBuilder()
                .setMessageRequest(cb)
                .build();
    }

    public static ProtoMessage.Message buildChatMsg(
            ChatMsg chatMsg,
            User user,
            ClientSession session) {
        ChatMsgBuilder builder =
                new ChatMsgBuilder(chatMsg, user, session);
        return builder.build();

    }
}
