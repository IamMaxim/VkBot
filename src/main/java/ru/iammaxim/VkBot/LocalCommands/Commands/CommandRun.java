package ru.iammaxim.VkBot.LocalCommands.Commands;

import org.json.JSONObject;
import org.json.JSONPointer;
import ru.iammaxim.VkBot.LocalCommands.CommandBase;
import ru.iammaxim.VkBot.Main;
import ru.iammaxim.VkBot.Net;
import ru.iammaxim.VkBot.Request;

import java.io.IOException;
import java.util.Arrays;

public class CommandRun extends CommandBase {
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

        System.out.println(args[0]);

        try {
            System.out.println("\n" + new JSONObject(
                    Net.processRequest(args[0], true, Arrays.copyOfRange(args, 1, args.length))
                    ).toString(4));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
