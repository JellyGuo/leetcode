import java.util.ArrayDeque;
import java.util.Deque;

//1670. 设计前中后队列
public class FrontMiddleBackQueue {
    private Deque<Integer> leftDeque;
    private Deque<Integer> rightDeque;

    public FrontMiddleBackQueue() {
        leftDeque = new ArrayDeque<>();
        rightDeque = new ArrayDeque<>();
    }

    public void pushFront(int val) {
        leftDeque.offerFirst(val);
        if (leftDeque.size() > rightDeque.size() + 1) {
            rightDeque.offerFirst(leftDeque.pollLast());
        }
    }

    public void pushMiddle(int val) {
        if (leftDeque.size() != rightDeque.size()) {
            rightDeque.offerFirst(leftDeque.pollLast());
        }
        leftDeque.offerLast(val);
    }

    public void pushBack(int val) {
        rightDeque.offerLast(val);
        if (rightDeque.size() > leftDeque.size()) {
            leftDeque.offerLast(rightDeque.pollFirst());
        }

    }

    public int popFront() {
        if (!leftDeque.isEmpty()) {
            int res = leftDeque.pollFirst();
            if (leftDeque.size() < rightDeque.size()) {
                leftDeque.offerLast(rightDeque.pollFirst());
            }
            return res;
        }
        return -1;
    }

    public int popMiddle() {
        if (leftDeque.isEmpty()) return -1;
        if (leftDeque.size() == rightDeque.size()) {
            int res = leftDeque.pollLast();
            leftDeque.offerLast(rightDeque.pollFirst());
            return res;
        } else {
            return leftDeque.pollLast();
        }
    }

    public int popBack() {
        if (rightDeque.isEmpty()) {
            if (leftDeque.isEmpty()) return -1;
            return leftDeque.pollLast();
        }
        int res = rightDeque.pollLast();
        if (leftDeque.size() > rightDeque.size() + 1) {
            rightDeque.offerFirst(leftDeque.pollLast());
        }
        return res;
    }
}
