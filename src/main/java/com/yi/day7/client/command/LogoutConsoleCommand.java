package com.yi.day7.client.command;

import lombok.Data;

import java.util.Scanner;

@Data
public class LogoutConsoleCommand implements BaseCommand {
    public static final String KEY = "10";

    @Override
    public String getKey() {
        return KEY;
    }

    @Override
    public String getTip() {
        return "注销";
    }

    @Override
    public void exec(Scanner scanner) {
        System.out.println("注销成功");
    }
}
