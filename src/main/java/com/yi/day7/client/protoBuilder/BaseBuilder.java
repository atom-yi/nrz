package com.yi.day7.client.protoBuilder;

import com.yi.day7.client.ClientSession;
import com.yi.day7.proto.ProtoMessage;

public class BaseBuilder {
    protected ProtoMessage.HeadType type;
    private long seqId;
    private ClientSession session;

    public BaseBuilder(ProtoMessage.HeadType type, ClientSession session) {
        this.type = type;
        this.session = session;
    }

    /**
     * 构建消息 基础部分
     */
    public ProtoMessage.Message buildCommon(long seqId) {
        this.seqId = seqId;

        ProtoMessage.Message.Builder mb =
                ProtoMessage.Message
                        .newBuilder()
                        .setType(type)
                        .setSessionId(session.getSessionId())
                        .setSequence(seqId);
        return mb.buildPartial();
    }
    /**
     * 构建消息 基础部分 的 Builder
     */
    public ProtoMessage.Message.Builder baseBuilder(long seqId) {
        this.seqId = seqId;
        return ProtoMessage.Message
                .newBuilder()
                .setType(type)
                .setSessionId(session.getSessionId())
                .setSequence(seqId);
    }
}
