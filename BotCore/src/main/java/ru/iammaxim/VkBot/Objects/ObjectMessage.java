package ru.iammaxim.VkBot.Objects;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import ru.iammaxim.VkBot.Main;

public class ObjectMessage {
    public int id, from_id, user_id;
    public String title, body;
    public long date;
    public boolean out, inChat;

    private static final int OUT_FLAG_OFFSET = 1;

    public ObjectMessage() {}

    public ObjectMessage(JSONObject object) {
        id = object.getInt("id");
        title = object.getString("title");
        body = object.getString("body");
        try {
            from_id = object.getInt("from_id");
        } catch (JSONException e) {}
        try {
            user_id = object.getInt("user_id");
        } catch (JSONException e) {}
        date = object.getLong("date");
        out = object.getInt("out") == 1;
        inChat = object.has("chat_id");
    }

    public ObjectMessage(String json) {
        this(new JSONObject(json));
    }

    public static ObjectMessage createFromLongPoll(JSONArray object) {
        ObjectMessage message = new ObjectMessage();
        message.id = object.getInt(1);
        message.processFlags(object.getInt(2));
        message.from_id = object.getInt(3);
        if (message.out)
            message.user_id = Main.instance.getBotUser().id;
        else
            message.user_id = message.from_id;
        message.date = object.getLong(4);
        message.title = object.getString(5);
        message.body = object.getString(6);
        try {
            message.user_id = Integer.parseInt(object.getJSONObject(7).getString("from"));
        } catch (JSONException e) {}
        return message;
    }

    private void processFlags(int flags) {
        out = ((flags & (1 << OUT_FLAG_OFFSET)) >> OUT_FLAG_OFFSET) == 1;
    }
}