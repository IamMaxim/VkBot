package ru.iammaxim.VkBot.Groups;

import org.json.JSONArray;
import org.json.JSONObject;
import ru.iammaxim.VkBot.Net;
import ru.iammaxim.VkBot.Objects.ObjectUser;
import ru.iammaxim.VkBot.UserDB;

import java.io.IOException;
import java.util.HashMap;
import java.util.StringJoiner;

/**
 * Created by maxim on 18.08.2016.
 */
public class Users {
    public static ObjectUser get() {
        try {
            return new ObjectUser(Net.processRequest("users.get", true));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ObjectUser get(int id) {
        ObjectUser user = UserDB.get(id);
        if (user == null) {
            try {
                String json = Net.processRequest("users.get", true, "user_ids=" + id);
                user = new ObjectUser(json);
                UserDB.add(user);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return user;
    }

    public static ObjectUser[] get(int[] ids) {
        ObjectUser[] users = new ObjectUser[ids.length];
        HashMap<Integer, Integer> indices = new HashMap<>();
        HashMap<Integer, ObjectUser> loadedUsers = new HashMap<>();
        for (int i = 0; i < ids.length; i++) {
            int id = ids[i];
            ObjectUser user = UserDB.get(id);
            if (user == null) {
                indices.put(i, id);
            } else
                users[i] = user;
        }

        StringJoiner sj = new StringJoiner(",");
        indices.forEach((index, id) -> sj.add(String.valueOf(id)));
        try {
            String json = Net.processRequest("users.get", true, "user_ids=" + sj);
            JSONArray arr = new JSONObject(json).getJSONArray("response");
            arr.forEach((ob) -> {
                JSONObject obj = (JSONObject) ob;
                ObjectUser user = new ObjectUser(obj);
                loadedUsers.put(obj.getInt("id"), user);
                UserDB.add(user);
            });
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        indices.forEach((index, id) -> {
            users[index] = loadedUsers.get(id);
        });
        return users;
    }
}
