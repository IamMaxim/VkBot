package ru.iammaxim.ModuleBase;

import ru.iammaxim.VkBot.Objects.ObjectMessage;

/**
 * Created by Maxim on 21.06.2016.
 */
public abstract class ModuleBase {
    public abstract void process(ObjectMessage inputMessage);
    public abstract String getName();
}
