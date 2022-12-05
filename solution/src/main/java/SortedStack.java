import java.util.Stack;

//面试题 03.05. 栈排序
public class SortedStack {

    Stack<Integer> stack;
    Stack<Integer> helper;

    public SortedStack() {
        stack = new Stack<>();
        helper = new Stack<>();
    }

    public void push(int val) {
        while (!stack.isEmpty() && stack.peek() < val) {
            helper.push(stack.pop());
        }
        stack.push(val);
        while (!helper.isEmpty()) {
            stack.push(helper.pop());
        }
    }

    public void pop() {
        if (!stack.isEmpty()) {
            stack.pop();
        }
    }

    public int peek() {
        return stack.isEmpty() ? -1 : stack.peek();
    }

    public boolean isEmpty() {
        return stack.isEmpty();
    }
}
