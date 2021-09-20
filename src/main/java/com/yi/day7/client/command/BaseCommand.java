package com.yi.day7.client.command;

import java.util.Scanner;

public interface BaseCommand {
    String getKey();
    String getTip();
    void exec(Scanner scanner);
}
