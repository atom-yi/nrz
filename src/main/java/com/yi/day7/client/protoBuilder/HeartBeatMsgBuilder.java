package com.yi.day7.client.protoBuilder;

import com.yi.day7.client.ClientSession;
import com.yi.day7.pojo.User;
import com.yi.day7.proto.ProtoMessage;

public class HeartBeatMsgBuilder extends BaseBuilder {
    private final User user;
    public HeartBeatMsgBuilder(User user, ClientSession session) {
        super(ProtoMessage.HeadType.KEEPALIVE_REQUEST, session);
        this.user = user;
    }
    public ProtoMessage.Message buildMsg() {
        ProtoMessage.Message msg = buildCommon(-1);
        ProtoMessage.MessageHeartBeat.Builder builder = ProtoMessage.MessageHeartBeat.newBuilder()
                .setSeq(0)
                .setJson("{\"from\":\"client\"")
                .setUid(user.getUid());
        return msg.toBuilder().setHeartBeat(builder).build();
    }
}
