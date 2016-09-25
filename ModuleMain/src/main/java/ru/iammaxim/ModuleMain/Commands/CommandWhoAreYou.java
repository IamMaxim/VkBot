package ru.iammaxim.ModuleMain.Commands;

import ru.iammaxim.VkBot.Groups.Messages;
import ru.iammaxim.VkBot.Main;
import ru.iammaxim.VkBot.Objects.ObjectMessage;
import ru.iammaxim.VkBot.Objects.ObjectUser;

/**
 * Created by maxim on 22.08.2016.
 */
public class CommandWhoAreYou extends CommandBase {
    @Override
    public String getCommandName() {
        return "whoareyou";
    }

    @Override
    public void process(ObjectMessage msg, String[] args) {
        ObjectUser user = Main.instance.getBotUser();
        Messages.send(msg.from_id, "Я " + user.first_name + " " + user.last_name);
    }
}
