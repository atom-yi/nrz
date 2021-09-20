package com.yi.day7.client;

import com.yi.day7.pojo.User;
import com.yi.day7.proto.ProtoMessage;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class ClientSession {
    public static final AttributeKey<ClientSession> SESSION_KEY = AttributeKey.valueOf("SESSION_KEY");

    private Channel channel;
    private User user;
    private String sessionId;
    private boolean isConnected = false;
    private boolean isLogin = false;
    private Map<String, Object> map = new HashMap<>();

    public static void loginSuccess(ChannelHandlerContext ctx, ProtoMessage.Message pkg) {
        Channel channel = ctx.channel();
        ClientSession session = channel.attr(ClientSession.SESSION_KEY).get();
        session.setSessionId(pkg.getSessionId());
        session.setLogin(true);
        System.out.println("登录成功");
    }

    public static ClientSession getSession(ChannelHandlerContext ctx) {
        Channel channel = ctx.channel();
        return channel.attr(ClientSession.SESSION_KEY).get();
    }

    public ClientSession(Channel channel) {
        this.channel = channel;
        this.sessionId = String.valueOf(-1);
        channel.attr(ClientSession.SESSION_KEY).set(this);
    }

    public String getRemoteAddress() {
        return channel.remoteAddress().toString();
    }

    public ChannelFuture writeAndFlush(Object pkg) {
        return channel.writeAndFlush(pkg);
    }

    public void writeAndClose(Object pkg) {
        channel.writeAndFlush(pkg).addListener(ChannelFutureListener.CLOSE);
    }

    public void close() {
        isConnected = false;

        ChannelFuture future = channel.close();
        future.addListener(fut -> {
            if (fut.isSuccess()) {
                System.out.println("连接成功断开");
            } else {
                System.out.println("连接断开失败");
            }
        });
    }
}
