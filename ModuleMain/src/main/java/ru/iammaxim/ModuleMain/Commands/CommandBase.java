package ru.iammaxim.ModuleMain.Commands;


import ru.iammaxim.VkBot.Objects.ObjectMessage;

/**
 * Created by Maxim on 22.06.2016.
 */
public abstract class CommandBase {
    public abstract String getCommandName();
    public abstract void process(ObjectMessage msg, String[] args);
    public abstract String getHelp();
}
