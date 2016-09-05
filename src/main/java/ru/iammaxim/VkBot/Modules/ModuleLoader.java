package ru.iammaxim.VkBot.Modules;

import java.io.*;

public class ModuleLoader extends ClassLoader {
    public ModuleLoader(ClassLoader parent) {
        super(parent);
    }

    public Class loadClass(String name) throws ClassNotFoundException {
        try {
            File f = new File("modules/" + name);
            InputStream is = new FileInputStream(f);
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            int data = is.read();
            while(data != -1){
                buffer.write(data);
                data = is.read();
            }
            is.close();
            byte[] classData = buffer.toByteArray();
            buffer.close();
            return defineClass(name, classData, 0, classData.length);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
