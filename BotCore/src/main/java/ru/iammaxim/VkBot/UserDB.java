package ru.iammaxim.VkBot;

import ru.iammaxim.VkBot.Objects.ObjectUser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.function.Function;

/**
 * Created by maxim on 18.08.2016.
 */
public class UserDB {
    private static final String filepath = "save/users.dat";
    private static HashMap<Integer, ObjectUser> userDB = new HashMap<>();
    public static Thread saveThread;

    public static void startSaveThread() {
        saveThread = new Thread(() -> {
            while (!saveThread.isInterrupted()) {
                try {
                    Thread.sleep(60000);
                    Main.instance.localCommandRegistry.commands.get("save").run();
                } catch (InterruptedException e) {
                }
            }
        }, "UserDBsaveThread");
        saveThread.start();
    }

    public static ObjectUser delete(int id) {
        return userDB.remove(id);
    }

    public static Collection<ObjectUser> getUsers() {
        return userDB.values();
    }

    private static byte[] intToBytes(int value) {
        return new byte[]{(byte) (value >> 24), (byte) (value >> 16), (byte) (value >> 8), (byte) value};
    }

    private static int bytesToInt(byte[] bytes) {
        int value = (bytes[0] << 24) & 0xff000000 | (bytes[1] << 16) & 0x00ff0000 | (bytes[2] << 8) & 0x0000ff00 | (bytes[3]) & 0x000000ff;
        return value;
    }

    public static void save() {
        try {
            File file = new File(filepath);
            if (!file.exists()) {
                new File(filepath.substring(0, filepath.lastIndexOf("/"))).mkdirs();
                file.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(file);

            fos.write(intToBytes(userDB.size()));
            for (ObjectUser user : userDB.values()) {
                fos.write(intToBytes(user.id));
                fos.write(user.first_name.getBytes().length);
                fos.write(user.first_name.getBytes());
                fos.write(user.last_name.getBytes().length);
                fos.write(user.last_name.getBytes());
                //System.out.println("saved user " + user.first_name + " " + user.last_name);
            }
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void load() {
        try {
            File file = new File(filepath);
            if (!file.exists()) {
                System.out.println(filepath + " doesn't exists. Couldn't load users database");
                return;
            }
            FileInputStream fis = new FileInputStream(file);

            byte[] intBuffer = new byte[4];

            Function<Void, Integer> readInt = v -> {
                try {
                    fis.read(intBuffer);
                    return bytesToInt(intBuffer);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            };

            Function<Void, String> readString = v -> {
                try {
                    int len = fis.read();
                    byte[] buffer = new byte[len];
                    fis.read(buffer);
                    return new String(buffer);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            };

            int size = readInt.apply(null);

            for (int i = 0; i < size; i++) {
                int id = readInt.apply(null);
                String first_name = readString.apply(null);
                String last_name = readString.apply(null);
                ObjectUser user = new ObjectUser();
                user.id = id;
                user.first_name = first_name;
                user.last_name = last_name;
                userDB.put(id, user);
                System.out.println("loaded user " + first_name + " " + last_name);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ObjectUser get(int id) {
        return userDB.get(id);
    }

    public static void add(int id, ObjectUser user) {
        userDB.put(id, user);
    }

    public static void add(ObjectUser user) {
        System.out.println("added user " + user.first_name + " " + user.last_name);
        userDB.put(user.id, user);
    }

    public void update() {

    }
}
