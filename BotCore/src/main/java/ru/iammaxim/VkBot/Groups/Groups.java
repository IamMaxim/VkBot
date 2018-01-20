package ru.iammaxim.VkBot.Groups;

import org.json.JSONArray;
import org.json.JSONObject;
import ru.iammaxim.VkBot.Net;
import ru.iammaxim.VkBot.Objects.ObjectGroup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.StringJoiner;

/**
 * Created by maxim on 7/1/17.
 */
public class Groups {

    public static ArrayList<ObjectGroup> get(int user_id) throws IOException {
        ArrayList<ObjectGroup> groups = new ArrayList<>();
        int count = 1;
        int loaded = 0;
        while (loaded < count) {
            JSONObject object = new JSONObject(Net.processRequest("groups.get", true, "user_id=" + user_id, "count=1000", "extended=1", "offset=" + loaded)).getJSONObject("response");
            count = object.getInt("count");
            object.getJSONArray("items").forEach(ob -> {
                JSONObject o = (JSONObject) ob;
                groups.add(new ObjectGroup(o));
            });
            loaded += object.getJSONArray("items").length();
        }
        return groups;
    }

    public static ObjectGroup[] getById(int[] groupIDs) throws IOException {
        ObjectGroup[] groups = new ObjectGroup[groupIDs.length];
        int loaded = 0;
        while (loaded < groupIDs.length) {
            StringJoiner sj = new StringJoiner(",");
            int toLoad = Math.min(200, groupIDs.length - loaded);
            for (int i = 0; i < toLoad; i++) {
                sj.add(String.valueOf(groupIDs[loaded + i]));
            }
            JSONArray arr = new JSONObject(Net.processRequest("groups.getById", true, "group_ids=" + sj.toString())).getJSONArray("response");
            for (int i = 0; i < toLoad; i++) {
                groups[loaded + i] = new ObjectGroup(arr.getJSONObject(i));
            }
            loaded += 200;
        }
        return groups;
    }
}
