package ru.iammaxim.VkBot.LocalCommands;

public abstract class CommandBase {
    public abstract String getName();
    public boolean runOnProcessThread() {
        return true;
    }
    public abstract void run(String[] args);
}