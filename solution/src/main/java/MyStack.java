import java.util.ArrayDeque;
import java.util.Queue;

//225. 用队列实现栈
public class MyStack {
    Queue<Integer> queue;

    public MyStack() {
        queue = new ArrayDeque<>();
    }

    public void push(int x) {
        int size = queue.size();
        queue.offer(x);

        while (size-- > 0) {
            queue.offer(queue.poll());
        }
    }

    public int pop() {
        return queue.poll();
    }

    public int top() {
        return queue.peek();
    }

    public boolean empty() {
        return queue.isEmpty();
    }
}
