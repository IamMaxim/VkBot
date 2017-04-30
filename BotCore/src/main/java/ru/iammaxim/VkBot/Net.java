package ru.iammaxim.VkBot;

import org.json.JSONObject;

import java.io.IOException;
import java.net.*;
import java.util.Scanner;

public class Net {
    private static final String version = "?v=5.63";

    public static String processRequest(String url) throws IOException {
        StringBuilder sb = new StringBuilder();
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        try {
            Scanner scanner = new Scanner(connection.getInputStream());
            while (scanner.hasNext()) {
                sb.append(scanner.nextLine());
            }
            //check for errors
            if (sb.toString().contains("error")) {
                JSONObject object = new JSONObject(sb.toString());

                //check if returned JSON contains error
                if (!object.has("error"))
                    return sb.toString();

                object = object.getJSONObject("error");
                int error_code = object.getInt("error_code");

                //error_code == 6 is too many requests per second, wait 1 sec and repeat
                if (error_code != 6) return "ERROR! CAN'T PERFORM REQUEST! " + sb.toString();

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
        } catch (MalformedURLException e) {}
        return sb.toString();
    }

    //example: "messages.getDialogs", true, "count=1", "offset=0"
    public static String processRequest(String groupAndName, boolean useAccessToken, String... keysAndValues) throws IOException {
        StringBuilder sb = new StringBuilder("https://api.vk.com/method/");
        sb.append(groupAndName).append(version);
        if (useAccessToken) {
            sb.append("&access_token=").append(Main.instance.getAccessToken());
        }
        for (String s : keysAndValues) {
            String[] KaV = s.split("=");
            //KaV[0] MUST not contain disallowed chars according to VK API
            KaV[1] = URLEncoder.encode(KaV[1], "UTF-8");
            sb.append("&").append(KaV[0]).append("=").append(KaV[1]);
        }
        return processRequest(sb.toString());
    }
}
