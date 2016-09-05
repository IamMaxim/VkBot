package ru.iammaxim.ModuleRP;

import ru.iammaxim.ModuleBase.ModuleBase;
import ru.iammaxim.VkBot.Groups.Messages;
import ru.iammaxim.VkBot.Groups.Users;
import ru.iammaxim.VkBot.Main;
import ru.iammaxim.VkBot.Objects.ObjectMessage;
import ru.iammaxim.VkBot.Objects.ObjectUser;

import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * Created by maxim on 18.08.2016.
 */
public class ModuleRP extends ModuleBase {
    private Main main = Main.instance;
    private Pattern urlPattern = Pattern.compile("[^\\.]+\\.[^\\.]+");

    private String replaceDigits(String source) {
        String result = source + "";
        result = result.replace("0", "0⃣")
                .replace("1", "1⃣")
                .replace("2", "2⃣")
                .replace("3", "3⃣")
                .replace("4", "4⃣")
                .replace("5", "5⃣")
                .replace("6", "6⃣")
                .replace("7", "7⃣")
                .replace("8", "8⃣")
                .replace("9", "9⃣");
        return result;
    }

    private String getErrorString() {
        return "An error occured during processing your request: " + Main.getRandomHex();
    }

    private String luckyOrNot() {
        return Math.random() < 0.5 ? "неудачно" : "удачно";
    }

    private boolean testURL(ObjectMessage msg) {
        boolean b = false;
        Scanner scanner = new Scanner(msg.body);
        while (scanner.hasNext()) {
            Scanner scanner1 = new Scanner(scanner.next());
            if (!scanner1.hasNext(urlPattern)) continue;
            String possibleURL = scanner1.next(urlPattern);
            Messages.send(msg.from_id, "Poshel nahooy, sooqa. URL detected: " + Main.getRandomHex());
            b = true;
            break;
        }
        scanner.close();
        return b;
    }

    @Override
    public void process(ObjectMessage msg) {
        System.out.println(msg.body);
        String[] args = msg.body.split(" ");
        String commandName = args[0];
        args = Arrays.copyOfRange(args, 1, args.length);
        if (commandName.equals("***")) {
            if (testURL(msg)) return;
            ObjectUser user;
            if (msg.out) user = main.getBotUser();
            else if (msg.title.equals(" ... "))
                user = Users.get(msg.from_id);
            else
                user = Users.get(msg.user_id);
            if (user == null)
                System.out.println("user == null");
            Messages.send(msg.from_id, "*" + user.first_name + " " + user.last_name + " " + String.join(" ", args) + "* (" + (Math.random() < 0.5 ? "неудачно" : "удачно") + ")");
        } else if (commandName.equals("/chance")) {
            if (testURL(msg)) return;
            Messages.send(msg.from_id, "Шанс того, что " + String.join(" ", args) + ": " + (int) (Math.random() * 100) + "%");
        }
    }

    @Override
    public String getName() {
        return "rp";
    }
}
