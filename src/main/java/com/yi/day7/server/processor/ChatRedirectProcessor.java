package com.yi.day7.server.processor;

import com.yi.day7.proto.ProtoMessage;
import com.yi.day7.server.ServerSession;
import com.yi.day7.server.SessionMap;

import java.util.List;

public class ChatRedirectProcessor extends AbstractServerProcessor {
    @Override
    public ProtoMessage.HeadType type() {
        return ProtoMessage.HeadType.MESSAGE_REQUEST;
    }

    @Override
    public boolean action(ServerSession session, ProtoMessage.Message msg) {
        ProtoMessage.MessageRequest req = msg.getMessageRequest();
        System.out.println("chatMsg | from=>" + req.getFrom()
                + " , to=>" + req.getTo()
                + " , content=>" + req.getContent());
        String to = req.getTo();
        List<ServerSession> toSessions = SessionMap.inst().getSessionsBy(to);
        if (toSessions.isEmpty()) {
            System.out.println("接收方离线");
        } else {
            toSessions.forEach(toSession -> toSession.writeAndFlush(msg));
        }
        return true;
    }
}
