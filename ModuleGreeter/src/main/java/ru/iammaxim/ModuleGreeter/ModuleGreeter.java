package ru.iammaxim.ModuleGreeter;

import ru.iammaxim.VkBot.Groups.Messages;
import ru.iammaxim.VkBot.ModuleBase.ModuleBase;
import ru.iammaxim.VkBot.Objects.ObjectMessage;
import ru.iammaxim.VkBot.UserManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by maxim on 13.04.2017.
 */
public class ModuleGreeter extends ModuleBase {
    private ArrayList<Integer> list = new ArrayList<>();

    private ArrayList<String> messages = new ArrayList<>();

    public ModuleGreeter() {
        new Thread(() -> {
            try {
                while (!Thread.interrupted()) {
                    System.out.println("Running send on " + list.size() + " IDs");
                    for (Integer id : list) {
                        Messages.send(id, getMessageText());
                        Thread.sleep(4000);
                    }
                    Thread.sleep(1);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        File f = new File("save/ModuleGreeter/phrases.txt");
        if (!f.exists()) {
            System.out.println("File \"save/ModuleGreeter/phrases.txt\" not found. No phrases for ModuleGreeter loaded.");
            return;
        }
        try (Scanner s = new Scanner(new FileInputStream(f))) {
            while (s.hasNext()) {
                String str = s.nextLine();
                messages.add(str);
            }
            System.out.println("Loaded " + messages.size() + " phrases for ModuleGreeter");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private String getMessageText() {
        int index = (int) Math.round(Math.random() * (messages.size() - 1));
        return messages.get(index);
    }

    private boolean checkAdmin(ObjectMessage msg) {
        if (UserManager.isAdmin(msg.user_id))
            return true;
        Messages.send(msg.from_id, UserManager.getAccessDeniedText());
        return false;
    }

    public void process(ObjectMessage msg) {
        String[] tokens = msg.body.split(" ");

        switch (tokens[0]) {
            case "/greeterAdd":
                if (!checkAdmin(msg))
                    return;
                if (tokens.length != 2) {
                    Messages.send(msg.from_id, "Invalid syntax");
                    return;
                }
/*                if (workerThread != null && workerThread.isAlive()) {
                    Messages.send(msg.from_id, "This module is already running. Stop it by typing /greeterStop");
                    return;
                }*/

                try {
                    list.add(Integer.parseInt(tokens[1]));
                } catch (NumberFormatException e) {
                    Messages.send(msg.from_id, "Invalid id");
                    return;
                }

                Messages.send(msg.from_id, "ID added");
                break;
            case "/greeterRemove":
                if (!checkAdmin(msg))
                    return;
                if (tokens.length != 2) {
                    Messages.send(msg.from_id, "Invalid syntax");
                    return;
                }
                /*if (!workerThread.isAlive()) {
                    Messages.send(msg.from_id, "This module is already stopped. Start it by typing /greeterStart");
                    return;
                }*/

                try {
                    list.remove((Integer) Integer.parseInt(tokens[1]));
                } catch (NumberFormatException e) {
                    Messages.send(msg.from_id, "Invalid id");
                    return;
                }

//                workerThread.interrupt();
                Messages.send(msg.from_id, "ID removed");
        }
    }

    public String getName() {
        return "ModuleGreeter";
    }

    public String getHelp() {
        return "/greeterAdd <id> - add <id> to greeter list<br>" +
                "/greeterRemove <id> - removes <id> from greeter list";
    }
}
