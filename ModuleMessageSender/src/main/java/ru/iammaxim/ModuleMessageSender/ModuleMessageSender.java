package ru.iammaxim.ModuleMessageSender;

import ru.iammaxim.VkBot.Groups.Messages;
import ru.iammaxim.VkBot.Main;
import ru.iammaxim.VkBot.ModuleBase.ModuleBase;
import ru.iammaxim.VkBot.Net;
import ru.iammaxim.VkBot.Objects.ObjectMessage;

import java.io.*;

/**
 * Created by maxim on 23.10.2016.
 */
public class ModuleMessageSender extends ModuleBase {
    private File messagesFile, configFile;
    private BufferedReader bufferedReader;

    {
        try {
            messagesFile = new File("ModuleMessageSenderMessages.txt");
            if (!messagesFile.exists()) {
                System.out.println("Error loading message sender module. File \"ModuleMessageSenderMessages.txt\" not found.");
            }
            configFile = new File("modules/MessageSender/config.txt");
            if (!configFile.exists())
                configFile.createNewFile();
            bufferedReader = new BufferedReader(new FileReader(messagesFile));
//            bufferedReader.
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void process(ObjectMessage inputMessage) {
        if (inputMessage.user_id == 151657174) {
            String s = inputMessage.body.toLowerCase();
            if (s.contains("подгони") && s.contains("сообщен")) {
                Main.instance.addTask(() -> {
                    try {
                        Net.processRequest("messages.setActivity", true, "type=typing", "peer_id=" + inputMessage.from_id);
                        Thread.sleep(1000);
                        Messages.send(inputMessage.from_id, "Ща");
                        Net.processRequest("messages.setActivity", true, "type=typing", "peer_id=" + inputMessage.from_id);
                        Thread.sleep(3000);
                        Messages.send(inputMessage.from_id, "На. не благодари");
                        Net.processRequest("messages.setActivity", true, "type=typing", "peer_id=" + inputMessage.from_id);
                        Thread.sleep(1000);


                    } catch (InterruptedException | IOException e) {
                        e.printStackTrace();
                    }
                });
            }
        }
    }

    @Override
    public String getName() {
        return "messagesender";
    }

    @Override
    public String getHelp() {
        return "";
    }
}
