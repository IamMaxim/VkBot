package ru.iammaxim.ModuleMain.Commands;

import ru.iammaxim.VkBot.Groups.Messages;
import ru.iammaxim.VkBot.Objects.ObjectMessage;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Created by maxim on 08.10.2016.
 */
public class CommandGetEmoji extends CommandBase {
    public static HashMap<String, String> emojiDB = new HashMap<>();
    public static final String filepath = "modules/main/emojis.txt";

    static {
        File file;
        file = new File(filepath);
        if (file.exists()) {
            try {
                Scanner scanner = new Scanner(file);
                while (scanner.hasNext()) {
                    String[] strs = scanner.nextLine().split("\\|");
                    String string = strs[0];
                    String emoji = strs[1];
                    emojiDB.put(string, emoji);
                }
                scanner.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String getCommandName() {
        return "getemoji";
    }

    @Override
    public void process(ObjectMessage msg, String[] args) {
        String emojis = emojiDB.get(msg.body.substring(10));
        if (emojis == null)
            Messages.send(msg.from_id, "Can't find emoji for this text.");
        else
            Messages.send(msg.from_id, emojis);
    }

    @Override
    public String getHelp() {
        return "/getemoji <text> - returns emoji based on <text> (if available)";
    }
}
