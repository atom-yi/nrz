package com.yi.day7.server;

import com.yi.day7.pojo.User;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Data
public class ServerSession {
    public static final AttributeKey<String> KEY_USER_ID = AttributeKey.valueOf("key_user_id");
    public static final AttributeKey<ServerSession> SESSION_KEY = AttributeKey.valueOf("SESSION_KEY");

    private Channel channel;
    private User user;
    private final String sessionId;
    private boolean isLogin = false;
    private Map<String, Object> map = new HashMap<>();

    public ServerSession(Channel channel) {
        this.channel = channel;
        this.sessionId = buildNewSessionId();
    }

    public static ServerSession getSession(ChannelHandlerContext ctx) {
        return ctx.channel().attr(SESSION_KEY).get();
    }

    public static void closeSession(ChannelHandlerContext ctx) {
        ServerSession session = getSession(ctx);
        if (session != null && session.isValid()) {
            session.close();
            SessionMap.inst().removeSession(session.getSessionId());
        }
    }

    public ServerSession bind() {
        channel.attr(SESSION_KEY).set(this);
        SessionMap.inst().addSession(this);
        isLogin = true;
        return this;
    }

    public ServerSession unbind() {
        isLogin = false;
        SessionMap.inst().removeSession(sessionId);
        close();
        return this;
    }

    public synchronized <T> T get(String key) {
        return (T) map.get(key);
    }

    public synchronized void set(String key, Object value) {
        map.put(key, value);
    }

    public synchronized void writeAndFlush(Object msg) {
        channel.writeAndFlush(msg);
    }

    private String buildNewSessionId() {
        String uuid = UUID.randomUUID().toString();
        return uuid.replaceAll("-", "");
    }

    private boolean isValid() {
        return user != null;
    }

    public void setUser(User user) {
        this.user = user;
        user.setSessionId(sessionId);
    }

    private void close() {
        ChannelFuture future = channel.close();
        future.addListener((ChannelFutureListener) f -> {
            if (!f.isSuccess()) {
                System.out.println("channel 关闭失败");
            }
        });
    }
}
