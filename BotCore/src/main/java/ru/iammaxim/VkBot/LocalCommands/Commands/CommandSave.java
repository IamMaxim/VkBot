package ru.iammaxim.VkBot.LocalCommands.Commands;

import ru.iammaxim.VkBot.LocalCommands.LocalCommandBase;
import ru.iammaxim.VkBot.Main;
import ru.iammaxim.VkBot.ModuleManager;
import ru.iammaxim.VkBot.UserDB;
import ru.iammaxim.VkBot.UserManager;

import java.io.File;

/**
 * Created by maxim on 19.08.2016.
 */
public class CommandSave extends LocalCommandBase {
    @Override
    public String getName() {
        return "save";
    }

    @Override
    public void run(String[] args) {
        System.out.println("Saving data...");
        UserDB.save();
        UserManager.save();
        Main.instance.getModuleManager().save();
    }
}
