package ru.iammaxim.VkBot;

import java.io.File;

/**
 * Created by maxim on 5/23/17 at 5:20 PM.
 */
public class Utils {
    public static File getFile(String filename) {
        if (filename.contains("/")) {
            String dir = filename.substring(0, filename.indexOf("/"));
            new File(dir).mkdirs();
        }
        return new File(filename);
    }
}
