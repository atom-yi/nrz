package com.yi.day7.client;

import com.yi.day7.client.command.ChatConsoleCommand;
import com.yi.day7.client.command.ClientMenuCommand;
import com.yi.day7.client.command.LoginConsoleCommand;
import com.yi.day7.client.command.LogoutConsoleCommand;
import com.yi.day7.client.handler.ChatMsgHandler;
import com.yi.day7.client.handler.ExceptionHandler;
import com.yi.day7.client.handler.HeartBeatHandler;
import com.yi.day7.client.handler.LoginResponseHandler;
import com.yi.day7.client.sender.ChatSender;
import com.yi.day7.client.sender.LoginSender;

public class ClientContext {
    // controller
    public static final CommandController controller = new CommandController();
    public static final ChatNettyClient nettyClient = new ChatNettyClient();

    // handler
    public static final ExceptionHandler exceptionHandler = new ExceptionHandler();
    public static final ChatMsgHandler chatMsgHandler = new ChatMsgHandler();
    public static final HeartBeatHandler heartBeatHandler = new HeartBeatHandler();
    public static final LoginResponseHandler loginHandler = new LoginResponseHandler();


    // command
    public static final ClientMenuCommand menuCommand = new ClientMenuCommand();
    public static final ChatConsoleCommand chatCommand = new ChatConsoleCommand();
    public static final LoginConsoleCommand loginCommand = new LoginConsoleCommand();
    public static final LogoutConsoleCommand logoutCommand = new LogoutConsoleCommand();

    // sender
    public static final LoginSender loginSender = new LoginSender();
    public static final ChatSender chatSender = new ChatSender();

}
