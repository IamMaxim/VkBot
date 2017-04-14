package ru.iammaxim.VkBot.LocalCommands.Commands;

import ru.iammaxim.VkBot.LocalCommands.LocalCommandBase;
import ru.iammaxim.VkBot.Main;

/**
 * Created by maxim on 21.09.2016.
 */
public class CommandUnloadModule extends LocalCommandBase {
    @Override
    public String getName() {
        return "unloadmodule";
    }

    @Override
    public void run(String[] args) {
        if (args.length == 1) {
            Main.instance.getModuleManager().removeModule(args[0]);
        } else {
            System.out.println("Invalid argument count");
        }
    }
}
