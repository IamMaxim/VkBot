package ru.iammaxim.ModuleNotifier;

import org.json.JSONObject;

/**
 * Created by maxim on 5/1/17 at 3:30 PM.
 */
public class Notify {
    public int peer_id;
    public boolean messages;

    public Notify(int peer_id, boolean messages) {
        this.peer_id = peer_id;
        this.messages = messages;
    }

    public String save() {
        return new JSONObject().put("peer_id", peer_id).put("messages", messages).toString();
    }

    public static Notify load(String s) {
        JSONObject o = new JSONObject(s);
        return new Notify(o.getInt("peer_id"), o.getBoolean("messages"));
    }
}
