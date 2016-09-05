package ru.iammaxim.ModuleMain;

import org.json.JSONException;
import org.json.JSONObject;
import ru.iammaxim.ModuleBase.ModuleBase;
import ru.iammaxim.ModuleMain.Commands.*;
import ru.iammaxim.VkBot.Main;
import ru.iammaxim.VkBot.Net;
import ru.iammaxim.VkBot.Objects.ObjectMessage;
import ru.iammaxim.VkBot.Request;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Maxim on 21.06.2016.
 */
public class ModuleMain extends ModuleBase {
    public static final String BOTNAME = "ImmaBot";
    private Main main = Main.instance;
    private List<CommandBase> commands = new ArrayList<>();

    public ModuleMain() {
        commands.add(new CommandSay());
        commands.add(new CommandExecute());
        commands.add(new CommandWhoAreYou());
        commands.add(new CommandGetChats());
    }

    @Override
    public void process(ObjectMessage msg) {
        if (!msg.body.startsWith("/") && !msg.body.startsWith("\\/")) return;
        String [] args = msg.body.split(" ");
        String commandName = args[0];
        commandName = commandName.replace("/", "");

        for (CommandBase cmd : commands) {
            if (commandName.equals(cmd.getCommandName())) {
                main.addTask(() -> {
                    cmd.process(msg, Arrays.copyOfRange(args, 1, args.length));
                });
                break;
            }
        }
    }

    @Override
    public String getName() {
        return "main";
    }
}
