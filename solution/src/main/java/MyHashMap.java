//706. 设计哈希映射
class MyHashMap {
    private static final int BASE = 769;
    Node[] nodes;

    public MyHashMap() {
        nodes = new Node[BASE];
    }

    public void put(int key, int value) {
        int hash = hash(key);
        Node tmp = nodes[hash];
        if (tmp != null) {
            while (tmp != null) {
                if (tmp.key == key) {
                    tmp.value = value;
                    return;
                }
                tmp = tmp.next;
            }
        }
        Node node = new Node(key, value);
        node.next = nodes[hash];
        nodes[hash] = node;
    }

    public int get(int key) {
        int hash = hash(key);
        Node tmp = nodes[hash];
        while (tmp != null) {
            if (tmp.key == key) {
                return tmp.value;
            }
            tmp = tmp.next;
        }
        return -1;
    }

    public void remove(int key) {
        int hash = hash(key);
        Node tmp = nodes[hash];
        Node prev = null;
        while (tmp != null) {
            if (tmp.key == key) {
                if (prev == null) {
                    nodes[hash] = tmp.next;
                } else {
                    prev.next = tmp.next;
                }
                return;
            }
            prev = tmp;
            tmp = tmp.next;
        }
    }

    private int hash(int key) {
        return key % BASE;
    }

    static class Node {
        private int key;
        private int value;
        private Node next;

        private Node(int key, int value) {
            this.key = key;
            this.value = value;
        }
    }
}