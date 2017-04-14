package ru.iammaxim.ModuleMain.Commands;

import ru.iammaxim.ModuleMain.ModuleMain;
import ru.iammaxim.VkBot.Groups.Messages;
import ru.iammaxim.VkBot.Objects.ObjectMessage;
import ru.iammaxim.VkBot.UserManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by maxim on 08.10.2016.
 */
public class CommandAddAdmin extends CommandBase {
    @Override
    public String getCommandName() {
        return "addadmin";
    }

    @Override
    public void process(ObjectMessage msg, String[] args) {
        try {
            if (!UserManager.isAdmin(msg.user_id)) {
                Messages.send(msg.from_id, UserManager.getAccessDeniedText());
                return;
            }

            UserManager.addAdmin(Integer.parseInt(args[0]));
            Messages.send(msg.from_id, "User " + args[0] + " is now admin");
        } catch (NumberFormatException e) {
            Messages.send(msg.from_id, "Invalid user_id. Must be integer number");
        }
    }

    @Override
    public String getHelp() {
        return "/addAdmin <user_id> - allows user <user_id> to use commands";
    }
}
