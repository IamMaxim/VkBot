package ru.iammaxim.VkBot;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by maxim on 6/13/17.
 */
public class AccessTokenManager {
    private String[] tokens;
    private int index = 0, maxIndex;

    public void init() {
        ArrayList<String> list = new ArrayList<>();
        File file = new File("access_token.txt");
        if (!file.exists()) {
            System.out.println("ERROR! Access token not found! Please, create file access_token.txt");
            throw new RuntimeException("ERROR! Access token not found!");
        }
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNext())
                list.add(scanner.next());
        } catch (IOException e) {
            e.printStackTrace();
        }
        maxIndex = list.size() - 1;
        tokens = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            tokens[i] = list.get(i);
        }
    }

    public String get() {
        String toReturn = tokens[index];
        index++;
        if (index > maxIndex)
            index = 0;
        return toReturn;
    }
}
