package ru.iammaxim.ModuleTalker;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Stack;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by maxim on 22.08.2016.
 */
public class Node {
    public Node parent;
    public String value;
    public int weight = 1;
    public int totalChildrenWeight = 0;
    public CopyOnWriteArrayList<Node> children = new CopyOnWriteArrayList<>();

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public void incrWeight() {
        weight++;
    }

    public Node() {
        value = "";
    }

    public Node(String value) {
        this.value = value;
    }

    public Node(Node parent, String value) {
        this.parent = parent;
        this.value = value;
    }


    public void add(Node child) {
        children.add(child);
    }

    public void computeTotalWeight() {
        totalChildrenWeight = 0;
        for (Node child : children) {
            totalChildrenWeight += child.weight;
        }
    }

    public Node getChild() {
        int total = totalChildrenWeight;
        double random = Math.random() * total;
        for (Node child : children) {
            random -= child.weight;
            if (random < 0) return child;
        }
        System.out.println("couldn't get node child");
        return null;
    }

    public void log(FileOutputStream fos, String indent) throws IOException {
        fos.write((indent + value + '\n').getBytes());
        children.forEach(child -> {
            try {
                child.log(fos, indent + '\t');
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public Stack<Integer> computeVariantsCount() {
        Stack<Integer> ints = new Stack<>();
        for (Node child : children) {
            Stack<Integer> ints2 = child.computeVariantsCount();
            while (!ints2.empty())
                ints.push(ints2.pop());
        }
        return ints;
    }

    public String getSentence(int i) {
        if (i == 500) return "";
        if (children.size() == 0) return value;
        else return value + " " + getChild().getSentence(++i);
    }
}
