package ru.iammaxim.ModuleMain.Commands;

import ru.iammaxim.VkBot.Groups.Messages;
import ru.iammaxim.VkBot.Main;
import ru.iammaxim.VkBot.Objects.ObjectMessage;
import ru.iammaxim.VkBot.UserManager;

/**
 * Created by maxim on 05.10.2016.
 */
public class CommandExec extends CommandBase {
    @Override
    public String getCommandName() {
        return "exec";
    }

    @Override
    public void process(ObjectMessage msg, String[] args) {
        try {
            if (!UserManager.isAdmin(msg.user_id)) {
                Messages.send(msg.from_id, UserManager.getAccessDeniedText());
                return;
            }

            String command = msg.body.substring(6);
            System.out.println("trying to exec " + command);
            Main.instance.logger.startLastLogging();
            Main.instance.localCommandRegistry.runCommand(command, true);
            String response = String.join("<br>", Main.instance.logger.lastLog);
            Main.instance.logger.stopLastLogging();
            if (!response.isEmpty())
                Messages.send(msg.from_id, response.replace("\n", "<br>"));
        } catch (Exception e) {
            Messages.send(msg.from_id, "Failed to execute action");
            e.printStackTrace();
        }
    }

    @Override
    public String getHelp() {
        return "/exec <command> - executes command in bot console";
    }
}
