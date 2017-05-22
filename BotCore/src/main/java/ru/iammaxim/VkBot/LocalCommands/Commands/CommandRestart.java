package ru.iammaxim.VkBot.LocalCommands.Commands;

import ru.iammaxim.VkBot.LocalCommands.LocalCommandBase;
import ru.iammaxim.VkBot.Main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

/**
 * Created by maxim on 27.08.2016.
 */
public class CommandRestart extends LocalCommandBase {
    @Override
    public String getName() {
        return "restart";
    }

    @Override
    public void run(String[] args) {
        System.out.println();
        Main.instance.destroy();
        Main.instance.init();

        try {
            String startCommand = null;
            Process process = Runtime.getRuntime().exec("ps -ef");
            BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String s;
            while ((s = br.readLine()) != null) {
                String[] arr = s.split(" +");
                if (arr[7].contains("java")) {
                    if (startCommand == null) {
                        startCommand = String.join(" ", Arrays.copyOfRange(arr, 7, arr.length));
                        if (!startCommand.contains("VkBot.jar"))
                            startCommand = null;
                    }
                }
            }

            if (startCommand == null) {
                System.out.println("Error reading process info. Cancelling core restart");
                return;
            }

            br.close();
            process.waitFor();
            process.destroy();
            startCommand = startCommand.substring(startCommand.indexOf("java"));
            System.out.println("Trying to run new bot process");
            Runtime.getRuntime().exec(startCommand);
            System.out.println("Starting new process successful. Gonna die!");
            Main.instance.stop();
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }
}
