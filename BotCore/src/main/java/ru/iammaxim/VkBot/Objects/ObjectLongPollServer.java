package ru.iammaxim.VkBot.Objects;

import org.json.JSONException;
import org.json.JSONObject;

public class ObjectLongPollServer {
    public String key, server;
    public long ts;
    //public int pts;
//    public Request request;

    public ObjectLongPollServer(String json) {
        JSONObject o = new JSONObject(json).getJSONObject("response");
        key = o.getString("key");
        server = o.getString("server");
        ts = o.getLong("ts");
        //pts = o.getInt("pts");
        //request = new Request("messages", "getLongPollHistory", Main.instance.getAccessToken(), "ts", ts+"", "pts", pts+"");
    }

    public static ObjectLongPollServer getServer(String JSON) {
        ObjectLongPollServer server = null;
        while (server == null) {
            try {
                server = new ObjectLongPollServer(JSON);
            } catch (JSONException e) {}
            if (server == null)
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
        }
        return server;
    }

    public void update(long ts) {
        this.ts = ts;
    }
}