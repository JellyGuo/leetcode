import java.util.HashMap;
import java.util.Map;

public class LRUClass {
    int size;
    int capacity;
    ListNode head;
    ListNode tail;
    Map<Integer, ListNode> map;

    public LRUClass(int capacity) {
        this.capacity = capacity;
        this.map = new HashMap<>();
        head = new ListNode(-1);
        tail = new ListNode(-1);
        head.after = tail;
    }

    public int get(int key) {
        ListNode node = map.get(key);
        if (node == null) {
            return -1;
        }
        moveToHead(node);
        return node.val;
    }

    public void put(int key, int value) {
        ListNode node = map.get(key);
        if (node == null) {
            node = new ListNode(value);
            size++;
            if (size > capacity) {
                ListNode tail = removeTail();
                map.remove(tail.val);
                size--;
            }
            addToHead(node);
            map.put(key, node);
        } else {
            node.val = value;
            moveToHead(node);
        }
    }

    private ListNode removeTail() {
        ListNode node = tail.before;
        removeNode(node);
        return node;
    }

    private void moveToHead(ListNode node) {
        removeNode(node);
        addToHead(node);
    }

    private void addToHead(ListNode node) {
        head.after.before = node;
        node.after = head.after;
        head.after = node;
        node.before = head;
    }

    private void removeNode(ListNode node) {
        node.before.after = node.after;
        node.after.before = node.before;
    }
}
