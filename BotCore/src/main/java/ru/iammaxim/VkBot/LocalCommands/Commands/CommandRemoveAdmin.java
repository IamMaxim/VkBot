package ru.iammaxim.VkBot.LocalCommands.Commands;

import ru.iammaxim.VkBot.LocalCommands.LocalCommandBase;
import ru.iammaxim.VkBot.UserManager;

/**
 * Created by maxim on 14.04.2017.
 */
public class CommandRemoveAdmin extends LocalCommandBase {
    @Override
    public String getName() {
        return "removeadmin";
    }

    @Override
    public void run(String[] args) {
        try {
            UserManager.removeAdmin(Integer.parseInt(args[0]));
            System.out.println("User " + args[0] + " is no more admin.");
        } catch (NumberFormatException e) {
            System.out.println("Invalid user_id. Must be integer number");
        }
    }
}
