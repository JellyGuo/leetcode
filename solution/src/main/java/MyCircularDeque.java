//641. 设计循环双端队列
class MyCircularDeque {
    ListNode head;
    ListNode tail;
    int size;
    int capacity;

    public MyCircularDeque(int k) {
        this.head = new ListNode();
        this.tail = new ListNode();
        head.next = tail;
        tail.prev = head;
        this.size = 0;
        this.capacity = k;
    }

    public boolean insertFront(int value) {
        if (size == capacity) {
            return false;
        }
        this.size++;
        ListNode node = new ListNode(value);
        head.next.prev = node;
        node.next = head.next;
        head.next = node;
        node.prev = head;
        return true;
    }

    public boolean insertLast(int value) {
        if (size == capacity) {
            return false;
        }
        this.size++;
        ListNode node = new ListNode(value);
        node.prev = tail.prev;
        tail.prev.next = node;
        node.next = tail;
        tail.prev = node;
        return true;
    }

    public boolean deleteFront() {
        if (size == 0) return false;
        head.next = head.next.next;
        head.next.prev = head;
        this.size--;
        return true;
    }

    public boolean deleteLast() {
        if (size == 0) return false;
        tail.prev = tail.prev.prev;
        tail.prev.next = tail;
        this.size--;
        return true;
    }

    public int getFront() {
        if (size == 0) return -1;
        return head.next.val;
    }

    public int getRear() {
        if (size == 0) return -1;
        return tail.prev.val;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public boolean isFull() {
        return size == capacity;
    }

    static class ListNode {
        int val;
        ListNode prev;
        ListNode next;
        ListNode() {}
        ListNode(int val) { this.val = val; }
    }
}