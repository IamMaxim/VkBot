package ru.iammaxim.VkBot.Groups;

import ru.iammaxim.VkBot.Main;
import ru.iammaxim.VkBot.Net;
import ru.iammaxim.VkBot.Objects.ObjectUser;
import ru.iammaxim.VkBot.Request;
import ru.iammaxim.VkBot.UserDB;

import java.io.IOException;

/**
 * Created by maxim on 18.08.2016.
 */
public class Users {
    private static Main main = Main.instance;

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
                String json = Net.processRequest("users.get", true, "user_ids="+id);
                user = new ObjectUser(json);
                UserDB.add(user);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return user;
    }
}
