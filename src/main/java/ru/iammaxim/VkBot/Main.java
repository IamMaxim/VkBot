package ru.iammaxim.VkBot;

import ru.iammaxim.GUIlib.ConsoleWindow;
import ru.iammaxim.GUIlib.Out;
import ru.iammaxim.Tasker.TaskController;
import ru.iammaxim.VkBot.Groups.Users;
import ru.iammaxim.VkBot.LocalCommands.CommandRegistry;
import ru.iammaxim.VkBot.Objects.ObjectUser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;
import java.util.Scanner;

public class Main {
    public static Main instance;
    private volatile boolean needToRun = true;
    private TaskController taskController;
    private LongPollThread longPollThread;
    public ConsoleWindow consoleWindow;
    public CommandRegistry localCommandRegistry;
    public Out logger;

    public ModuleManager getModuleManager() {
        return moduleManager;
    }

    private ModuleManager moduleManager;
    private String access_token;
    private Scanner input;
    private static Random random = new Random();
    private ObjectUser botUser;

    public void init() {
        taskController = new TaskController(4);
        setupAccessToken();
        moduleManager = new ModuleManager();
        moduleManager.loadModules();
        input = new Scanner(System.in);
        localCommandRegistry = new CommandRegistry();
        localCommandRegistry.register();
        setBotUser(Users.get());

        if (needToRun) {
            UserDB.load();
            UserDB.startSaveThread();
            longPollThread = new LongPollThread("LongPollThread");
            longPollThread.start();
        }
    }

    public void destroy() {
        taskController.stop();
        System.out.println("Shutting down task controller");
        longPollThread.interrupt();
        UserDB.save();
        UserDB.saveThread.interrupt();
    }

    public static void main(String[] args) {
        Thread.currentThread().setName("MainThread");
        instance = new Main();
        boolean nogui = false;
        for (String arg : args) {
            if (arg.toLowerCase().equals("-nogui")) nogui = true;
        }
        instance.run(nogui);
    }

    private void run(boolean nogui) {
        if (!nogui) {
            consoleWindow = ConsoleWindow.create();
            logger = consoleWindow.out;
        }
        init();
        while (needToRun && input.hasNext()) {
            processInput(input.nextLine());
        }
        destroy();
        System.out.println("Shutting down main thread");
    }

    private void processInput(String input) {
        localCommandRegistry.runCommand(input, false);
    }

    public void addTask(Runnable task) {
        taskController.addTask(task);
    }

    public ObjectUser getBotUser() {
        return botUser;
    }

    public void setBotUser(ObjectUser user) {
        botUser = user;
        System.out.println("botUser set to " + user.first_name + " " + user.last_name);
    }

    public void stopThread() {
        needToRun = false;
    }

    public String getAccessToken() {
        return access_token;
    }

    public void setupAccessToken() {
        File file = new File("access_token.txt");
        if (!file.exists()) {
            System.out.println("ERROR! Access token not found!");
            needToRun = false;
            return;
        }
        try {
            InputStream is = new FileInputStream(file);
            byte[] at = new byte[(int) file.length()];
            is.read(at);
            access_token = new String(at);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getRandomHex() {
        return Integer.toHexString(random.nextInt());
    }
}
