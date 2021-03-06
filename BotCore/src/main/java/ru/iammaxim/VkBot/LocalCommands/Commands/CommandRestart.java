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
        try {
            String startCommand = null;
            Process process = Runtime.getRuntime().exec("ps -ef");
            BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String s;
            while ((s = br.readLine()) != null) {
                if (startCommand != null)
                    break;
                String[] arr = s.split(" +");
                if (arr[7].contains("java")) {
                    startCommand = String.join(" ", Arrays.copyOfRange(arr, 7, arr.length));
                    if (!startCommand.contains("VkBot.jar"))
                        startCommand = null;
                }
            }
            br.close();
            process.destroy();

            if (startCommand == null) {
                System.out.println("Error reading process info. Cancelling core restart");
                Main.instance.destroy();
                Main.instance.init();
                return;
            }

            startCommand = startCommand.substring(startCommand.indexOf("java"));
            Runtime.getRuntime().exec(startCommand);
            System.out.println("Starting new process successful. Gonna die!");
            Main.instance.addTask(() -> Main.instance.stop());
        } catch (IOException e) {
            e.printStackTrace(System.out);
        }
    }
}
