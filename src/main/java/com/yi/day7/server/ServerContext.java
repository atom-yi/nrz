package com.yi.day7.server;

import com.yi.day7.server.builder.ChatMsgBuilder;
import com.yi.day7.server.builder.LoginResponseBuilder;
import com.yi.day7.server.handler.ChatRedirectHandler;
import com.yi.day7.server.handler.ExceptionHandler;
import com.yi.day7.server.handler.LoginHandler;
import com.yi.day7.server.processor.ChatRedirectProcessor;
import com.yi.day7.server.processor.LoginProcessor;

public class ServerContext {
    public static final ChatServer chatServer = new ChatServer();

    // handler
    public static final ExceptionHandler exceptionHandler = new ExceptionHandler();
    public static final LoginHandler loginHandler = new LoginHandler();
    public static final ChatRedirectHandler chatRedirectHandler = new ChatRedirectHandler();

    // processor
    public static final LoginProcessor loginProcessor = new LoginProcessor();
    public static final ChatRedirectProcessor chatRedirectProcessor = new ChatRedirectProcessor();

    // builder
    public static final LoginResponseBuilder loginResponseBuilder = new LoginResponseBuilder();
    public static final ChatMsgBuilder chatMsgBuilder = new ChatMsgBuilder();
}
