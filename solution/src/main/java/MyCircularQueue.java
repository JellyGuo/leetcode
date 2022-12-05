//622. 设计循环队列
class MyCircularQueue {
    private int front;
    private int rear;
    private int[] elements;
    private int length;

    public MyCircularQueue(int k) {
        this.length = k + 1;
        elements = new int[length];
        front = rear = 0;
    }

    public boolean enQueue(int value) {
        if (isFull()) return false;
        elements[rear] = value;
        rear = (rear + 1) % length;
        return true;
    }

    public boolean deQueue() {
        if (isEmpty()) return false;
        front = (front + 1) % length;
        return true;
    }

    public int Front() {
        if (isEmpty()) return -1;
        return elements[front];
    }

    public int Rear() {
        if (isEmpty()) return -1;
        return elements[(rear - 1 + length) % length];
    }

    public boolean isEmpty() {
        return front == rear;
    }

    public boolean isFull() {
        return (rear + 1) % length == front;
    }
}