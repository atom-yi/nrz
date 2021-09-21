package com.yi.day7.server.processor;

import com.yi.day7.server.ServerSession;
import io.netty.channel.Channel;

public abstract class AbstractServerProcessor implements ServerProcessor {
    protected String getKey(Channel ch) {
        return ch.attr(ServerSession.KEY_USER_ID).get();
    }

    protected void setKey(Channel ch, String key) {
        ch.attr(ServerSession.KEY_USER_ID).set(key);
    }

    protected void checkAuth(Channel ch) throws Exception {
        if (getKey(ch) == null) {
            throw new Exception("该用户未登录");
        }
    }
}
