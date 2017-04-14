package ru.iammaxim.VkBot;

import ru.iammaxim.VkBot.ModuleBase.ModuleBase;
import ru.iammaxim.VkBot.Objects.ObjectMessage;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Maxim on 21.06.2016.
 */
public class ModuleManager {
    private final List<ModuleBase> modules = new ArrayList<>();

    public List<ModuleBase> getModules() {
        return modules;
    }

    public void addModule(ModuleBase module) {
        modules.add(module);
    }

    public void removeModule(String name) {
        synchronized (modules) {
            Iterator<ModuleBase> iterator = modules.iterator();
            while (iterator.hasNext()) {
                ModuleBase module = iterator.next();
                if (module.getName().equals(name))
                    iterator.remove();
            }
        }
    }

    public void process(ObjectMessage message) {
        synchronized (modules) {
            modules.forEach((module) -> {
                module.process(message);
            });
        }
    }

    public ModuleBase getModule(String name) {
        for (ModuleBase moduleBase : modules) {
            if (moduleBase.getName().equals(name))
                return moduleBase;
        }
        return null;
    }

    public void loadModules() {
        File folder = new File("modules");
        folder.mkdirs();
        for (File f : folder.listFiles()) {
            if (new File("modules/" + f.getName()).isDirectory()) {
                continue;
            }
            loadModule(f);
        }
    }

    public void loadModule(File f) {
        try {
            ClassLoader classLoader = new URLClassLoader(new URL[]{f.toURL()});
            InputStream is = classLoader.getResourceAsStream("info.txt");
            Scanner scanner = new Scanner(is);
            String name = scanner.nextLine();
            scanner.close();
            is.close();
            System.out.println("loading module: " + name);
            ModuleBase module = (ModuleBase) classLoader.loadClass(name).newInstance();
            module.load("save/" + module.getName() + "/");
            System.out.println("Loaded " + module.getName() + "'s data");
            Main.instance.getModuleManager().addModule(module);
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | IOException e) {
            e.printStackTrace();
        }
    }

    public void save() {
        modules.forEach(m -> {
            new File("save").mkdirs();
            m.save("save/" + m.getName() + "/");
            System.out.println(m.getName() + "'s data saved.");
        });
    }
}
