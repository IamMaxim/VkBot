package ru.iammaxim.VkBot.ModuleBase;

import ru.iammaxim.VkBot.Objects.ObjectMessage;

public abstract class ModuleBase {
    public abstract void process(ObjectMessage inputMessage);
    public abstract String getName();
    public abstract String getHelp();
}
