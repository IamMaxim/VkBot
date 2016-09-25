package ru.iammaxim.VkBot.LocalCommands.Commands;

import ru.iammaxim.VkBot.LocalCommands.CommandBase;
import ru.iammaxim.VkBot.Main;

import java.io.File;

/**
 * Created by maxim on 21.09.2016.
 */
public class CommandLoadModule extends CommandBase {
    @Override
    public String getName() {
        return "loadmodule";
    }

    @Override
    public void run(String[] args) {
        if (args.length == 1) {
            Main.instance.getModuleManager().loadModule(new File("modules/" + args[0]));
        }
    }
}
