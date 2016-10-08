package ru.iammaxim.VkBot.LocalCommands.Commands;

import ru.iammaxim.VkBot.LocalCommands.LocalCommandBase;
import ru.iammaxim.VkBot.Objects.ObjectUser;
import ru.iammaxim.VkBot.UserDB;

/**
 * Created by maxim on 20.08.2016.
 */
public class CommandDeleteUser extends LocalCommandBase {
    @Override
    public String getName() {
        return "deleteuser";
    }

    @Override
    public void run(String[] args) {
        try {
            ObjectUser user = UserDB.delete(Integer.parseInt(args[0]));
            if (user == null) {
                System.out.println("Error: no such user in database");
                return;
            }
            System.out.println("Deleted user " + user.first_name + " " + user.last_name);
        } catch (NumberFormatException e) {
            System.out.println("Error: user_id must be a number");
        }
    }
}
