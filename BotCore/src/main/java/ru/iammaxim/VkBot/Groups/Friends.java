package ru.iammaxim.VkBot.Groups;

import org.json.JSONObject;
import ru.iammaxim.VkBot.Net;
import ru.iammaxim.VkBot.Objects.ObjectUser;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by maxim on 6/12/17.
 */
public class Friends {
    public static ArrayList<ObjectUser> get(int id) throws IOException {
        JSONObject o = new JSONObject(Net.processRequest("friends.get", true, "user_id=" + id, "fields=photo_200")).getJSONObject("response");
        ArrayList<ObjectUser> users = new ArrayList<>(o.getInt("count"));
        o.getJSONArray("items").forEach(obj -> users.add(new ObjectUser((JSONObject) obj)));
        return users;
    }
}
