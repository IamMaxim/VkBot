package ru.iammaxim.ModuleMain.Commands;

import org.json.JSONObject;
import ru.iammaxim.VkBot.Groups.Messages;
import ru.iammaxim.VkBot.Main;
import ru.iammaxim.VkBot.MultipartUtility;
import ru.iammaxim.VkBot.Net;
import ru.iammaxim.VkBot.Objects.ObjectMessage;

import java.io.File;
import java.io.IOException;

/**
 * Created by maxim on 24.05.2017.
 */
public class CommandSendTestPhoto extends CommandBase {
    @Override
    public String getCommandName() {
        return "sendtestphoto";
    }

    @Override
    public void process(ObjectMessage msg, String[] args) {
        try {
            if (args.length != 1) {
                Messages.send(msg.from_id, "Invalid syntax");
                return;
            }
            JSONObject serverData = new JSONObject(Net.processRequest("photos.getMessagesUploadServer", true)).getJSONObject("response");
            MultipartUtility utility = new MultipartUtility(serverData.getString("upload_url"), "UTF-8");
            utility.addFilePart("photo", new File(args[0]));
            String response = utility.finish();
            JSONObject obj = new JSONObject(response);
            String response2 = Net.processRequest("photos.saveMessagesPhoto", true,
                    "photo=" + obj.getString("photo"),
                    "server=" + obj.getInt("server"),
                    "hash=" + obj.getString("hash"));
            JSONObject obj2 = new JSONObject(response2).getJSONArray("response").getJSONObject(0);
            System.out.println(Net.processRequest("messages.send", true, "peer_id=" + msg.from_id, "attachment=photo" + Main.instance.getBotUser().id + "_" + obj2.getInt("id")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getHelp() {
        return "/sendTestPhoto <filename> - sends photo with name <filename>";
    }
}
