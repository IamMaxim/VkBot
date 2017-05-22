package ru.iammaxim.VkBot;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by maxim on 14.04.2017.
 */
public class UserManager {
    public static final String filepath = "save/admins.dat";
    private static final ArrayList<Integer> admins = new ArrayList<>();
    public static final Object adminsLock = new Object();

    public static void load() {
        try {
            File file = new File(filepath);
            if (file.exists()) {
                try (DataInputStream dis = new DataInputStream(new FileInputStream(file))) {
                    while (dis.available() > 0) {
                        admins.add(dis.readInt());
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void save() {
        try {
            File file = new File(filepath);
            if (!file.exists()) {
                System.out.println("'" + filepath + "' doesn't exists. Creating one.");
                new File(filepath.substring(0, filepath.lastIndexOf("/"))).mkdirs();
                file.createNewFile();
            }

            try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(file))) {
                synchronized (adminsLock) {
                    for (Integer user_id : admins) {
                        dos.writeInt(user_id);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void removeAdmin(Integer user_id) {
        admins.remove(user_id);
    }

    public static List<Integer> getAdmins() {
        return admins;
    }

    public static void addAdmin(int user_id) {
        admins.add(user_id);
    }

    public static boolean isAdmin(int id) {
        synchronized (adminsLock) {
            for (Integer user_id : admins) {
                if (user_id == id) return true;
            }
        }
        return false;
    }

    public static String getAccessDeniedText() {
        return "access denied: " + Main.getRandomHex();
    }
}
