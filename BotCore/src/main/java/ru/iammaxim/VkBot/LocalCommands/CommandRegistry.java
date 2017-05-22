package ru.iammaxim.VkBot.LocalCommands;

import ru.iammaxim.VkBot.LocalCommands.Commands.*;
import ru.iammaxim.VkBot.Main;

import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by Maxim on 20.06.2016.
 */
public class CommandRegistry {
    public HashMap<String, LocalCommandBase> commands = new HashMap<>();

    public void register() {
        registerCommand(new CommandRun());
        registerCommand(new CommandStop());
        registerCommand(new CommandSave());
        registerCommand(new CommandGetUsers());
        registerCommand(new CommandDeleteUser());
        registerCommand(new CommandGC());
        registerCommand(new CommandHelp());
        registerCommand(new CommandRestart());
        registerCommand(new CommandGetActiveModules());
        registerCommand(new CommandUnloadModule());
        registerCommand(new CommandLoadModule());
        registerCommand(new CommandAddAdmin());
        registerCommand(new CommandRemoveAdmin());
        registerCommand(new CommandExec());
    }

    public void registerCommand(LocalCommandBase command) {
        commands.put(command.getName(), command);
    }

    public void runCommand(String command, boolean forceCurrentThread) {
        String[] args = command.split(" ");
        LocalCommandBase cmd = commands.get(args[0].toLowerCase());
        if (cmd != null) {
            if (forceCurrentThread || cmd.runOnProcessThread())
                cmd.run(Arrays.copyOfRange(args, 1, args.length));
            else
                Main.instance.addTask(() -> cmd.run(Arrays.copyOfRange(args, 1, args.length)));
        } else System.out.println("command not found");
    }
}
