package ru.iammaxim.VkBot.Groups;

import org.json.JSONObject;
import ru.iammaxim.VkBot.Main;
import ru.iammaxim.VkBot.Net;
import ru.iammaxim.VkBot.Objects.ObjectMessage;
import ru.iammaxim.VkBot.Request;

import java.io.IOException;

/**
 * Created by maxim on 18.08.2016.
 */
public class Messages {
    private static Main main = Main.instance;

    public static void send(int id, String message) {
            main.addTask(() -> {
                try {
                    Net.processRequest(new Request("messages", "send", main.getAccessToken(), "peer_id", id + "", "message", message));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
    }

    public static ObjectMessage getById(int id) throws IOException {
        JSONObject o = new JSONObject(Net.processRequest(new Request("messages", "getById", main.getAccessToken(), "message_ids", id + ""))).getJSONObject("response").getJSONArray("items").getJSONObject(0);
        return new ObjectMessage(o);
    }
}
