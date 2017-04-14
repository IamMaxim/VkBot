package ru.iammaxim.VkBot.LocalCommands;

public abstract class LocalCommandBase {
    public abstract String getName();
    public boolean runOnProcessThread() {
        return true;
    }
    public abstract void run(String... args);
}