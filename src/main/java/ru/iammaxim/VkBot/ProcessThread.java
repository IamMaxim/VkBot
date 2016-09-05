package ru.iammaxim.VkBot;

import ru.iammaxim.VkBot.Groups.Users;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ProcessThread extends Thread {
    private List<Runnable> tasks = new CopyOnWriteArrayList<>();
    private volatile boolean needToRun = true;

    public ProcessThread(String name) {
        super(name);
    }

    private void sleep() {
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        Main.instance.setBotUser(Users.get());
        while (needToRun) {
            Iterator<Runnable> iterator = tasks.iterator();

            if (tasks.size() == 0) {
                sleep();
                continue;
            }

            iterator.forEachRemaining((task) -> {
                task.run();
                tasks.remove(task);
            });
        }
        System.out.println("Shutting down process thread");
    }

    public void stopThread() {
        needToRun = false;
    }

    public void addTask(Runnable task) {
        tasks.add(task);
        notify();
    }


}