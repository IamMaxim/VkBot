package ru.iammaxim.VkBot.LocalCommands.Commands;

import ru.iammaxim.VkBot.LocalCommands.LocalCommandBase;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by maxim on 5/22/17 at 5:55 PM.
 */
public class CommandExec extends LocalCommandBase {
    @Override
    public String getName() {
        return "exec";
    }

    @Override
    public void run(String... args) {
        try {
            String arg = String.join(" ", args);
            Process p = Runtime.getRuntime().exec(arg);
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String s;
            while ((s = br.readLine()) != null)
                System.out.println(s);
            p.waitFor();
            p.destroy();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
