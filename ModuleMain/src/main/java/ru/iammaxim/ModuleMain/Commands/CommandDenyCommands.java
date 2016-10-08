package ru.iammaxim.ModuleMain.Commands;

import ru.iammaxim.ModuleMain.ModuleMain;
import ru.iammaxim.VkBot.Groups.Messages;
import ru.iammaxim.VkBot.Objects.ObjectMessage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by maxim on 08.10.2016.
 */
public class CommandDenyCommands extends CommandBase {
    @Override
    public String getCommandName() {
        return "denycommands";
    }

    @Override
    public void process(ObjectMessage msg, String[] args) {
        try {
            File f = new File(ModuleMain.filepath);
            if (!f.exists()) {
                Messages.send(msg.from_id, "Failed to open file");
            }
            Scanner scanner = new Scanner(f);
            ArrayList<Integer> ids = new ArrayList<>();
            int to_deny = Integer.parseInt(msg.body.substring(14));
            ModuleMain.denyUser(to_deny);
            boolean denied = false;
            while (scanner.hasNext()) {
                int id = scanner.nextInt();
                if (id != to_deny) ids.add(id);
                else denied = true;
            }
            scanner.close();
            if (!denied) {
                Messages.send(msg.from_id, "User " + to_deny + " not found");
                return;
            }

            FileOutputStream fos = new FileOutputStream(f);
            ids.forEach(id -> {
                try {
                    fos.write((String.valueOf(id) + '\n').getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            fos.close();
            Messages.send(msg.from_id, "User " + to_deny + " denied to use commands");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public String getHelp() {
        return "/denycommands <user_id> - denies user <user_id> to use commands";
    }
}
