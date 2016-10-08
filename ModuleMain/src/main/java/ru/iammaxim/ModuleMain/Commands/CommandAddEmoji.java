package ru.iammaxim.ModuleMain.Commands;

import ru.iammaxim.VkBot.Groups.Messages;
import ru.iammaxim.VkBot.Objects.ObjectMessage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by maxim on 08.10.2016.
 */
public class CommandAddEmoji extends CommandBase {
    @Override
    public String getCommandName() {
        return "addemoji";
    }

    @Override
    public void process(ObjectMessage msg, String[] args) {
        String[] strs = msg.body.substring(10).split("\\|");
        if (strs.length != 2) {
            Messages.send(msg.from_id, "Invalid syntax");
            return;
        }
        CommandGetEmoji.emojiDB.put(strs[0], strs[1]);
        try {
            File f = new File(CommandGetEmoji.filepath);
            if (!f.exists()) f.createNewFile();
            FileOutputStream fos = new FileOutputStream(f, true);
            fos.write((strs[0] + '|' + strs[1] + '\n').getBytes());
            fos.close();
            Messages.send(msg.from_id, "Emoji added");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getHelp() {
        return "/addemoji - adds emoji to database to use /getemoji";
    }
}
