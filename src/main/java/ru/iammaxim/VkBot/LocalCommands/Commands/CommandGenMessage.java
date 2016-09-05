package ru.iammaxim.VkBot.LocalCommands.Commands;

import ru.iammaxim.ModuleBase.ModuleBase;
import ru.iammaxim.ModuleTalker.ModuleTalker;
import ru.iammaxim.VkBot.LocalCommands.CommandBase;
import ru.iammaxim.VkBot.Main;
import ru.iammaxim.VkBot.Modules.ModuleManager;

/**
 * Created by maxim on 25.08.2016.
 */
public class CommandGenMessage extends CommandBase {
    private ModuleTalker talkerModule = (ModuleTalker) Main.instance.getModuleManager().getModule("talker");

    @Override
    public String getName() {
        return "genmessage";
    }

    @Override
    public void run(String[] args) {
        System.out.println(talkerModule.tree.getSentence(0));
    }
}
