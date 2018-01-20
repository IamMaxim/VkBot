package ru.iammaxim.VkBot;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by maxim on 19.08.2016.
 */
public class Logger extends PrintStream {
    public ArrayList<String> lastLog = new ArrayList<>(10);
    private SimpleDateFormat format = new SimpleDateFormat("'['HH:mm:ss dd.MM.yyyy'] '");
    private boolean writeLastLog = false;
    private FileOutputStream fos;
    private OutputStream parentStream;

    public Logger(OutputStream out, String filename) {
        this(out);
        this.parentStream = out;
        try {
            File f = Utils.getFile(filename);
            fos = new FileOutputStream(f, true);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Logger(OutputStream out) {
        super(out);
    }

    public void startLastLogging() {
        lastLog.clear();
        writeLastLog = true;
    }

    public void stopLastLogging() {
        writeLastLog = false;
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
        if (parentStream instanceof PrintStream)
            ((PrintStream) parentStream).println(s);
        if (writeLastLog)
            lastLog.add(s);
        if (fos != null)
            try {
                fos.write((s + '\n').getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    @Override
    public void println(Object x) {
        println(String.valueOf(x));
    }
}
