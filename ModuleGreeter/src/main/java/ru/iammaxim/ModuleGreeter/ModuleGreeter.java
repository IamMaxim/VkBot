package ru.iammaxim.ModuleGreeter;

import ru.iammaxim.VkBot.Groups.Messages;
import ru.iammaxim.VkBot.ModuleBase.ModuleBase;
import ru.iammaxim.VkBot.Objects.ObjectMessage;

/**
 * Created by maxim on 13.04.2017.
 */
public class ModuleGreeter extends ModuleBase {
    private static Thread workerThread;

    private static final String[] messages = {
            "Привет! Как дела?",
            "Как жизнь? Хочешь пообщаться?",
            "Ты прикольный. Хочешь познакомиться?",
            "Ты прикольная. Хочешь познакомиться?"
    };

    private String getMessageText() {
        int index = (int) Math.round(Math.random() * messages.length);
        return messages[index];
    }

    public void process(ObjectMessage inputMessage) {
        if (inputMessage.from_id == 151657174 || inputMessage.from_id == 122528012)
            switch (inputMessage.body) {
                case "greeterStart":
                    if (workerThread.isAlive()) {
                        Messages.send(inputMessage.from_id, "This module is already running. Stop it by typing /greeterStop");
                        return;
                    }
                    workerThread = new Thread(() -> {
                        try {
                            while (!Thread.interrupted()) {
                                Messages.send(122528012, getMessageText());
                                Thread.sleep(10000);
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    });
                    workerThread.start();
                    break;
                case "greeterStop":
                    if (!workerThread.isAlive()) {
                        Messages.send(inputMessage.from_id, "This module is already stopped. Start it by typing /greeterStart");
                        return;
                    }
                    workerThread.interrupt();
            }
    }

    public String getName() {
        return "ModuleGreeter";
    }

    public String getHelp() {
        return "This module sends messages to random or specified people";
    }
}
