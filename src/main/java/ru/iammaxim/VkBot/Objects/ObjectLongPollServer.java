package ru.iammaxim.VkBot.Objects;

import org.json.JSONObject;
import ru.iammaxim.VkBot.Main;
import ru.iammaxim.VkBot.Request;

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
        return new ObjectLongPollServer(JSON);
    }

    public void update(long ts) {
        this.ts = ts;
    }
}