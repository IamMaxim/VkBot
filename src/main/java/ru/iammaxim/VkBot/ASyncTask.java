package ru.iammaxim.VkBot;

public abstract class ASyncTask<T> extends Thread {
    public abstract T doInBackground();
    public abstract void doInMainThread(T result);
    @Override
    public void run() {
        Main.instance.addTask(()->doInMainThread(doInBackground()));
    }
}
