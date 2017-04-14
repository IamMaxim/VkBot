package ru.iammaxim.VkBot.LocalCommands.Commands;

import ru.iammaxim.VkBot.LocalCommands.LocalCommandBase;
import ru.iammaxim.VkBot.Main;

/**
 * Created by maxim on 27.08.2016.
 */
public class CommandRestart extends LocalCommandBase {
    @Override
    public String getName() {
        return "restart";
    }

    @Override
    public void run(String[] args) {
        System.out.println();
        Main.instance.destroy();
        Main.instance.init();
    }
}
