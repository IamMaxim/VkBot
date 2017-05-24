package ru.iammaxim.VkBot;

import java.io.File;
import java.lang.reflect.Array;
import java.util.Arrays;

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

    public static <T> T[] concatArrays(T[] arr1, T[] arr2) {
        @SuppressWarnings("unchecked")
        T[] arr = (T[]) Array.newInstance(arr1.getClass().getComponentType(), arr1.length + arr2.length);
        System.arraycopy(arr1, 0, arr, 0, arr1.length);
        System.arraycopy(arr2, 0, arr, arr1.length, arr2.length);
        return arr;
    }

    public static byte[] concatArrays(byte[] arr1, byte[] arr2) {
        byte[] arr = new byte[arr1.length + arr2.length];
        System.arraycopy(arr1, 0, arr, 0, arr1.length);
        System.arraycopy(arr2, 0, arr, arr1.length, arr2.length);
        return arr;
    }
}
