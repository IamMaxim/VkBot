package ru.iammaxim.ModuleMain.Commands;

import ru.iammaxim.VkBot.Groups.Messages;
import ru.iammaxim.VkBot.Main;
import ru.iammaxim.VkBot.Net;
import ru.iammaxim.VkBot.Objects.ObjectMessage;
import ru.iammaxim.VkBot.Request;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public class CommandSay extends CommandBase {
    Main main = Main.instance;
    private static List<Integer> allowed_to_use = new ArrayList<>();
    private static final String filepath = "modules/main/say/allowed_to_use.txt";


    //due to flood control
    private String getAccessDeniedText() {
        return "access denied: " + Main.getRandomHex();
    }

    {
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
    }

    public boolean isAllowed(int id) {
        for (Integer anAllowed_to_use : allowed_to_use) {
            if (anAllowed_to_use == id) return true;
        }
        return false;
    }

    @Override
    public String getCommandName() {
        return "say";
    }

    @Override
    public void process(ObjectMessage msg, String[] args) {
        if (!(isAllowed(msg.user_id) || msg.out)) {
            Messages.send(msg.from_id, getAccessDeniedText());
            return;
        }

        try {
            String s = String.join(" ", Arrays.copyOfRange(args, 0, args.length));
            if (s.isEmpty()) return;
            Messages.send(msg.from_id, s);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            Messages.send(msg.from_id, "AH TI SOOQA! POLOMAT' MENYA RESHIL!? " + Main.getRandomHex());
        }
    }
}
