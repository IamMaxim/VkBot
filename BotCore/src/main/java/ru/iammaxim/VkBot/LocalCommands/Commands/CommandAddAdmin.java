package ru.iammaxim.VkBot.LocalCommands.Commands;

import ru.iammaxim.VkBot.LocalCommands.LocalCommandBase;
import ru.iammaxim.VkBot.UserManager;

/**
 * Created by maxim on 14.04.2017.
 */
public class CommandAddAdmin extends LocalCommandBase {
    @Override
    public String getName() {
        return "addadmin";
    }

    @Override
    public void run(String[] args) {
        try {
            UserManager.addAdmin(Integer.parseInt(args[0]));
            System.out.println("User " + args[0] + " is now admin.");
        } catch (NumberFormatException e) {
            System.out.println("Invalid user_id. Must be integer number");
        }
    }
}
