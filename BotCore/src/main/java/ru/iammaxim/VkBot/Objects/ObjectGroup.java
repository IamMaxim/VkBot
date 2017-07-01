package ru.iammaxim.VkBot.Objects;

import org.json.JSONObject;

/**
 * Created by maxim on 7/1/17.
 */
public class ObjectGroup {
    public int id;
    public String name;

    public ObjectGroup(JSONObject object) {
        this.id = object.getInt("id");
        this.name = object.getString("name");
    }

    @Override
    public String toString() {
        return name;
    }
}
