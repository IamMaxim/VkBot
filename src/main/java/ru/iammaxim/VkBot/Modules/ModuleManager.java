package ru.iammaxim.VkBot.Modules;

import ru.iammaxim.ModuleBase.ModuleBase;
import ru.iammaxim.VkBot.Main;
import ru.iammaxim.VkBot.Objects.ObjectMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Maxim on 21.06.2016.
 */
public class ModuleManager {
    private List<ModuleBase> modules = new ArrayList<>();

    public List<ModuleBase> getModules() {
        return modules;
    }

    public void addModule(ModuleBase module) {
        modules.add(module);
    }

    public void removeModule(String name) {
        modules.forEach((module) -> {
            if (module.getName().equals(name))
                modules.remove(module);
        });
    }

    public void process(ObjectMessage message) {
        modules.forEach((module) -> {
            module.process(message);
        });
    }

    public ModuleBase getModule(String name) {
        for (ModuleBase moduleBase : modules) {
            if (moduleBase.getName().equals(name))
                return moduleBase;
        }
        return null;
    }
}
