package ru.iammaxim.VkBot;

public class Request {
    public String group, name, accessToken;
    public String[] args;

    public Request(String group, String name, String accessToken, String... args) {
        this.group = group;
        this.name = name;
        this.accessToken = accessToken;
        this.args = args;
    }
}
