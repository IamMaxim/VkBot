package ru.iammaxim.ModuleNNTalker;

import com.sun.jna.Library;
import com.sun.jna.Native;

/**
 * Created by maxim on 01.10.2016.
 */
public interface BotLibrary extends Library {
    BotLibrary instance = (BotLibrary) Native.loadLibrary("BotLibrary", BotLibrary.class);
    void init();
    String answer(String str);
}
