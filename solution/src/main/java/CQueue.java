import java.util.Stack;

/**
 * 两个栈实现队列
 */
public class CQueue {
    Stack<Integer> stack;
    Stack<Integer> helper;

    public CQueue() {
        stack = new Stack<>();
        helper = new Stack<>();
    }

    public void appendTail(int val) {
        helper.push(val);
    }

    public int deleteHead() {
        if (!stack.isEmpty()) return stack.pop();
        if (helper.isEmpty()) return -1;
        while (!helper.isEmpty()) {
            stack.push(helper.pop());
        }
        return stack.pop();
    }
}
