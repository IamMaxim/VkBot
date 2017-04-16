package ru.iammaxim.ModuleMain.Commands;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import ru.iammaxim.VkBot.Groups.Messages;
import ru.iammaxim.VkBot.Main;
import ru.iammaxim.VkBot.Net;
import ru.iammaxim.VkBot.Objects.ObjectMessage;

import java.io.IOException;

/**
 * Created by maxim on 22.08.2016.
 */
public class CommandGetChats extends CommandBase {
    @Override
    public String getCommandName() {
        return "getchats";
    }

    @Override
    public void process(ObjectMessage msg, String[] args) {
        try {
            String s = Net.processRequest("messages.getDialogs", true);
            JSONArray array = new JSONObject(s).getJSONObject("response").getJSONArray("items");
            StringBuilder message = new StringBuilder();
            final boolean[] completed = {false};
            array.forEach(o -> {
                if (completed[0]) return;
                JSONObject obj = ((JSONObject) o).getJSONObject("message");
                try {
                    String s1 = String.valueOf(obj.getInt("chat_id")) + " " + obj.getString("title") + "<br>";
                    message.append(s1);
                } catch (JSONException e) {}
            });
            Messages.send(msg.from_id, message.toString());
        } catch (IOException e) {
            e.printStackTrace();
            Messages.send(msg.from_id, "Error occured during processing your request: " + Main.getRandomHex());
        }
    }

    @Override
    public String getHelp() {
        return "/getchats - returns last 10 active chats";
    }
}
