package ru.iammaxim.ModuleMain.Commands;

import org.json.JSONArray;
import org.json.JSONObject;
import ru.iammaxim.VkBot.Groups.Messages;
import ru.iammaxim.VkBot.Net;
import ru.iammaxim.VkBot.Objects.ObjectMessage;

import java.io.IOException;

/**
 * Created by maxim on 02.11.2016.
 */
public class CommandSearchMusic extends CommandBase {
    @Override
    public String getCommandName() {
        return "searchmusic";
    }

    @Override
    public void process(ObjectMessage msg, String[] args) {
        try {
            JSONObject tracks = new JSONObject(Net.processRequest("audio.search", true, "q=" + String.join(" ", args))).getJSONObject("response");
            int count = tracks.getInt("count");
            JSONArray items = tracks.getJSONArray("items");
            StringBuilder attachment = new StringBuilder();
            for (int i = 0; i < 10 && i < count; i++) {
                JSONObject track = items.getJSONObject(i);
                attachment.append("audio").append(track.getInt("owner_id")).append("_").append(track.getInt("id")).append(",");
            }
            Net.processRequest("messages.send", true, "peer_id=" + msg.from_id,
                    "message=Found " + count + " tracks:",
                    //remove last comma
                    "attachment=" + attachment.substring(0, attachment.length()-1));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getHelp() {
        return "/searchMusic <name> - find track named <name>";
    }
}
