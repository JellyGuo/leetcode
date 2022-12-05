//705. 设计哈希集合
class MyHashSet {

    boolean[] nodes;

    public MyHashSet() {
        nodes = new boolean[1000001];
    }

    public void add(int key) {
        nodes[key] = true;
    }

    public void remove(int key) {
        nodes[key] = false;
    }

    public boolean contains(int key) {
        return nodes[key];
    }
}
