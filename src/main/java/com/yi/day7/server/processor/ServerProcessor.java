package com.yi.day7.server.processor;

import com.yi.day7.proto.ProtoMessage;
import com.yi.day7.server.ServerSession;

public interface ServerProcessor {
    ProtoMessage.HeadType type();
    boolean action(ServerSession session, ProtoMessage.Message msg);
}
