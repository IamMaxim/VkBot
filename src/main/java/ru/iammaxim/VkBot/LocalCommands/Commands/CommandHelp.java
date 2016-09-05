package ru.iammaxim.VkBot.LocalCommands.Commands;

import ru.iammaxim.VkBot.LocalCommands.CommandBase;
import ru.iammaxim.VkBot.LocalCommands.CommandRegistry;
import ru.iammaxim.VkBot.Main;

/**
 * Created by maxim on 26.08.2016.
 */
public class CommandHelp extends CommandBase {
    @Override
    public String getName() {
        return "help";
    }

    @Override
    public void run(String[] args) {
        StringBuilder s = new StringBuilder("Avaible commands:");
        Main.instance.localCommandRegistry.commands.entrySet().forEach(c -> {
            s.append('\n').append(c.getKey());
        });
        System.out.println(s.toString());
    }
}
