package ru.iammaxim.ModuleNNTalker;

import ru.iammaxim.VkBot.Groups.Messages;
import ru.iammaxim.VkBot.ModuleBase.ModuleBase;
import ru.iammaxim.VkBot.Objects.ObjectMessage;

public class ModuleNNTalker extends ModuleBase {
    BotLibrary botlib;
    {
//        System.out.println(System.getProperty("user.dir"));
//        System.setProperty("jna.library.path", System.getProperty("user.dir"));
        botlib = BotLibrary.instance;
        botlib.init();
    }

    @Override
    public void process(ObjectMessage inputMessage) {
        if (inputMessage.body.startsWith("/nn ")) {
            Messages.send(inputMessage.from_id, botlib.answer(inputMessage.body.substring(4)));
        }
    }

    @Override
    public String getName() {
        return "nntalker";
    }

    @Override
    public String getHelp() {
        return "empty";
    }
}
