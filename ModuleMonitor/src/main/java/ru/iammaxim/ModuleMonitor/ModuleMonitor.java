package ru.iammaxim.ModuleMonitor;

import ru.iammaxim.VkBot.Groups.Friends;
import ru.iammaxim.VkBot.Groups.Groups;
import ru.iammaxim.VkBot.Groups.Messages;
import ru.iammaxim.VkBot.Groups.Users;
import ru.iammaxim.VkBot.Main;
import ru.iammaxim.VkBot.ModuleBase.ModuleBase;
import ru.iammaxim.VkBot.Objects.ObjectGroup;
import ru.iammaxim.VkBot.Objects.ObjectMessage;
import ru.iammaxim.VkBot.Objects.ObjectUser;
import ru.iammaxim.VkBot.UserManager;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by maxim on 6/12/17.
 */
public class ModuleMonitor extends ModuleBase {
    private static final long minUpdatePeriod = 10000, periodBetweenUpdates = 1000;

    private final ArrayList<UserData> usersData = new ArrayList<>();
    private ArrayList<Integer> subscribers = new ArrayList<>();

    private static SimpleDateFormat sdf = new SimpleDateFormat("'['HH:mm:ss dd.MM.yyyy'] '");

    @Override
    public void save(String path) {
        synchronized (usersData) {
            usersData.forEach(d -> {
                try {
                    File f = new File(path, d.id + ".bin");
                    if (!f.exists())
                        f.createNewFile();

                    try (FileOutputStream fos = new FileOutputStream(f)) {
                        d.saveTo(fos);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            try {
                File f = new File(path, "subscribers.list");
                if (!f.exists())
                    f.createNewFile();

                try (FileOutputStream fos = new FileOutputStream(f);
                     DataOutputStream dos = new DataOutputStream(fos)) {
                    dos.writeInt(subscribers.size());
                    for (Integer subscriber : subscribers)
                        dos.writeInt(subscriber);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void load(String path) {
        File f = new File(path);
        if (f.exists()) {
            File[] files = f.listFiles();
            if (files == null)
                return;
            for (File file : files) {
                if (file.getName().endsWith(".bin"))
                    try {
                        Integer id = Integer.valueOf(file.getName().replace(".bin", ""));
                        UserData d = new UserData(id);
                        try (FileInputStream fis = new FileInputStream(file)) {
                            d.loadFrom(fis);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        usersData.add(d);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
            }

            File f1 = new File(path, "subscribers.list");
            if (f1.exists()) {
                try (FileInputStream fis = new FileInputStream(f1);
                     DataInputStream dis = new DataInputStream(fis)) {
                    int count = dis.readInt();
                    for (int i = 0; i < count; i++) {
                        subscribers.add(dis.readInt());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


        new Thread(() -> {
            while (Main.instance.needToRun()) {
                long startTime = System.currentTimeMillis();
                // workaround not to stop other threads because of synchronization
                ArrayList<UserData> usersData = new ArrayList<>(this.usersData);
                usersData.forEach(d -> {
                    try {
                        d.update();
                        Thread.sleep(periodBetweenUpdates);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                try {
                    Thread.sleep(Math.max(0, minUpdatePeriod - (System.currentTimeMillis() - startTime)));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "ModuleMonitorThread").start();
    }

    private boolean checkAdmin(int id) {
        if (!UserManager.isAdmin(id)) {
            Messages.send(id, UserManager.getAccessDeniedText());
            return false;
        }
        return true;
    }

    @Override
    public void process(ObjectMessage im) {
        String[] command = im.body.split(" ");

        switch (command[0]) {
            case "/monitorAdd": {
                if (!checkAdmin(im.user_id))
                    return;
                if (command.length != 2) {
                    Messages.send(im.from_id, "Invalid syntax");
                    return;
                }
                try {
                    int id = Integer.parseInt(command[1]);
                    usersData.add(new UserData(id));
                    Messages.send(im.from_id, "User " + id + " (" + Users.get(id) + ") added to monitor");
                } catch (NumberFormatException e) {
                    Messages.send(im.from_id, "Invalid user ID");
                }
                break;
            }
            case "/monitorRemove": {
                if (!checkAdmin(im.user_id))
                    return;
                if (command.length != 2) {
                    Messages.send(im.from_id, "Invalid syntax");
                    return;
                }
                try {
                    int id = Integer.parseInt(command[1]);
                    usersData.removeIf(data -> data.id == id);
                    Messages.send(im.from_id, "User " + id + " (" + Users.get(id) + ") removed from monitor");
                } catch (NumberFormatException e) {
                    Messages.send(im.from_id, "Invalid user ID");
                }
                break;
            }
            case "/monitorList": {
                if (!checkAdmin(im.user_id))
                    return;
                StringBuilder sb = new StringBuilder();
                sb.append("I'm monitoring these users:<br>");
                synchronized (usersData) {
                    usersData.forEach(data -> sb.append(data.id).append(" (").append(Users.get(data.id)).append(")<br>"));
                }
                Messages.send(im.from_id, sb.toString());
                break;
            }
            case "/monitorSubscribe": {
                if (!checkAdmin(im.user_id))
                    return;
                if (isSubscriber(im.from_id)) {
                    Messages.send(im.from_id, "You are already subscriber");
                    return;
                }
                subscribers.add(im.from_id);
                Messages.send(im.from_id, "Subscribed successfully");
                break;
            }
            case "/monitorUnsubscribe": {
                if (!checkAdmin(im.user_id))
                    return;
                if (!isSubscriber(im.from_id)) {
                    Messages.send(im.from_id, "You are not a subscriber");
                    return;
                }
                subscribers.removeIf(sub -> sub == im.from_id);
                Messages.send(im.from_id, "Unsubscribed successfully");
                break;
            }
            case "/monitorSubscribers": {
                if (!checkAdmin(im.user_id))
                    return;
                StringBuilder sb = new StringBuilder();
                sb.append("I send updates to these subscribers:<br>");
                subscribers.forEach(sub -> sb.append(sub.toString()).append(" (").append(Users.get(sub)).append(")<br>"));
                Messages.send(im.from_id, sb.toString());
                break;
            }
            case "/monitorHistory": {
                if (!checkAdmin(im.user_id))
                    return;
                if (command.length != 2) {
                    Messages.send(im.from_id, "Invalid syntax");
                    return;
                }
                int id;
                try {
                    id = Integer.parseInt(command[1]);
                } catch (NumberFormatException e) {
                    Messages.send(im.from_id, "invalid user_id");
                    return;
                }
                StringBuilder sb = new StringBuilder();
                UserData ud = null;
                synchronized (usersData) {
                    for (UserData data : usersData)
                        if (data.id == id) {
                            ud = data;
                            break;
                        }
                }
                if (ud == null) {
                    Messages.send(im.from_id, "No such user found in monitor.");
                    return;
                }
                sb.append("All I know about history of ").append(id).append(" (").append(Users.get(id)).append("):<br>");
                ud.history.forEach(change -> {
                    sb.append(sdf.format(change.date));
                    sb.append(change.type);
                    sb.append(" ");
                    sb.append(change.data);
                    sb.append("<br>");
                });
                Messages.send(im.from_id, sb.toString());
                break;
            }
            case "/monitorListFriends": {
                if (!checkAdmin(im.user_id))
                    return;
                if (command.length != 2) {
                    Messages.send(im.from_id, "Invalid syntax");
                    return;
                }
                int id;
                try {
                    id = Integer.parseInt(command[1]);
                } catch (NumberFormatException e) {
                    Messages.send(im.from_id, "invalid user_id");
                    return;
                }
                StringBuilder sb = new StringBuilder();
                UserData ud = null;
                synchronized (usersData) {
                    for (UserData data : usersData)
                        if (data.id == id) {
                            ud = data;
                            break;
                        }
                }
                if (ud == null) {
                    Messages.send(im.from_id, "No such user found in monitor.");
                    return;
                }
                sb.append("Here's what I know about friends of ").append(id).append(" (").append(Users.get(id)).append("):<br>");
/*                ud.friends.forEach(friend -> {
                    sb.append(friend.id);
                    sb.append(" ");
                    sb.append(friend.toString());
                    sb.append("<br>");
                });*/
                sb.append("Count of friends: ");
                sb.append(ud.friends.size());
                Messages.send(im.from_id, sb.toString());
                break;
            }
        }
    }

    @Override
    public String getName() {
        return "ModuleMonitor";
    }

    @Override
    public String getHelp() {
        return "/monitorAdd <user_id>\n" +
                "/monitorRemove <user_id>\n" +
                "/monitorList\n" +
                "/monitorSubscribe\n" +
                "/monitorUnsubscribe\n" +
                "/monitorSubscribers\n" +
                "/monitorHistory <user_id>\n" +
                "/monitorListFriends <user_id>";
    }

    class UserData {
        int id;

        ArrayList<ObjectUser> friends = new ArrayList<>();
        ArrayList<ObjectGroup> groups = new ArrayList<>();
        boolean online = false;
        long last_seen = -1;

        ArrayList<Change> history = new ArrayList<>();


        UserData(int id) {
            this.id = id;
        }

        void loadFrom(InputStream is) throws IOException {
            DataInputStream dis = new DataInputStream(is);
            int count = dis.readInt();
            for (int i = 0; i < count; i++) {
                history.add(new Change(dis.readLong(), Change.Type.values()[dis.readInt()], dis.readInt()));
            }
            // check if friend list is available
            if (dis.available() == 0)
                return;
            int friendsCount = dis.readInt();
            if (friendsCount > 0) {
                int[] friendIDs = new int[friendsCount];
                for (int i = 0; i < friendsCount; i++) {
                    friendIDs[i] = dis.readInt();
                }
                ObjectUser[] friends1 = null;
                while (friends1 == null) {
                    friends1 = Users.get(friendIDs);
                    if (friends1 == null)
                        System.out.println("Couldn't load friends in ModuleMonitor. Retrying...");
                }
                friends.addAll(Arrays.asList(friends1));
            }

            // check if group list is available
            if (dis.available() == 0)
                return;

            int groupsCount = dis.readInt();
            int[] groupIDs = new int[groupsCount];
            for (int i = 0; i < groupsCount; i++) {
                groupIDs[i] = dis.readInt();
            }
            ObjectGroup[] groups1 = null;
            while (groups1 == null) {
                groups1 = Groups.getById(groupIDs);
                if (groups1 == null)
                    System.out.println("Couldn't load groups in ModuleMonitor. Retrying...");
            }
            groups.addAll(Arrays.asList(groups1));
        }

        void saveTo(OutputStream os) throws IOException {
            DataOutputStream dos = new DataOutputStream(os);
            dos.writeInt(history.size());
            for (Change change : history) {
                dos.writeLong(change.date);
                dos.writeInt(change.type.ordinal());
                dos.writeInt(change.data);
            }
            dos.writeInt(friends.size());
            for (ObjectUser friend : friends) {
                dos.writeInt(friend.id);
            }
            dos.writeInt(groups.size());
            for (ObjectGroup group : groups) {
                dos.writeInt(group.id);
            }
        }

        void update() throws IOException {
            processFriends();
            processGroups();
            processOnline();
        }

        private void processFriends() throws IOException {
            if (friends.isEmpty()) {
                friends = Friends.get(id);
            } else {
                ArrayList<ObjectUser> newFriends = Friends.get(id);
                if (newFriends == null)
                    return;

                // check for friend adds
                for (int i = newFriends.size() - 1; i >= 0; i--) {
                    ObjectUser user = newFriends.get(i);
                    boolean found = false;
                    for (ObjectUser u : friends) {
                        if (user.id == u.id) {
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        history.add(new Change(Change.Type.ADDED_FRIEND, user.id));
                        String msg = "User " + id + " (" + Users.get(id).getName() + ") added " + user.id + " (" + user.toString() + ") to friends";
                        subscribers.forEach(s -> Messages.send(s, msg));
                    }
                }

                // check for friend removes
                for (int i = friends.size() - 1; i >= 0; i--) {
                    ObjectUser user = friends.get(i);
                    boolean found = false;
                    for (ObjectUser u : newFriends) {
                        if (user.id == u.id) {
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        history.add(new Change(Change.Type.REMOVED_FRIEND, user.id));
                        String msg = "User " + id + " (" + Users.get(id).getName() + ") removed friend " + user.id + " (" + user.toString() + ") from friends";
                        subscribers.forEach(s -> Messages.send(s, msg));
                    }
                }

                friends = newFriends;
            }
        }

        private void processOnline() {
            ObjectUser user = Users.get(id, "fields=online,last_seen");
            if (user.online != online || user.last_seen != last_seen) {
                if (last_seen == -1)
                    last_seen = user.last_seen;

                String msg;
                if (user.online || user.last_seen != last_seen)
                    history.add(new Change(Change.Type.CAME_ONLINE, user.last_seen_platform));
//                    msg = "User " + user.getName() + " (" + user.id + ") came online (" + user.getPlatform() + ")";
                else
                    history.add(new Change(Change.Type.CAME_OFFLINE, user.last_seen_platform));
//                    msg = "User " + user.getName() + " (" + user.id + ") came offline";

                online = user.online;
            }
        }

        private void processGroups() throws IOException {
            if (groups.isEmpty()) {
                groups = Groups.get(id);
            } else {
                ArrayList<ObjectGroup> newGroups = Groups.get(id);
                if (newGroups == null)
                    return;

                // check for group adds
                for (int i = newGroups.size() - 1; i >= 0; i--) {
                    ObjectGroup group = newGroups.get(i);
                    boolean found = false;
                    for (ObjectGroup g : groups) {
                        if (group.id == g.id) {
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        history.add(new Change(Change.Type.ADDED_GROUP, group.id));
                        String msg = "User " + id + " (" + Users.get(id).getName() + ") added group " + group.id + " (" + group.toString() + ")";
                        subscribers.forEach(s -> Messages.send(s, msg));
                    }
                }

                // check for group removes
                for (int i = groups.size() - 1; i >= 0; i--) {
                    ObjectGroup group = groups.get(i);
                    boolean found = false;
                    for (ObjectGroup g : newGroups) {
                        if (group.id == g.id) {
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        history.add(new Change(Change.Type.REMOVED_GROUP, group.id));
                        String msg = "User " + id + " (" + Users.get(id).getName() + ") removed group " + group.id + " (" + group.toString() + ")";
                        subscribers.forEach(s -> Messages.send(s, msg));
                    }
                }

                groups = newGroups;
            }
        }
    }

    static class Change {
        long date;
        Type type;
        int data;

        Change(Type type, int data) {
            this(System.currentTimeMillis(), type, data);
        }

        Change(long date, Type type, int data) {
            this.type = type;
            this.data = data;
            this.date = date;
        }

        enum Type {
            CAME_ONLINE,
            CAME_OFFLINE,
            ADDED_FRIEND,
            REMOVED_FRIEND,
            ADDED_GROUP,
            REMOVED_GROUP
        }
    }

    private boolean isSubscriber(int id) {
        for (Integer subscriber : subscribers) {
            if (id == subscriber)
                return true;
        }
        return false;
    }
}
