package ru.iammaxim.ModuleMain.Commands;

import ru.iammaxim.ModuleMain.ModuleMain;
import ru.iammaxim.VkBot.Groups.Messages;
import ru.iammaxim.VkBot.Objects.ObjectMessage;
import ru.iammaxim.VkBot.UserManager;
import sun.plugin2.message.Message;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by maxim on 08.10.2016.
 */
public class CommandRemoveAdmin extends CommandBase {
    @Override
    public String getCommandName() {
        return "removeadmin";
    }

    @Override
    public void process(ObjectMessage msg, String[] args) {
        try {
            if (!UserManager.isAdmin(msg.user_id)) {
                Messages.send(msg.from_id, UserManager.getAccessDeniedText());
                return;
            }

            UserManager.removeAdmin(Integer.parseInt(args[0]));
            Messages.send(msg.from_id, "User " + args[0] + " is no more admin");

        } catch (NumberFormatException e) {
            Messages.send(msg.from_id, "Invalid user_id. Must be integer number");
        }
    }

    @Override
    public String getHelp() {
        return "/removeAdmin <user_id> - denies user <user_id> to use commands";
    }
}
