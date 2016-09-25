package ru.iammaxim.ModuleTalker;

import java.io.FileOutputStream;
import java.io.IOException;
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

    public Node() {
        value = "root";
    }

    public Node(String value) {
        this.value = value;
    }

    public Node(Node parent, String value) {
        this.parent = parent;
        this.value = value;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public void incrWeight() {
        weight++;
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

    private Node getChild() {
        int total = totalChildrenWeight;
        double random = Math.random() * total;
        for (Node child : children) {
            random -= child.weight;
            if (random < 0)
                if (value.equals(child.value)) {
                    return getChild();
                } else {
                    return child;
                }
        }
        System.out.println("couldn't get node child");
        return null;
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
        if (i == 0) return getChild().getSentence(i+1);
        if (i == 100) return "";
        if (children.size() == 0) return value;
        else return value + " " + getChild().getSentence(i+1);
    }
}
