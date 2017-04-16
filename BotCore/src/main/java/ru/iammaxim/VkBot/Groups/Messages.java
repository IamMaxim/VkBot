package ru.iammaxim.VkBot.Groups;

import org.json.JSONObject;
import ru.iammaxim.VkBot.Main;
import ru.iammaxim.VkBot.Net;
import ru.iammaxim.VkBot.Objects.ObjectMessage;

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

    public static ObjectMessage getById(int id) throws IOException {
        JSONObject o = new JSONObject(Net.processRequest("messages.getById", true, "message_ids=" + id))
                .getJSONObject("response").getJSONArray("items").getJSONObject(0);
        return new ObjectMessage(o);
    }
}
