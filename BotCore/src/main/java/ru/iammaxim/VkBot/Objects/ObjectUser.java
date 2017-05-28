package ru.iammaxim.VkBot.Objects;

import org.json.JSONObject;

/**
 * Created by Maxim on 20.06.2016.
 */
public class ObjectUser {
    public int id;
    public String first_name, last_name;

    public ObjectUser() {
    }

    //load from users.get()
    public ObjectUser(String json) {
        this(new JSONObject(json).getJSONArray("response").getJSONObject(0));
    }

    public ObjectUser(JSONObject o) {
        id = o.getInt("id");
        first_name = o.getString("first_name");
        last_name = o.getString("last_name");
    }
}
