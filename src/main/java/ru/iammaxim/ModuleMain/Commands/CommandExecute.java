package ru.iammaxim.ModuleMain.Commands;

import ru.iammaxim.VkBot.Groups.Messages;
import ru.iammaxim.VkBot.LocalCommands.CommandRegistry;
import ru.iammaxim.VkBot.Main;
import ru.iammaxim.VkBot.Objects.ObjectMessage;

/**
 * Created by maxim on 20.08.2016.
 */
public class CommandExecute extends CommandBase {
    @Override
    public String getCommandName() {
        return "execute";
    }

    @Override
    public void process(ObjectMessage msg, String[] args) {
        Main.instance.localCommandRegistry.runCommand(String.join(" ", args), true);
        System.out.println(Main.instance.logger.lastString.replace(" ", "%20"));
        Messages.send(msg.from_id, Main.instance.logger.lastString);
    }
}
