package ru.iammaxim.ModuleTalker;

import org.json.JSONObject;
import ru.iammaxim.ModuleBase.ModuleBase;
import ru.iammaxim.VkBot.Groups.Messages;
import ru.iammaxim.VkBot.Main;
import ru.iammaxim.VkBot.Objects.ObjectMessage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by maxim on 22.08.2016.
 */
public class ModuleTalker extends ModuleBase {
    public Node tree = new Node();
    public ConcurrentHashMap<String, Node> data = new ConcurrentHashMap<>();

    public ModuleTalker() {
        try {
            System.out.println("loading talker module...");
            loadFromMessages("messages_in.txt");
            System.out.println("talker module loaded.");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void loadFromMessages(String filename) throws FileNotFoundException {
        File file = new File(filename);
        if (!file.exists()) return;
        Scanner scanner = new Scanner(file);
        //prepass; load messages into data
        while (scanner.hasNext()) {
            String s = scanner.nextLine();
            JSONObject o = new JSONObject(s);
            int out = o.getInt("out");
            if (out == 1) continue;
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

//        logData(data);
        //unload from memory
        data = null;
    }

    private void logData(Map<String, Node> data) {
        try {
            File file = new File("data_out.txt");
            FileOutputStream fos = new FileOutputStream(file);
            data.forEach(((s, node) -> {
                try {
                    fos.write((node.value + ' ' + node.weight + ' ' + node.totalChildrenWeight + '\n').getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }));
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void log(Node tree, int index) {
        System.out.println("started logging with index " + index);
        try (FileOutputStream fos = new FileOutputStream(new File("tree_out.txt." + index))) {
            tree.log(fos, "");
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("logging completed.");
    }

    @Override
    public void process(ObjectMessage inputMessage) {
        if (inputMessage.body.equals("/gen")) {
            Messages.send(inputMessage.from_id, "(bot Dasha) " + tree.getSentence(0));
        }
    }

    @Override
    public String getName() {
        return "talker";
    }
}
