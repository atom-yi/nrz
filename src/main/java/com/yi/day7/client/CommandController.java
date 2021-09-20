package com.yi.day7.client;

import com.yi.day7.client.command.BaseCommand;
import com.yi.day7.client.command.ChatConsoleCommand;
import com.yi.day7.client.command.LoginConsoleCommand;
import com.yi.day7.client.command.LogoutConsoleCommand;
import com.yi.day7.concurrent.FutureTaskScheduler;
import com.yi.day7.pojo.User;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoop;
import io.netty.util.concurrent.GenericFutureListener;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class CommandController {
    private Channel channel;
    private ClientSession session;
    private User user;
    private Map<String, BaseCommand> commandMap;
    private boolean connectedFlag = false;

    GenericFutureListener<ChannelFuture> closeFuture = f -> {
        System.out.println(new Date() + " :连接断开");
        channel = f.channel();

        ClientSession session = channel.attr(ClientSession.SESSION_KEY).get();
        session.close();

        notifyCommandThread();
    };

    GenericFutureListener<ChannelFuture> connectedListener = f -> {
        final EventLoop eventLoop = f.channel().eventLoop();
        if (!f.isSuccess()) {
            System.out.println("连接不成功，将在3s后重试");
            eventLoop.schedule(() -> ClientContext.nettyClient.doConnect(), 3, TimeUnit.SECONDS);
            connectedFlag = false;
        } else {
            connectedFlag = true;
            System.out.println("连接服务器成功");
            channel = f.channel();
            session = new ClientSession(channel);
            channel.closeFuture().addListener(closeFuture);

            notifyCommandThread();
        }
    };

    public void initCommandMap() {
        commandMap = new HashMap<>();
        commandMap.put(ClientContext.menuCommand.getKey(), ClientContext.menuCommand);
        commandMap.put(ClientContext.chatCommand.getKey(), ClientContext.chatCommand);
        commandMap.put(ClientContext.loginCommand.getKey(), ClientContext.loginCommand);
        commandMap.put(ClientContext.logoutCommand.getKey(), ClientContext.logoutCommand);

        ClientContext.menuCommand.setAllCommandShow(commandMap);
    }

    public void startConnectServer() {
        FutureTaskScheduler.add(() -> {
            ClientContext.nettyClient.setConnectedListener(connectedListener);
            ClientContext.nettyClient.doConnect();
        });
    }

    private synchronized void notifyCommandThread() {
        this.notify();
    }

    private synchronized void waitCommandThread() {
        try {
            this.wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void commandThreadRunning() {
        while (true) {
            while (connectedFlag == false) {
                startConnectServer();
                waitCommandThread();
            }

            while (session != null) {
                Scanner scanner = new Scanner(System.in);
                ClientContext.menuCommand.exec(scanner);
                String key = ClientContext.menuCommand.getCommandInput();
                BaseCommand command = commandMap.get(key.trim());
                if (command == null) {
                    System.out.println("无法识别[" + command + "]指令，请重新输入！");
                    continue;
                }

                switch (key) {
                    case ChatConsoleCommand.KEY:
                        command.exec(scanner);
                        startOneChat((ChatConsoleCommand) command);
                        break;
                    case LoginConsoleCommand.KEY:
                        command.exec(scanner);
                        startLogin((LoginConsoleCommand) command);
                        break;
                    case LogoutConsoleCommand.KEY:
                        command.exec(scanner);
                        startLogout((LogoutConsoleCommand) command);
                        break;
                    default:
                        System.out.println(command.getTip() + "命令将不会执行");
                }
            }
        }
    }

    private void startOneChat(ChatConsoleCommand command) {
        if (!isLogin()) {
            System.out.println("请先登录");
            return;
        }
        ClientContext.chatSender.setSession(session);
        ClientContext.chatSender.setUser(user);
        ClientContext.chatSender.sendChatMsg(command.getToUserId(), command.getMessage());
    }

    private void startLogin(LoginConsoleCommand command) {
        if (!connectedFlag) {
            System.out.println("连接未建立");
            return;
        }

        User user = new User();
        user.setUid(command.getUserName());
        user.setToken(command.getPassword());
        user.setDevId("1111");
        this.user = user;
        session.setUser(user);
        ClientContext.loginSender.setUser(user);
        ClientContext.loginSender.setSession(session);
        ClientContext.loginSender.sendLoginMsg();
    }

    private void startLogout(LogoutConsoleCommand command) {
        if (!connectedFlag) {
            System.out.println("连接未建立");
            return;
        }
        // todo: 登出
    }

    private boolean isLogin() {
        if (session == null) {
            System.out.println("session is null");
            return false;
        }
        return session.isLogin();
    }
}
