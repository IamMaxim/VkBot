package ru.iammaxim.VkBot.LocalCommands;

import ru.iammaxim.VkBot.LocalCommands.Commands.*;
import ru.iammaxim.VkBot.Main;

import java.util.*;

/**
 * Created by Maxim on 20.06.2016.
 */
public class CommandRegistry {
    public HashMap<String, CommandBase> commands = new HashMap<>();

    public void register() {
        registerCommand(new CommandRun());
        registerCommand(new CommandStop());
        registerCommand(new CommandSave());
        registerCommand(new CommandGetUsers());
        registerCommand(new CommandDeleteUser());
        registerCommand(new CommandGenMessage());
        registerCommand(new CommandClear());
        registerCommand(new CommandGC());
        registerCommand(new CommandHelp());
        registerCommand(new CommandRestart());
    }

    public void registerCommand(CommandBase command) {
        commands.put(command.getName(), command);
    }

    public void runCommand(String command, boolean forceCurrentThread) {
        String[] args = command.split(" ");
        CommandBase cmd = commands.get(args[0].toLowerCase());
        if (cmd != null) {
            if (!forceCurrentThread && cmd.runOnProcessThread())
                Main.instance.addTask(() -> cmd.run(Arrays.copyOfRange(args, 1, args.length)));
            else cmd.run(Arrays.copyOfRange(args, 1, args.length));
        } else System.out.println("command not found");
    }
}
