package ru.iammaxim.VkBot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import ru.iammaxim.VkBot.Objects.ObjectLongPollServer;
import ru.iammaxim.VkBot.Objects.ObjectMessage;

import java.io.IOException;

/**
 * Created by Maxim on 21.06.2016.
 */
public class LongPollThread extends Thread {
    private ObjectLongPollServer currentLongPollServer;

    public LongPollThread(String name) {
        super(name);
    }

    private void init() {
        try {
            currentLongPollServer = ObjectLongPollServer.getServer(Net.processRequest("messages.getLongPollServer", true, "use_ssl=1", "need_pts=1"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        System.out.println("starting long poll thread");
        init();
        while (!isInterrupted()) {
            processLongPollMessage();
        }
        System.out.println("Shutting down long poll thread");
    }

    private void processLongPollMessage() {
        String json = "Not yet got";
        try {
            json = Net.processRequest("https://" + currentLongPollServer.server + "?act=a_check&key=" + currentLongPollServer.key + "&ts=" + currentLongPollServer.ts + "&wait=50&mode=2");
            if (isInterrupted()) return;
            JSONObject o = new JSONObject(json);
            if (!o.isNull("failed")) {
                int code = o.getInt("failed");

                if (code == 1) {
                    System.out.println("Detected data loss!");
                    currentLongPollServer.ts = o.getLong("ts");
                    return;
                }

                if (code == 2 || code == 3) {
                    System.out.println("Detected expired long poll token.");
                    currentLongPollServer = null;
                    while (currentLongPollServer == null)
                        try {
                            currentLongPollServer = ObjectLongPollServer.getServer(Net.processRequest("messages.getLongPollServer", true, "use_ssl=1", "need_pts=1"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    System.out.println("Long poll server data updated.");
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
        } catch (IOException | JSONException e) {
            System.err.println("Response: " + json);
            e.printStackTrace();
            try {
                Thread.sleep(1);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
        }
    }
}
