package ru.iammaxim.ModuleMain.Commands;

import ru.iammaxim.ModuleMain.ModuleMain;
import ru.iammaxim.VkBot.Groups.Messages;
import ru.iammaxim.VkBot.Objects.ObjectMessage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by maxim on 08.10.2016.
 */
public class CommandAllowCommands extends CommandBase {
    @Override
    public String getCommandName() {
        return "allowcommands";
    }

    @Override
    public void process(ObjectMessage msg, String[] args) {
        if (!ModuleMain.isAllowed(msg.user_id)) {
            Messages.send(msg.from_id, ModuleMain.getAccessDeniedText());
            return;
        }
        try {
            File f = new File(ModuleMain.filepath);
            if (!f.exists())
                f.createNewFile();
            FileOutputStream fos = new FileOutputStream(f, true);
            int to_allow = Integer.parseInt(msg.body.substring(15));
            ModuleMain.allowUser(to_allow);
            fos.write((to_allow + "\n").getBytes());
            fos.close();
            Messages.send(msg.from_id, "User " + to_allow + " allowed to use commands");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getHelp() {
        return "/allowcommands <user_id> - allows user <user_id> to use commands";
    }
}
