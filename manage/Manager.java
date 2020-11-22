package main.java.com.homework.manage;


import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Manager {

    private int slot = 150;

    private final long MAX_COUNT = (1l << 32) - 1;

    private Map<Long, Node> nodeMap = new HashMap<Long, Node>();
    // 测试用
    private List<Node> nodeList = new ArrayList<Node>();
    private List<Long> keyList = new ArrayList<Long>();

    public Manager(int nodeCount, int slot) {
        this.slot = slot;
        init(nodeCount);
    }
    public Manager(int nodeCount) {
        init(nodeCount);
    }

    private void init(int nodeCount) {
        for (int i = 0; i < nodeCount; i++) {
            addNode();
        }
        keyList.sort(new Comparator<Long>() {
            @Override
            public int compare(Long o1, Long o2) {
                return o1 > o2 ? 1 : o1 == o2 ? 0 : -1;
            }
        });
    }

    public List<Integer> getAllNode() {
        List<Integer> nodeSize = new ArrayList<Integer>();
        for (Node node : nodeList) {
            nodeSize.add(node.size());
        }
        return nodeSize;
    }

    public void addNode() {
        Node node = new Node();
        nodeList.add(node);
        addNode(node);
    }

    private void addNode(Node node) {
        Random rand = new Random();
        int index = 0;
        List<Long> allocatedSlots = new ArrayList<Long>();
        node.setSlots(allocatedSlots);
        while(index < slot) {
            long nextSlot = rand.nextInt() + (long)Integer.MAX_VALUE + 1;
            if (!nodeMap.containsKey(nextSlot)) {
                nodeMap.put(nextSlot, node);
                keyList.add(nextSlot);
                allocatedSlots.add(nextSlot);
                index++;
            }
        }
    }

    public String get(String key) {
        long hashCode = key.hashCode() + (long)Integer.MAX_VALUE + 1;
        long slot = hashCode % MAX_COUNT;
        Node node = getNodeBySlot(slot);
        return node.get(key);
    }

    public void put(String key, String value) {
        long hashCode = key.hashCode() + (long)Integer.MAX_VALUE + 1;
        long slot = hashCode % MAX_COUNT;
        Node node = getNodeBySlot(slot);
        node.put(key, value);
    }
    // 获取节点的方式可以优化
    private Node getNodeBySlot(long slot) {
        if (keyList.contains(slot)) {
            return nodeMap.get(slot);
        }
        long key = 0l;
        int index = 0;
        while(key < slot && index < keyList.size()) {
            key = keyList.get(index++);
        }
        Node node = nodeMap.get(key);
        return node != null ? node : nodeMap.get(keyList.get(0));
    }

    private static class Node {

        private Map<String, String> cache = new ConcurrentHashMap<>();

        private List<Long> slots = new ArrayList();

        public void put(String key, String value) {
            cache.putIfAbsent(key, value);
        }

        public String get(String key) {
            return cache.getOrDefault(key, null);
        }

        public void setSlots(List<Long> slots) {
            this.slots = slots;
        }

        public List<Long> getSlots() {
            return slots;
        }

        public int size() {
            return cache.size();
        }
    }

}
