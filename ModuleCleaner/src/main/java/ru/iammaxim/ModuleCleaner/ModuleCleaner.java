package ru.iammaxim.ModuleCleaner;

import org.json.JSONObject;
import ru.iammaxim.VkBot.ModuleBase.ModuleBase;
import ru.iammaxim.VkBot.Groups.Messages;
import ru.iammaxim.VkBot.Net;
import ru.iammaxim.VkBot.Objects.ObjectMessage;
import ru.iammaxim.VkBot.UserManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by maxim on 6/19/17.
 */
public class ModuleCleaner extends ModuleBase {
    @Override
    public String getName() {
        return "clean";
    }

    @Override
    public void process(ObjectMessage msg) {
        if (!msg.body.equals("/clean"))
            return;

        if (!UserManager.isAdmin(msg.user_id)) {
            Messages.send(msg.from_id, UserManager.getAccessDeniedText());
            return;
        }

        try {
            // messages
            Messages.send(msg.from_id, "Starting messages download & cleanup...");
            processDialogs();
            Messages.send(msg.from_id, "Messages cleanup completed.");

            // subscribers
            Messages.send(msg.from_id, "Starting subscribers cleanup...");
            processSubscribers();
            Messages.send(msg.from_id, "Subscribers cleanup completed.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getHelp() {
        return "/clean - downloads all data and cleans account";
    }

    private void clean(String name, String methodName) {

    }

    private void processDialogs() throws IOException {
        new File("ModuleCleanerOutput").mkdirs();

        int count = 1;
        int processed = 0;
        while (processed < count) {
            JSONObject o = new JSONObject(Net.processRequest("messages.getDialogs", true, "count=200", "offset=" + processed)).getJSONObject("response");
            count = o.getInt("count");
            o.getJSONArray("items").forEach(e -> {
                JSONObject ob = ((JSONObject) e).getJSONObject("message");
                int id;
                if (ob.has("chat_id"))
                    id = 2000000000 + ob.getInt("chat_id");
                else
                    id = ob.getInt("user_id");
                try {
                    processDialog(id);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            });
            processed += 200;
        }

        processed = 0;

        while (processed < count) {
            JSONObject o = new JSONObject(Net.processRequest("messages.getDialogs", true, "count=200", "offset=" + processed)).getJSONObject("response");
            count = o.getInt("count");
            o.getJSONArray("items").forEach(e -> {
                JSONObject ob = ((JSONObject) e).getJSONObject("message");
                int id;
                if (ob.has("chat_id"))
                    id = 2000000000 + ob.getInt("chat_id");
                else
                    id = ob.getInt("user_id");
                try {
                    Net.processRequest("messages.deleteDialog", true, "peer_id=" + id);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            });
            processed += 200;
        }
    }

    private void processDialog(int id) throws IOException {
        FileOutputStream fos = new FileOutputStream("ModuleCleanerOutput/" + id + ".txt");
        int count = 1;
        int processed = 0;
        while (processed < count) {
            JSONObject o = new JSONObject(Net.processRequest("messages.getHistory", true, "rev=1", "count=200", "offset=" + processed, "peer_id=" + id)).getJSONObject("response");
            count = o.getInt("count");
            o.getJSONArray("items").forEach(e -> {
                JSONObject ob = (JSONObject) e;
                try {
                    fos.write((ob.toString() + "\n").getBytes());
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            });
            processed += 200;
        }
        fos.close();
    }

    private void processSubscribers() throws IOException {
        int count = 1;
        int processed = 0;

        while (processed < count) {
            JSONObject o = new JSONObject(Net.processRequest("users.getFollowers", true, "count=200", "offset=" + processed)).getJSONObject("response");

            o.getJSONArray("items").forEach(e -> {
                int id = (int) e;
                try {
                    Net.processRequest("account.banUser", true, "user_id=" + id);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            });

            processed += 200;
        }
    }
}
