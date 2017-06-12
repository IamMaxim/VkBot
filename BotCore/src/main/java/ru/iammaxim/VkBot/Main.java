package ru.iammaxim.VkBot;

import ru.iammaxim.Tasker.TaskController;
import ru.iammaxim.VkBot.Groups.Users;
import ru.iammaxim.VkBot.LocalCommands.CommandRegistry;
import ru.iammaxim.VkBot.Objects.ObjectUser;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;
import java.util.Scanner;

public class Main {
    public static Main instance;
    private static Random random = new Random();
    public CommandRegistry localCommandRegistry;
    public Logger logger, errLogger;
    private volatile boolean needToRun = true;
    private TaskController taskController;
    private LongPollThread longPollThread;
    private ModuleManager moduleManager;
    private String access_token;
    private ObjectUser botUser;
    private Thread mainThread = Thread.currentThread();

    public static void main(String[] args) {
        Thread.currentThread().setName("MainThread");
        instance = new Main();
        instance.run();
    }

    public static String getRandomHex() {
        return Integer.toHexString(random.nextInt());
    }

    public ModuleManager getModuleManager() {
        return moduleManager;
    }

    public void init() {
        setupAccessToken();
        taskController = new TaskController(4);
        moduleManager = new ModuleManager();
        moduleManager.loadModules();
        localCommandRegistry = new CommandRegistry();
        localCommandRegistry.register();
        setBotUser(Users.get());

        if (needToRun) {
            UserDB.load();
            UserManager.load();
            UserDB.startSaveThread();
            longPollThread = new LongPollThread("LongPollThread");
            longPollThread.start();
        }
    }

    public void destroy() {
        longPollThread.interrupt();
        UserDB.save();
        UserDB.saveThread.interrupt();
        taskController.stop();
        logger.close();
        errLogger.close();
        System.out.println("Shutting down task controller");
    }

    private void run() {
        logger = new Logger(System.out, "out.log");
        errLogger = new Logger(System.err, "err.log");
        System.setOut(logger);
        System.setErr(errLogger);
        init();

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        do {
            try {
                while (!br.ready() && needToRun) {
                    Thread.sleep(1);
                }
                processInput(br.readLine());
            } catch (InterruptedException e) {
            } catch (IOException e) {
                e.printStackTrace();
            }
        } while (needToRun);

        destroy();
        System.out.println("Shutting down main thread");
    }

    public void processInput(String input) {
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

    public void stop() {
        needToRun = false;
        mainThread.interrupt();
    }

    public String getAccessToken() {
        return access_token;
    }

    public void setupAccessToken() {
        File file = new File("access_token.txt");
        if (!file.exists()) {
            needToRun = false;
            throw new RuntimeException("ERROR! Access token not found!");
        }
        try {
            Scanner scanner = new Scanner(file);
            access_token = scanner.next();
            scanner.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean needToRun() {
        return needToRun;
    }
}
