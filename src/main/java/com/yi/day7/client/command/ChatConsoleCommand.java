package com.yi.day7.client.command;

import lombok.Data;

import java.util.Scanner;

@Data
public class ChatConsoleCommand implements BaseCommand {
    public static final String KEY = "2";

    private String toUserId;
    private String message;

    @Override
    public String getKey() {
        return KEY;
    }

    @Override
    public String getTip() {
        return "聊天";
    }

    @Override
    public void exec(Scanner scanner) {
        System.out.println("请输入聊天信息(id:message): ");
        String[] info;
        while (true) {
            String input = scanner.next();
            info = input.split(":");
            if (info.length != 2) {
                System.out.println("格式错误，请按照 id:message 的格式输入");
            } else {
                break;
            }
        }

        toUserId = info[0];
        message = info[1];
    }
}
