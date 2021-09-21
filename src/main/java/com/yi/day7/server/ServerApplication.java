package com.yi.day7.server;

public class ServerApplication {
    public static void main(String[] args) {
        startChatServer();
    }

    private static void startChatServer() {
        ServerContext.chatServer.init(8080);
        ServerContext.chatServer.run();
    }

}
