package ru.iammaxim.ModuleMain;

import ru.iammaxim.ModuleMain.Commands.*;
import ru.iammaxim.VkBot.Main;
import ru.iammaxim.VkBot.ModuleBase.ModuleBase;
import ru.iammaxim.VkBot.Objects.ObjectMessage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Maxim on 21.06.2016.
 */
public class ModuleMain extends ModuleBase {
    private Main main = Main.instance;
    private List<CommandBase> commands = new ArrayList<>();

    public ModuleMain() {
        commands.add(new CommandSay());
        commands.add(new CommandWhoAreYou());
        commands.add(new CommandGetChats());
        commands.add(new CommandExec());
        commands.add(new CommandHelp());
//        commands.add(new CommandGetEmoji());
//        commands.add(new CommandAddEmoji());
        commands.add(new CommandAddAdmin());
        commands.add(new CommandRemoveAdmin());
        commands.add(new CommandGetAdmins());
        commands.add(new CommandSearchMusic());
    }

    @Override
    public void process(ObjectMessage msg) {
        if (!msg.body.startsWith("/") && !msg.body.startsWith("\\/")) return;
        String [] args = msg.body.split(" ");
        String commandName = args[0];
        commandName = commandName.replace("/", "");

        for (CommandBase cmd : commands) {
            if (commandName.toLowerCase().equals(cmd.getCommandName())) {
                main.addTask(() -> cmd.process(msg, Arrays.copyOfRange(args, 1, args.length)));
                break;
            }
        }
    }

    @Override
    public String getName() {
        return "ModuleMain";
    }

    @Override
    public String getHelp() {
        StringBuilder sb = new StringBuilder();
        commands.forEach(c -> sb.append(c.getHelp()).append("<br>"));
        return sb.substring(0, sb.length() - 4);
    }
}
