package ru.iammaxim.ModuleMain.Commands;

import ru.iammaxim.VkBot.Groups.Messages;
import ru.iammaxim.VkBot.Objects.ObjectMessage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;

/**
 * Created by maxim on 08.10.2016.
 */
public class CommandAddEmoji extends CommandBase {
    public static boolean isEmoji(int charID) {
        return charID == 0x3030 || charID == 0x00AE || charID == 0x00A9 ||
                (charID > 0x2100 && charID < 0x27BF) ||
                (charID > 0xFE00 && charID < 0xFE0F) ||
                (charID > 0x1F900 && charID < 0x1F9FF);
    }

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

        //check if all characters are emojis
        boolean isValid = true;
        Iterator<Integer> it = strs[1].chars().iterator();
        while (it.hasNext()) {
            int c = it.next();
            if (!isEmoji(c)) {
                Messages.send(msg.from_id, "Invalid emoji: " + c);
                return;
            }
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
