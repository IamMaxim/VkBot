package ru.iammaxim.ModuleMain;

import ru.iammaxim.ModuleMain.Commands.*;
import ru.iammaxim.VkBot.Main;
import ru.iammaxim.VkBot.ModuleBase.ModuleBase;
import ru.iammaxim.VkBot.Objects.ObjectMessage;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Maxim on 21.06.2016.
 */
public class ModuleMain extends ModuleBase {
    private Main main = Main.instance;
    private List<CommandBase> commands = new ArrayList<>();
    private static List<Integer> allowed_to_use = new ArrayList<>();
    private static final String filepath = "modules/main/say/allowed_to_use.txt";

    public ModuleMain() {
        try {
            File file = new File(filepath);
            Scanner scanner = new Scanner(file);
            while (scanner.hasNext()) {
                int i = scanner.nextInt();
                System.out.println("Added " + i + " to say allowed list");
                allowed_to_use.add(i);
            }
            scanner = null;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        commands.add(new CommandSay());
        commands.add(new CommandWhoAreYou());
        commands.add(new CommandGetChats());
        commands.add(new CommandExecute());
    }

    public static String getAccessDeniedText() {
        return "access denied: " + Main.getRandomHex();
    }

    public static boolean isAllowed(int id) {
        for (Integer anAllowed_to_use : allowed_to_use) {
            if (anAllowed_to_use == id) return true;
        }
        return false;
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
