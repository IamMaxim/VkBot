package ru.iammaxim.VkBot.LocalCommands.Commands;

import ru.iammaxim.VkBot.LocalCommands.LocalCommandBase;
import ru.iammaxim.VkBot.Objects.ObjectUser;
import ru.iammaxim.VkBot.UserDB;

import java.util.Collection;

/**
 * Created by maxim on 20.08.2016.
 */
public class CommandGetUsers extends LocalCommandBase {
    @Override
    public String getName() {
        return "getusers";
    }

    @Override
    public void run(String[] args) {
        StringBuilder sb = new StringBuilder('\n');
        Collection<ObjectUser> users = UserDB.getUsers();
        users.forEach(user -> {
            sb.append('\n').append(user.id).append(' ').append(user.first_name).append(' ').append(user.last_name);
        });
        System.out.println(sb.toString());
    }
}
