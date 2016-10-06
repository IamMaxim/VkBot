package ru.iammaxim.VkBot.LocalCommands.Commands;

import ru.iammaxim.VkBot.LocalCommands.CommandBase;
import ru.iammaxim.VkBot.Main;
import ru.iammaxim.VkBot.ModuleBase.ModuleBase;

/**
 * Created by maxim on 21.09.2016.
 */
public class CommandGetActiveModules extends CommandBase {
    @Override
    public String getName() {
        return "getactivemodules";
    }

    @Override
    public void run(String[] args) {
        StringBuilder sb = new StringBuilder();
        for (ModuleBase module : Main.instance.getModuleManager().getModules()) {
            sb.append('\n' + module.getName());
        }
        System.out.println(sb.toString());
    }
}
