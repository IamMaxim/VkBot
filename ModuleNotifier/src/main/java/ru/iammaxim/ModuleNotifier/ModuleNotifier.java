package ru.iammaxim.ModuleNotifier;

import org.json.JSONArray;
import ru.iammaxim.VkBot.Groups.Messages;
import ru.iammaxim.VkBot.Main;
import ru.iammaxim.VkBot.ModuleBase.ModuleBase;
import ru.iammaxim.VkBot.Objects.ObjectMessage;
import ru.iammaxim.VkBot.UserManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by maxim on 5/1/17 at 3:28 PM.
 */
public class ModuleNotifier extends ModuleBase {
    private ArrayList<Notify> notifies = new ArrayList<>();

    @Override
    public void save(String path) {
        try {
            File f = new File(path + "notifies.txt");
            if (!f.exists())
                f.createNewFile();

            FileOutputStream fos = new FileOutputStream(f);
            JSONArray arr = new JSONArray();
            notifies.forEach(n -> arr.put(n.save()));
            fos.write(arr.toString().getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void load(String path) {
        try {
            File f = new File(path + "notifies.txt");
            if (!f.exists())
                return;

            Scanner scanner = new Scanner(f).useDelimiter("\\A");
            if (scanner.hasNext()) {
                JSONArray arr = new JSONArray(scanner.next());
                scanner.close();
                arr.forEach(o -> notifies.add(Notify.load(o.toString())));
            } else
                scanner.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void process(ObjectMessage inputMessage) {
        if (inputMessage.user_id != Main.instance.getBotUser().id)
            _notify(inputMessage.from_id);

        if (!inputMessage.body.startsWith("/notifier "))
            return;

        if (!UserManager.isAdmin(inputMessage.user_id)) {
            Messages.send(inputMessage.from_id, UserManager.getAccessDeniedText());
            return;
        }

        String message = inputMessage.body.replace("/notifier ", "");
        String[] tokens = message.split(" ");
        if (tokens[0].equals("messages")) {
            if (tokens.length < 2) {
                Messages.send(inputMessage.from_id, "Invalid syntax");
                return;
            }

            if (tokens[1].equals("on")) {
                Notify n = getNotify(inputMessage.from_id);
                n.messages = true;
                Messages.send(inputMessage.from_id, "Message notifications for this chat turned on");
            } else if (tokens[1].equals("off")) {
                Notify n = getNotify(inputMessage.from_id);
                n.messages = false;
                Messages.send(inputMessage.from_id, "Message notifications for this chat turned off");
            }
        }
    }

    private void _notify(int from_id) {
        notifies.forEach(n -> {
            if (n.messages)
                Messages.send(n.peer_id, "New message in chat " + Messages.getChatName(from_id));
        });
    }

    private Notify getNotify(int peer_id) {
        Notify notify = null;

        for (Notify n : notifies) {
            if (n.peer_id == peer_id)
                notify = n;
        }

        if (notify == null) {
            notify = new Notify(peer_id, false);
            notifies.add(notify);
        }

        return notify;
    }

    @Override
    public String getName() {
        return "ModuleNotifier";
    }

    @Override
    public String getHelp() {
        return "Usage: /notifier <type> <on/off>";
    }
}
