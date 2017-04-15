package ru.iammaxim.VkBot;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by maxim on 19.08.2016.
 */
public class Logger extends PrintStream {
    private SimpleDateFormat format = new SimpleDateFormat("'['HH:mm:ss dd.MM.yyyy'] '");
    public ArrayList<String> lastLog = new ArrayList<>(10);
    private boolean writeLastLog = false;

    public void startLastLogging() {
        lastLog.clear();
        writeLastLog = true;
    }

    public void stopLastLogging() {
        writeLastLog = false;
    }

    public Logger(OutputStream out) {
        super(out);
    }

    private String getString(String s) {
        return format.format(new Date()) + s;
    }

    @Override
    public void println(boolean x) {
        println(x ? "true" : "false");
    }

    @Override
    public void println(char x) {
        println(String.valueOf(x));
    }

    @Override
    public void println(int x) {
        println(String.valueOf(x));
    }

    @Override
    public void println(long x) {
        println(String.valueOf(x));
    }

    @Override
    public void println(float x) {
        println(String.valueOf(x));
    }

    @Override
    public void println(double x) {
        println(String.valueOf(x));
    }

    @Override
    public void println(char[] x) {
        println(String.valueOf(x));
    }

    @Override
    public void println(String x) {
        String s = getString(x);
        if (writeLastLog)
            lastLog.add(s);
        super.println(s);
    }

    @Override
    public void println(Object x) {
        println(String.valueOf(x));
    }
}
