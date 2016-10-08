package ru.iammaxim.ModuleMain.Commands;

import ru.iammaxim.ModuleMain.ModuleMain;
import ru.iammaxim.VkBot.Groups.Messages;
import ru.iammaxim.VkBot.Main;
import ru.iammaxim.VkBot.Objects.ObjectMessage;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class CommandSay extends CommandBase {
    Main main = Main.instance;

    @Override
    public String getCommandName() {
        return "say";
    }

    @Override
    public void process(ObjectMessage msg, String[] args) {
        if (!(ModuleMain.isAllowed(msg.user_id))) {
            Messages.send(msg.from_id, ModuleMain.getAccessDeniedText());
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

    @Override
    public String getHelp() {
        return "/say <message> - sends message to current dialog";
    }
}
