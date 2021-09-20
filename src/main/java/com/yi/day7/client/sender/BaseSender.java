package com.yi.day7.client.sender;

import com.yi.day7.client.ClientSession;
import com.yi.day7.pojo.User;
import com.yi.day7.proto.ProtoMessage;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import lombok.Data;

@Data
public abstract class BaseSender {
    private User user;
    private ClientSession session;

    public boolean isConnected() {
        return checkSession() && session.isConnected();
    }

    public boolean isLogin() {
        return checkSession() && session.isLogin();
    }

    public void sendMsg(ProtoMessage.Message msg) {
        if (!isConnected()) {
            System.out.println("当前还未成功连接");
            return;
        }

        Channel channel = getSession().getChannel();
        ChannelFuture future = channel.writeAndFlush(msg);
        future.addListener(f -> {
            if (f.isSuccess()) {
                sendSuccess(msg);
            } else {
                sendFailed(msg);
            }
        });
    }

    public void sendSuccess(ProtoMessage.Message msg) {
        System.out.println("消息发送成功");
    }

    public void sendFailed(ProtoMessage.Message msg) {
        System.out.println("消息发送失败");
    }

    private boolean checkSession() {
        if (session == null) {
            System.out.println("session is null");
            return false;
        }
        return true;
    }
}
