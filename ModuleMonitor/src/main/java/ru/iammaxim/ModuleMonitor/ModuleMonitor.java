package ru.iammaxim.ModuleMonitor;

import ru.iammaxim.VkBot.Groups.Friends;
import ru.iammaxim.VkBot.Groups.Messages;
import ru.iammaxim.VkBot.Groups.Users;
import ru.iammaxim.VkBot.Main;
import ru.iammaxim.VkBot.ModuleBase.ModuleBase;
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
    private static final long minUpdatePeriod = 60000, waitBetweenUpdates = 1000;

    private ArrayList<UserData> usersData = new ArrayList<>();
    private ArrayList<Integer> subsribers = new ArrayList<>();

    private static SimpleDateFormat sdf = new SimpleDateFormat("'['HH:mm:ss dd.MM.yyyy'] '");

    @Override
    public void save(String path) {
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
                dos.writeInt(subsribers.size());
                for (Integer subsriber : subsribers)
                    dos.writeInt(subsriber);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void load(String path) {
        File f = new File(path);
        if (f.exists()) {
            for (File file : f.listFiles()) {
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
                        subsribers.add(dis.readInt());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


        new Thread(() -> {
            while (Main.instance.needToRun()) {
                long startTime = System.currentTimeMillis();
                usersData.forEach(d -> {
                    try {
                        d.update();
                        Thread.sleep(waitBetweenUpdates);
                    } catch (InterruptedException | IOException e) {
                        e.printStackTrace();
                    }
                });
                try {
                    Thread.sleep(Math.max(0, minUpdatePeriod - (System.currentTimeMillis() - startTime)));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private boolean checkAdmin(int id) {
        if (!UserManager.isAdmin(id)) {
            Messages.send(id, UserManager.getAccessDeniedText());
            return false;
        }
        return true;
    }

    @Override
    public void process(ObjectMessage inputMessage) {
        String[] command = inputMessage.body.split(" ");

        switch (command[0]) {
            case "/monitorAdd": {
                if (!checkAdmin(inputMessage.user_id))
                    return;
                if (command.length != 2) {
                    Messages.send(inputMessage.from_id, "Invalid syntax");
                    return;
                }
                try {
                    int id = Integer.parseInt(command[1]);
                    usersData.add(new UserData(id));
                    Messages.send(inputMessage.from_id, "User " + id + " (" + Users.get(id) + ") added to monitor");
                } catch (NumberFormatException e) {
                    Messages.send(inputMessage.from_id, "Invalid user ID");
                }
                break;
            }
            case "/monitorRemove": {
                if (!checkAdmin(inputMessage.user_id))
                    return;
                if (command.length != 2) {
                    Messages.send(inputMessage.from_id, "Invalid syntax");
                    return;
                }
                try {
                    int id = Integer.parseInt(command[1]);
                    usersData.removeIf(data -> data.id == id);
                    Messages.send(inputMessage.from_id, "User " + id + " (" + Users.get(id) + ") removed from monitor");
                } catch (NumberFormatException e) {
                    Messages.send(inputMessage.from_id, "Invalid user ID");
                }
                break;
            }
            case "/monitorList": {
                if (!checkAdmin(inputMessage.user_id))
                    return;
                StringBuilder sb = new StringBuilder();
                sb.append("I'm monitoring these users:<br>");
                usersData.forEach(data -> sb.append(data.id).append(" (").append(Users.get(data.id)).append(")<br>"));
                Messages.send(inputMessage.from_id, sb.toString());
                break;
            }
            case "/monitorSubscribe": {
                if (!checkAdmin(inputMessage.user_id))
                    return;
                if (isSubscriber(inputMessage.from_id)) {
                    Messages.send(inputMessage.from_id, "You are already subscriber");
                    return;
                }
                subsribers.add(inputMessage.from_id);
                Messages.send(inputMessage.from_id, "Subscribed successfully");
                break;
            }
            case "/monitorUnsubscribe": {
                if (!checkAdmin(inputMessage.user_id))
                    return;
                if (!isSubscriber(inputMessage.from_id)) {
                    Messages.send(inputMessage.from_id, "You are not a subscriber");
                    return;
                }
                subsribers.removeIf(sub -> sub == inputMessage.from_id);
                Messages.send(inputMessage.from_id, "Unsubscribed successfully");
                break;
            }
            case "/monitorSubscribers": {
                if (!checkAdmin(inputMessage.user_id))
                    return;
                StringBuilder sb = new StringBuilder();
                sb.append("I send updates to these subscribers:<br>");
                subsribers.forEach(sub -> sb.append(sub.toString()).append(" (").append(Users.get(sub)).append(")<br>"));
                Messages.send(inputMessage.from_id, sb.toString());
                break;
            }
            case "/monitorHistory": {
                if (!checkAdmin(inputMessage.user_id))
                    return;
                if (command.length != 2) {
                    Messages.send(inputMessage.from_id, "Invalid syntax");
                    return;
                }
                int id;
                try {
                    id = Integer.parseInt(command[1]);
                } catch (NumberFormatException e) {
                    Messages.send(inputMessage.from_id, "invalid user_id");
                    return;
                }
                StringBuilder sb = new StringBuilder();
                UserData ud = null;
                for (UserData data : usersData)
                    if (data.id == id) {
                        ud = data;
                        break;
                    }
                if (ud == null) {
                    Messages.send(inputMessage.from_id, "No such user found in monitor.");
                    return;
                }
                sb.append("All I know about history of ").append(id).append(" (").append(Users.get(id)).append("):<br>");
                ud.history.forEach(change -> {
                    sb.append(sdf.format(change.date));
                    sb.append(change.event);
                    sb.append("<br>");
                });
                Messages.send(inputMessage.from_id, sb.toString());
                break;
            }
            case "/monitorListFriends": {
                if (!checkAdmin(inputMessage.user_id))
                    return;
                if (command.length != 2) {
                    Messages.send(inputMessage.from_id, "Invalid syntax");
                    return;
                }
                int id;
                try {
                    id = Integer.parseInt(command[1]);
                } catch (NumberFormatException e) {
                    Messages.send(inputMessage.from_id, "invalid user_id");
                    return;
                }
                StringBuilder sb = new StringBuilder();
                UserData ud = null;
                for (UserData data : usersData)
                    if (data.id == id) {
                        ud = data;
                        break;
                    }
                if (ud == null) {
                    Messages.send(inputMessage.from_id, "No such user found in monitor.");
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
                Messages.send(inputMessage.from_id, sb.toString());
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
        ArrayList<Integer> groups = new ArrayList<>();

        ArrayList<Change> history = new ArrayList<>();


        public UserData(int id) {
            this.id = id;
        }

        public void loadFrom(InputStream is) throws IOException {
            DataInputStream dis = new DataInputStream(is);
            int count = dis.readInt();
            for (int i = 0; i < count; i++) {
                history.add(new Change(dis.readLong(), dis.readUTF()));
            }
            int friendsCount = dis.readInt();
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

        public void saveTo(OutputStream os) throws IOException {
            DataOutputStream dos = new DataOutputStream(os);
            dos.writeInt(history.size());
            for (Change change : history) {
                dos.writeLong(change.date);
                dos.writeUTF(change.event);
            }
            dos.writeInt(friends.size());
            for (ObjectUser friend : friends) {
                dos.writeInt(friend.id);
            }
        }

        public void update() throws IOException {
            if (friends.isEmpty()) {
                friends = Friends.get(id);
            } else {
                ArrayList<ObjectUser> newFriends = Friends.get(id);

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
                        String msg = "User " + id + " (" + Users.get(id).toString() + ") added " + user.id + " (" + user.toString() + ") to friends";
                        history.add(new Change(System.currentTimeMillis(), msg));
                        subsribers.forEach(s -> Messages.send(s, msg));
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
                        String msg = "User " + id + " (" + Users.get(id).toString() + ") removed " + user.id + " (" + user.toString() + ") from friends";
                        history.add(new Change(System.currentTimeMillis(), msg));
                        subsribers.forEach(s -> Messages.send(s, msg));
                    }
                }

                friends = newFriends;
            }
        }
    }

    class Change {
        long date;
        String event;

        Change(long date, String event) {
            this.date = date;
            this.event = event;
        }
    }

    private boolean isSubscriber(int id) {
        for (Integer subsriber : subsribers) {
            if (id == subsriber)
                return true;
        }
        return false;
    }
}
