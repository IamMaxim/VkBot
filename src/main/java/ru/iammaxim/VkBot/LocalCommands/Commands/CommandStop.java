package ru.iammaxim.VkBot.LocalCommands.Commands;

import ru.iammaxim.VkBot.LocalCommands.CommandBase;
import ru.iammaxim.VkBot.Main;

/**
 * Created by maxim on 19.08.2016.
 */
public class CommandStop extends CommandBase {
    @Override
    public String getName() {
        return "stop";
    }

    @Override
    public boolean runOnProcessThread() {
        return false;
    }

    @Override
    public void run(String[] args) {
        Main.instance.stopThread();
    }
}
