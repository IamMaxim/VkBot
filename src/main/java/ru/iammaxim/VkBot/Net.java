package ru.iammaxim.VkBot;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.URL;
import java.util.Scanner;

public class Net {
    private static final String version = "?v=5.52";

    public static String processRequest(String url) throws IOException {
        StringBuilder sb = new StringBuilder();
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        try {
            Scanner scanner = new Scanner(connection.getInputStream());
            while (scanner.hasNext()) {
                sb.append(scanner.nextLine());
            }
            //too many requests per second, wait 1 sec and repeat
            if (sb.toString().contains("error_code")) {
                do {
                    try {
                        Thread.sleep(1000);
                        sb.delete(0, sb.length());
                        connection = (HttpURLConnection) new URL(url).openConnection();
                        scanner = new Scanner(connection.getInputStream());
                        while (scanner.hasNext()) {
                            sb.append(scanner.nextLine());
                        }
                    } catch (InterruptedException | SocketException e) {
                        e.printStackTrace();
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e1) {
                            e1.printStackTrace();
                        }
                        return processRequest(url);
                    }
                    System.out.println(sb.toString());
                } while (sb.toString().contains("error_code"));
            }
        } catch (MalformedURLException e) {};
        return sb.toString();
    }

    //example: messages.getDialogs true count=1, offset=0
    public static String processRequest(String groupAndName, boolean useAccessToken, String... keysAndValues) throws IOException {
        StringBuilder sb = new StringBuilder("https://api.vk.com/method/");
        sb.append(groupAndName).append(version);
        if (useAccessToken) {
            sb.append("&access_token=").append(Main.instance.getAccessToken());
        }
        for (String s : keysAndValues) {
            sb.append("&").append(s.replace(" ", "%20"));
        }
        return processRequest(sb.toString());
    }

    public static String processRequest(Request request) throws IOException {
        StringBuilder sb = new StringBuilder("https://api.vk.com/method/");
        sb.append(request.group).append(".").append(request.name).append(version);
        if (request.accessToken != null) {
            sb.append("&access_token=").append(request.accessToken);
        }
        for (int i = 0; i < request.args.length/2; i++) {
            sb.append("&").append(request.args[i * 2]).append("=").append(request.args[i * 2 + 1].replace(" ", "%20"));
        }
        return processRequest(sb.toString());
    }
}
