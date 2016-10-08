package ru.iammaxim.ModuleMain.Commands;

import ru.iammaxim.ModuleMain.ModuleMain;
import ru.iammaxim.VkBot.Groups.Messages;
import ru.iammaxim.VkBot.Groups.Users;
import ru.iammaxim.VkBot.Objects.ObjectMessage;
import ru.iammaxim.VkBot.Objects.ObjectUser;
import ru.iammaxim.VkBot.UserDB;

/**
 * Created by maxim on 08.10.2016.
 */
public class CommandGetAllowed extends CommandBase {
    @Override
    public String getCommandName() {
        return "getallowed";
    }

    @Override
    public void process(ObjectMessage msg, String[] args) {
        StringBuilder sb = new StringBuilder();
        ModuleMain.getAllowed().forEach(i -> {
            ObjectUser user = Users.get(i);
            sb.append(i).append(" (").append(user.first_name).append(' ').append(user.last_name).append(")<br>");
        });
        Messages.send(msg.from_id, sb.substring(0, sb.length() - 4));
    }

    @Override
    public String getHelp() {
        return "/getallowed - returns list of users allowed to use commands";
    }
}
