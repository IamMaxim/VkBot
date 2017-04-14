package ru.iammaxim.VkBot;

import org.json.JSONArray;
import org.json.JSONObject;
import ru.iammaxim.VkBot.Objects.ObjectLongPollServer;
import ru.iammaxim.VkBot.Objects.ObjectMessage;

import java.io.IOException;

/**
 * Created by Maxim on 21.06.2016.
 */
public class LongPollThread extends Thread {
    private ObjectLongPollServer currentLongPollServer;

    private void init() {
        try {
            currentLongPollServer = ObjectLongPollServer.getServer(Net.processRequest(new Request("messages", "getLongPollServer", Main.instance.getAccessToken(), "use_ssl", "1", "need_pts", "1")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public LongPollThread(String name) {
        super(name);
    }

    @Override
    public void run() {
        System.out.println("starting long poll thread...");
        init();
        while (!isInterrupted()) {
            processLongPollMessage();
        }
        System.out.println("Shutting down long poll thread...");
    }

    private void processLongPollMessage() {
        try {
            String json = Net.processRequest("https://" + currentLongPollServer.server + "?act=a_check&key=" + currentLongPollServer.key + "&ts=" + currentLongPollServer.ts + "&wait=50&mode=2");
            if (isInterrupted()) return;
            JSONObject o = new JSONObject(json);
            if (!o.isNull("failed")) {
                int code = o.getInt("failed");
                if (code == 2 || code == 3) {
                    System.out.println("Detected expired long poll token.");
                    currentLongPollServer = ObjectLongPollServer.getServer(Net.processRequest(new Request("messages", "getLongPollServer", Main.instance.getAccessToken(), "use_ssl", "1", "need_pts", "1")));
                    System.out.println("Server data updated.");
                    return;
                }
            }
            long ts = o.getLong("ts");
            JSONArray arr = o.getJSONArray("updates");
            arr.forEach((object) -> {
                if (object instanceof JSONArray) {
                    JSONArray obj = (JSONArray) object;
                    int updateCode = obj.getInt(0);
                    if (updateCode == 4) {
                        Main.instance.getModuleManager().process(ObjectMessage.createFromLongPoll(obj));
                    }
                }
            });
            currentLongPollServer.update(ts);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
