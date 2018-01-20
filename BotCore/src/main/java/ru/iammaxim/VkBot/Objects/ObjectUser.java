package ru.iammaxim.VkBot.Objects;

import org.json.JSONObject;

/**
 * Created by Maxim on 20.06.2016.
 */
public class ObjectUser {
    public int id;
    public String first_name, last_name;
    public boolean online = false;
    public long last_seen = 0;
    public int last_seen_platform = 0;

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
        if (o.has("online"))
//            online = o.getBoolean("online");
            online = o.getInt("online") == 1;
        if (o.has("last_seen")) {
            last_seen = o.getJSONObject("last_seen").getLong("time");
            last_seen_platform = o.getJSONObject("last_seen").getInt("platform");
        }
    }

    @Override
    public String toString() {
        return first_name + " " + last_name;
    }

    public String getName() {
        return first_name + " " + last_name;
    }

    public String getPlatform() {
        switch (last_seen_platform) {
            case 1:
                return "Mobile";
            case 2:
                return "iPhone";
            case 3:
                return "iPad";
            case 4:
                return "Android";
            case 5:
                return "Windows Phone";
            case 6:
                return "Windows 10";
            case 7:
                return "Web";
            case 8:
                return "VK Mobile";

            default:
                return String.valueOf(last_seen_platform);
        }
    }
}
