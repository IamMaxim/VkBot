package ru.iammaxim.ModuleMain.Commands;

import ru.iammaxim.VkBot.Groups.Messages;
import ru.iammaxim.VkBot.Main;
import ru.iammaxim.VkBot.Objects.ObjectMessage;

/**
 * Created by maxim on 08.10.2016.
 */
public class CommandHelp extends CommandBase {
    @Override
    public String getCommandName() {
        return "help";
    }

    @Override
    public void process(ObjectMessage msg, String[] args) {
        StringBuilder sb = new StringBuilder();
        Main.instance.getModuleManager().getModules().forEach(m -> {
            sb.append("Module ").append(m.getName()).append(":<br>").append(m.getHelp()).append("<br>");
        });
        String s = sb.substring(0, sb.length() - 4);
        Messages.send(msg.from_id, s);
    }

    @Override
    public String getHelp() {
        return "/help - returns help about all loaded modules";
    }
}
