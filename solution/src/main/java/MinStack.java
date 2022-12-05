import java.util.Stack;

public class MinStack {
    Stack<Integer> stack;
    Stack<Integer> helper;

    public MinStack() {
        stack = new Stack<>();
        helper = new Stack<>();
    }

    public void push(int val) {
        stack.push(val);
        if (helper.isEmpty() || helper.peek() >= val) {
            helper.push(val);
        }
    }

    public void pop() {
        if (stack.isEmpty()) return;
        if (!helper.isEmpty() && helper.peek().equals(stack.peek())) {
            helper.pop();
        }
        stack.pop();
    }

    public int top() {
        return stack.peek();
    }

    public int getMin() {
        return helper.peek();
    }
}
