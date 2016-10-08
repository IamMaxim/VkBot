package ru.iammaxim.VkBot.LocalCommands.Commands;

import org.json.JSONObject;
import ru.iammaxim.VkBot.LocalCommands.LocalCommandBase;
import ru.iammaxim.VkBot.Net;

import java.io.IOException;
import java.util.Arrays;

public class CommandRun extends LocalCommandBase {
    @Override
    public String getName() {
        return "run";
    }

    @Override
    public void run(String[] args) {
        if (args.length < 1) {
            System.out.println("Not enough arguments");
            return;
        }

        try {
            System.out.println("\n" + new JSONObject(Net.processRequest(args[0], true, Arrays.copyOfRange(args, 1, args.length))).toString(4));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
