package com.yi.day7.client.command;

import lombok.Data;

import java.util.Scanner;

@Data
public class LoginConsoleCommand implements BaseCommand {
    public static final String KEY = "1";
    private String userName;
    private String password;

    @Override
    public String getKey() {
        return KEY;
    }

    @Override
    public String getTip() {
        return "登录";
    }

    @Override
    public void exec(Scanner scanner) {
        System.out.println("请输入用户信息(id@password) ");
        String[] info = null;
        while (true) {
            String input = scanner.next();
            info = input.split("@");
            if (info.length != 2) {
                System.out.println("格式错误，请按照 id@password 的格式输入");
            } else {
                break;
            }
        }

        userName = info[0];
        password = info[1];
    }
}
