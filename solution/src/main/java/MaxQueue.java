import java.util.Deque;
import java.util.LinkedList;
import java.util.Queue;

//面试题59 - II. 队列的最大值
public class MaxQueue {

    Queue<Integer> queue;
    Deque<Integer> helper;

    public MaxQueue() {
        queue = new LinkedList<>();
        helper = new LinkedList<>();
    }

    public int max_value() {
        if (helper.isEmpty()) {
            return -1;
        }
        return helper.peekFirst();
    }

    public void push_back(int value) {
        while (!helper.isEmpty() && helper.peekLast() < value) {
            helper.pollLast();
        }
        helper.offerLast(value);
        queue.add(value);
    }

    public int pop_front() {
        if (queue.isEmpty()) {
            return -1;
        }
        if (queue.peek().intValue() == helper.peekFirst().intValue()) {
            helper.pollFirst();
            return queue.poll();
        } else {
            return queue.poll();
        }
    }
}
