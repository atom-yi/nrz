package com.yi.day7.client;

public class ClientApplication {

    public static void main(String[] args) {
        startChatClient();
    }

    private static void startChatClient() {
        ClientContext.controller.initCommandMap();
        ClientContext.nettyClient.init("localhost", 8080);
        ClientContext.controller.commandThreadRunning();
    }

}
