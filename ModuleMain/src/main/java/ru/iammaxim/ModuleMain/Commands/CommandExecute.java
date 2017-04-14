package ru.iammaxim.ModuleMain.Commands;

import ru.iammaxim.ModuleMain.ModuleMain;
import ru.iammaxim.VkBot.Groups.Messages;
import ru.iammaxim.VkBot.Main;
import ru.iammaxim.VkBot.Objects.ObjectMessage;
import ru.iammaxim.VkBot.UserManager;

/**
 * Created by maxim on 05.10.2016.
 */
public class CommandExecute extends CommandBase {
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
            Main.instance.localCommandRegistry.runCommand(command, true);
            String response = (String) Main.instance.logger.getClass().getField("lastString").get(Main.instance.logger);
            System.out.println(response);
            Messages.send(msg.from_id, response.replace("\n", "<br>").replace("    ", ""));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            Messages.send(msg.from_id, "Failed to execute action");
            e.printStackTrace();
        }
    }

    @Override
    public String getHelp() {
        return "/exec <command> - executes command in bot console";
    }
}
