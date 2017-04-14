package ru.iammaxim.ModuleMain.Commands;

import ru.iammaxim.ModuleMain.ModuleMain;
import ru.iammaxim.VkBot.Groups.Messages;
import ru.iammaxim.VkBot.Groups.Users;
import ru.iammaxim.VkBot.Objects.ObjectMessage;
import ru.iammaxim.VkBot.Objects.ObjectUser;
import ru.iammaxim.VkBot.UserDB;
import ru.iammaxim.VkBot.UserManager;

/**
 * Created by maxim on 08.10.2016.
 */
public class CommandGetAdmins extends CommandBase {
    @Override
    public String getCommandName() {
        return "getadmins";
    }

    @Override
    public void process(ObjectMessage msg, String[] args) {
        StringBuilder sb = new StringBuilder();
        UserManager.getAdmins().forEach(i -> {
            ObjectUser user = Users.get(i);
            sb.append(i).append(" (").append(user.first_name).append(' ').append(user.last_name).append(")<br>");
        });
        if (sb.length() > 0)
            Messages.send(msg.from_id, sb.substring(0, sb.length() - 4));
        else
            Messages.send(msg.from_id, "No admins. Add one from bot console via /addAdmin <user_id>");
    }

    @Override
    public String getHelp() {
        return "/getAdmins - returns list of users allowed to use commands";
    }
}
