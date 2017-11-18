package ru.iammaxim.ModuleTalker;

import org.json.JSONObject;
import ru.iammaxim.VkBot.Groups.Messages;
import ru.iammaxim.VkBot.ModuleBase.ModuleBase;
import ru.iammaxim.VkBot.Objects.ObjectMessage;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by maxim on 22.08.2016.
 */
public class ModuleTalker extends ModuleBase {
    public Node tree = new Node();
    public ConcurrentHashMap<String, Node> data = new ConcurrentHashMap<>();
    public String botName = "(bot Maxim) ";

    public ModuleTalker() {
        try {
            loadFromMessages("messages_in.txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void loadFromMessages(String filename) throws FileNotFoundException {
        long startTime = System.currentTimeMillis();
        File file = new File(filename);
        if (!file.exists()) return;
        Scanner scanner = new Scanner(file);
        //prepass; load messages into data
        while (scanner.hasNext()) {
            String s = scanner.nextLine();
            JSONObject o = new JSONObject(s);
            String body = o.getString("body");
            String[] strings = body.split(" ");
            for (int i = 1; i < strings.length; i++) {
                String prev = strings[i - 1];
                String cur = strings[i];
                Node node = data.get(prev);
                if (node == null) data.put(prev, node = new Node(prev));
                node.add(new Node(cur));
            }
        }
        //main pass
        data.forEach((s, node) -> {
            for (int i1 = 0; i1 < node.children.size(); i1++) {
                Node src = node.children.get(i1);
                Node node1 = data.get(src.value);
                if (node1 != null) {
                    node.children.remove(src);
                    node.children.add(node1);
                    node1.incrWeight();
                }
            }
            node.computeTotalWeight();
            if (node.parent == null)
                tree.add(node);
        });
        System.out.println("messages loaded in " + (float)(System.currentTimeMillis() - startTime)/1000 + " seconds. added " + data.size() + " words.");

        //unload from memory
        data = null;
    }

    @Override
    public void process(ObjectMessage inputMessage) {
        if (inputMessage.body.equals("/gen")) {
            Messages.send(inputMessage.from_id, botName + tree.getSentence(0));
        }
    }

    @Override
    public String getName() {
        return "talker";
    }

    @Override
    public String getHelp() {
        return "/gen - generate sentence from word database";
    }
}
