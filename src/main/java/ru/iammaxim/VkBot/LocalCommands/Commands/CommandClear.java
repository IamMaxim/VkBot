package ru.iammaxim.VkBot.LocalCommands.Commands;

import ru.iammaxim.VkBot.LocalCommands.LocalCommandBase;
import ru.iammaxim.VkBot.Main;

/**
 * Created by maxim on 26.08.2016.
 */
public class CommandClear extends LocalCommandBase {
    @Override
    public String getName() {
        return "clear";
    }

    @Override
    public void run(String[] args) {
        Main.instance.consoleWindow.console.label.setText("");
    }
}
