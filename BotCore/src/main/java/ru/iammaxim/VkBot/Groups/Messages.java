package ru.iammaxim.VkBot.Groups;

import org.json.JSONObject;
import ru.iammaxim.VkBot.Main;
import ru.iammaxim.VkBot.Net;
import ru.iammaxim.VkBot.Objects.ObjectMessage;
import ru.iammaxim.VkBot.Objects.ObjectUser;

import java.io.IOException;

/**
 * Created by maxim on 18.08.2016.
 */
public class Messages {
    public static void send(int id, String message) {
        Main.instance.addTask(() -> {
            try {
                Net.processRequest("messages.send", true, "peer_id=" + id, "message=" + message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public static void sendFromCurrentThread(int id, String message) {
        try {
            Net.processRequest("messages.send", true, "peer_id=" + id, "message=" + message);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static ObjectMessage getById(int id) throws IOException {
        JSONObject o = new JSONObject(Net.processRequest("messages.getById", true, "message_ids=" + id))
                .getJSONObject("response").getJSONArray("items").getJSONObject(0);
        return new ObjectMessage(o);
    }

    public static String getChatName(int peer_id) {
        if (peer_id > 2000000000)
            try {
                return new JSONObject(Net.processRequest("messages.getChat", true, "chat_id=" + (peer_id - 2000000000))).getJSONObject("response").getString("title");
            } catch (IOException e) {
                e.printStackTrace();
                return "ERROR";
            }
        else {
            ObjectUser user = Users.get(peer_id);
            return user.first_name + " " + user.last_name;
        }
    }
}
