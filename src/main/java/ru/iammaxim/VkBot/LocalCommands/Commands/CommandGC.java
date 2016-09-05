package ru.iammaxim.VkBot.LocalCommands.Commands;

import ru.iammaxim.VkBot.LocalCommands.CommandBase;

/**
 * Created by maxim on 26.08.2016.
 */
public class CommandGC extends CommandBase {
    @Override
    public String getName() {
        return "gc";
    }

    @Override
    public void run(String[] args) {
        System.gc();
        System.out.println("Initiated garbage collection.");
    }
}
