package ru.iammaxim.VkBot.LocalCommands.Commands;

import ru.iammaxim.VkBot.LocalCommands.CommandBase;
import ru.iammaxim.VkBot.UserDB;

/**
 * Created by maxim on 19.08.2016.
 */
public class CommandSave extends CommandBase {
    @Override
    public String getName() {
        return "save";
    }

    @Override
    public void run(String[] args) {
        UserDB.save();
    }
}
