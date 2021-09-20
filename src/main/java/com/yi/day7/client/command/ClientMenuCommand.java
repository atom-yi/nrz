package com.yi.day7.client.command;

import lombok.Data;

import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;

@Data
public class ClientMenuCommand implements BaseCommand {
    public static final String KEY = "0";
    private String allCommandShow;
    private String commandInput;

    @Override
    public String getKey() {
        return KEY;
    }

    @Override
    public String getTip() {
        return "显示所有命令";
    }

    @Override
    public void exec(Scanner scanner) {
        System.out.println("请输入操作指令：" + allCommandShow);
        commandInput = scanner.next();
    }

    public void setAllCommandShow(Map<String, BaseCommand> commandMap) {
        StringBuilder menu = new StringBuilder();
        menu.append("【菜单】");

        Iterator<Map.Entry<String, BaseCommand>> iter = commandMap.entrySet().iterator();
        while (iter.hasNext()) {
            BaseCommand cmd = iter.next().getValue();
            menu.append(cmd.getKey())
                    .append("->")
                    .append(cmd.getTip())
                    .append(" | ");
        }
        allCommandShow = menu.toString();
    }
}
