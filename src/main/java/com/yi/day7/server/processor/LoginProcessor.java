package com.yi.day7.server.processor;

import com.yi.day7.ProtoInstant;
import com.yi.day7.pojo.User;
import com.yi.day7.proto.ProtoMessage;
import com.yi.day7.server.ServerContext;
import com.yi.day7.server.ServerSession;
import com.yi.day7.server.SessionMap;

public class LoginProcessor extends AbstractServerProcessor {

    @Override
    public ProtoMessage.HeadType type() {
        return ProtoMessage.HeadType.LOGIN_REQUEST;
    }

    @Override
    public boolean action(ServerSession session, ProtoMessage.Message msg) {
        ProtoMessage.LoginRequest req = msg.getLoginRequest();
        long seqNo = msg.getSequence();
        User user = User.fromMsg(req);
        if (!checkUser(user)) {
            ProtoInstant.ResultCodeEnum resultCode = ProtoInstant.ResultCodeEnum.NO_TOKEN;
            ProtoMessage.Message resp = ServerContext.loginResponseBuilder.build(resultCode,
                    seqNo, "-1");
            session.writeAndFlush(msg);
            return false;
        }

        session.setUser(user);
        session.bind();

        ProtoInstant.ResultCodeEnum successCode = ProtoInstant.ResultCodeEnum.SUCCESS;
        ProtoMessage.Message resp = ServerContext.loginResponseBuilder.build(successCode, seqNo,
                session.getSessionId());
        session.writeAndFlush(resp);
        return true;
    }

    private boolean checkUser(User user) {
        if (SessionMap.inst().hasLogin(user)) {
            return false;
        }
        System.out.println("校验用户信息正确");
        return true;
    }
}
