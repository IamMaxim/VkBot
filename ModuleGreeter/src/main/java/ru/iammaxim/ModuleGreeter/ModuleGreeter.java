package ru.iammaxim.ModuleGreeter;

import ru.iammaxim.VkBot.Groups.Messages;
import ru.iammaxim.VkBot.ModuleBase.ModuleBase;
import ru.iammaxim.VkBot.Objects.ObjectMessage;
import ru.iammaxim.VkBot.UserManager;

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

    private boolean checkAdmin(ObjectMessage msg) {
        if (UserManager.isAdmin(msg.user_id))
            return true;
        Messages.send(msg.from_id, UserManager.getAccessDeniedText());
        return false;
    }

    public void process(ObjectMessage msg) {
            switch (msg.body) {
                case "/greeterStart":
                    if (!checkAdmin(msg))
                        return;
                    if (workerThread != null && workerThread.isAlive()) {
                        Messages.send(msg.from_id, "This module is already running. Stop it by typing /greeterStop");
                        return;
                    }
                    workerThread = new Thread(() -> {
                        try {
                            while (!Thread.interrupted()) {
                                Messages.send(msg.from_id, getMessageText());
                                Thread.sleep(10000);
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    });
                    workerThread.start();
                    Messages.send(msg.from_id, "Greeter started");
                    break;
                case "/greeterStop":
                    if (!checkAdmin(msg))
                        return;
                    if (!workerThread.isAlive()) {
                        Messages.send(msg.from_id, "This module is already stopped. Start it by typing /greeterStart");
                        return;
                    }
                    workerThread.interrupt();
                    Messages.send(msg.from_id, "Greeter stopped");
            }
    }

    public String getName() {
        return "ModuleGreeter";
    }

    public String getHelp() {
        return "/greeterStart - starts greeter<br>" +
                "/greeterStop - stops greeter";
    }
}
