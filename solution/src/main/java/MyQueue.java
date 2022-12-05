import java.util.Stack;

//232. 用栈实现队列
public class MyQueue {
    Stack<Integer> stack;
    Stack<Integer> helper;

    public MyQueue() {
        stack = new Stack<>();
        helper = new Stack<>();
    }

    public void push(int x) {
        helper.push(x);
    }

    public int pop() {
        if (stack.isEmpty()) {
            while (!helper.isEmpty()) {
                stack.push(helper.pop());
            }
        }
        return stack.pop();
    }

    public int peek() {
        if (stack.isEmpty()) {
            while (!helper.isEmpty()) {
                stack.push(helper.pop());
            }
        }
        return stack.peek();
    }

    public boolean empty() {
        return stack.isEmpty() && helper.isEmpty();
    }
}
